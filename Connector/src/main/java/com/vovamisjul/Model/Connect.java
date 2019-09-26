package com.vovamisjul.Model;


/*
 * You have log4j2.xml and import classes that use log4j.xml
 * Maybe replace with import org.apache.logging.log4j.LogManager;
 *                    import org.apache.logging.log4j.Logger;
 */
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
            logger.info("Connector started");
            while (true) {
                Socket user = serverSocket.accept();
                new Redirector(user).start();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
