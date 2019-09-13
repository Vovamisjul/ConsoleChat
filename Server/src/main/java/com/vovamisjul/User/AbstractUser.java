package com.vovamisjul.User;

import com.vovamisjul.ChatProcess.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;

public abstract class AbstractUser {
    protected String name;
    private int id;
    private ArrayDeque<Message> unsentMessages = new ArrayDeque<>();
    protected static final Logger logger = LogManager.getLogger(AbstractUser.class);

    public AbstractUser(String name, int id) {
        this.name = name;
        this.id = id;
        logger.info(this + " registered");
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addNewMessage(String message) {
        unsentMessages.add(new Message(name, message));
    }

    public Message pollMessage() {
        return unsentMessages.pollFirst();
    }

    @Override
    public String toString() {
        return "User: " + name;
    }
}
