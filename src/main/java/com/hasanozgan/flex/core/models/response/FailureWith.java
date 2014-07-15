package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
class FailureWith<T> implements ResultWith<T> {
    private final Status status;
    private final T entity;

    public FailureWith(Status status, T entity) {
        this.entity = entity;
        this.status = status;
    }

    public FailureWith(Status status) {
        this(status, (T) new Object());
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public Boolean isSucceed() {
        return false;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public static <T> ResultWith<T> withType(Status status) {
        return (ResultWith<T>) new FailureWith<Object>(status, new Object());
    }
}
