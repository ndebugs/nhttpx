package com.ndebugs.nhttpx.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class FileWriterWrapper {

    private final File file;
    private OutputStreamWriter writer;

    public FileWriterWrapper(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public OutputStreamWriter getWriter() {
        return writer;
    }

    public void open(boolean overwrite) throws IOException {
        writer = new FileWriter(file, !overwrite);
    }

    public boolean isClosed() {
        return writer == null;
    }

    public void close() throws IOException {
        writer.close();
        writer = null;
    }
}
