package com.gonzaga.turingtest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TestViewController {
    @FXML
    private  TextField inputField;
    private static LoginView loginView;
    @FXML
    private TextArea chatBox;
    @FXML
    private TextField recentMessageBox;
    @FXML
    private Text precentCorrectDisplay;
    @FXML
    private  Text curUserDisplay;

    private Client client;

    private int numCorrect = 0;

    private int numTotal = 0;

    @FXML
    public void initialize(){
        System.out.println("init");
        LoginView.setTestViewController(this);
        client = new Client(UserInfo.getIP(), UserInfo.getPort(), UserInfo.getUsername(), this);
    }

    public void testEvent(ArrayList<Message> answers) throws IOException {
        int num = (int)(Math.random()*50);

        String string = "";
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatBox.appendText(recentMessageBox.getText() + "\r\n");
                for (Message m: answers) {
                    if(m.isAI()){
                        chatBox.appendText("\tAI: " + m.message() + "\r\n");
                    }else{
                        chatBox.appendText("\tHuman: " + m.message() +"\r\n");
                    }
                }
            }
        });

        if(num%2 ==0){
            loginView.questionEvent(answers.get(0),answers.get(1));
        }else{
            loginView.questionEvent(answers.get(1),answers.get(0));
        }
    }

    public void correctEvent(){
        numCorrect++;
        numTotal++;
        client.answerEvent(true);
        precentCorrectDisplay.setText("%"+(((double)(numCorrect / numTotal))*100));
    }
    public void incorrectEvent(){
        numTotal++;
        client.answerEvent(false);
        precentCorrectDisplay.setText("%"+(((double)(numCorrect / numTotal))*100));
    }

    public void setHost(String s){ curUserDisplay.setText("Host: " +s);}

    public static LoginView getLoginView() {
        return loginView;
    }

    public static void setLoginView(LoginView loginView) {
        TestViewController.loginView = loginView;
    }

    @FXML
    public void askQuestion(){
        client.askQuestion(inputField.getText());
        inputField.clear();
    }

    public void setRecent(String s){recentMessageBox.setText(s);
    }
}

class Client{
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private TestViewController controller;
    private Socket socket;


    Client(String IP, int port, String username, TestViewController controller){
        this.controller = controller;
        try {
            this.socket = new Socket(IP, port);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("output created ");
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Input created");
            initSetup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Listen extends Thread{
        @Override
        public void run() {
            ArrayList<Message> message;
            while(true){
                System.out.println("looping");
                try {
                    if(socket.getInputStream().available()>0){
                        ArrayList<Message> messages = new ArrayList<>();
                        while(socket.getInputStream().available()>0){
                            messages.add((Message)inputStream.readObject());
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Client.this.controller.testEvent(messages);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        interrupt();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    interrupt();
                    return;
                }
            }
        }
    }

    public void askQuestion(String question){
        try {
            System.out.println("question sent");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.setRecent(question);
                }
            });
            outputStream.writeObject(question);
            outputStream.flush();

            System.out.println("final send ");
            new Listen().start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void answerEvent(Boolean b){
        try {
            outputStream.writeObject(b);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initSetup(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.writeObject(UserInfo.getUsername());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while(true){
                    try {
                        if(inputStream.available()>0){
                            String str = inputStream.readUTF();
                            System.out.println("username recieved");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    controller.setHost(str);
                                }
                            });
                            return;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}


