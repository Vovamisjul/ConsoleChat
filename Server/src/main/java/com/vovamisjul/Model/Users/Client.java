package com.vovamisjul.Model.Users;


import java.io.*;

public class Client extends AbstractUser {

    public Client(BufferedReader in, BufferedWriter out, String name) {
        super(in, out, name);
        logger.info("Client " + name + " register");
    }
}