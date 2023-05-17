package com.gonzaga.turingtest;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AnswerPopupController {
    @FXML
    private TextField hostAnswer;
    @FXML
    private Text UserQuestion;
    private static LoginView loginView;

    public void initialize(){
        UserQuestion.setText(MessageSetup.getUserMessage());
    }

    public static LoginView getLoginView() {
        return loginView;
    }

    public static void setLoginView(LoginView loginView) {
        AnswerPopupController.loginView = loginView;
    }

    @FXML
    public void onSubmit(){
        MessageSetup.setMessage1(new Message(hostAnswer.getText(), false));
        loginView.mainView();
    }
}
