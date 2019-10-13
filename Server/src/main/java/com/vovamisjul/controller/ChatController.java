package com.vovamisjul.controller;

import com.vovamisjul.chatlogic.Dialog;
import com.vovamisjul.chatlogic.Message;
import com.vovamisjul.chatlogic.Response;
import com.vovamisjul.chatlogic.Users;
import com.vovamisjul.chatlogic.user.AbstractUser;
import com.vovamisjul.chatlogic.user.Agent;
import com.vovamisjul.chatlogic.user.Client;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    private Users users;
    protected static final Logger logger = LogManager.getLogger(ChatController.class);

    public ChatController(Users users) {
        this.users = users;
    }

    @ApiOperation(value = "Exit from chat")
    @PostMapping("/exit")
    public void exit(@ApiParam(value = "Your id issued at registration", required = true) @RequestParam(value="userId") int userId,
                     @ApiParam(value = "Your type (client/agent)", required = true) @RequestParam(value="userType") String userType) {
        Dialog dialog = users.getDialog(userId);
        if (dialog != null)
            dialog.exit(userType, users);
    }

    @ApiOperation(value = "Leave from chat (you will continue chatting with another agent/client)")
    @PostMapping("/leave")
    public void leave(@ApiParam(value = "Your id issued at registration", required = true) @RequestParam(value="userId") int userId) {
        Dialog dialog = users.getDialog(userId);
        if (dialog != null)
            dialog.leave(users);
    }

    @ApiOperation(value = "Get unread messages")
    @GetMapping("/getMessages")
    public List<Message> getMessages(@ApiParam(value = "Your id issued at registration", required = true) @RequestParam(value="userId") int userId,
                                     @ApiParam(value = "Your type (agent/client)", required = true) @RequestParam(value="userType") String userType) {
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

    @ApiOperation(value = "Register user with name and type", response = Response.class)
    @PostMapping("/register")
    public Response register(@ApiParam(value = "Your name", required = true) @RequestParam(value="name") String name,
                             @ApiParam(value = "Your type (agent/client)", required = true) @RequestParam(value="type") String type) {
        try {
            return new Response("Welcome, " + name + " to chat!", users.addNewUser(type, name), type);
        }
        catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @ApiOperation(value = "Send message to server")
    @PostMapping("/sendMessage")
    public void sendMessage(@ApiParam(value = "Your id issued at registration", required = true) @RequestParam(value="userId") int userId,
                            @ApiParam(value = "Your type (agent/client)", required = true) @RequestParam(value="userType") String userType,
                            @ApiParam(value = "Message to send", required = true) @RequestParam(value="message") String message) {
        Dialog dialog = users.getDialog(userId);
        if (dialog != null) {
            dialog.sendTo(userType, message);
        } else {
            AbstractUser user = users.getUser(userId);
            if (user != null)
                user.addNewMessage(message);
        }
    }

    @ApiOperation(value = "Get free (without client) agents")
    @GetMapping("/freeAgents")
    public List<Agent> getFreeAgents() {
        return users.getFreeAgents();
    }

    @ApiOperation(value = "Get all agents")
    @GetMapping("/allAgents")
    public List<Agent> getAllAgents() {
        return users.getAllAgents();
    }

    @ApiOperation(value = "Get agent by id")
    @GetMapping("/agent")
    public Agent getAgent(@ApiParam(value = "Id of the agent", required = true) @RequestParam(value="id") int id) {
        return users.getAgent(id);
    }

    @ApiOperation(value = "Get free agents count")
    @GetMapping("/freeAgentsCount")
    public int getFreeAgentsCount() { return users.getFreeAgents().size(); }

    @ApiOperation(value = "Get all active dialogs")
    @GetMapping("/dialogs")
    public List<Dialog> getDialog() { return users.getDialogs(); }

    @ApiOperation(value = "Get dialog by id")
    @GetMapping("/dialog")
    public Dialog getDialog(@ApiParam(value = "Id of agent or client in dialog", required = true) @RequestParam(value="id") int id) { return users.getDialog(id); }

    @ApiOperation(value = "Get all awaiting (without agent) clients")
    @GetMapping("/awaitingClients")
    public List<Client> getAwaitingClients() { return users.getFreeClients(); }

    @ApiOperation(value = "Get client by id")
    @GetMapping("/client")
    public Client getClient(@ApiParam(value = "Id of the client", required = true) @RequestParam(value="id") int id) {
        return users.getClient(id);
    }
}
