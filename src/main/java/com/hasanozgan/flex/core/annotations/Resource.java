package com.hasanozgan.flex.core.annotations;

import com.hasanozgan.flex.core.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hasanozgan on 14/07/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Resource {
    public String path();
    public HttpMethod method();
}
