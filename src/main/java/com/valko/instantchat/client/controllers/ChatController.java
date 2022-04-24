package com.valko.instantchat.client.controllers;

import com.valko.instantchat.client.models.Client;
import com.valko.instantchat.client.models.Message;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;



public class ChatController implements Initializable
{
    private Parent root;
    private Stage stage;
    private Scene scene;
    private Client client;

    @FXML
    private Button sendButton;
    @FXML
    private TextField textField;
    @FXML
    public VBox vbox;
    @FXML
    private ScrollPane scrollPane;

    public VBox getVBox()
    {
        return vbox;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }



    @FXML
    public void onSendButtonClick()
    {
        String message = textField.getText();
        if (!message.isEmpty())
        {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: rgb(58,118,240); -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.WHITE);
            hbox.getChildren().add(textFlow);
            vbox.getChildren().add(hbox);
            client.sendMessage(message);
            textField.clear();
        }
    }



    public static void displayServerMessage(VBox vbox, Message message)
    {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text payload = new Text(message.payload);
        payload.setFill(Color.WHITE);
        payload.setStyle("-fx-font: 12 arial;");
        TextFlow textFlow = new TextFlow();

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.getChildren().add(payload);
        hbox.getChildren().add(textFlow);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                vbox.getChildren().add(hbox);
            }
        });

    }



    public static void displayClientMessage(VBox vbox, Message message) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        Text author = new Text(message.author + "\n");
        author.setFill(Color.BLACK);
        author.setStyle("-fx-font: 10 arial;");

        Text payload = new Text(message.payload);
        payload.setFill(Color.WHITE);
        payload.setStyle("-fx-font: 12 arial;");
        TextFlow textFlow = new TextFlow();

        textFlow.setStyle(String.format("-fx-color: rgb(255,255,255); -fx-background-color: \"%s\"; -fx-background-radius: 20px", message.color));
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        textFlow.getChildren().addAll(author, payload);
        hbox.getChildren().add(textFlow);
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                vbox.getChildren().add(hbox);
            }
        });
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        sendButton.setDefaultButton(true);
        vbox.heightProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1)
            {
                scrollPane.setVvalue((Double) t1);
                scrollPane.setVvalue(1.0);
            }
        });
    }
}