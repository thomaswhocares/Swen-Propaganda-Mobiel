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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import static android.content.ContentValues.TAG;

public class Client {

    private Socket connectionSocket;
    private String stringServerIP;
    private int serverPort, timeout = 3000;
    private Handler handlerMainUIThread;
    private Button buttonConnect, buttonDisconnect, buttonReverse, buttonLeft, buttonRight;
    private TextView textViewConnectionStatus;
    private Context mainContext;

    private ConnectRunnable connectRunnable;
    private Thread connectionThread;
    private SendRunnable sendRunnable;
    private Thread sendThread;


    public Client(Context mainContext, Handler handlerMainUIThread, Button buttonConnect, Button buttonDisconnect, Button buttonReverse, Button buttonLeft, Button buttonRight, TextView textViewConnectionStatus) {
        this.mainContext = mainContext;
        this.handlerMainUIThread = handlerMainUIThread;
        this.buttonConnect = buttonConnect;
        this.buttonDisconnect = buttonDisconnect;
        this.buttonReverse = buttonReverse;
        this.buttonLeft = buttonLeft;
        this.buttonRight = buttonRight;
        this.textViewConnectionStatus = textViewConnectionStatus;
    }

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
                    buttonConnect.setEnabled(true);
                    buttonDisconnect.setEnabled(false);
                    buttonReverse.setEnabled(false);
                    buttonLeft.setEnabled(false);
                    buttonRight.setEnabled(false);
                }
            });
        } catch (IOException e) {

        }

    }

    public boolean sendString(String s) {
        if (sendRunnable == null) {
            sendRunnable = new SendRunnable(connectionSocket);
            sendThread = new Thread(sendRunnable);
            sendThread.start();
        }

        if (sendRunnable.HasStringToSend()) {
            return false;
        }
        sendRunnable.StringToSend(s);
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
                        buttonDisconnect.setEnabled(true);
                        buttonReverse.setEnabled(true);
                        buttonLeft.setEnabled(true);
                        buttonRight.setEnabled(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                handlerMainUIThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainContext, "Fehler beim verbinden", Toast.LENGTH_LONG).show();
                        buttonConnect.setEnabled(true);
                    }
                });

            }
            Log.d(TAG, "Connetion thread stopped");
        }
    }

    class SendRunnable implements Runnable {

        OutputStream streamOut;
        private String stringToSend;
        private boolean hasStringToSend;

        public SendRunnable(Socket server) {
            Log.d(TAG, "Send Thread spawned" + this);
            this.hasStringToSend = false;
            try {
                streamOut = server.getOutputStream();
                /*BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(server.getInputStream()));*/
            } catch (IOException e) {
            }
        }

        public boolean isConnected() {
            return connectionSocket != null &&
                    connectionSocket.isConnected() &&
                    !connectionSocket.isClosed();
        }

        public boolean HasStringToSend() {
            return hasStringToSend;
        }

        public void StringToSend(String StringToSend) {

            this.stringToSend = StringToSend;
            this.hasStringToSend = true;
            Log.d(TAG, "Sollte senden,,,," + StringToSend);


        }
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && isConnected()) {
                if (this.hasStringToSend) {
                    try {
                        streamOut.write(this.stringToSend.getBytes("UTF-8"));
                        this.hasStringToSend = false;
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

