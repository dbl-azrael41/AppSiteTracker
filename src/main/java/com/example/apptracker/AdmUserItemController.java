/*
Форма: AdmUserItemController
Язык: Java
Краткое описание:
Форма, которая содержит информацию о пользователе и помещается на главной форме в разделе "Режим администратора".

Локальные переменные:
user - данные о пользователе, которые будут помещены на форму;
al - элемент главной формы, на который будет помещена информация о сайте;
alt - альтернативный текст, который будет помещен на главной форме, если не будет добавлен ни один сайт.

Функции, используемые в форме:
on_admin_change_user - функция, изменяющая информацию о пользователе;
initialize - функция, необходимая для установки начальных состояний элементов управления;
setData - функция, которая добавляет информацию о пользователе на главную форму.
*/

package com.example.apptracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdmUserItemController implements Initializable {

    @FXML
    private Button admin_change_user;

    @FXML
    private Text admin_user_id;

    @FXML
    private Text admin_user_login;

    @FXML
    private TextField admin_user_status;

    User user = new User();
    VBox al = new VBox();
    Label alt = new Label();

    /*
    on_admin_change_user - функция, изменяющая информацию о пользователе.
    */
    @FXML
    void on_admin_change_user(ActionEvent event) throws IOException {
        if (admin_user_status.isEditable()) {

            // Проверка вводимых данных

            if (!admin_user_status.getText().equals("admin") && !admin_user_status.getText().equals("user")) {

                //Информирование о том, что введены недопустимые значения

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Неверные данные.\nДопустимыми значениями являются: \"user\", \"admin\".");
                alert.showAndWait();
            } else {

                // Обновление информации о пользователе

                Main.conn.send_req(String.format("update Users set Status_us = \"%s\" where ID_user = %s;", admin_user_status.getText(), admin_user_id.getText()));
                admin_user_status.setEditable(false);
                admin_user_status.setStyle("-fx-text-box-border: transparent;\n-fx-background-color: -fx-control-inner-background;");

                if (Main.Login.equals(admin_user_login.getText())) {
                    Main.Status = "user";
                    Main.stage.close();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainForm.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 700, 650);
                    Main.stage.setTitle("AppTracker - приложение для мониторинга вашего сайта");
                    Main.stage.setScene(scene);
                    Main.stage.show();
                }
            }

        } else {
            admin_user_status.setEditable(true);
            admin_user_status.setStyle("-fx-border-color: silver");
        }
    }

    /*
    setData - функция, которая добавляет инофрмацию о пользователе на главную форму.
    Формальные параметры:
    user - данные о пользователе, которые будут помещены на форму;
    sal - элемент главной формы, на который будет помещена информация о сайте;
    alt - альтернативный текст, который будет помещен на главной форме, если не будет добавлен ни один сайт.
    */
    public void setData(User user, VBox al, Label alt) {
        this.al = al;
        this.user = user;
        this.alt = alt;

        //Добавление информации на форму

        admin_user_id.setText(Integer.toString(user.id));
        admin_user_login.setText(user.login);
        admin_user_status.setText(user.status);
    }

    /*
    initialize - функция, необходимая для установки начальных состояний элементов управления.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin_user_status.setEditable(false);
    }
}
