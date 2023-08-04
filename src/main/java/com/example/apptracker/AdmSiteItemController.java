/*
Форма: AdmSiteItemController
Язык: Java
Краткое описание:
Форма, которая содержит информацию о сайте и помещается на главной форме в разделе "Режим администратора".

Локальные переменные:
activity - данные о сайте, которые будут помещены на форму;
al - элемент главной формы, на который будет помещена информация о сайте;
alt - альтернативный текст, который будет помещен на главной форме, если не будет добавлен ни один сайт.

Функции, используемые в форме:
on_admin_change_site - функция, изменяющая информацию о сайте;
on_admnin_delete_site - функция, удаляющая информацию о данном сайте;
initialize - функция, необходимая для установки начальных состояний элементов управления;
setData - функция, которая добавляет информацию о сайте на главную форму.
*/

package com.example.apptracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdmSiteItemController implements Initializable {

    @FXML
    private Button admin_change_site;

    @FXML
    private Text admin_id;

    @FXML
    private Button admnin_delete_site;

    @FXML
    private TextField admin_url;

    Activity activity = new Activity();
    VBox al = new VBox();
    Label alt = new Label();

    /*
    on_admin_change_site - функция, изменяющая информацию о сайте.
    */
    @FXML
    void on_admin_change_site(ActionEvent event) throws IOException {
        if (admin_url.isEditable()) {

            //Обновление информации о сайте

            if (admin_url.getText().contains("http://") || admin_url.getText().contains("https://")) {
                Main.conn.send_req(String.format("update site set Url = \"%s\" where ID_site = %s;", admin_url.getText(), admin_id.getText()));
                admin_url.setEditable(false);
                admin_url.setStyle("-fx-text-box-border: transparent;\n-fx-background-color: -fx-control-inner-background;");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("URL сайта должен содержать протокол http или https");
                alert.showAndWait();
            }
        } else {

            //Разблокирует поле для редактирования информации о сайте

            admin_url.setEditable(true);
            admin_url.setStyle("-fx-border-color: silver");
        }
    }

    /*
    on_admnin_delete_site - функция, удаляющая информацию о данном сайте.
    */
    @FXML
    void on_admnin_delete_site(ActionEvent event) throws IOException, ParseException {

        //Удаление сайта из БД

        Main.conn.send_req(String.format("delete from Site where ID_site = %s;", admin_id.getText()));
        SetDataInMainForm setData = new SetDataInMainForm();
        setData.addSiteInAdminPanel(Main.conn.getSite(), al, alt);
    }

    /*
    setData - функция, которая добавляет информацию о сайте на главную форму.
    Формальные параметры:
    activity - данные о сайте, которые будут помещены на форму;
    sal - элемент главной формы, на который будет помещена информация о сайте;
    alt - альтернативный текст, который будет помещен на главной форме, если не будет добавлен ни один сайт.
    Используемая функция:
    follow_link - функция перехода по ссылке.
    Локальная переменная:
    m - объект класса Main.
    */
    public void setData(Activity activity, VBox al, Label alt) {
        this.al = al;
        this.activity = activity;
        this.alt = alt;

        // Установка значений полей формы

        admin_id.setText(Integer.toString(activity.id));
        admin_url.setText(activity.url);

        //Создание гиперссылки

        admin_url.setOnAction(e -> {
            Main m = new Main();
            m.follow_link(activity.url);
        });
    }

    /*
    initialize - функция, необходимая для установки начальных состояний элементов управления.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        admin_url.setEditable(false);
    }
}
