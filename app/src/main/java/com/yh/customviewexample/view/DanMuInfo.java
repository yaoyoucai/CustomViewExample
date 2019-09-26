package com.yh.customviewexample.view;

public class DanMuInfo {
    private String text;
    private String type;
    private String userName;

    public DanMuInfo(String text, String type, String userName) {
        this.text = text;
        this.type = type;
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
