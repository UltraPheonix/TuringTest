package com.gonzaga.turingtest;

class UserInfo {

    private static int Port;

    private static String IP;

    private static String username;

    public static int getPort() {
        return Port;
    }

    public static void setPort(int port) {
        Port = port;
    }

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        UserInfo.IP = IP;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }
}
