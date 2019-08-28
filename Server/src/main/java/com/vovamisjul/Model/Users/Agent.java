package com.vovamisjul.Model.Users;


import java.io.*;

public class Agent extends AbstractUser {

    private boolean vacant = true;

    public Agent(BufferedReader in, BufferedWriter out, String name) {
        super(in, out, name);
        logger.info("Agent " + name + " register");
    }

    public boolean isVacant() {
        return vacant;
    }

    public void setVacant(boolean vacant) {
        this.vacant = vacant;
    }
}
