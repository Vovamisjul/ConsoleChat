package com.vovamisjul.Model;

import com.vovamisjul.Model.Dialog.Connector;
import com.vovamisjul.Model.Users.Agent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    ServerSocket serverSocket;
    protected static final Logger logger = LogManager.getLogger(Server.class);

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter port");
            int port = scanner.nextInt();
            try {
                serverSocket = new ServerSocket(port);
                logger.info("Server started");
                processUsers();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void processUsers() {
        while (true) {
            try {
                Socket userSocket = serverSocket.accept();
                logger.info("User connected: " + userSocket.toString());
                new Connector(userSocket, agents).start();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
