package com.ging.chat.config;

import com.ging.chat.R;

import java.util.HashMap;
import java.util.Map;

public class Define {

    private static final boolean USE_LOCAL = false;

    public static class API {
        public static final String BASE_URL =
                USE_LOCAL ? "http://192.168.23.102:8080/" : "https://chat-app-ging-nodejs.herokuapp.com/";
    }

    public static class Socket {
        public static final String HOST =
                USE_LOCAL ? "http://192.168.23.102:8080" : "https://chat-app-ging-nodejs.herokuapp.com";
    }

    public static class Answer {
        public static final String A = "A";
        public static final String B = "B";
        public static final String C = "C";
        public static final String D = "D";

        public static final Map<String, Integer> mapIds = new HashMap<>();
        public static void buildMapIds() {
            mapIds.put(Define.Answer.A, R.id.answer_a);
            mapIds.put(Define.Answer.B, R.id.answer_b);
            mapIds.put(Define.Answer.C, R.id.answer_c);
            mapIds.put(Define.Answer.D, R.id.answer_d);
        }
    }

}
