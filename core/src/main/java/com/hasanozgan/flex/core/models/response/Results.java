package com.hasanozgan.flex.core.models.response;

/**
 * Created by hasan.ozgan on 7/17/2014.
 */
public class Results {
    public static <T> Result<T> ok() {
        return new Success<T>();
    }

    public static <T> Result<T> ok(T entity) {
        return new Success<T>(entity);
    }

    public static <T> Result<T> error(Status status) {
        return Failure.withType(status);
    }

    public static <T> Result<T> error(Status status, T entity) {
        return new Failure<T>(status, entity);
    }
}
