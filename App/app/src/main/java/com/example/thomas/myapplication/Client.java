package com.example.thomas.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;


import static android.content.ContentValues.TAG;

public class Client {

    private MainActivity mainActivity;
    private Socket connectionSocket;
    private String stringServerIP;
    private int serverPort, timeout = 3000;
    private Handler handlerMainUIThread;
    private TextView textViewConnectionStatus;
    private Context mainContext;
    private ConnectRunnable connectRunnable;
    private Thread connectionThread;
    private SendRunnable sendRunnable;
    private Thread sendThread;


    public Client(MainActivity mainActivity,Context mainContext, Handler handlerMainUIThread, TextView textViewConnectionStatus) {
        this.mainActivity = mainActivity;
        this.mainContext = mainContext;
        this.handlerMainUIThread = handlerMainUIThread;
        this.textViewConnectionStatus = textViewConnectionStatus;
    }

    //Instanziert die Connect Runnable und die Send Runnable
    public void connectTo(String stringServerIP, int serverPort) {
        this.stringServerIP = stringServerIP;
        this.serverPort = serverPort;

        connectRunnable = new ConnectRunnable();
        connectionThread = new Thread(connectRunnable);
        connectionThread.start();
    }

    public void disconnect() {
        try {
            connectionSocket.close();
            handlerMainUIThread.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mainContext, "Trenne Verbindung " + stringServerIP, Toast.LENGTH_SHORT).show();
                    mainActivity.setButtons(false);
                }
            });
        } catch (IOException e) {

        }

    }

    public boolean sendByte(byte byteToSend) {

        if (sendRunnable.HasByteToSend()) {
            return false;
        }
        sendRunnable.SetByteToSend(byteToSend);
        return true;
    }


    class ConnectRunnable implements Runnable {
        ConnectRunnable() {
            //Hier könnte ihre Werbung stehen
        }

        @Override
        public void run() {
            try {
                //Log eintrag.
                Log.d(TAG, "C: Connecting...");
                //Toast Connecting
                handlerMainUIThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainContext, "Connecting", Toast.LENGTH_SHORT).show();
                    }
                });
                //IP string to IP object
                InetAddress serverAddr = InetAddress.getByName(stringServerIP);

                //Create a new instance of Socket
                connectionSocket = new Socket();

                //Start connecting to the server with 3000ms timeout
                //This will block the thread until a connection is established
                connectionSocket.connect(new InetSocketAddress(serverAddr, serverPort), timeout);

                //more log
                Log.d(TAG, "Connected!");
                Log.d(TAG, "Release THE KRAKEN");

                //Change UI to Show that commands may be send now.
                handlerMainUIThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainContext, "Verbunden mit " + stringServerIP, Toast.LENGTH_LONG).show();
                        textViewConnectionStatus.setText("Verbunden");
                        mainActivity.setButtons(true);
                    }
                });

                sendRunnable = new SendRunnable(connectionSocket);
                sendThread = new Thread(sendRunnable);
                sendThread.start();

            } catch (Exception e) {
                e.printStackTrace();
                handlerMainUIThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainContext, "Fehler beim verbinden", Toast.LENGTH_LONG).show();
                        mainActivity.setButtons(false);
                    }
                });

            }
            Log.d(TAG, "Connetion thread stopped");
        }
    }

    class SendRunnable implements Runnable {

        OutputStream streamOut;
        private byte byteToSend;
        private boolean hasByteToSend;

        public SendRunnable(Socket server) {
            Log.d(TAG, "Send Thread spawned" + this);
            this.hasByteToSend = false;
            try {
                streamOut = server.getOutputStream();
            } catch (IOException e) {
            }
        }

        public boolean isConnected() {
            return connectionSocket != null &&
                    connectionSocket.isConnected() &&
                    !connectionSocket.isClosed();
        }

        public boolean HasByteToSend() {
            return hasByteToSend;
        }

        public void SetByteToSend(Byte byteToSend) {

            this.byteToSend = byteToSend;
            this.hasByteToSend = true;
            Log.d(TAG, "Sollte senden,,,," + byteToSend);


        }
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && isConnected()) {
                if (this.hasByteToSend) {
                    try {
                        streamOut.write(byteToSend);
                        this.hasByteToSend = false;
                    }catch (UnsupportedEncodingException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}

