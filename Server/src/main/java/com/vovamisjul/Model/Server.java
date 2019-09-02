package com.vovamisjul.Model;

import com.vovamisjul.Model.Users.Agent;
import com.vovamisjul.Model.Users.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    protected static final Logger logger = LogManager.getLogger(Server.class);
    private ArrayList<Agent> agents = new ArrayList<Agent>();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter port");
            int port = scanner.nextInt();
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                logger.info("Server started");
                processUsers(serverSocket);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            return;
        }
    }

    private void processUsers(ServerSocket serverSocket) {
        while (true) {
            try {
                Socket userSocket = serverSocket.accept();
                logger.info("Client connected: " + userSocket.toString());
                new Thread(() -> {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(userSocket.getOutputStream()));
                        while (true) {
                            String message = in.readLine();
                            if (message.startsWith("/register agent ")) {
                                Agent agent = new Agent(in, out, message.substring("/register agent ".length()));
                                agents.add(agent);
                                break;
                            }
                            else if (message.startsWith("/register client ")) {
                                Client client = new Client(in, out, message.substring("/register client ".length()));
                                Agent freeAgent = findFreeAgent();
                                if (freeAgent == null) {
                                    out.write("There are no free agents. Try register later\n");
                                    out.flush();
                                } else {
                                    out.write("Print your message to start\n");
                                    out.flush();
                                    message = in.readLine();
                                    new Dialog(freeAgent, client, Server.this.agents).start(message);
                                    break;
                                }
                            }
                            else {
                                out.write("There are no such command\n");
                                out.flush();
                            }

                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }).start();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private Agent findFreeAgent() {
        for (Agent agent:agents
        ) {
            if (agent.isVacant()) {
                agent.setVacant(false);
                return agent;
            }
        }
        return null;
    }
}
