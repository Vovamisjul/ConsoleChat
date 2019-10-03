package com.vovamisjul.ChatProcess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Exit")
public class Exit extends HttpServlet {
    protected static final Logger logger = LogManager.getLogger(Exit.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String userId = req.getParameter("userId");
            String type = req.getParameter("userType");
            int id = Integer.parseInt(userId);
            Dialog dialog = Users.getDialog(id);
            dialog.exit(type);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);}
    }
}