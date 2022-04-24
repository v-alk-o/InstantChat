package com.valko.instantchat.client.models;

import com.valko.instantchat.client.controllers.ChatController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;

import javafx.scene.layout.VBox;



public class Client
{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;



    public Client(String ip, int port)
    {
        try
        {
            this.socket = new Socket(ip, port);
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }



    public void sendMessage(String message)
    {
        try
        {
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }
        catch(IOException e)
        {
            closeSocket();
        }

    }



    public String receiveSingleMessage()
    {
        String message = "";
        try
        {
            message = this.bufferedReader.readLine();
            System.out.println(message);
        }
        catch(IOException e)
        {
            message = "ERROR";
            closeSocket();
        }
        return message;
    }



    public void receiveMessage(VBox vbox)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String buffer;
                while(socket.isConnected())
                {
                    try
                    {
                        buffer = bufferedReader.readLine();
                        System.out.println(buffer);

                        Message message = new Gson().fromJson(buffer, new TypeToken<Message>(){}.getType());
                        if(message.author.equals("SERVER"))
                        {
                            ChatController.displayServerMessage(vbox, message);
                        }
                        else
                        {
                            ChatController.displayClientMessage(vbox, message);
                        }
                    }
                    catch(IOException e)
                    {
                        closeSocket();
                        e.printStackTrace();
                    }
                    catch(JsonSyntaxException e){}
                }
            }
        }).start();
    }



    public void closeSocket()
    {
        try
        {
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}