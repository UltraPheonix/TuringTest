package com.gonzaga.turingtest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostViewController {
    /**
     * This is my HostViewController, it controls everything for my host window
     *
     * Functionality-
     *
     * Hosts should be able to confer back and forth, untill a user asks a question, which will start the questio
     * This question sequence involves calling the AI and asking for its responce, and then the Host answering the question,
     *
     * This question sequence will end with a popup on the user side, explained in the user code
     */
    @FXML
    private TextArea chatBox;
    @FXML
    private  TextField recentMessageBox;
    @FXML
    private  Text precentCorrectDisplay;
    @FXML
    private Text curUserDisplay;
    private int numAnswered = 0;
    private int numCorrect = 0;
    private HostingClient client;
    private static LoginView loginView;


    @FXML
    public void initialize(){
        this.client = new HostingClient(UserInfo.getPort(), UserInfo.getUsername(), this, loginView);
    }

    public void addCorrect(){
        numCorrect ++;
        numAnswered++;
        precentCorrectDisplay.setText("% "+(((numCorrect / numAnswered))*100));
    }

    public void addIncorrect(){
        numAnswered++;
        precentCorrectDisplay.setText("% "+(((numCorrect / numAnswered))*100));
    }
    public void setRecent(String s){
        recentMessageBox.setText(s);
    }

    public void appendTextbox(String s){
        chatBox.appendText(s + "\r\n");
    }

    public void setCurUser(String s){curUserDisplay.setText("Current User: " + s);}

    public static LoginView getLoginView() {
        return loginView;
    }

    public static void setLoginView(LoginView loginView) {
        HostViewController.loginView = loginView;
    }
}


class HostingClient extends Thread {

    private Socket socket;

    private String username;

    private String playerUsername;

    private transient ObjectInputStream inputStream;

    private transient ObjectOutputStream outputStream;

    private HostViewController hostViewController;

    private int port;

    private LoginView loginView;


    HostingClient(int port, String hostUsername, HostViewController hostViewController, LoginView loginView){
        this.hostViewController = hostViewController;
        this.username = hostUsername;
        this.port = port;
        this.loginView = loginView;
        this.start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            this.socket = serverSocket.accept();
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Boolean first = true;

        while(true){
            try {
                if(socket.getInputStream().available()>0){
                    System.out.println("running");
                    Object obj = inputStream.readObject();
                    StringCheck:
                    if(obj instanceof String){
                        String message = (String)obj;
                        if(first){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    hostViewController.setCurUser(message);
                                }
                            });
                            System.out.println("sending username back ");
                            outputStream.writeUTF(username);
                            outputStream.flush();
                            first = false;
                            break StringCheck;
                        }
                        MessageSetup.setMessage1(null);
                        MessageSetup.setMessage2(null);
                        MessageSetup.setUserMessage(message);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                hostViewController.setRecent(message);
                            }
                        });

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MessageSetup.setMessage2(new Message(TestControllerAI.sendMessage(message), true));
                            }
                        });
                        thread.start();
                        AnswerPopupController.setLoginView(loginView);
                        loginView.answerEvent();

                        int numTimes = 0;
                        waiting_loop:
                        while(true){
                            if(numTimes%10 == 0){
                                System.out.println("in loop");
                                System.out.println(MessageSetup.getMessage1());
                                System.out.println(MessageSetup.getMessage2());
                            }
                            numTimes++;
                            if(!(MessageSetup.getMessage1() == null) && !(MessageSetup.getMessage2() == null)){

                                hostViewController.appendTextbox(MessageSetup.getUserMessage()+ "\r\n\tAI: " + MessageSetup.getMessage2().message()
                                                                    + "\r\n" + "\tHuman: " + MessageSetup.getMessage1().message());

                                outputStream.writeObject(MessageSetup.getMessage1());
                                outputStream.flush();
                                outputStream.writeObject(MessageSetup.getMessage2());
                                outputStream.flush();
                                System.out.println("sent");
                                break waiting_loop;
                            }
                        }
                    }else if(obj instanceof Boolean){
                        System.out.println("boolean recieved");
                        if((boolean) obj){
                            hostViewController.addCorrect();
                        }else{
                            hostViewController.addIncorrect();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
