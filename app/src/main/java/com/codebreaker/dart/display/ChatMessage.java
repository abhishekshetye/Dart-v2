package com.codebreaker.dart.display;

import android.graphics.drawable.Drawable;

/**
 * Created by abhishek on 1/14/17.
 */

public class ChatMessage {
    private boolean isImage, isMine;
    private String content;
    private Drawable imagesource;
    private String deeplink;

    public ChatMessage(String message, boolean mine, boolean image) {
        content = message;
        isMine = mine;
        isImage = image;
    }

    public ChatMessage(String message, boolean mine, boolean image, String deeplink) {
        content = message;
        isMine = mine;
        isImage = image;
        this.deeplink = deeplink;
    }


    public String getDeeplink(){
        return deeplink;
    }

    public Drawable getImagesource() {
        return imagesource;
    }

    public void setImagesource(Drawable imagesource) {
        this.imagesource = imagesource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }
}