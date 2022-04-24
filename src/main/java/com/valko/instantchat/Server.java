package com.valko.instantchat;

import com.valko.instantchat.server.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;



public class Server
{
    private static final int MAX_CLIENTS = 10;
    public static Semaphore semaphore = new Semaphore(1);
    public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    public static List<String> colors = Arrays.asList("BLUE", "BLUEVIOLET", "CORNFLOWERBLUE", "DODGERBLUE", "MEDIUMPURPLE", "MEDIUMTURQUOISE", "ROYALBLUE", "SLATEBLUE", "STEELBLUE");
    private ServerSocket serverSocket;



    public Server(int port)
    {
        try
        {
            this.serverSocket = new ServerSocket(port);
        }
        catch(Exception e)
        {
            closeServerSocket();
            e.printStackTrace();
        }
    }



    public void startServer()
    {
        System.out.println("Server listening...");
        try
        {
            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();

                if(clients.size() >= MAX_CLIENTS)
                {
                    System.out.println("Connection refused !");
                    socket.close();
                }
                else
                {
                    System.out.println("Connection accepted !");
                    ClientHandler clientHandler = new ClientHandler(socket);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            closeServerSocket();
        }
    }



    public void closeServerSocket()
    {
        try
        {
            if (serverSocket != null)
            {
                serverSocket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public static void main(String[] args)
    {
        Server server = new Server(1234);
        server.startServer();
    }
}