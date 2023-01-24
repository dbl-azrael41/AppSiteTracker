package com.example.apptracker_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    @FXML
    private ChoiceBox<String> act_choice;

    @FXML
    private VBox sites_activity_list;

    @FXML
    private TextField act_field_search;

    @FXML
    private ListView<?> act_list;

    @FXML
    private Button adm_change;

    @FXML
    private ChoiceBox<String> adm_choice;

    @FXML
    private TextField adm_field_search;

    @FXML
    private ListView<?> adm_list;

    @FXML
    private Button btn_act_search;

    @FXML
    private Button btn_adm_search;

    @FXML
    private Button btn_stat_search;

    @FXML
    private ImageView img_search;

    @FXML
    private ImageView img_search1;

    @FXML
    private ImageView img_search11;

    @FXML
    private TabPane mainF_tabPane;

    @FXML
    private Button mf_close;

    @FXML
    private ChoiceBox<String> stat_choice;

    @FXML
    private TextField stat_field_search;

    @FXML
    private ListView<?> stat_list;

    @FXML
    private Button user_change_photo;

    @FXML
    private ChoiceBox<String> user_choiceB;

    @FXML
    private Text user_id;

    @FXML
    private TextField user_log;

    @FXML
    private TextField user_mail;

    @FXML
    private TextField user_pass;

    @FXML
    private TextField user_phoneN;

    @FXML
    private ImageView user_photo;

    @FXML
    private Button user_reset;

    @FXML
    private Button user_save;

    @FXML
    private RadioButton user_show_pass;

    @FXML
    private Text user_status;

    @FXML
    void onClose(ActionEvent event) {

    }

    @FXML
    void on_act_search(ActionEvent event) {

    }

    @FXML
    void on_adm_change(ActionEvent event) {

    }

    @FXML
    void on_adm_search(ActionEvent event) {

    }

    @FXML
    void on_change_photo(ActionEvent event) {

    }

    @FXML
    void on_stat_search(ActionEvent event) {

    }

    @FXML
    void on_user_reset(ActionEvent event) {

    }

    @FXML
    void on_user_save(ActionEvent event) {

    }

    private String[] selected_sites = {"Все сайты", "Избранное"};
    private String[] choice_adm = {"Добавление / Удаление сайтов", "Настройка прав пользователей"};
    private String[] choice_user = {"Да, на почту", "Нет"};

    String[] url_list = {
            "https://arenda.crocusgroup.ru/",
            "https://emin-music.ru/",
            "https://estatemall.ru/",
            "https://crocusteam.ru/",
            "https://crocusfriends.ru/",
            "https://crocusgroup.ru/",
            "https://crocusarenda.ru/",
            "http://seabreeze.az/",
            "https://vegas-city.ru/",
            "http://crocuscitymall.ru/",
            "http://boxcityrussia.ru/",
            "https://www.agalarovestate.com/",
            "http://uk.agalarovestate.com/",
            "http://m-magomaev.ru/",
            "http://noburestaurants.ru/",
            "https://arenda.crocusgroup.ru/",
            "https://shop.crocuscitymall.ru",
            "https://shop.crocuscitymall.ru",
            "http://zharafm.ru/",
            "https://beautylab7.ru/",
            "https://click.beautylab7.ru/",
            "http://m-magomaev.ru/elka/",
            "http://gstarraw.ru/",
            "http://crocusland.ru/",
            "http://crocusnewyear.ru/",
            "https://resort.seabreeze.az/",
            "https://business.seabreeze.az/",
            "https://bethefirst.vegas-city.ru/",
            "https://vegascard.vegas-city.ru/",
            "http://zhara.energy/",
            "http://optom.zhara.energy/",
            "http://wedding.vegas-city.ru/",
            "http://loyalty-program.crocuscitymall.ru/",
            "https://zharafestpromo.ru/",
            "http://vegasgiftcard.tilda.ws/",
            "https://kidsclub.vegas-city.ru/",
            "http://back2school.vegas-city.ru/",
            "http://gastro.vegas-city.ru/",
            "http://seabreezecube.vegas-city.ru/",
            "https://resort.seabreeze.az/",
            "https://business.seabreeze.az/",
            "http://zhityanino.ru/"
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Добавление вариантов выбора в choice_box
        act_choice.getItems().addAll(selected_sites);
        act_choice.setValue(selected_sites[0]);

        stat_choice.getItems().addAll(selected_sites);
        stat_choice.setValue(selected_sites[0]);

        adm_choice.getItems().addAll(choice_adm);
        adm_choice.setValue(choice_adm[0]);

        user_choiceB.getItems().addAll(choice_user);
        user_choiceB.setValue(choice_user[0]);

        for (var k: url_list) {
            Activity act = new Activity();

            CheckSite cs = new CheckSite();
            boolean isCon;
            try {
                isCon = cs.isConnect(k, 10000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            act.setIsFavorite(false);
            act.setUrl(k);
            act.setIsActive(isCon);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("activity_item.fxml"));

            try {
                HBox hBox = fxmlLoader.load();
                ActivityItemController aic = fxmlLoader.getController();
                aic.setData(act);
                sites_activity_list.getChildren().add(hBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
