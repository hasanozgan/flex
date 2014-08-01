package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
class Failure<T> implements Result<T> {
    private final Status status;
    private final T entity;

    public Failure(Status status, T entity) {
        this.entity = entity;
        this.status = status;
    }

    public Failure(Status status) {
        this(status, null);
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public Boolean isSuccess() {
        return false;
    }

    @Override
    public Boolean isFailure() {
        return true;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public static <T> Result<T> withType(Status status) {
        return new Failure<T>(status);
    }
}
