package com.vovamisjul.chatlogic;

public class Response {
    private String message;
    private int userId;
    private String userType;

    public Response(String message, int userId, String userType) {
        this.message = message;
        this.userId = userId;
        this.userType = userType;
    }
}
