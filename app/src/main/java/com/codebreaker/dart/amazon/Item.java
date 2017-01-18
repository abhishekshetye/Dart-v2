package com.codebreaker.dart.amazon;

/**
 * Created by abhishek on 1/15/17.
 */

public class Item {

    private final String name;

    public Item(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
