package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 */
public interface Result<T> {
    public Boolean isSuccess();
    public Boolean isFailure();
    public Status getStatus();
    public T getEntity();
}
