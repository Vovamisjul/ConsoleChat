package com.vovamisjul.ChatProcess;

import com.google.gson.Gson;
import com.vovamisjul.User.AbstractUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/SendMessage")
public class SendMessage extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger(SendMessage.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = req.getParameter("message");
        String userId = req.getParameter("userId");
        PrintWriter writer = resp.getWriter();
        try {
            int id = Integer.parseInt(userId);
            String type = req.getParameter("userType");
            Dialog dialog = Users.getDialog(id);
            if (dialog != null) {
                dialog.sendTo(type, message);
            } else {
                Users.getUser(id).addNewMessage(message);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
