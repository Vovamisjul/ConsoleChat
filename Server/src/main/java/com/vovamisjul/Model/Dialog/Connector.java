package com.vovamisjul.Model.Dialog;

import com.vovamisjul.Model.Server;
import com.vovamisjul.Model.Users.Agent;
import com.vovamisjul.Model.Users.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connector extends Thread {
    private BufferedReader in;
    private BufferedWriter out;
    private ArrayList<Agent> agents;
    protected static final Logger logger = LogManager.getLogger(Connector.class);

    public Connector(Socket userSocket, ArrayList<Agent> agents) {
        this.agents = agents;
        try {
            in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(userSocket.getOutputStream()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                if (message.startsWith("/register agent ")) {
                    Agent agent = new Agent(in, out, message.substring("/register agent ".length()));
                    agents.add(agent);
                    break;
                } else if (message.startsWith("/register client ")) {
                    Client client = new Client(in, out, message.substring("/register client ".length()));
                    Agent freeAgent = findFreeAgent();
                    if (freeAgent == null) {
                        out.write("There are no free agents. Try register later\n");
                        out.flush();
                    } else {
                        out.write("Print your message to start\n");
                        out.flush();
                        message = in.readLine();
                        new Dialog(freeAgent, client, agents).start(message);
                        break;
                    }
                } else {
                    out.write("There are no such command\n");
                    out.flush();
                }

            }
        } catch (IOException e) {
            logger.error(e.getMessage());
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
