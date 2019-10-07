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
                     @RequestParam(value="userType") String userType) {
        Dialog dialog = Users.getDialog(userId);
        if (dialog != null)
            dialog.exit(userType);
    }

    @PostMapping("leave")
    public void leave(@RequestParam(value="userId") int userId) {
        Dialog dialog = Users.getDialog(userId);
        if (dialog != null)
            dialog.leave();
    }

    @GetMapping("getMessages")
    public List<Message> getMessages(@RequestParam(value="userId") int userId,
                                     @RequestParam(value="userType") String userType) {
        Dialog dialog = Users.getDialog(userId);
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
                             @RequestParam(value="type") String type) {
        try {
            return new Response("Welcome, " + name + " to chat!", Users.addNewUser(type, name), type);
        }
        catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @PostMapping("sendMessage")
    public void sendMessage(@RequestParam(value="userId") int userId,
                             @RequestParam(value="userType") String userType,
                             @RequestParam(value="message") String message) {
        Dialog dialog = Users.getDialog(userId);
        if (dialog != null) {
            dialog.sendTo(userType, message);
        } else {
            AbstractUser user = Users.getUser(userId);
            if (user != null)
                user.addNewMessage(message);
        }
    }

    @GetMapping("/freeAgents")
    public List<Agent> getFreeAgents() {
        return Users.getFreeAgents();
    }

    @GetMapping("/allAgents")
    public List<Agent> getAllAgents() {
        return Users.getAllAgents();
    }

    @GetMapping("/agent")
    public Agent getAgent(@RequestParam(value="id") int id) {
        return Users.getAgent(id);
    }

    @GetMapping("/freeAgentsCount")
    public int getFreeAgentsCount() { return Users.getFreeAgents().size(); }

    @GetMapping("/dialogs")
    public List<Dialog> getDialog() { return Users.getDialogs(); }

    @GetMapping("/dialog")
    public Dialog getDialog(@RequestParam(value="id") int id) { return Users.getDialog(id); }

    @GetMapping("/awaitingClients")
    public List<Client> getAwaitingClients() { return Users.getFreeClients(); }

    @GetMapping("/client")
    public Client getClient(@RequestParam(value="id") int id) {
        return Users.getClient(id);
    }
}
