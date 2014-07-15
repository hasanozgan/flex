package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Result implements ResultWith<Object> {
    public static Result ok() {
        return new Success(new Object());
    }

    public static Result error(Status status) {
        return Failure.withType(status);
    }

    public static <T> ResultWith<T> ok(T entity) {
        return new SuccessWith<T>(entity);
    }

    public static <T> ResultWith<T> errorWith(Status status) {
        return FailureWith.withType(status);
    }

    public static <T> ResultWith<T> error(Status status, T entity) {
        return new FailureWith<T>(status, entity);
    }
}
