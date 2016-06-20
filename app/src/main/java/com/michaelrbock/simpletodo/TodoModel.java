package com.michaelrbock.simpletodo;

/**
 * Created by michaelbock on 6/19/16.
 */
public class TodoModel {

    public Long _id;
    public String text;
    public Long date;
    public String priority;

    public TodoModel() {
        this.text = "";
        this.date = new Long(0);
        this.priority = "";
    }

    public TodoModel(String text, long date, String priority) {
        this.text = text;
        this.date = new Long(date);
        this.priority = priority;
    }

}
