package com.vovamisjul.chatlogic.user;

import com.vovamisjul.chatlogic.Message;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;

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

    public void addNewStartMessage(String message) {
        unsentMessages.addFirst(new Message("Server", message));
    }

    public Message pollMessage() {
        return unsentMessages.pollFirst();
    }

    @Override
    public String toString() {
        return "User " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        AbstractUser user = (AbstractUser)obj;
        return (user.name.equals(name) && user.id == id);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).toHashCode();
    }
}
