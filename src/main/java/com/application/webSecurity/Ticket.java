package com.application.webSecurity;

public class Ticket {
    private String token;

    public Ticket(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "token='" + token + '\'' +
                '}';
    }
}
