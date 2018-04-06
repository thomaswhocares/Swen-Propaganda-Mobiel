package com.example.thomas.myapplication;

import java.net.Socket;

public class Client {
    private Socket connectionSocket;
    private String serverIP;
    private int serverPort;


    public Client(){

    }

    public void connectTo(String serverIP,int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }



}
