/*
Класс: SetDataInMainForm
Язык: Java
Краткое описание:
Класс, записывающий информацию на главную форму.

Локальные переменные:
filter - переменная, необходимая для реализации поиска на вкладках;
container - элемент-контейнер главной формы, внутрь которого будут добавляться другие элементы;
alternative - альтернативный текст, который появляется в случае, если ни один из сайтов не был добавлен на форму;
actChoice - определяет альтернативный текст, который будет отображаться на форме, если туда не будет добавлен ни один элемент.

Функции, используемые в форме:
setVBoxInList - функция добавляет элементы VBox на главную форму;
setHBoxInList - функция добавляет элементы HBox на главную форму;
checkIsEmpty - функция проверяет содержит ли контейнер другие элементы;
addSiteInActivity - функция добавления информации о сайте в раздел "Активность";
addSiteInAdminPanel - функция добавления информации о сайте в раздел "Режим администратора";
addUserInAdminPanel - функция добавления информации о пользователе в раздел "Режим администратора";
addStat - функция добавления статистики сайта в раздел "Статистика".
*/
package com.example.apptracker;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class SetDataInMainForm {
    public VBox container;
    public Label alternative;
    public int actChoice;
    public String filter = "";

    /*
    setHboxInList - функция добавляет элементы HBox на главную форму.
    Формальный параметр:
    hb - элемент, который нужно добавить на форму.
    Используемая функция:
    checkIsEmpty - функция проверяет содержит ли контейнер другие элементы.
    */
    public void setHboxInList(HBox hb) {

        //Добавление элемента

        this.container.getChildren().add(hb);
        checkIsEmpty(container, alternative);
    }

    /*
    setVBoxInList - функция добавляет элементы VBox на главную форму.
    Формальный параметр:
    vb - элемент, который нужно добавить на форму.
    Используемая функция:
    checkIsEmpty - функция проверяет содержит ли контейнер другие элементы.
    */
    public void setVBoxInList(VBox vb) {

        //Добавление элемента

        this.container.getChildren().add(vb);
        checkIsEmpty(container, alternative);
    }


    /*
    checkIsEmpty - функция проверяет содержит ли контейнер другие элементы.
    Формальные параметры:
    container - элемент-контейнер главной формы, внутрь которого будут добавляться другие элементы;
    alternative - альтернативный текст, который будет добавлен в контейнер в случае, если не будет добавлен ни один сайт.
    */
    public void checkIsEmpty(VBox container, Label alternative) {

        //Изменение альтернативного текста

        if (actChoice == 0) {
            alternative.setText("Идет загрузка");
        } else {
            alternative.setText("Ваш список избранных сайтов пуст");
        }

        //Добавление текста в случае, если контейнер пустой

        if (container.getChildren().isEmpty()) {
            container.getChildren().add(alternative);
            alternative.setStyle("-fx-font: 17px System;");
            container.setAlignment(Pos.CENTER);
        } else {
            if (container.getChildren().contains(alternative)) {
                container.getChildren().remove(alternative);
                container.setAlignment(Pos.TOP_LEFT);
            }
        }
    }

    /*
    addSiteInActivity - функция добавления информации о сайте в раздел "Активность".
    Формальные параметры:
    list - список элементов, которые будут добавляться в контейнер;
    container - элемент-контейнер главной формы, внутрь которого будут добавляться другие элементы;
    alternative - альтернативный текст, который будет добавлен в контейнер в случае, если не будет добавлен ни один сайт.
    Используемые функции:
    checkIsEmpty - функция проверяет содержит ли контейнер другие элементы;
    isConnect - функция, проверяющая активность сайта;
    setData - записывает данные о сайте на элемент, который будет добавлен на главную форму;
    setHboxInList - добавление элемента HBox на форму.
    Локальные переменные:
    site - сайт, информацию а котором будет добавляться на форму;
    act - объект класса Activity;
    aiLoader - переменная, необходимая для создания объекта формы ActivityItem;
    aic - объект класса ActivityItemController;
    hBox - элемент, хранящий информацию о сайте.
    */
    public void addSiteInActivity(ArrayList<Activity> list, VBox container, Label alternative) throws IOException, ParseException {
        this.container = container;
        this.alternative = alternative;

        //Очистка контейнера

        container.getChildren().clear();

        //Проверка контейнера

        checkIsEmpty(container, alternative);

        //Получение избранных сайтов данного пользователя

        ArrayList<Activity> selected_sites_list = Main.conn.getSelectedSites(Main.Id);

        for (var site: list) {

            //Фильтрация выводимых данных

            if ((filter.length() == 0) || site.url.matches(".*" + filter + ".*")) {

                //Добавление информации о сайтах

                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws IOException {
                                Activity act = new Activity();
                                act.id = site.id;
                                act.url = site.url;
                                act.isActive = site.isActive;
                                act.isFavorite = false;
                                for (var ss_site: selected_sites_list) {
                                    if (site.id == ss_site.id) {
                                        act.isFavorite = true;
                                        break;
                                    }
                                }

                                //Добавление информации о сайте на форму

                                Platform.runLater(() -> {
                                    FXMLLoader aiLoader = new FXMLLoader();
                                    aiLoader.setLocation(getClass().getResource("activity_item.fxml"));
                                    try {
                                        HBox hBox = aiLoader.load();
                                        ActivityItemController aic = aiLoader.getController();
                                        aic.setData(act, container, alternative);
                                        setHboxInList(hBox);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                return null;
                            }
                        };

                    }

                };
                service.start();
            }

        }
    }

    /*
    addSiteInAdminPanel - функция добавления информации о сайте в раздел "Режим администратора".
    Формальные параметры:
    list - список элементов, которые будут добавляться в контейнер;
    container - элемент-контейнер главной формы, внутрь которого будут добавляться другие элементы;
    alternative - альтернативный текст, который будет добавлен в контейнер в случае, если не будет добавлен ни один элемент.
    Используемые функции:
    checkIsEmpty - функция проверяет содержит ли контейнер другие элементы;
    isConnect - функция, проверяющая активность сайта;
    setData - записывает данные о сайте на элемент, который будет добавлен на главную форму;
    setHboxInList - добавление элемента HBox на форму.
    Локальные переменные:
    site - сайт, информацию а котором будет добавляться на форму;
    act - объект класса Activity;
    asiLoader - переменная, необходимая для создания объекта формы AdmSiteItem;
    asic - объект класса AdmSiteItem;
    hBox - элемент, хранящий информацию о сайте.
    */
    public void addSiteInAdminPanel(ArrayList<Activity> list, VBox container, Label alternative) throws IOException, ParseException {
        this.container = container;
        this.alternative = alternative;

        //Очистка контейнера

        container.getChildren().clear();

        //Проверка контейнера

        checkIsEmpty(container, alternative);

        //Фильтрация выводимых данных

        for (var site: list) {
            if ((filter.length() == 0) || site.url.matches(".*" + filter + ".*")) {
                Activity act = new Activity();
                act.id = site.id;
                act.url = site.url;

                //Добавление информации о сайте на форму

                Platform.runLater(() -> {
                    FXMLLoader asiLoader = new FXMLLoader();
                    asiLoader.setLocation(getClass().getResource("adm_site_item.fxml"));
                    try {
                        HBox hBox = asiLoader.load();
                        AdmSiteItemController asic = asiLoader.getController();
                        asic.setData(act, container, alternative);
                        setHboxInList(hBox);
                    } catch (IOException e) {
                        throw  new RuntimeException(e);
                    }
                });
            }
        }
    }

    /*
    addUserInAdminPanel - функция добавления информации о пользователе в раздел "Режим администратора".
    Формальные параметры:
    list - список элементов, которые будут добавляться в контейнер;
    container - элемент-контейнер главной формы, внутрь которого будут добавляться другие элементы;
    alternative - альтернативный текст, который будет добавлен в контейнер в случае, если не будет добавлен ни один пользователь.
    Используемые функции:
    checkIsEmpty - функция проверяет содержит ли контейнер другие элементы;
    isConnect - функция, проверяющая активность сайта;
    setData - записывает данные о сайте на элемент, который будет добавлен на главную форму;
    setHboxInList - добавление элемента HBox на форму.
    Локальные переменные:
    row - пользователь, информацию а котором будет добавляться на форму;
    user - объект класса User;
    auiLoader - переменная, необходимая для создания объекта формы AdmUserItem;
    auic - объект класса AdmUserItem;
    hBox - элемент, хранящий информацию о пользователе.
    */
    public void addUserInAdminPanel(ArrayList<User> list, VBox container, Label alternative) {
        this.container = container;
        this.alternative = alternative;

        //Очистка контейнера

        container.getChildren().clear();

        //Проверка контейнера

        checkIsEmpty(container, alternative);

        //Фильтрация выводимых данных

        for (var row: list) {
            if ((filter.length() == 0) || row.login.matches(".*" + filter + ".*")) {

                //Добавление информации о пользователях

                User user = new User();
                user.id = row.id;
                user.login = row.login;
                user.pass = row.pass;
                user.status = row.status;
                user.tg_id = row.tg_id;

                //Добавление информации о пользователе на форму

                Platform.runLater(() -> {
                    FXMLLoader auiLoader = new FXMLLoader();
                    auiLoader.setLocation(getClass().getResource("adm_user_item.fxml"));
                    try {
                        HBox hBox = auiLoader.load();
                        AdmUserItemController auic = auiLoader.getController();
                        auic.setData(user, container, alternative);
                        setHboxInList(hBox);
                    } catch (IOException e) {
                        throw  new RuntimeException(e);
                    }
                });
            }
        }
    }

    /*
    addStat - функция добавления статистики сайта в раздел "Статистика".
    Формальные параметры:
    list - список элементов, которые будут добавляться в контейнер;
    container - элемент-контейнер главной формы, внутрь которого будут добавляться другие элементы;
    alternative - альтернативный текст, который будет добавлен в контейнер в случае, если не будет добавлен ни один пользователь.
    Используемые функции:
    checkIsEmpty - функция проверяет содержит ли контейнер другие элементы;
    isConnect - функция, проверяющая активность сайта;
    setData - записывает данные о сайте на элемент, который будет добавлен на главную форму;
    setHboxInList - добавление элемента HBox на форму.
    Локальные переменные:
    site_st - статистика сайта, которая будет добавляться на форму;
    stat - объект класса Statistics;
    siLoader - переменная, необходимая для создания объекта формы StatItem;
    sic - объект класса StatItem;
    vBox - элемент, хранящий статистику сайта.
    */
    public void addStat(ArrayList<Statistics> list, VBox container, Label alternative) throws IOException, ParseException {
        this.container = container;
        this.alternative = alternative;

        //Очистка контейнера

        container.getChildren().clear();

        //Проверка контейнера

        checkIsEmpty(container, alternative);

        //Фильтрация выводимых данных

        for (var site_st : list) {
            if ((filter.length() == 0) || site_st.url.matches(".*" + filter + ".*")) {

                //Добавление статистики в соответствующий раздел

                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws IOException {

                                Statistics stat = new Statistics();
                                stat.url = site_st.url;
                                stat.period = site_st.period;
                                stat.visitors = site_st.visitors;
                                stat.new_visitors = site_st.new_visitors;
                                stat.failures = site_st.failures;
                                stat.viewing_depth = site_st.viewing_depth;
                                stat.time_on_site = site_st.time_on_site;

                                //Добавление статистики на форму

                                Platform.runLater(() -> {
                                    FXMLLoader siLoader = new FXMLLoader();
                                    siLoader.setLocation(getClass().getResource("stat_item.fxml"));
                                    try {
                                        VBox vBox = siLoader.load();
                                        StatItemController sic = siLoader.getController();
                                        sic.setData(stat);
                                        setVBoxInList(vBox);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                return null;
                            }
                        };

                    }

                };
                service.start();
            }
        }
    }
}
