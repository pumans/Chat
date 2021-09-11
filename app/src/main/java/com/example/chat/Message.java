package com.example.chat;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Класс для отображения сообщений чата (ORM)
 */
public class Message {
    private int id;
    private String author;
    private String text;
    private String moment;

    public Message() {

    }
    public Message(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.author = jsonObject.getString("author");
        this.text = jsonObject.getString("text");
        this.moment = jsonObject.getString("moment");
    }
    @Override
    public String toString(){
        // * работа с датой: если сегодня то только время
        String str = author + ": " + text + ", время: " + moment +"\n";
        return str;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }
}
