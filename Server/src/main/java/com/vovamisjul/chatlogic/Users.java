package com.vovamisjul.chatlogic;
import com.vovamisjul.chatlogic.user.AbstractUser;
import com.vovamisjul.chatlogic.user.Agent;
import com.vovamisjul.chatlogic.user.Client;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Users {
    private static Deque<Client> freeClients = new ArrayDeque<>();
    private static Deque<Agent> freeAgents = new ArrayDeque<>();
    private static List<Dialog> dialogs = new ArrayList<>();
    private static int id = 0;

    public synchronized int addNewUser(String type, String name) {
        switch (type) {
            case "client":
                if (freeAgents.size()==0) {
                    freeClients.add(new Client(name, id));
                }
                else {
                    Agent freeAgent = freeAgents.pollFirst();
                    dialogs.add(new Dialog(new Client(name, id), freeAgent));
                }
                break;
            case "agent":
                if (freeClients.size()==0) {
                    freeAgents.add(new Agent(name, id));
                }
                else {
                    Client freeClient = freeClients.pollFirst();
                    dialogs.add(new Dialog(freeClient, new Agent(name, id)));
                }
                break;
            default: throw new IllegalArgumentException("No such type");
        }
        id++;
        return id-1;
    }

    public synchronized Dialog getDialog(int userId) {
        for (Dialog dialog: dialogs
             ) {
            if (dialog.getAgent().getId() == userId || dialog.getClient().getId() == userId)
                return dialog;
        }
        return null;
    }

    public synchronized AbstractUser getUser(int id) {
        for (AbstractUser user: freeClients
        ) {
            if (user.getId()==id)
                return user;
        }
        for (AbstractUser user: freeAgents
        ) {
            if (user.getId()==id)
                return user;
        }
        return null;
    }

    public synchronized void deleteDialog(Dialog dialog) {
        dialogs.remove(dialog);
    }

    public synchronized void addFreeAgent(Agent agent) {
        if (freeClients.size()==0) {
            freeAgents.add(agent);
        }
        else {
            Client freeClient = freeClients.pollFirst();
            dialogs.add(new Dialog(freeClient, agent));
        }
    }

    public synchronized void addFreeClient(Client client) {
        if (freeAgents.size()==0) {
            freeClients.add(client);
        }
        else {
            Agent freeAgent = freeAgents.pollFirst();
            dialogs.add(new Dialog(client, freeAgent));
        }
    }

    public synchronized List<Agent> getFreeAgents() {
        return new ArrayList<>(freeAgents);
    }

    public synchronized List<Client> getFreeClients() {
        return new ArrayList<>(freeClients);
    }

    public synchronized List<Agent> getAllAgents() {
        List<Agent> agents = new LinkedList<>();
        for (Dialog dialog:dialogs
             ) {
            agents.add((dialog.getAgent()));
        }
        agents.addAll(freeAgents);
        return agents;
    }

    public synchronized Agent getAgent(int id) {
        for (Dialog dialog:dialogs
        ) {
            if (dialog.getAgent().getId() == id)
                return dialog.getAgent();
        }
        for (Agent agent:freeAgents
             ) {
            if (agent.getId() == id)
                return agent;
        }
        return null;
    }

    public synchronized Client getClient(int id) {
        for (Dialog dialog:dialogs
        ) {
            if (dialog.getClient().getId() == id)
                return dialog.getClient();
        }
        for (Client client:freeClients
        ) {
            if (client.getId() == id)
                return client;
        }
        return null;
    }

    public synchronized List<Dialog> getDialogs() {
        return dialogs;
    }

}
