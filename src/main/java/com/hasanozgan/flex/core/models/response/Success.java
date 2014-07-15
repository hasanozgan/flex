package com.hasanozgan.flex.core.models.response;

/**
 * Created with IntelliJ IDEA.
 * User: hasan.ozgan
 * Date: 7/1/14
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
class Success extends Result {
    private Object entity;

    public Success(Object entity) {
        this.entity = entity;
    }

    @Override
    public Boolean isSucceed() {
        return true;
    }

    @Override
    public Status getStatus() {
        return SuccessStatus.SUCCESS;
    }

    @Override
    public Object getEntity() {
        return entity;
    }
}
