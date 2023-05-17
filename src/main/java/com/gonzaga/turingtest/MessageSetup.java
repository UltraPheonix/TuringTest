package com.gonzaga.turingtest;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

class MessageSetup {

    private static String userMessage;
    private static Message message1;

    private static Message message2;

    public static String getUserMessage() {
        return userMessage;
    }

    public static void setUserMessage(String userMessage) {
        MessageSetup.userMessage = userMessage;
    }

    public static Message getMessage1() {
        return message1;
    }

    public static void setMessage1(Message message1) {
        MessageSetup.message1 = message1;
    }

    public static Message getMessage2() {
        return message2;
    }

    public static void setMessage2(Message message2) {
        MessageSetup.message2 = message2;
    }
}

class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private final String message;
    private final boolean isAI;

    Message(String message, Boolean isAI) {
        this.message = message;
        this.isAI = isAI;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Message) obj;
        return Objects.equals(this.message, that.message) &&
                Objects.equals(this.isAI, that.isAI);
    }

    @Override
    public String toString() {
        return "Message[" +
                "message=" + message + ", " +
                "isAI=" + isAI + ']';
    }

    public String message() {
        return message;
    }

    public Boolean isAI() {
        return isAI;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, isAI);
    }

}
