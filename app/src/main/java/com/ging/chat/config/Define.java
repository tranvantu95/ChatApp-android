package com.ging.chat.config;

public class Define {

    public static class Socket {
        private static final boolean LOCAL = false;
        public static final String HOST = LOCAL ? "http://localhost:8080" : "https://chat-app-ging-nodejs.herokuapp.com";
    }

}
