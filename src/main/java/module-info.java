module com.example.apptracker_v1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.example.apptracker_v1 to javafx.fxml;
    exports com.example.apptracker_v1;
}