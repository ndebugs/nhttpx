package com.ndebugs.nhttpx.io;

import java.util.Arrays;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class WritableRow {

    private String[] fields;

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof WritableRow) {
            WritableRow anotherObj = (WritableRow) obj;
            return Arrays.equals(fields, anotherObj.getFields());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.deepHashCode(this.fields);
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(fields);
    }
}
