package com.example.apptracker_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ActivityItemController implements Initializable {
    @FXML
    private ImageView is_active;

    @FXML
    private ImageView fav_img;

    @FXML
    private Button is_favorite;

    @FXML
    private Label url;

    @FXML
    void on_select(ActionEvent event) {
        if(activity.getIsFavorite()) {
            fav_img.setImage(new Image("C:/Users/kosty/IdeaProjects/AppTracker_V1/src/main/resources/com/example/apptracker_v1/images/main_form_tabs/icon_favorites.png"));
            activity.setIsFavorite(false);
        } else {
            fav_img.setImage(new Image("C:/Users/kosty/IdeaProjects/AppTracker_V1/src/main/resources/com/example/apptracker_v1/images/main_form_tabs/icon_favorites_blue.png"));
            activity.setIsFavorite(true);
        }
    }
    Activity activity = new Activity();
    public void setData(Activity activity) throws IOException {
        this.activity = activity;

        if(activity.getIsFavorite()) {
            fav_img.setImage(new Image("C:/Users/kosty/IdeaProjects/AppTracker_V1/src/main/resources/com/example/apptracker_v1/images/main_form_tabs/icon_favorites_blue.png"));
        } else {
            fav_img.setImage(new Image("C:/Users/kosty/IdeaProjects/AppTracker_V1/src/main/resources/com/example/apptracker_v1/images/main_form_tabs/icon_favorites.png"));
        }

        url.setText(activity.getUrl());

        if(activity.getIsActive()) {
            is_active.setImage(new Image("C:/Users/kosty/IdeaProjects/AppTracker_V1/src/main/resources/com/example/apptracker_v1/images/main_form_tabs/icon_green_tick.png"));
        } else {
            is_active.setImage(new Image("C:/Users/kosty/IdeaProjects/AppTracker_V1/src/main/resources/com/example/apptracker_v1/images/main_form_tabs/icon_red_cross.png"));
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
