package ru.itmo.wp.servlet;

import java.util.HashMap;

class Message extends HashMap <String, String> {
    Message(String user, String text) {
        this.put("user", user);
        this.put("text", text);
    }
}
