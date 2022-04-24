package com.valko.instantchat.client.models;

import com.google.gson.Gson;



public class Message
{
    public String author;
    public String payload;
    public String color;



    public Message(String author, String payload, String color)
    {
        this.author = author;
        this.payload = payload;
        this.color = color;
    }



    public String toJsonString()
    {
        return new Gson().toJson(this);
    }



    @Override
    public String toString()
    {
        return (this.author + " -> ALL : " + this.payload);
    }
}