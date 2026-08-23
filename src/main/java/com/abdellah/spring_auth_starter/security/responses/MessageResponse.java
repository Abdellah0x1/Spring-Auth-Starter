package com.abdellah.spring_auth_starter.security.responses;

public class MessageResponse {

    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public MessageResponse(String message) {
        this.message = message;
    }
}
