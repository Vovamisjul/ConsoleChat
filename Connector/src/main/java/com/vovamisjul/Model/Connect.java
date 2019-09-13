package com.vovamisjul.Model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connect {
    ServerSocket serverSocket;
    protected static final Logger logger = LogManager.getLogger(Connect.class);
    public Connect() {
        try {
            serverSocket = new ServerSocket(11111);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void start() {
        try {
            while (true) {
                Socket user = serverSocket.accept();
                new Redirector(user).start();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
