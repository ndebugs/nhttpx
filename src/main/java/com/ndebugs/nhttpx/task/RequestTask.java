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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
