package com.vovamisjul.chatlogic.user;

public class Agent extends AbstractUser {
    public Agent(String name, int id) {
        super(name, id);
    }

    @Override
    public String toString() {
        return "Agent " + name;
    }
}
