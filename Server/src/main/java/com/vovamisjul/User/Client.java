package com.vovamisjul.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Client extends AbstractUser {

    public Client(String name, int id) {
        super(name, id);
    }
    @Override
    public String toString() {
        return "Client " + name;
    }

}
