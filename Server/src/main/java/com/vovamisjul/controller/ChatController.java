package com.vovamisjul.controller;

import com.vovamisjul.chatlogic.Dialog;
import com.vovamisjul.chatlogic.Message;
import com.vovamisjul.chatlogic.Response;
import com.vovamisjul.chatlogic.Users;
import com.vovamisjul.chatlogic.user.Agent;
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

    @GetMapping("/getFreeAgents")
    public List<Agent> getFreeAgents() {
        return Users.getFreeAgents();
    }

    @PostMapping("exit")
    public void exit(@RequestParam(value="userId") String userId,
                     @RequestParam(value="userType") String userType) {
        try {
            int id = Integer.parseInt(userId);
            Dialog dialog = Users.getDialog(id);
            dialog.exit(userType);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);}
    }

    @PostMapping("leave")
    public void leave(@RequestParam(value="userId") String userId) {
        try {
            int id = Integer.parseInt(userId);
            Dialog dialog = Users.getDialog(id);
            dialog.leave();
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);}
    }

    @GetMapping("getMessages")
    public List<Message> getMessages(@RequestParam(value="userId") String userId,
                                     @RequestParam(value="userType") String userType) {
        try {
            int id = Integer.parseInt(userId);
            Dialog dialog = Users.getDialog(id);
            Message message;
            List<Message> messages = new ArrayList<>();
            while ((message = dialog.pollFrom(userType)) != null) {
                messages.add(message);
            }
            return messages;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @PostMapping("register")
    public Response register(@RequestParam(value="name") String name,
                             @RequestParam(value="type") String type) {
        return new Response("Welcome, " + name + " to chat!", Users.addNewUser(type, name), type);
    }

    @PostMapping("sendMessage")
    public void sendMessage(@RequestParam(value="userId") String userId,
                             @RequestParam(value="userType") String userType,
                             @RequestParam(value="message") String message) {
        try {
            int id = Integer.parseInt(userId);
            Dialog dialog = Users.getDialog(id);
            if (dialog != null) {
                dialog.sendTo(userType, message);
            } else {
                Users.getUser(id).addNewMessage(message);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
