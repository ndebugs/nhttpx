package com.ndebugs.nhttpx.io;

import java.util.Arrays;
import lombok.Data;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Data
public class WritableRow {

    private String[] values;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof WritableRow) {
            WritableRow anotherObj = (WritableRow) obj;
            return Arrays.equals(values, anotherObj.getValues());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.deepHashCode(this.values);
        return hash;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
