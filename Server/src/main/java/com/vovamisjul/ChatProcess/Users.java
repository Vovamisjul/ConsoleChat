package com.vovamisjul.ChatProcess;
import com.vovamisjul.User.AbstractUser;
import com.vovamisjul.User.Agent;
import com.vovamisjul.User.Client;

import java.util.*;
public class Users {
    private static ArrayDeque<Client> freeClients = new ArrayDeque<>();
    private static ArrayDeque<Agent> freeAgents = new ArrayDeque<>();
    private static List<Dialog> dialogs = new ArrayList<>();
    private static int id = 0;

    public static synchronized int addNewUser(String type, String name) {
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
        }
        id++;
        return id-1;
    }

    public static synchronized Dialog getDialog(int userId) {
        for (Dialog dialog: dialogs
             ) {
            if (dialog.getAgent().getId() == userId || dialog.getClient().getId() == userId)
                return dialog;
        }
        return null;
    }

    public static synchronized AbstractUser getUser(int id) {
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

    public static synchronized void deleteDialog(Dialog dialog) {
        dialogs.remove(dialog);
    }

    public static synchronized void addFreeAgent(Agent agent) {
        if (freeClients.size()==0) {
            freeAgents.add(agent);
        }
        else {
            Client freeClient = freeClients.pollFirst();
            dialogs.add(new Dialog(freeClient, agent));
        }
    }

    public static synchronized void addFreeClient(Client client) {
        if (freeAgents.size()==0) {
            freeClients.add(client);
        }
        else {
            Agent freeAgent = freeAgents.pollFirst();
            dialogs.add(new Dialog(client, freeAgent));
        }
    }
}
