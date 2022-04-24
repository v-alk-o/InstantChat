package com.valko.instantchat.server;

import com.valko.instantchat.Server;
import com.valko.instantchat.client.models.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;



public class ClientHandler implements Runnable
{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String color;
    private String clientUsername;



    public ClientHandler(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.color = pickRandomColor();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public void run()
    {
        // Make sure the client username is not already taken by another client
        try
        {
            this.clientUsername = this.bufferedReader.readLine();
            while(isUsernameTaken(this.clientUsername) || this.clientUsername.equals("SERVER"))
            {
                unicastToClient("USERNAME_TAKEN");
                this.clientUsername = this.bufferedReader.readLine();
            }
            Server.semaphore.acquire();
            Server.clients.add(this);
            Server.semaphore.release();
            unicastToClient("USERNAME_OK");
            broadcastFromServer(clientUsername + " has joined the chat!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String payload;
        while(socket.isConnected())
        {
            try
            {
                payload = this.bufferedReader.readLine();
                broadcastFromClient(payload);
            }
            catch(IOException e)
            {
                closeClientSocket(this.socket, this.bufferedReader, this.bufferedWriter);
                break;
            }
        }
    }



    public void unicastToClient(String payload)
    {
        Message message = new Message("SERVER", payload, null);
        System.out.println(String.format("SERVER -> %s : %s" , this.clientUsername , payload));
        try
        {
            this.bufferedWriter.write(message.toJsonString());
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }
        catch(Exception e)
        {
            closeClientSocket(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }



    public void broadcastFromClient(String payload)
    {
        Message message = new Message(this.clientUsername, payload, this.color);
        System.out.println(message);
        try
        {
            Server.semaphore.acquire();
            for(ClientHandler clientHandler : Server.clients)
            {
                if(!clientHandler.equals(this))
                {
                    clientHandler.bufferedWriter.write(message.toJsonString());
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
            Server.semaphore.release();
        }
        catch (Exception e)
        {
            closeClientSocket(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }



    public void broadcastFromServer(String payload)
    {
        Message message = new Message("SERVER", payload, null);
        System.out.println(message);
        try
        {
            Server.semaphore.acquire();
            for(ClientHandler clientHandler : Server.clients)
            {
                clientHandler.bufferedWriter.write(message.toJsonString());
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            }
            Server.semaphore.release();
        }
        catch(Exception e)
        {
            closeClientSocket(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }



    public void removeClientHandler()
    {
        try
        {
            if(Server.clients.contains(this))
            {
                Server.clients.remove(this);
                broadcastFromServer(this.clientUsername + " has left the chat");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    public void closeClientSocket(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        removeClientHandler();
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



    private static String pickRandomColor()
    {
        Random random = new Random();
        int index = random.nextInt(Server.colors.size());
        return Server.colors.get(index);
    }



    public boolean isUsernameTaken(String username)
    {
        try
        {
            for(ClientHandler clientHandler : Server.clients)
            {
                if(clientHandler.clientUsername.equals(this.clientUsername))
                {
                    return true;
                }
            }
            return false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return true;
        }
    }
}