package com.ndebugs.nhttpx.evaluation;

import com.ndebugs.nhttpx.message.Message;
import java.io.StringWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class FieldEvaluator {

    private final VelocityContext context;
    private final Message message;

    private boolean trimmed;

    public FieldEvaluator(VelocityContext context, Message message) {
        this.context = context;
        this.message = message;
    }

    public VelocityContext getContext() {
        return context;
    }

    public Message getMessage() {
        return message;
    }

    public boolean isTrimmed() {
        return trimmed;
    }

    public void setTrimmed(boolean trimmed) {
        this.trimmed = trimmed;
    }

    public String evaluate(String field) {
        String name = message.getName();

        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, name, field);

        String result = stringWriter.toString();
        if (trimmed) {
            return StringUtils.normalizeSpace(result);
        } else {
            return result;
        }
    }

    public String[] evaluateAll(String[] fields) {
        String[] result = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            result[i] = evaluate(field);
        }

        return result;
    }
}
