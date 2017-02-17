package com.codebreaker.dart.database;

/**
 * Created by abhishek on 2/17/17.
 */

public class Message {

    int who;
    String message;
    String type;

    public int getWho() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
