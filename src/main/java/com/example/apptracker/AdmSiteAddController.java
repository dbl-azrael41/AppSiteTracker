/*
Форма: AdmSiteAddController
Язык: Java
Краткое описание:
Форма для добавления новых сайтов.

Локальная переменная:
adm_stage - переменная необходимая для запуска формы в отдельном окне.

Функция, используемая в форме:
on_admin_add_new_site - функция добавления нового сайта.
*/

package com.example.apptracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class AdmSiteAddController {

    @FXML
    private Button admin_add_new_site;

    @FXML
    private Text admin_new_url_label;

    @FXML
    private TextField admin_new_url;

    static Stage adm_stage;

    /*
    on_admin_add_new_site - функция добавления нового сайта.
    */
    @FXML
    void on_admin_add_new_site(ActionEvent event) throws IOException {
        if (admin_new_url.getText().contains("http://") || admin_new_url.getText().contains("https://")) {
            Main.conn.send_req(String.format("insert into site(Url, Activity) values (\"%s\", 0)", admin_new_url.getText()));

            //Закрытие формы добавления нового сайта

            adm_stage.close();
            adm_stage = null;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("URL сайта должен содержать протокол http или https");
            alert.showAndWait();
        }

    }

}
