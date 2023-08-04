/*
Форма: StatItemController
Язык: Java
Краткое описание:
Форма, которая содержит информацию о статистике сайта и помещается на главной форме в разделе "Статистика".

Функция, используемая в форме:
setData - функция, которая добавляет информацию о пользователе на главную форму.
*/

package com.example.apptracker;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;

public class StatItemController {

    @FXML
    private Text stat_failures;

    @FXML
    private Text stat_new_visitors;

    @FXML
    private Text stat_period;

    @FXML
    private Text stat_time_on_site;

    @FXML
    private Hyperlink stat_url;

    @FXML
    private Text stat_viewing_depth;

    @FXML
    private Text stat_visitors;

    /*
    setData - функция, которая добавляет инофрмацию о сайте на главную форму.
    Формальный параметр:
    statistics - статистика сайта, которая будет помещена на форму.
    Используемая функция:
    follow_link - функция перехода по ссылке.
    Локальная переменная:
    m - объект класса Main.
    */
    public void setData(Statistics statistics) {

        //Добавление информации на форму
        stat_url.setText(statistics.url);
        stat_period.setText(statistics.period);
        stat_visitors.setText(Integer.toString(statistics.visitors));
        stat_new_visitors.setText(Integer.toString(statistics.new_visitors));
        stat_failures.setText(Double.toString(statistics.failures));
        stat_viewing_depth.setText(Double.toString(statistics.viewing_depth));
        stat_time_on_site.setText(statistics.time_on_site);

        //Создание гиперссылки
        stat_url.setOnAction(e -> {
            Main m = new Main();
            m.follow_link(statistics.url);
        });
    }
}
