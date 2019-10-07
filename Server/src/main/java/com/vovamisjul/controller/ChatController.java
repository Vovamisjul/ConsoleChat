package com.vovamisjul.controller;

import com.vovamisjul.chatlogic.Dialog;
import com.vovamisjul.chatlogic.Message;
import com.vovamisjul.chatlogic.Response;
import com.vovamisjul.chatlogic.Users;
import com.vovamisjul.chatlogic.user.AbstractUser;
import com.vovamisjul.chatlogic.user.Agent;
import com.vovamisjul.chatlogic.user.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {
    protected static final Logger logger = LogManager.getLogger(ChatController.class);

    @PostMapping("exit")
    public void exit(@RequestParam(value="userId") int userId,
                     @RequestParam(value="userType") String userType,
                     Users users) {
        Dialog dialog = users.getDialog(userId);
        if (dialog != null)
            dialog.exit(userType, users);
    }

    @PostMapping("leave")
    public void leave(@RequestParam(value="userId") int userId,
                      Users users) {
        Dialog dialog = users.getDialog(userId);
        if (dialog != null)
            dialog.leave(users);
    }

    @GetMapping("getMessages")
    public List<Message> getMessages(@RequestParam(value="userId") int userId,
                                     @RequestParam(value="userType") String userType,
                                     Users users) {
        Dialog dialog = users.getDialog(userId);
        if (dialog == null)
            return null;
        Message message;
        List<Message> messages = new ArrayList<>();
        while ((message = dialog.pollFrom(userType)) != null) {
            messages.add(message);
        }
        return messages;
    }

    @PostMapping("register")
    public Response register(@RequestParam(value="name") String name,
                             @RequestParam(value="type") String type,
                             Users users) {
        try {
            return new Response("Welcome, " + name + " to chat!", users.addNewUser(type, name), type);
        }
        catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @PostMapping("sendMessage")
    public void sendMessage(@RequestParam(value="userId") int userId,
                             @RequestParam(value="userType") String userType,
                             @RequestParam(value="message") String message,
                            Users users) {
        Dialog dialog = users.getDialog(userId);
        if (dialog != null) {
            dialog.sendTo(userType, message);
        } else {
            AbstractUser user = users.getUser(userId);
            if (user != null)
                user.addNewMessage(message);
        }
    }

    @GetMapping("/freeAgents")
    public List<Agent> getFreeAgents(Users users) {
        return users.getFreeAgents();
    }

    @GetMapping("/allAgents")
    public List<Agent> getAllAgents(Users users) {
        return users.getAllAgents();
    }

    @GetMapping("/agent")
    public Agent getAgent(@RequestParam(value="id") int id,
                          Users users) {
        return users.getAgent(id);
    }

    @GetMapping("/freeAgentsCount")
    public int getFreeAgentsCount(Users users) { return users.getFreeAgents().size(); }

    @GetMapping("/dialogs")
    public List<Dialog> getDialog(Users users) { return users.getDialogs(); }

    @GetMapping("/dialog")
    public Dialog getDialog(@RequestParam(value="id") int id,
                            Users users) { return users.getDialog(id); }

    @GetMapping("/awaitingClients")
    public List<Client> getAwaitingClients(Users users) { return users.getFreeClients(); }

    @GetMapping("/client")
    public Client getClient(@RequestParam(value="id") int id,
                            Users users) {
        return users.getClient(id);
    }
}
