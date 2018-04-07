package com.example.thomas.myapplication;

import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;


import static android.content.ContentValues.TAG;

public class Client {

    private Socket connectionSocket;
    private String stringServerIP;
    private InetAddress serverIP;
    private int serverPort;
    private long startTime;

    public Client(){
    }

    public boolean connectTo(String stringServerIP,int serverPort){
        this.stringServerIP = stringServerIP;
        this.serverPort = serverPort;
        new Thread(new ConnectRunnable()).start();
        return true;
    }

    public boolean send(byte[] dataToSend){
            new Thread(new SendRunnable(connectionSocket, dataToSend)).start();
        return false;
    }

    class ConnectRunnable implements Runnable{
        @Override
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
    class SendRunnable implements Runnable{

        private OutputStream streamOut;
        private byte[] dataToSend;
        private boolean hasdata;

        public SendRunnable(Socket server,byte[] dataToSend){
            this.dataToSend = dataToSend;
            this.hasdata = true;
            try{
                this.streamOut = server.getOutputStream();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean isConnected(){
            return connectionSocket != null &&
                    connectionSocket.isConnected() &&
                    !connectionSocket.isClosed();
        }

        @Override
        public void run(){
            while(!Thread.currentThread().isInterrupted() && isConnected()){

                if (this.hasdata) {
                    try{
                        this.streamOut.write(ByteBuffer.allocate(4).putInt(dataToSend.length).array());
                        this.streamOut.write(dataToSend,0, dataToSend.length);
                        this.streamOut.flush();

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    this.hasdata = false;
                    this.dataToSend = null;

                }
            }
        }
    }
}

