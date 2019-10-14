package com.vovamisjul.controller;

import com.vovamisjul.chatlogic.Dialog;
import com.vovamisjul.chatlogic.Message;
import com.vovamisjul.chatlogic.Response;
import com.vovamisjul.chatlogic.Users;
import com.vovamisjul.chatlogic.security.jwt.JwtTokenProvider;
import com.vovamisjul.chatlogic.security.jwt.JwtUser;
import com.vovamisjul.chatlogic.user.AbstractUser;
import com.vovamisjul.chatlogic.user.Agent;
import com.vovamisjul.chatlogic.user.Client;
import com.vovamisjul.database.DataBaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private Users users;

    private JwtTokenProvider jwtTokenProvider;

    private DataBaseController dataBaseController;
    private JwtTokenProvider tokenProvider;

    protected static final Logger logger = LogManager.getLogger(ChatController.class);

    public ChatController(Users users, JwtTokenProvider jwtTokenProvider, DataBaseController dataBaseController,
                          JwtTokenProvider tokenProvider) {
        this.users = users;
        this.jwtTokenProvider = jwtTokenProvider;
        this.dataBaseController = dataBaseController;
        this.tokenProvider = tokenProvider;
    }

    @ApiOperation(value = "Exit from chat")
    @PostMapping("/exit")
    public void exit(@RequestHeader("Authorization") String token) {
        JwtUser user = tokenProvider.validateTokenAll(token);
        Dialog dialog = users.getDialog(user.getId());
        if (dialog != null)
            dialog.exit(user.getType(), users);
    }

    @ApiOperation(value = "Leave from chat (you will continue chatting with another agent/client)")
    @PostMapping("/leave")
    public void leave(@RequestHeader("Authorization") String token) {
        JwtUser user = tokenProvider.validateTokenAll(token);
        Dialog dialog = users.getDialog(user.getId());
        if (dialog != null)
            dialog.leave(users);
    }

    @ApiOperation(value = "Get unread messages")
    @GetMapping("/getMessages")
    public List<Message> getMessages(@RequestHeader("Authorization") String token) {
        JwtUser user = tokenProvider.validateTokenAll(token);
        Dialog dialog = users.getDialog(user.getId());
        if (dialog == null)
            return null;
        Message message;
        List<Message> messages = new ArrayList<>();
        while ((message = dialog.pollFrom(user.getType())) != null) {
            messages.add(message);
        }
        return messages;
    }

    @ApiOperation(value = "Register user with name and type", response = Response.class)
    @PostMapping("/register")
    public ResponseEntity register(@ApiParam(value = "Your name", required = true) @RequestParam(value="name") String name,
                                   @ApiParam(value = "Password", required = true) @RequestParam(value="password") String password,
                                   @ApiParam(value = "Your type (agent/client)", required = true) @RequestParam(value="type") String type) {
        try {
            int id = dataBaseController.register(name, password, type);
            users.addNewUser(type, name, id);
            String token = jwtTokenProvider.createToken(id, type, password);
            Map<Object, Object> responce = new HashMap<>();
            responce.put("message", "Welcome, " + name + " to chat!");
            responce.put("userId", id);
            responce.put("userType", type);
            responce.put("token", token);

            return ResponseEntity.ok(responce);
        }
        catch (IllegalArgumentException | SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @ApiOperation(value = "Login user with name and password", response = Response.class)
    @PostMapping("/login")
    public Response login(@ApiParam(value = "Your name", required = true) @RequestParam(value="name") String name,
                             @ApiParam(value = "Password", required = true) @RequestParam(value="password") String password) {
        try {
            StringBuilder type = new StringBuilder();
            int id = dataBaseController.login(name, password, type);
            users.addNewUser(type.toString(), name, id);
            String token = jwtTokenProvider.createToken(id, type.toString(), password);
            return new Response("Welcome, " + name + " to chat!", id, type.toString(), token);
        }
        catch (IllegalArgumentException | SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @ApiOperation(value = "Send message to server")
    @PostMapping("/sendMessage")
    public void sendMessage(@RequestHeader("Authorization") String token,
                            @ApiParam(value = "Message to send", required = true) @RequestParam(value="message") String message) {
        JwtUser user = tokenProvider.validateTokenAll(token);
        Dialog dialog = users.getDialog(user.getId());
        if (dialog != null) {
            dialog.sendTo(user.getType(), message);
        } else {
            AbstractUser aUser = users.getUser(user.getId());
            if (aUser != null)
                aUser.addNewMessage(message);
        }
    }

    @ApiOperation(value = "Get free (without client) agents")
    @GetMapping("/freeAgents")
    public List<Agent> getFreeAgents(@RequestHeader("Authorization") String token) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getFreeAgents();
        else
            return null;
    }

    @ApiOperation(value = "Get all agents")
    @GetMapping("/allAgents")
    public List<Agent> getAllAgents(@RequestHeader("Authorization") String token) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getAllAgents();
        else
            return null;
    }

    @ApiOperation(value = "Get agent by id")
    @GetMapping("/agent")
    public Agent getAgent(@RequestHeader("Authorization") String token,
                          @ApiParam(value = "Id of the agent", required = true) @RequestParam(value="id") int id) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getAgent(id);
        else
            return null;
    }

    @ApiOperation(value = "Get free agents count")
    @GetMapping("/freeAgentsCount")
    public int getFreeAgentsCount(@RequestHeader("Authorization") String token) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getFreeAgents().size();
        else
            return -1;
    }

    @ApiOperation(value = "Get all active dialogs")
    @GetMapping("/dialogs")
    public List<Dialog> getDialog(@RequestHeader("Authorization") String token) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getDialogs();
        else
            return null;
    }

    @ApiOperation(value = "Get dialog by id")
    @GetMapping("/dialog")
    public Dialog getDialog(@RequestHeader("Authorization") String token,
                            @ApiParam(value = "Id of agent or client in dialog", required = true) @RequestParam(value="id") int id) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getDialog(id);
        else
            return null;
    }

    @ApiOperation(value = "Get all awaiting (without agent) clients")
    @GetMapping("/awaitingClients")
    public List<Client> getAwaitingClients(@RequestHeader("Authorization") String token) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getFreeClients();
        else
            return null;
    }

    @ApiOperation(value = "Get client by id")
    @GetMapping("/client")
    public Client getClient(@RequestHeader("Authorization") String token,
                            @ApiParam(value = "Id of the client", required = true) @RequestParam(value="id") int id) {
        if (tokenProvider.validateTokenAgent(token) != null)
            return users.getClient(id);
        else
            return null;
    }
}
