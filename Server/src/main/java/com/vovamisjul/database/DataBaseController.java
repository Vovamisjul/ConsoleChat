package com.vovamisjul.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseController {
    private final String db_user = "root";
    private final String db_password = "";
    private final String connectionURL = "jdbc:mysql://localhost:3306/webchat";
    protected final Logger logger = LogManager.getLogger(DataBaseController.class);

    public DataBaseController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public int register(String name, String password, String type) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionURL, db_user, db_password)) {
            PreparedStatement statement = connection.prepareStatement("select * from users where name=?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();;
            if (result.next())
                throw new IllegalArgumentException();
            statement = connection.prepareStatement("insert into users values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, type);
            statement.executeUpdate();
            result = statement.getGeneratedKeys();
            result.next();
            int id =  result.getInt(1);
            statement = connection.prepareStatement("insert into passwords values (?, ?)");
            statement.setInt(1, id);
            statement.setString(2, password);
            statement.executeUpdate();
            return id;
        }
    }

    public int login(String name, String password, StringBuilder type) throws SQLException {
        try(Connection connection = DriverManager.getConnection(connectionURL, db_user, db_password)) {
            PreparedStatement statement = connection.prepareStatement("select * from users join passwords on passwords.id=users.id where name=? and password=?");
            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                type.append(result.getString("type"));
                return result.getInt(1);
            }
            throw new IllegalArgumentException();
        }
    }
}
