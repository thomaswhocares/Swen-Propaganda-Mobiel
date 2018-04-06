package com.example.thomas.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

public class Client {
    Context context;
    private Socket connectionSocket;
    private String stringServerIP;
    private InetAddress serverIP;
    private int serverPort;
    private long startTime;


    public Client(Context context){
        this.context = context;
    }

    public boolean connectTo(String stringServerIP,int serverPort){
        this.stringServerIP = stringServerIP;
        this.serverPort = serverPort;
        new Thread(new ConnectRunnable()).start();
        Toast.makeText(context, "KEKEKE",
                Toast.LENGTH_LONG).show();

        return true;
    }


    class ConnectRunnable implements Runnable{
        public void run(){
            try {
                Log.d(TAG, "C: Connecting...");
                InetAddress serverAddr = InetAddress.getByName(stringServerIP);
                startTime = System.currentTimeMillis();
                //Create a new instance of Socket
                connectionSocket = new Socket();

                //Start connecting to the server with 5000ms timeout
                //This will block the thread until a connection is established
                connectionSocket.connect(new InetSocketAddress(serverAddr, serverPort), 3000);

                long time = System.currentTimeMillis() - startTime;
                Log.d(TAG, "Connected! Current duration: " + time + "ms");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Connetion thread stopped");
        }
    }
}

