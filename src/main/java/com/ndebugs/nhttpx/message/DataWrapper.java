package com.ndebugs.nhttpx.message;

import lombok.Data;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
@Data
public class DataWrapper<T> {

    private DataWrapper<T> parent;
    private T data;

    public DataWrapper(DataWrapper<T> parent, T data) {
        this.parent = parent;
        this.data = data;
    }

    @Override
    public String toString() {
        return parent != null
                ? String.format("%s -> %s", parent, data)
                : data.toString();
    }
}
