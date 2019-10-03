package com.vovamisjul.ChatProcess;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Register")
public class Register extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger(SendMessage.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String type = req.getParameter("type");
        if (name == null || type == null)
            return;
        Response responce = new Response("Welcome, " + name + " to chat!", Users.addNewUser(type, name), type);
        PrintWriter writer = resp.getWriter();
        writer.println(new Gson().toJson(responce));
    }
}

class Response {
    public String message;
    public int userId;
    public String userType;

    public Response(String message, int userId, String userType) {
        this.message = message;
        this.userId = userId;
        this.userType = userType;
    }
}