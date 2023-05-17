package com.gonzaga.turingtest;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class TestPopupController {
    @FXML
    private CheckBox message1Text;
    @FXML
    private CheckBox message2Text;

    private static Boolean correct;


    @FXML
    public void initialize(){
        message1Text.setText(MessageSetup.getMessage1().message());
        message2Text.setText(MessageSetup.getMessage2().message());
    }

    public void submit(){
        if(message1Text.isSelected() && MessageSetup.getMessage1().isAI()){
            TestViewController.getLoginView().correctEvent();
        } else if (message2Text.isSelected() && MessageSetup.getMessage2().isAI()) {
            TestViewController.getLoginView().correctEvent();
        }else{
            TestViewController.getLoginView().incorrectEvent();
        }
    }

    public static Boolean getCorrect() {
        return correct;
    }

    public static void setCorrect(Boolean correct) {
        TestPopupController.correct = correct;
    }
}
