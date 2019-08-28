package com.vovamisjul.Model;

import java.io.*;
import java.net.Socket;

public abstract class AbstractUser {
    protected BufferedReader in;
    protected BufferedWriter out;

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
