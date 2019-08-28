package com.vovamisjul.Model;

import com.vovamisjul.Model.MyLogger.MyLogger;

import java.io.IOException;
import java.util.ArrayList;

public class Dialog{
    private Agent agent;
    private Client client;
    private Thread toClient;
    private Thread toAgent;
    private boolean active = true;
    private ArrayList<Agent> agents;

    public Dialog(Agent agent, Client client, ArrayList<Agent> agents) {
        this.agent = agent;
        this.client = client;
        this.agents = agents;
        MyLogger.info("Chat started");
    }

    public void start(String message) {
        try {
            agent.out.write(client.getName()+ ": " + message+'\n');
            agent.out.flush();
        } catch (IOException e) {
            MyLogger.error(e.getMessage());
        }
        toAgent = new Thread(() -> {
            try {
                while (active) {
                    String message1 = client.in.readLine();
                    if (message1.equals("/exit")) {
                        active = false;
                        break;
                    }
                    agent.out.write(client.getName()+ ": " + message1 + '\n');
                    agent.out.flush();
                }
            } catch (IOException e) {
                MyLogger.error(e.getMessage());
            }
            agents.remove(agent);
            MyLogger.info("dialog end");
            try {
                client.in.close();
                agent.out.close();
            } catch (IOException e) {
                MyLogger.error(e.getMessage());
            }
        });
        toClient = new Thread(() -> {
            try {
                while (active) {
                    String message12 = agent.in.readLine();
                    if (message12.equals("/exit")) {
                        active = false;
                        break;
                    }
                    client.out.write(agent.getName()+ ": " + message12 + '\n');
                    client.out.flush();
                }
            } catch (IOException e) {
                MyLogger.error(e.getMessage());
            }
            agents.remove(agent);
            MyLogger.info("dialog end");
            try {
                agent.in.close();
                client.out.close();
            } catch (IOException e) {
                MyLogger.error(e.getMessage());
            }
        });
        toClient.start();
        toAgent.start();

    }
}
