package com.ndebugs.nhttpx.evaluation;

import com.ndebugs.nhttpx.message.Message;
import java.io.StringWriter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Data
public class StringEvaluator {

    private final VelocityContext context;
    private final Message message;

    private boolean trimmed;

    public StringEvaluator(VelocityContext context, Message message) {
        this.context = context;
        this.message = message;
    }

    public String evaluate(String value) {
        String name = message.getName();

        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, name, value);

        String result = stringWriter.toString();
        if (trimmed) {
            return StringUtils.normalizeSpace(result);
        } else {
            return result;
        }
    }

    public String[] evaluateAll(String[] values) {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            result[i] = evaluate(value);
        }

        return result;
    }
}
