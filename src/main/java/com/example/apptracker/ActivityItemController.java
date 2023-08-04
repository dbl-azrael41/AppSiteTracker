/*
Форма: ActitvityItem
Язык: Java
Краткое описание:
Форма, которая содержит информацию о сайте и помещается на главной форме в разделе "Активность".

Локальные переменные:
activity - данные о сайте, которые будут помещены на форму;
sal - элемент главной формы, на который будет помещена информация о сайте;
alt - альтернативный текст, который будет помещен на главной форме, если не будет добавлен ни один сайт.

Функции, используемые в форме:
on_select - функция, которая добавляет или удаляет сайт из раздела "Избранное";
setData - функция, которая добавляет информацию о сайте на главную форму.
*/
package com.example.apptracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ActivityItemController {
    @FXML
    private ImageView is_active;

    @FXML
    private ImageView fav_img;

    @FXML
    private Button is_favorite;

    @FXML
    private Hyperlink link;

    Activity activity = new Activity();
    VBox sal = new VBox();
    Label alt = new Label();

    /*
    on_select - функция, которая добавляет или удаляет сайт из раздела "Избранное".
    Локальная переменная:
    setData - переменная, необходимая для добавления данных на главную форму.
    */
    @FXML
    void on_select(ActionEvent event) throws IOException, ClassNotFoundException, ParseException {

        //Удаляет сайт из раздела "Избранное"

        if(activity.isFavorite) {
            fav_img.setImage(new Image(getClass().getResourceAsStream("images/main_form_tabs/icon_favorites.png")));
            activity.isFavorite = false;
            Main.conn.send_req(String.format("delete from Selected_sites where ID_us = '%s' and ID_s = '%s';", Main.Id, activity.id));
            SetDataInMainForm setData = new SetDataInMainForm();

            // Добавление элементов на форму

            if (MainFormController.activity_choice_index == 0)
                setData.addSiteInActivity(Main.conn.getSite(), sal, alt);
            else
                setData.addSiteInActivity(Main.conn.getSelectedSites(Main.Id), sal, alt);

        } else {

            //Добавляет сайт в раздел "Избранное"

            fav_img.setImage(new Image(getClass().getResourceAsStream("images/main_form_tabs/icon_favorites_blue.png")));
            activity.isFavorite = true;
            Main.conn.send_req(String.format("insert into Selected_sites(ID_us, ID_s) values ('%s', '%s')", Main.Id, activity.id));
        }
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
    public void setData(Activity activity, VBox sal, Label alt) throws IOException {
        this.activity = activity;
        this.sal = sal;
        this.alt = alt;

        //Проверяет, находится ли сайт в разделе "Избранное"

        if(activity.isFavorite) {
            fav_img.setImage(new Image(getClass().getResourceAsStream("images/main_form_tabs/icon_favorites_blue.png")));
        } else {
            fav_img.setImage(new Image(getClass().getResourceAsStream("images/main_form_tabs/icon_favorites.png")));
        }

        //Создание гиперссылки

        link.setText(activity.url);
        link.setOnAction(e -> {
            Main m = new Main();
            m.follow_link(activity.url);
        });


        //Добавление картинки

        if(activity.isActive) {
            is_active.setImage(new Image(getClass().getResourceAsStream("images/main_form_tabs/icon_green_tick.png")));
        } else {
            is_active.setImage(new Image(getClass().getResourceAsStream("images/main_form_tabs/icon_red_cross.png")));
        }
    }

}
