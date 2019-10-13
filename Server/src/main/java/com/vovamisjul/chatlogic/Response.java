package com.vovamisjul.chatlogic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public class Response {
    private String message;
    private int userId;
    private String userType;
    private String token;

    public Response(String message, int userId, String userType, String token) {
        this.message = message;
        this.userId = userId;
        this.userType = userType;
        this.token = token;
    }
}
