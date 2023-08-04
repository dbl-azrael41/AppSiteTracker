/*
Форма: MainForm
Язык: Java
Краткое описание:
Главная форма приложения.

Локальные переменные:
mask - переменная, используемая для создания маски пароля;
empty_selected - альтернативный текст, который будет добавлен во вкладку "Активность", если ни один из сайтов не появится на форме;
selected_sites - список строк, который используется для инициализации выпадающего меню на вкладке "Активность";
activity_choice_index - хранит индекс значения выпадающего списка со вкладки "Активность";
setData - объект класса SetDataInMainForm;
choice_adm - список строк, который используется для инициализации выпадающего меню на вкладке "Режим администратора".

Функции, используемые в форме:
onClose - функция возврата к форме "Авторизация";
on_user_reset - функция сбрасывает изменения, которые пользователь ввел в разделе "Ваш профиль", и устанавливает значения, записанные в базе данных;
on_user_save - функция сохраняет изменения в разделе "Ваш профиль";
on_show_pass - функция, которая показывает/скрывает пароль;
on_adm_add - функция, открывающая форму добавления нового сайта;
setInActivity - функция заполнения вкладки "Активность";
setInStatistics - функция заполнения вкладки "Статистика";
setInAdminPanel - функция заполнения вкладки "Режим администратора";
initialize - фукнция, необходимая инициализации первоначального состояния главной формы.
*/
package com.example.apptracker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {
    @FXML
    private Hyperlink tg_bot_link;

    @FXML
    private Button tg_id_clue;

    @FXML
    private Button tg_bot_clue;

    @FXML
    private Label admin_header_action;

    @FXML
    private Label admin_header_id;

    @FXML
    private Label admin_header_login;

    @FXML
    private Label admin_header_status;

    @FXML
    private Label admin_header_url;

    @FXML
    public ChoiceBox<String> act_choice;

    @FXML
    public VBox sites_activity_list;

    @FXML
    private VBox sites_stat_list;

    @FXML
    private VBox administration_list;

    @FXML
    private TextField act_field_search;

    @FXML
    private Button adm_add;

    @FXML
    private ChoiceBox<String> adm_choice;

    @FXML
    private TextField adm_field_search;

    @FXML
    private TabPane mainF_tabPane;

    @FXML
    private ChoiceBox<String> stat_choice;

    @FXML
    private TextField stat_field_search;

    @FXML
    private Text user_id;

    @FXML
    private TextField user_log;

    @FXML
    private TextField user_pass;

    @FXML
    private TextField user_tg_id;

    @FXML
    private RadioButton user_show_pass;

    @FXML
    private Text user_status;

    private String[] selected_sites = {"Все сайты", "Избранное"};

    private String[] choice_adm = {"Сайты", "Пользователи"};

    Label empty_selected = new Label();

    public static int activity_choice_index = 0;

    private SetDataInMainForm setData = new SetDataInMainForm();

    String mask = "";

    /*
    onClose - функция возврата к форме "Авторизация".
    Локальные переменные:
    stage - переменная, запускающая форму;
    fxmlLoader - переменная, хранящая в себе fxml-код формы;
    scene - переменная, задающая размеры формы.
    */
    @FXML
    void onClose(ActionEvent event) throws IOException {
        Main.conn.send_req("disconnect");

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);
        Main.stage.setTitle("AppSiteTracker - приложение для мониторинга вашего сайта");
        Main.stage.setScene(scene);
        Main.stage.show();
    }

    /*
    on_adm_add - функция, открывающая форму добавления нового сайта.
    Локальные переменные:
    stage - переменная, запускающая форму;
    loader - переменная, хранящая в себе fxml-код формы;
    scene - переменная, задающая размеры формы.
    */
    @FXML
    void on_adm_add(ActionEvent event) throws IOException, ParseException {

        //Вызов формы добавления нового сайта

        if (adm_choice.getSelectionModel().getSelectedIndex() == 0) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("adm_site_add.fxml"));
            Scene scene = new Scene(loader.load(), 462, 45);
            stage.setTitle("AppSiteTracker - приложение для мониторинга вашего сайта");
            stage.setScene(scene);
            AdmSiteAddController.adm_stage = stage;
            stage.showAndWait();

            //Обновление списка сайтов

            if (AdmSiteAddController.adm_stage == null) {
                setData.addSiteInAdminPanel(Main.conn.getSite(), administration_list, empty_selected);
            }
        }
    }

    /*
    on_user_reset - функция сбрасывает изменения, которые пользователь ввел в разделе "Ваш профиль", и устанавливает значения, записанные в базе данных.
    Локальные переменные:
    mask - переменная, необходимая для генерации маски пароля;
    len - длина пароля пользователя.
    */
    @FXML
    void on_user_reset(ActionEvent event) {

        //Создание маски для пароля

        mask = "";
        int len = Main.Password.length();
        for (int i = 0; i < len; i++) {
            mask += '\u25cf';
        }

        //Сброс введенных значений

        user_pass.setText(mask);
        user_log.setText(Main.Login);
        user_tg_id.setText(Main.Tg_ID);
        user_show_pass.setSelected(false);
    }

    /*
    on_user_save - функция сохраняет изменения в разделе "Ваш профиль".
    Локальные переменные:
    choice - переменная, хранящая в себе результат выбора пользователя о том, хочет ли он получать рассылку;
    iv - объект класса InputValidation;
    successful - логическая переменная, хранящая в себе результат проверки введенных пользователем данных;
    alert - переменная, необходимая для создания информирующего пользователя окна.
    */
    @FXML
    void on_user_save(ActionEvent event) throws IOException, ParseException {

        //Получение пароля пользователя

        if (!user_show_pass.isSelected()) {
            user_show_pass.setSelected(true);
            user_pass.setText(Main.Password);
        }
        InputValidation iv = new InputValidation(user_log, user_pass, user_tg_id);
        if (user_log.getText().equals(Main.Login)) {
            iv.isCheckUniqueLog = false;
        }

        //Проверка введенных данных

        if (iv.checkInput()) {
            Main.conn.send_req(String.format("update Users set Login = '%s',  Pass = '%s', Tg_id = '%s'  where ID_user = '%s';", user_log.getText(), user_pass.getText(), user_tg_id.getText(), Main.Id));

            //Информирование пользователя об успешном изменении его данных

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully");
            alert.setHeaderText(null);
            alert.setContentText("Ваши данные успешно обновлены");
            alert.showAndWait();

            Main.Login = user_log.getText();
            Main.Password = user_pass.getText();
            Main.Tg_ID = user_tg_id.getText();
        }
    }

    /*
    on_show_pass - функция, которая показывает/скрывает пароль.
    Локальные переменные:
    mask - переменная, необходимая для генерации маски пароля;
    len - длина пароля пользователя.
    */
    @FXML
    void on_show_pass(ActionEvent event) {
        if (user_show_pass.isSelected()) {
            user_pass.setText(Main.Password);
        } else {
            int len = Main.Password.length();
            String mask = "";
            for (int i = 0; i < len; i++) {
                mask += '\u25cf';
            }
            user_pass.setText(mask);
        }
    }

    /*
    setInActivity - функция заполнения вкладки "Активность".
    */
    public void setInActivity() {
        setData.actChoice = act_choice.getSelectionModel().getSelectedIndex();

        //Добавление на форму всех сайтов, если в выпадающем списке выбран пункт "Все сайты"

        if (act_choice.getSelectionModel().getSelectedIndex() == 0) {
            activity_choice_index = 0;
            try {
                setData.addSiteInActivity(Main.conn.getSite(), sites_activity_list, empty_selected);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }

        //Добавление на форму тех сайтов, которые находятся у пользователя в избранном, если в выпадающем списке выбран пункт "Избранное"

        else {
            activity_choice_index = 1;
            try {
                setData.addSiteInActivity(Main.conn.getSelectedSites(Main.Id), sites_activity_list, empty_selected);
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /*
    setInStatistics - функция заполнения вкладки "Статистика".
    */
    public void setInStatistics() {

        //Добавление на форму статистику каждого сайта, если в выпадающем списке выбран пункт "Все сайты"

        if (stat_choice.getSelectionModel().getSelectedIndex() == 0) {
            try {
                setData.addStat(Main.conn.getStat(), sites_stat_list, empty_selected);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

        //Добавление на форму статистику тех сайтов, которые находятся у пользователя в избранном, если в выпадающем списке выбран пункт "Избранное"

        else {
            try {
                setData.addStat(Main.conn.getSelectedSitesStat(Main.Id), sites_stat_list, empty_selected);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    setInAdminPanel - функция заполнения вкладки "Режим администратора".
    */
    public void setInAdminPanel() {

        //Заполнение вкладки информацией о сайтах, если в выпадающем списке выбран пункт "Сайты"

        if (adm_choice.getSelectionModel().getSelectedIndex() == 0) {

            //Настройка стилей шапки таблицы

            admin_header_login.setMaxWidth(0);
            admin_header_login.setMinWidth(0);
            admin_header_login.setVisible(false);
            admin_header_status.setMaxWidth(0);
            admin_header_status.setMinWidth(0);
            admin_header_status.setVisible(false);
            admin_header_url.setMaxWidth(384);
            admin_header_url.setMinWidth(384);
            admin_header_url.setVisible(true);
            admin_header_action.setStyle("-fx-padding: 0");
            adm_add.setVisible(true);
            try {
                setData.addSiteInAdminPanel(Main.conn.getSite(), administration_list, empty_selected);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

        //Заполнение вкладки информацией о пользователях, если в выпадающем списке выбран пункт "Пользователи"

        else {

            //Настройка стилей шапки таблицы

            admin_header_url.setMaxWidth(0);
            admin_header_url.setMinWidth(0);
            admin_header_url.setVisible(false);
            admin_header_login.setMinWidth(200);
            admin_header_login.setVisible(true);
            admin_header_status.setMinWidth(155);
            admin_header_status.setVisible(true);
            admin_header_action.setStyle("-fx-padding: 0 -55 0 0");
            adm_add.setVisible(false);
            try {
                setData.addUserInAdminPanel(Main.conn.getUsers(), administration_list, empty_selected);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    initialize - фукнция, необходимая инициализации первоначального состояния главной формы.
    Локальные переменные:
    mask - переменная, необходимая для генерации маски пароля;
    len - длина пароля пользователя.
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Удаление вкладки администраторов у обычных пользователей

        if (!Main.Status.equals("admin")) {
            mainF_tabPane.getTabs().remove(2);
        }

        //Добавление вариантов выбора в choice_box

        act_choice.getItems().addAll(selected_sites);
        act_choice.setValue(selected_sites[0]);

        stat_choice.getItems().addAll(selected_sites);
        stat_choice.setValue(selected_sites[0]);

        adm_choice.getItems().addAll(choice_adm);
        adm_choice.setValue(choice_adm[0]);

        //Заполнение данных в разделе "Профиль"

        if (Main.Status.equals("admin")) {
            user_status.setText("Администратор");
        } else {
            user_status.setText("Пользователь");
        }

        //Создание маски для пароля

        int len = Main.Password.length();
        for (int i = 0; i < len; i++) {
            mask += '\u25cf';
        }

        //Устанавливает значение полям в разделе "Ваш профиль"

        user_id.setText(Integer.toString(Main.Id));
        user_log.setText(Main.Login);
        user_pass.setText(mask);
        user_tg_id.setText(Main.Tg_ID);

        user_pass.setOnKeyReleased(e -> {
            if (!user_show_pass.isSelected()) {
                user_show_pass.setSelected(true);
            }
        });

        //Добавление подсказок для полей телеграм id и телеграм бот

        Tooltip tg_id_tooltip = new Tooltip("Если вы пользуетесь нашим ботом в телеграмме,\nто, указав ваш телеграм id, у вас появится возможность\nиз всего списка сайтов проверять активность только тех,\nкоторые добавлены у вас в раздел \"Избранное\".\nЧтобы узнать свой id через нашего бота, введите команду /my_id");
        tg_id_clue.setTooltip(tg_id_tooltip);
        tg_id_tooltip.setShowDuration(Duration.INDEFINITE);
        tg_id_tooltip.setShowDelay(Duration.ZERO);
        tg_id_clue.setOnMouseEntered(event -> tg_id_tooltip.show(tg_id_clue, event.getScreenX(), event.getScreenY()));
        tg_id_clue.setOnMouseExited(event -> tg_id_tooltip.hide());

        Tooltip tg_bot_tooltip = new Tooltip("Вступайте в наш чат в телеграмме.\nТам вы сможете общаться с другими пользователями\nнашего приложения, а также получать уведомления об\nокончании проверки активности сайтов.");
        tg_bot_clue.setTooltip(tg_bot_tooltip);
        tg_bot_tooltip.setShowDuration(Duration.INDEFINITE);
        tg_bot_tooltip.setShowDelay(Duration.ZERO);
        tg_bot_clue.setOnMouseEntered(event -> tg_bot_tooltip.show(tg_bot_clue, event.getScreenX(), event.getScreenY()));
        tg_bot_clue.setOnMouseExited(event -> tg_bot_tooltip.hide());

        tg_bot_link.setOnAction(e -> {
            Main m = new Main();
            m.follow_link(tg_bot_link.getText());
        });

        //Заполнение вкладки "Активность"

        setInActivity();

        // Заполнение определенной вкладки в зависимости от выбора пользователя

        mainF_tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                setData.filter = "";
                act_field_search.setText("");
                stat_field_search.setText("");
                adm_field_search.setText("");

                if (mainF_tabPane.getSelectionModel().getSelectedIndex() == 0) {
                    setInActivity();
                } else if (mainF_tabPane.getSelectionModel().getSelectedIndex() == 1) {
                    setInStatistics();
                    empty_selected.setText("Данные отсутствуют");
                } else if (mainF_tabPane.getSelectionModel().getSelectedIndex() == 2) {
                    setInAdminPanel();
            }
        }
        });

        //Изменение данных на вкладке "Активность" в зависимости от выбранного поля в выпадающем списке

        act_choice.setOnAction(e -> {
            setInActivity();
        });

        //Изменение данных на вкладке "Статистика" в зависимости от выбранного поля в выпадающем списке

        stat_choice.setOnAction(e -> {
            setInStatistics();
        });

        //Изменение данных на вкладке "Режим администратора" в зависимости от выбранного поля в выпадающем списке

        adm_choice.setOnAction(e -> {
            setInAdminPanel();
        });

        //Поиск по элементам во вкладке "Активность"

        act_field_search.setOnKeyReleased(keyEvent ->  {
            setData.filter = act_field_search.getText();
            setInActivity();
            if (sites_activity_list.getChildren().contains(empty_selected)) {
                empty_selected.setText("Совпадений не найдено");
            }
        });

        //Поиск по элементам во вкладке "Статистика"

        stat_field_search.setOnKeyReleased(keyEvent -> {
            setData.filter = stat_field_search.getText();
            setInStatistics();
            if (sites_stat_list.getChildren().contains(empty_selected)) {
                empty_selected.setText("Совпадений не найдено");
            }
        });

        // Поиск по элементам во вкладке администратора

        adm_field_search.setOnKeyReleased(keyEvent -> {
            setData.filter = adm_field_search.getText();
            setInAdminPanel();
            if (administration_list.getChildren().contains(empty_selected)) {
                empty_selected.setText("Совпадений не найдено");
            }
        });
    }
}