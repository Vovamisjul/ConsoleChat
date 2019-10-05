package com.vovamisjul.chatlogic;

public class Message {
    public String from;
    public String text;

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Message user = (Message)obj;
        return (user.from.equals(from) && user.text.equals(text));
    }
}