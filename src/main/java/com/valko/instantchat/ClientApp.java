package com.valko.instantchat;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class ClientApp extends javafx.application.Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        Parent root = FXMLLoader.load(Paths.get("src/main/resources/fxmls/LoginView.fxml").toUri().toURL());
        Scene scene = new Scene(root);
        stage.setTitle("InstantChat");
        stage.getIcons().add(new Image(String.valueOf(Paths.get("src/main/resources/images/logo.png").toUri().toURL())));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
    }



    public static void main(String[] args)
    {
        launch();
    }
}