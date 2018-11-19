package com.ging.chat.config;

import com.ging.chat.R;

import java.util.HashMap;
import java.util.Map;

public class Define {

    public static class Socket {
        private static final boolean LOCAL = false;
        public static final String HOST = LOCAL ? "http://localhost:8080" : "https://chat-app-ging-nodejs.herokuapp.com";
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
