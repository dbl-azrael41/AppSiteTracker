module com.example.apptracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires json.simple;


    opens com.example.apptracker to javafx.fxml;
    exports com.example.apptracker;
}