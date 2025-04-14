package com.ndebugs.nhttpx.task;

import com.ndebugs.nhttpx.connection.HTTPConnection;
import com.ndebugs.nhttpx.message.Message;
import com.ndebugs.nhttpx.message.Parameter;
import com.ndebugs.nhttpx.message.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class RequestTask extends MessageTask {

    private int id;
    private VelocityContext context;
    private RequestTaskListener listener;
    private String responseCodePattern;
    private int maxErrorRepeat;
    private boolean hasNext;

    public RequestTask(VelocityContext context, Message message) {
        super(message);

        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VelocityContext getContext() {
        return context;
    }

    public void setContext(VelocityContext context) {
        this.context = context;
    }

    public RequestTaskListener getListener() {
        return listener;
    }

    public void setListener(RequestTaskListener listener) {
        this.listener = listener;
    }

    public String getResponseCodePattern() {
        return responseCodePattern;
    }

    public void setResponseCodePattern(String responseCodePattern) {
        this.responseCodePattern = responseCodePattern;
    }

    public int getMaxErrorRepeat() {
        return maxErrorRepeat;
    }

    public void setMaxErrorRepeat(int maxErrorRepeat) {
        this.maxErrorRepeat = maxErrorRepeat;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public void run() {
        Request request = getMessage().getRequest();

        boolean error;
        int errorCount = maxErrorRepeat;
        do {
            try {
                StringWriter urlWriter = new StringWriter();
                Velocity.evaluate(context, urlWriter, getMessage().getName(), request.getUrl());

                Map<String, String> params = toParameterMap(context, request.getParameters());
                HTTPConnection connection = new HTTPConnection();
                int code = connection.open(urlWriter.toString(), params, request.getMethod());
                if (Pattern.matches(responseCodePattern, Integer.toString(code))) {
                    ObjectMapper mapper = new ObjectMapper();
                    Object value = mapper.readValue(connection.getResponseBytes(), Object.class);

                    listener.onComplete(this, value);
                    error = false;
                } else {
                    error = true;
                }
            } catch (Exception e) {
                error = true;

                listener.onError(this, e);
            }
        } while (error && --errorCount > 0);
    }

    private Map<String, String> toParameterMap(VelocityContext context, List<Parameter> params) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        if (params != null) {
            for (Parameter param : params) {
                StringWriter writer = new StringWriter();
                Velocity.evaluate(context, writer, getMessage().getName(), param.getValue());

                paramMap.put(param.getKey(), writer.toString());
            }
        }
        return paramMap;
    }
}
