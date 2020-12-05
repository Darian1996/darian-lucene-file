
package com.darian.darianlucenefile.module;

public class Result<T> extends PublicResult {

    private T value;

    public static <T> Result<T> success(T value) {
        Result<T> result = new Result<T>();
        result.setValue(value);
        return result;
    }

    public static <T> Result<T> fail(T value, String errorMessage) {
        Result<T> result = new Result<T>();
        result.setValue(value);
        result.setResultCode(500);
        result.setResultMsg(errorMessage);
        return result;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}