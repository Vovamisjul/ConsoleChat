package com.vovamisjul.ChatProcess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Leave")
public class Leave extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger(Leave.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String userId = req.getParameter("userId");
            int id = Integer.parseInt(userId);
            Dialog dialog = Users.getDialog(id);
            dialog.leave();
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);}
    }
}
