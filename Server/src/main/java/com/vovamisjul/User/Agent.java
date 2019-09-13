package com.vovamisjul.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Agent extends AbstractUser {
    public Agent(String name, int id) {
        super(name, id);
    }

    @Override
    public String toString() {
        return "Agent: " + name;
    }
}
