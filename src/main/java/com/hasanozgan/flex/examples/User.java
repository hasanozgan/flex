package com.hasanozgan.flex.examples;

/**
 * Created by hasan.ozgan on 7/15/2014.
 */
public class User {
    private final int id;
    private final String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
