package com.example.thomas.myapplication;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import android.os.Handler;
import android.widget.Button;


import static android.content.ContentValues.TAG;

public class Client {

    private Socket connectionSocket;
    private String stringServerIP;
    private int serverPort, timeout =3000;
    private Thread sendThread;
    private Handler handlerMainUIThread;
    private Button buttonConnect;

    public Client(Handler handlerMainUIThread, Button buttonConnect){
        this.handlerMainUIThread = handlerMainUIThread;
        this.buttonConnect = buttonConnect;
    }

    public void connectTo(String stringServerIP, int serverPort) {
        this.stringServerIP = stringServerIP;
        this.serverPort = serverPort;
        Thread connectionThread = new Thread(new ConnectRunnable());
        connectionThread.start();


    }

    public boolean send(byte[] dataToSend){
        sendThread = new Thread(new SendRunnable(connectionSocket, dataToSend));
        sendThread.start();

        return false;
    }

    class ConnectRunnable implements Runnable{
        ConnectRunnable(){
        }
        @Override
        public void run(){
            try {
                Log.d(TAG, "C: Connecting...");
                InetAddress serverAddr = InetAddress.getByName(stringServerIP);

                //Create a new instance of Socket
                connectionSocket = new Socket();

                //Start connecting to the server with 3000ms timeout
                //This will block the thread until a connection is established
                connectionSocket.connect(new InetSocketAddress(serverAddr, serverPort), timeout);

                Log.d(TAG, "Connected!");
            } catch (Exception e) {
                e.printStackTrace();

                handlerMainUIThread.post(new Runnable() {
                    @Override
                    public void run() {
                        buttonConnect.setEnabled(true);
                    }
                });

            }
            Log.d(TAG, "Connetion thread stopped");

        }
    }
    class SendRunnable implements Runnable{

        private OutputStream streamOut;
        private byte[] dataToSend;
        private boolean hasdata;

        public SendRunnable(Socket server,byte[] dataToSend){
            Log.d(TAG,"Send Thread spawned" + this);
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

            if (!Thread.currentThread().isInterrupted() && isConnected() && this.hasdata) {
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
            sendThread.interrupt();

        }
    }
}

