package com.example.apptracker_v1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        stage.setTitle("AppTracker - приложение для мониторинга вашего сайта");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}