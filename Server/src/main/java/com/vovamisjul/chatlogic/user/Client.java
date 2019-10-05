package com.vovamisjul.chatlogic.user;

public class Client extends AbstractUser {

    public Client(String name, int id) {
        super(name, id);
    }
    @Override
    public String toString() {
        return "Client " + name;
    }

}
