package com.vovamisjul.ChatProcess;
import com.vovamisjul.User.AbstractUser;
import com.vovamisjul.User.Agent;
import com.vovamisjul.User.Client;

import java.util.*;

// This class is used by many threads but it isn't thread-safe. 
public class Users {
    
    // User interfaces Deque, List instead of ArrayDeque, ArrayList.
    private static ArrayDeque<Client> freeClients = new ArrayDeque<>();
    private static ArrayDeque<Agent> freeAgents = new ArrayDeque<>();
    private static ArrayList<Dialog> dialogs = new ArrayList<>();
    private static int id = 0;

    public static int addNewUser(String type, String name) {
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

    public static Dialog getDialog(int id) {
        for (Dialog dialog: dialogs
             ) {
            if (dialog.getAgent().getId()==id || dialog.getClient().getId()==id)
                return dialog;
        }
        return null;
    }
    public static AbstractUser getUser(int id) {
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

    public static void deleteDialog(Dialog dialog) {
        dialogs.remove(dialog);
    }

    public static void addFreeAgent(Agent agent) {
        if (freeClients.size()==0) {
            freeAgents.add(agent);
        }
        else {
            Client freeClient = freeClients.pollFirst();
            dialogs.add(new Dialog(freeClient, agent));
        }
    }

    public static void addFreeClient(Client client) {
        if (freeAgents.size()==0) {
            freeClients.add(client);
        }
        else {
            Agent freeAgent = freeAgents.pollFirst();
            dialogs.add(new Dialog(client, freeAgent));
        }
    }
}
