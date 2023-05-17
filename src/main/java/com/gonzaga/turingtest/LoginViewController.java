package com.gonzaga.turingtest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class LoginViewController {
    private static LoginView loginView;
    @FXML
    private CheckBox typeSelector;
    @FXML
    private TextField IPField;
    @FXML
    private Button loginButton;
    @FXML
    private TextField portField;
    @FXML
    private TextField usernameField;

    @FXML
    public void initialize(){
        try {
            IPField.setText(Inet4Address.getLocalHost().getHostAddress().toString());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void login() throws IOException {
        UserInfo.setIP(IPField.getText());
        UserInfo.setPort(Integer.parseInt(portField.getText()));
        UserInfo.setUsername(usernameField.getText());
        loginView.loginEvent(typeSelector.isSelected());
    }

    public static LoginView getLoginView() {
        return loginView;
    }

    public static void setLoginView(LoginView loginView) {
        LoginViewController.loginView = loginView;
    }
}



