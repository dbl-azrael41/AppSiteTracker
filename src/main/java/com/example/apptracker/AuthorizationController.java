/*
Форма: Authorization
Язык: Java
Краткое описание:
Форма авторизации пользователей.

Функции, используемые в форме:
onEnter - функция вызова главной формы в случае успешной авторизации;
onReg - функция вызова формы регистрации.
*/
package com.example.apptracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationController {

    @FXML
    private Button auth_enter;

    @FXML
    private TextField auth_login;

    @FXML
    private PasswordField auth_pass;

    @FXML
    private Button auth_reg;

    /*
    onEnter - функция авторизации пользователей.
    Локальные переменные:
    stage - переменная, запускающая форму;
    fxmlLoader - переменная, хранящая в себе fxml-код формы;
    scene - переменная, задающая размеры формы.
    */
    @FXML
    void onEnter(ActionEvent event) throws IOException, ClassNotFoundException, ParseException {
        Main.conn = new ConnectionToServer(Main.Server_IP, 8000);

        boolean rightLogin = false;
        boolean rightPass = false;
        ArrayList<User> users = Main.conn.getUsers();

        // Проверка введенных пользователем данных

        for (var k: users) {
            if (k.login.equals(auth_login.getText())) {
                rightLogin = true;
                if (k.pass.equals(auth_pass.getText())) {
                    rightPass = true;

                    //Сохранение данных текущего пользователя для дальнейшего использования

                    Main.Id = k.id;
                    Main.Login = k.login;
                    Main.Password = k.pass;
                    Main.Status = k.status;
                    Main.Tg_ID = k.tg_id;
                    break;
                }
            }
        }

        // Изменение стилей поля для ввода логина в случае, если пользователя с введенным логином не существует

        if (!rightLogin) {
            auth_login.setStyle("-fx-border-color: red");
            Tooltip tooltip = new Tooltip("Пользователя с таким логином не существует");
            tooltip.setShowDelay(Duration.seconds(0.2));
            auth_login.setTooltip(tooltip);
            auth_pass.setStyle("-fx-border-color: silver");
            auth_pass.setTooltip(null);

            // Изменение стилей поля для ввода пароля в случае, если введенный пароль недействителен

        } else if (!rightPass) {
            auth_login.setStyle("-fx-border-color: silver");
            auth_login.setTooltip(null);
            auth_pass.setStyle("-fx-border-color: red");
            Tooltip tooltip = new Tooltip("Неверный пароль");
            tooltip.setShowDelay(Duration.seconds(0.2));
            auth_pass.setTooltip(tooltip);
        } else {

            // Установка стилей по умолчанию

            auth_login.setStyle("-fx-border-color: silver");
            auth_login.setTooltip(null);
            auth_pass.setStyle("-fx-border-color: silver");
            auth_pass.setTooltip(null);

            // Отображение главной формы в случае успешной авторизации

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 650);
            Main.stage.setTitle("AppTracker - приложение для мониторинга вашего сайта");
            Main.stage.setScene(scene);
            Main.stage.show();
        }
    }

    /*
    onReg - функция вызова формы "Регистрации".
    Локальные переменные:
    stage - переменная, запускающая форму;
    fxmlLoader - переменная, хранящая в себе fxml-код формы;
    scene - переменная, задающая размеры формы.
    */
    @FXML
    void onReg(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        Main.stage.setTitle("AppSiteTracker - приложение для мониторинга вашего сайта");
        Main.stage.setScene(scene);
        Main.stage.show();
    }
}
