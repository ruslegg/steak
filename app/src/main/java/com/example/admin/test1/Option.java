package com.example.admin.test1;

public class Option {
    private String type,doneness,comment;

    public Option(String type, String doneness, String comment) {
        this.type = type;
        this.doneness = doneness;
        this.comment = comment;
    }

    public Option() {
    }

    public Option(String type, String doneness) {
        this.type = type;
        this.doneness = doneness;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoneness() {
        return doneness;
    }

    public void setDoneness(String doneness) {
        this.doneness = doneness;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
