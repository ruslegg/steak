package com.example.admin.test1;

public class Guideline {

    String header,text;


    Guideline(String header, String text) {
        this.header = header;
        this.text = text;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
