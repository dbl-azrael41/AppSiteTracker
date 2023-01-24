package com.example.apptracker_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthorizationController {

    @FXML
    private Button auth_enter;

    @FXML
    private TextField auth_login;

    @FXML
    private PasswordField auth_pass;

    @FXML
    private Button auth_reg;

    @FXML
    void onEnter(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        Main.stage.setTitle("AppTracker - приложение для мониторинга вашего сайта");
        Main.stage.setScene(scene);
        Main.stage.show();
    }

    @FXML
    void onReg(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        Main.stage.setTitle("AppTracker - приложение для мониторинга вашего сайта");
        Main.stage.setScene(scene);
        Main.stage.show();
    }

}
