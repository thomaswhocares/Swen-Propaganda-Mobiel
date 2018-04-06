package com.example.thomas.myapplication;

import android.net.wifi.WifiInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket socket;

    private static final int port = 10000;
    static  String Server_ip = "0";



     public String localIpAdress(){

         WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
         WifiInfo wifiInf = wifiMan.getConnectionInfo();
         int ipAddress = wifiInf.getIpAddress();
         String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
         return ip;
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.localIP);
        textView.setText(localIpAdress());

    }
}

