package com.ndebugs.nhttpx.task;

import com.ndebugs.nhttpx.io.WritableRow;
import com.ndebugs.nhttpx.message.Message;
import java.io.IOException;
import java.io.OutputStreamWriter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileWriterTask extends MessageTask {

    private WritableRow row;
    private OutputStreamWriter writer;
    private FileWriterTaskListener listener;

    public FileWriterTask(WritableRow row, Message message) {
        super(message);

        this.row = row;
    }

    @Override
    public void run() {
        try {
            String[] fields = row.getFields();
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];

                if (i > 0) {
                    writer.write(',');
                }
                StringEscapeUtils.escapeCsv(writer, field);
            }
            writer.append('\n');

            listener.onComplete(this);
        } catch (IOException e) {
            listener.onError(this, e);
        }
    }
}
