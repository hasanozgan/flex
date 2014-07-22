package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Success<T> implements Result<T> {
    private final T entity;

    public Success() {
        this.entity = null;
    }

    public Success(T entity) {
        this.entity = entity;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public Boolean isSuccess() {
        return true;
    }

    @Override
    public Boolean isFailure() {
        return false;
    }

    @Override
    public Status getStatus() {
        return SuccessStatus.SUCCESS;
    }

    public static <T> Result<T> withType() {
        return (Result<T>) new Failure<Object>(SuccessStatus.SUCCESS, new Object());
    }
}
