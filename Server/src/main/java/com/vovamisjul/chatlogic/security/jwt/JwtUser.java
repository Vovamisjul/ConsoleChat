package com.vovamisjul.chatlogic.security.jwt;

public class JwtUser {
    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    private String type;
    private String password;
    private int id;

    public JwtUser(String type, String password, String id) {
        this.type = type;
        this.password = password;
        this.id = Integer.parseInt(id);
    }
}
