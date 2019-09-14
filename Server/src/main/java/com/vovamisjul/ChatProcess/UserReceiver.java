package com.vovamisjul.ChatProcess;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/UserReceiver")
public class UserReceiver extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = req.getParameter("message");
        String userId = req.getParameter("userId");
        PrintWriter writer = resp.getWriter();
        if (userId == null) {
            if (!message.matches("^/(reg \\w+ \\w+$|exit)")) {
                Responce responce = new Responce(406, "There are no such command");
                writer.println(new Gson().toJson(responce));
                return;
            }
            if (message.startsWith("/reg")) {
                String param = message.substring("/reg ".length());
                String[] params = param.split(" ");
                Responce responce = new Responce(200, Users.addNewUser(params[0], params[1]), params[0]);
                writer.println(new Gson().toJson(responce));
                return;
            }
        }
        int id = Integer.parseInt(userId);
        String type = req.getParameter("userType");
        Dialog dialog = Users.getDialog(id);
        if (dialog != null) {
            if (message.equals("/exit")) {
                dialog.exit(type);
                return;
            }
            dialog.sendTo(type, message);
        }
        else
            Users.getUser(id).addNewMessage(message);

    }
}

class Responce {
    public int code;
    public String message;
    public int userId;
    public String userType;

    public Responce(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Responce(int code, int userId, String userType) {
        this.code = code;
        this.userId = userId;
        this.userType = userType;
    }
}
