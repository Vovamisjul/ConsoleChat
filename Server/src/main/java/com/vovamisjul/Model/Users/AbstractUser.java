package com.vovamisjul.Model.Users;

import org.apache.logging.log4j.*;

import java.io.*;

public abstract class AbstractUser {
    public BufferedReader in;
    public BufferedWriter out;
    protected static final Logger logger = LogManager.getLogger(AbstractUser.class);

    public String getName() {
        return name;
    }

    private String name;

    public AbstractUser(BufferedReader in, BufferedWriter out, String name) {
        this.in = in;
        this.out = out;
        this.name = name;
    }
}
