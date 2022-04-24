package com.valko.instantchat.client.controllers;

import com.valko.instantchat.client.models.Client;
import com.valko.instantchat.client.models.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class LoginController implements Initializable
{
    private Parent root;
    private Stage stage;
    private Scene scene;
    private Client client;

    @FXML
    private Label errorLabel;
    @FXML
    private Button enterButton;
    @FXML
    private TextField usernameField;



    @FXML
    public void onEnterButtonClick(ActionEvent event)
    {
        String username = usernameField.getText();
        usernameField.clear();
        if(!username.isEmpty())
        {
            client.sendMessage(username);

            String responseFromServer = client.receiveSingleMessage();
            Message message = new Gson().fromJson(responseFromServer, new TypeToken<Message>(){}.getType());
            String rep = message.payload;

            if(rep.equals("USERNAME_TAKEN"))
            {
                errorLabel.setVisible(true);
            }
            else if(rep.equals("ERROR"))
            {
                errorLabel.setVisible(true);
            }
            else if(rep.equals("USERNAME_OK"))
            {
                errorLabel.setVisible(false);
                try
                {
                    FXMLLoader loader = new FXMLLoader(Paths.get("src/main/resources/fxmls/ChatView.fxml").toUri().toURL());
                    root = loader.load();
                    ChatController chatController = loader.getController();
                    chatController.setClient(client);

                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
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
                    client.receiveMessage(chatController.getVBox());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Error : Could not understand received message");
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.client = client;
        errorLabel.setVisible(false);
        enterButton.setDefaultButton(true);
        try
        {
            client = new Client("127.0.0.1", 1234);
        }
        catch(Exception e)
        {
            System.out.println("Error connecting to the server");
        }
    }
}