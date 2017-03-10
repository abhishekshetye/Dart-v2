package com.codebreaker.dart;

/**
 * Created by abhishek on 3/10/17.
 */

public class News {

    public String title;
    public String description;

    public News(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
