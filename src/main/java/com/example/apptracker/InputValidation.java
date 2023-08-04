/*
Класс InputValidation
Язык: Java
Краткое описание:
Данный класс проверяет вводимые пользователем значения.

Локальные переменные:
isCheckUniqueLog - определяет, нужно ли проверять логин на уникальность;
login - поле с логином;
password - поле с паролем;
tg_id - поле с Телеграм id.

Функции, используемая в классе:
InputValidation - конструктор класса InputValidation;
checkInput - проверяет вводимые пользователем данные.
*/
package com.example.apptracker;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class InputValidation {
    public boolean isCheckUniqueLog = true;
    public TextField login;
    public TextField password;
    public TextField tg_id = null;

    /*
    InputValidation - конструктор класса InputValidation.
    */
    public InputValidation() {}

    /*
    InputValidation - конструктор класса InputValidation.
    Формальные параметры:
    login - поле с введенным логином;
    password - поле с введенным паролем;
    tg_id - поле с введенным Телеграм id.
    */
    public InputValidation(TextField login, TextField password, TextField tg_id) {
        this.login = login;
        this.password = password;
        this.tg_id = tg_id;
    }

    /*
    InputValidation - конструктор класса InputValidation.
    Формальные параметры:
    login - поле с введенным логином;
    password - поле с введенным паролем.
    */
    public InputValidation(TextField login, TextField password) {
        this.login = login;
        this.password = password;
    }

    /*
    checkInput - проверяет вводимые пользователем данные.
    Локальные переменные:
    users - список с информацией о пользователях;
    isAdd - переменная, хранящая в себе результат проверок входных данных;
    rightLog - проверка логина на валидность;
    checkTgID - переменная, определяющая нужно ли проверять поле с id телеграмма;
    unique_log - переменная, хранящая в себе результат проверки логина на уникальность;
    unique_tg_id - переменная, хранящая в себе результат проверки id телеграм аккаунта на уникальность;
    user - пользователь, информация о котором хранится в базе данных.
    */
    public boolean checkInput() throws IOException, ParseException {

        //Получение списка с информацией о зарегистрированных пользователях

        ArrayList<User> users = new ArrayList<>(Main.conn.getUsers());

        boolean isAdd = false;
        boolean rightLog;
        boolean rightPass;

        boolean checkTgID = true;

        //Проверка на то, нужно ли проверять id телеграмма

        if (this.tg_id == null) {
            checkTgID = false;
        } else if (this.tg_id.getText().equals("None") || this.tg_id.getText().equals("") || this.tg_id.getText().equals(Main.Tg_ID)) {
            checkTgID = false;
        }

        //Стилизация полей для ввода

        login.setStyle("-fx-border-color: silver");
        password.setStyle("-fx-border-color: silver");
        if (!(tg_id == null)) {
            tg_id.setStyle("-fx-border-color: silver");
        }


        //Проверка логина на соответствие требованиям

        Tooltip tooltip_login = new Tooltip("Логин должен содержать только латинские буквы, цифры и знак \"_\".\nДлина пароля должна быть не менее 6 символов и не более 20.");
        tooltip_login.setShowDuration(Duration.INDEFINITE);
        tooltip_login.setShowDelay(Duration.ZERO);


        if (this.login.getText().matches("^[a-zA-Z0-9_]{6,20}$")){
            rightLog = true;
            Tooltip.uninstall(this.login, tooltip_login);
        } else {
            rightLog = false;

            //Изменение стилей поля для ввода логина

            this.login.setStyle("-fx-border-color: red");
            this.login.setTooltip(tooltip_login);
            this.login.setOnMouseEntered(event -> tooltip_login.show(this.login, event.getScreenX(), event.getScreenY()));
            this.login.setOnMouseExited(event -> tooltip_login.hide());
        }

        //Проверка пароля на соответствие требованиям

        Tooltip tooltip_pas = new Tooltip("Пароль должен отвечать следующим требованиям:\n-содежрать хотя бы одну заглавную букву;\n-содержать хотя бы одну строчную букву;\n-содержать хотя бы одну цифр;\n-не должен содержать спец символы;\n-допустимая длина пароля от 6 до 20 символов.");
        tooltip_pas.setShowDuration(Duration.INDEFINITE);
        tooltip_pas.setShowDelay(Duration.ZERO);

        if (this.password.getText().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z\\d_]{6,20}$")){
            rightPass = true;
            Tooltip.uninstall(this.password, tooltip_pas);
        } else {
            rightPass = false;

            //Изменение стилей поля для ввода логина

            this.password.setStyle("-fx-border-color: red");
            this.password.setTooltip(tooltip_pas);
            this.password.setOnMouseEntered(event -> tooltip_pas.show(this.password, event.getScreenX(), event.getScreenY()));
            this.password.setOnMouseExited(event -> tooltip_pas.hide());
        }

        if (rightLog && rightPass) {
            boolean unique_log = true;
            boolean unique_tg_id = true;

            //Проверка логина и id телеграм аккаунта на уникальность

            for (User user: users) {
                if (user.login.equals(this.login.getText()) && isCheckUniqueLog) {
                    unique_log = false;
                }
                if (checkTgID) {
                    if (user.tg_id.equals(this.tg_id.getText())) {
                        unique_tg_id = false;
                    }
                }
            }

            if (!unique_log) {

                //Изменение стилей для поля с логином, если введенный логин не уникален

                this.login.setStyle("-fx-border-color: red");
                tooltip_login.setText("Пользователь с таким логином уже зарегистрирован");
                this.login.setTooltip(tooltip_login);
                this.login.setOnMouseEntered(event -> tooltip_login.show(this.login, event.getScreenX(), event.getScreenY()));
                this.login.setOnMouseExited(event -> tooltip_login.hide());
            } else {
                Tooltip.uninstall(this.login, tooltip_login);

                Tooltip tooltip_tg_id = new Tooltip("Пользователь с таким id уже зарегистрирован");
                tooltip_login.setShowDuration(Duration.INDEFINITE);
                tooltip_login.setShowDelay(Duration.ZERO);

                isAdd = true;
                if (checkTgID) {
                    if (!unique_tg_id) {

                        //Изменение стилей для поля с id телеграм аккаунта, если введенный id не уникален

                        this.tg_id.setStyle("-fx-border-color: red");
                        this.tg_id.setTooltip(tooltip_tg_id);
                        this.tg_id.setOnMouseEntered(event -> tooltip_tg_id.show(this.tg_id, event.getScreenX(), event.getScreenY()));
                        this.tg_id.setOnMouseExited(event -> tooltip_tg_id.hide());

                        isAdd = false;
                    } else {
                        Tooltip.uninstall(this.tg_id, tooltip_tg_id);
                    }
                }
            }
        }

        return isAdd;
    }
}
