package com.vovamisjul.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseController {
    private final String user = "root";
    private final String password = "";
    private final String connectionURL = "jdbc:mysql://localhost:3306/webchat";
    protected final Logger logger = LogManager.getLogger(DataBaseController.class);

    public DataBaseController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void register(String name, int hash) {
        try(Connection connection = DriverManager.getConnection(connectionURL, user, password)) {
            logger.info("COnnected");
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
