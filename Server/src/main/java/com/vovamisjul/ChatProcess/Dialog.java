package com.vovamisjul.ChatProcess;

import com.vovamisjul.User.Agent;
import com.vovamisjul.User.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dialog {

    private Client client;
    private Agent agent;
    protected static final Logger logger = LogManager.getLogger(Dialog.class);

    public Dialog(Client client, Agent agent) {
        this.client = client;
        this.agent = agent;
        logger.info("Dialog with " + agent + " and " + client + " created");
    }

    public Client getClient() {
        return client;
    }

    public Agent getAgent() {
        return agent;
    }

    public void sendTo(String type, String message) {
        switch (type) {
            case "client":
                client.addNewMessage(message);
                break;
            case "agent":
                agent.addNewMessage(message);
                break;
        }
    }

    public Message pollFrom(String type) {
        switch (type) {
            case "client":
                return agent.pollMessage();
            case "agent":
                return client.pollMessage();
        }
        return null;
    }
}
