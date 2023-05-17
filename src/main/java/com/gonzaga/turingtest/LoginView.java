package com.gonzaga.turingtest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView extends Application {

    private Stage mainStage;

    private Scene mainScene;

    private static TestViewController testViewController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.mainStage = primaryStage;

        LoginViewController.setLoginView(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gonzaga/turingtest/LoginView.fxml"));

        Parent root = loader.load();

        mainStage.setScene(new Scene(root));

        mainStage.show();

        TestViewController.setLoginView(this);

        HostViewController.setLoginView(this);

    }

    public void loginEvent(Boolean host) throws IOException {
        Parent root;
        if(host){
            root = FXMLLoader.load(getClass().getResource("/com/gonzaga/turingtest/HostView.fxml"));
            HostViewController.setLoginView(this);
        }else{
            root = FXMLLoader.load(getClass().getResource("/com/gonzaga/turingtest/TestView.fxml"));
        }
        Scene scene = new Scene(root);
        mainScene = scene;
        mainStage.setScene(scene);
    }

    public void changeScene(Scene scene){
        Platform.runLater(()->{
            mainStage.setScene(scene);
        });
    }


    public void questionEvent(Message message1, Message message2) throws IOException {
        MessageSetup.setMessage1(message1);
        MessageSetup.setMessage2(message2);
        Parent root = FXMLLoader.load(getClass().getResource("/com/gonzaga/turingtest/TestPopup.fxml"));
        changeScene(new Scene(root));
    }

    public void answerEvent() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/gonzaga/turingtest/AnswerPopup.fxml"));

        changeScene(new Scene(root));
    }

    public void correctEvent(){
        testViewController.correctEvent();
        mainView();
    }

    public void incorrectEvent(){
        testViewController.incorrectEvent();
        mainView();
    }

    public void mainView(){
        changeScene(mainScene);
    }

    public static TestViewController getTestViewController() {
        return testViewController;
    }

    public static void setTestViewController(TestViewController testViewController) {
        LoginView.testViewController = testViewController;
    }

    public static void main(String[] args) {
        launch();
    }
}




