package com.ndebugs.nhttpx.message;

/**
 *
 * @author van de Bugs <van.de.bugs@gmail.com>
 */
public class DataWrapper<T> {

    private DataWrapper<T> parent;
    private T data;

    public DataWrapper(DataWrapper<T> parent, T data) {
        this.parent = parent;
        this.data = data;
    }

    public DataWrapper<T> getParent() {
        return parent;
    }

    public void setParent(DataWrapper<T> parent) {
        this.parent = parent;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return parent != null
                ? String.format("%s -> %s", parent, data)
                : data.toString();
    }
}
