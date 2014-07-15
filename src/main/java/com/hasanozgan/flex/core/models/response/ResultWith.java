package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResultWith<T> {
    public Boolean isSucceed();
    public Status getStatus();
    public T getEntity();
}
