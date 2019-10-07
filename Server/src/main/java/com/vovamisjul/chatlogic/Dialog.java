package com.vovamisjul.chatlogic;

import com.vovamisjul.chatlogic.user.Agent;
import com.vovamisjul.chatlogic.user.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dialog {

    private Client client;
    private Agent agent;
    protected static final Logger logger = LogManager.getLogger(Dialog.class);

    public Dialog(Client client, Agent agent) {
        this.client = client;
        this.agent = agent;
        this.agent.addNewStartMessage("You chat now with " + agent);
        this.client.addNewStartMessage("You chat now with " + client);
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

    public void exit(String type, Users users) {
        switch (type) {
            case "client":
                users.addFreeAgent(agent);
                logger.info(client + "exit from gialog");
                break;
            case "agent":
                users.addFreeClient(client);
                logger.info(agent + "exit from gialog");
                break;
            default: return;
        }
        users.deleteDialog(this);
    }

    public void leave(Users users) {
        users.deleteDialog(this);
        users.addFreeClient(client);
        users.addFreeAgent(agent);
    }
}
