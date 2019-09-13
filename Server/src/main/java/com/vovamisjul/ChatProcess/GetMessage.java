package com.vovamisjul.ChatProcess;

import com.google.gson.Gson;
import org.apache.logging.log4j.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/GetMessage")
public class GetMessage extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger(GetMessage.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        if (userId == null)
            return;
        int id = Integer.parseInt(userId);
        Dialog dialog = Users.getDialog(id);
        if (dialog == null)
            return;
        String type = req.getParameter("userType");
        Message message;
        ArrayList<Message> messages = new ArrayList<>();
        while ((message = dialog.pollFrom(type)) != null) {
            messages.add(message);
        }
        PrintWriter writer = resp.getWriter();
        writer.println(new Gson().toJson(messages));
    }

}
