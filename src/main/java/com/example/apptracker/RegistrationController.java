/*
Форма: Registration
Язык: Java
Краткое описание:
Форма регистрации новых пользователей.

Функции, используемые в форме:
onClose - функция возврата к форме "Авторизация";
onReg - функция регистрации нового пользователя и вызов формы "Авторизация".
*/
package com.example.apptracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class RegistrationController {

    @FXML
    private Button reg_btn;

    @FXML
    public ChoiceBox<String> reg_choiceB;

    @FXML
    private Button reg_close;

    @FXML
    private TextField reg_login;

    @FXML
    private PasswordField reg_pass;

    @FXML
    private Label reg_quastion;


    /*
    onClose - функция возврата к форме "Авторизация".
    Локальные переменные:
    stage - переменная, запускающая форму;
    fxmlLoader - переменная, хранящая в себе fxml-код формы;
    scene - переменная, задающая размеры формы.
    */
    @FXML
    void onClose(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        Main.stage.setTitle("AppSiteTracker - приложение для мониторинга вашего сайта");
        Main.stage.setScene(scene);
        Main.stage.show();
    }

    /*
    onReg - функция регистрации нового пользователя и вызов формы "Авторизация".
    Локальные переменные:
    db - объект класса Database;
    iv - объект классса InputValidation;
    isAdd - булевая переменная, хранящая в себе результат проверки введенных данных;
    alert - переменная, необходимая для создания информирующего пользователя окна;
    choice - переменная, хранящая в себе результат выбора из выпадающего списка;
    stage - переменная, запускающая форму;
    fxmlLoader - переменная, хранящая в себе fxml-код формы;
    scene - переменная, задающая размеры формы.
    */
    @FXML
    void onReg(ActionEvent event) throws IOException, ParseException {
        Main.conn = new ConnectionToServer(Main.Server_IP, 8000);
        InputValidation iv = new InputValidation(reg_login, reg_pass);

        //Проверка введенных данных

        boolean isAdd = iv.checkInput();

        if (isAdd) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully");
            alert.setHeaderText(null);
            alert.setContentText("Регистрация прошла успешно");

            //Добавление данных в базу

            Main.conn.send_req(String.format("insert into Users(Login, Pass, Status_us) values (\"%s\", \"%s\", \"user\");", reg_login.getText(), reg_pass.getText()));
            alert.showAndWait();
            Main.conn.send_req("disconnect");

            //Вызов формы "Авторизация"

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authorization.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 650);
            Main.stage.setTitle("AppSiteTracker - приложение для мониторинга вашего сайта");
            Main.stage.setScene(scene);
            Main.stage.show();
        }

    }
}
