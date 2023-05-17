package com.gonzaga.turingtest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestControllerAI{

    private static final String fileUrl = System.getProperty("user.dir") + "/src/main/java/com/gonzaga/turingtest/AIHandler.py";


    public static String sendMessage(String message){
        try {
            ProcessBuilder builder = new ProcessBuilder("Python3", fileUrl, message);
            Process process = builder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        System.out.println(TestControllerAI.sendMessage("Whats 1+1"));
    }

}
