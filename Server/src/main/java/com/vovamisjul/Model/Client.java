package com.vovamisjul.Model;

import com.vovamisjul.Model.MyLogger.MyLogger;

import java.io.*;

public class Client extends AbstractUser {

    public Client(BufferedReader in, BufferedWriter out, String name) {
        super(in, out, name);
        MyLogger.info("Client " + name + " register");
    }
}