module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.valko.instantchat to javafx.fxml;
    exports com.valko.instantchat;
    exports com.valko.instantchat.server;
    opens com.valko.instantchat.server to javafx.fxml;
    exports com.valko.instantchat.client.models;
    opens com.valko.instantchat.client.models to javafx.fxml;
    exports com.valko.instantchat.client.controllers;
    opens com.valko.instantchat.client.controllers to javafx.fxml;
}