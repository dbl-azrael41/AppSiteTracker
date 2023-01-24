package com.example.apptracker_v1;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML
    private CheckBox reg_admin;

    @FXML
    private Button reg_btn;

    @FXML
    public ChoiceBox<String> reg_choiceB;

    @FXML
    private Button reg_close;

    @FXML
    private TextField reg_login;

    @FXML
    private PasswordField reg_mail;

    @FXML
    private PasswordField reg_pass;

    @FXML
    private Label reg_quastion;

    private String[] answer = {"Да, на почту", "Нет"};




    @FXML
    void onClose(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        Main.stage.setTitle("AppTracker - приложение для мониторинга вашего сайта");
        Main.stage.setScene(scene);
        Main.stage.show();
    }

    @FXML
    void onReg(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        Main.stage.setTitle("AppTracker - приложение для мониторинга вашего сайта");
        Main.stage.setScene(scene);
        Main.stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reg_choiceB.getItems().addAll(answer);
        reg_choiceB.setValue(answer[0]);
    }
}
