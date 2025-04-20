package com.ndebugs.nhttpx.manager;

import com.ndebugs.nhttpx.config.ApplicationProperties;
import com.ndebugs.nhttpx.evaluation.StringEvaluator;
import com.ndebugs.nhttpx.io.WritableRow;
import com.ndebugs.nhttpx.message.Message;
import com.ndebugs.nhttpx.message.MessageSettings;
import com.ndebugs.nhttpx.message.DataWrapper;
import com.ndebugs.nhttpx.message.Response;
import com.ndebugs.nhttpx.task.MessageTask;
import com.ndebugs.nhttpx.task.RequestTask;
import com.ndebugs.nhttpx.task.RequestTaskListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Component
@Log4j2
public class ProcessManager implements RequestTaskListener {

    private static final String CONTEXT_PARENT_DATA = "parent";
    private static final String CONTEXT_DATA = "data";

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MessageSettings messageSettings;

    @Autowired
    private ExecutorManager executorManager;

    @Autowired
    private FileWriterManager fileWriterManager;

    public void doProcess() throws Exception {
        List<Message> messages = messageSettings.getMessages();

        if (messages != null && !messages.isEmpty()) {
            doProcess(0, null, 0);
        }
    }

    private void doProcess(int index, DataWrapper parent, int position) throws Exception {
        List<Message> messages = messageSettings.getMessages();

        Message message = messages.get(index);

        VelocityContext context = new VelocityContext();
        context.put(CONTEXT_PARENT_DATA, parent);

        RequestTask task = new RequestTask(context, message);
        task.setId(index);
        task.setPosition(position);
        task.setListener(this);
        task.setResponseCodePattern(applicationProperties.getResponseCodePattern());
        task.setErrorAttempts(applicationProperties.getRequestErrorAttempts());
        task.setHasNext(index + 1 < messages.size());

        executorManager.execute(task);
    }

    private int makePosition(int index, int length) {
        int position = 0;
        if (index == 0) {
            position |= MessageTask.POSITION_FIRST_SECTION;
        }

        if (index == length - 1) {
            position |= MessageTask.POSITION_LAST_SECTION;
        }
        return position;
    }

    private void doFetch(RequestTask task, List datas) throws Exception {
        Message message = task.getMessage();
        Response response = message.getResponse();

        VelocityContext context = task.getContext();
        DataWrapper parentData = (DataWrapper) context.get(CONTEXT_PARENT_DATA);

        int nextIndex = task.getId() + 1;
        Set<WritableRow> rowSet = new HashSet<>();
        for (int i = 0; i < datas.size(); i++) {
            Object data = datas.get(i);

            VelocityContext subContext = new VelocityContext();
            subContext.put(CONTEXT_PARENT_DATA, parentData);
            subContext.put(CONTEXT_DATA, data);

            StringEvaluator evaluator = new StringEvaluator(subContext, message);
            evaluator.setTrimmed(applicationProperties.isOutputTrimmed());

            String[] values = evaluator.evaluateAll(response.getValues());

            WritableRow row = new WritableRow();
            row.setValues(values);

            if (applicationProperties.isOutputAllowDuplicate() || !rowSet.contains(row)) {
                log.debug("Data: {}", row);

                int position = makePosition(i, datas.size());
                fileWriterManager.write(row, message, position);

                if (task.isHasNext()) {
                    DataWrapper nextParent = new DataWrapper(parentData, data);
                    doProcess(nextIndex, nextParent, position);
                }

                rowSet.add(row);
            } else {
                log.warn("Duplicate data: {}", row);
            }
        }
    }

    public void stop() throws IOException {
        executorManager.stopConnectionService();
        fileWriterManager.closeAll();
    }

    @Override
    public void onComplete(RequestTask task, Object data) {
        try {
            Message message = task.getMessage();
            Response response = message.getResponse();

            List subDatas = (List) (StringUtils.isNotBlank(response.getDataSource())
                    ? PropertyUtils.getProperty(data, response.getDataSource()) : data);

            if (subDatas != null && !subDatas.isEmpty()) {
                doFetch(task, subDatas);
            } else {
                VelocityContext context = task.getContext();
                DataWrapper parentData = (DataWrapper) context.get(CONTEXT_PARENT_DATA);

                log.warn("No data from parent: {}", parentData);
            }
        } catch (Exception e) {
            onError(task, e);
        }
    }

    @Override
    public void onError(MessageTask task, Exception e) {
        log.catching(e);
    }
}
