package com.example.thomas.myapplication;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private TextView textViewConnectionStatus;
    private TextView localeIP;
    private EditText editTextIpAddress, editTextPort;
    private Button buttonConnect,buttonDisconnect, buttonControlLeftForwards, buttonControlLeftBackwards, buttonControlRightForwards,buttonControlRightBackwards;
    private Client client;
    private String serverIP;
    private int serverPort;
    private Handler handeler;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localeIP = findViewById(R.id.textEditLocalIP);
        localeIP.setText(localIpAddress());

        editTextIpAddress = findViewById(R.id.textEditServerIP);
        editTextPort = findViewById(R.id.textEditServerPort);
        buttonConnect = findViewById(R.id.buttonServerConnect);
        buttonDisconnect = findViewById(R.id.buttonServerDisconnect);
        textViewConnectionStatus = findViewById(R.id.textViewConnectionStatus);

        buttonControlLeftForwards = findViewById(R.id.buttonControlLeftForwards);
        buttonControlLeftBackwards = findViewById(R.id.buttonControlLeftBackwards);
        buttonControlRightForwards = findViewById(R.id.buttonControlRightForwards);
        buttonControlRightBackwards = findViewById(R.id.buttonControlRightBackwards);

        editTextIpAddress.setText("192.168.2.171");

        //Müsste sich mit dem Looper des UI threads verbinden.
        handeler = new Handler();
        //
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verhindert erneutes drücken des "Verbinden" buttons.

                buttonConnect.setEnabled(false);
                serverIP = editTextIpAddress.getText().toString();
                serverPort = Integer.parseInt(editTextPort.getText().toString());

                //Instanzieren der Client Klasse
                client = new Client(getApplicationContext(), handeler, buttonConnect, buttonDisconnect, buttonControlLeftBackwards, buttonControlLeftForwards, buttonControlRightForwards, textViewConnectionStatus);
                client.connectTo(serverIP, serverPort);

            }
        });
        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.disconnect();
            }
        });

        buttonControlLeftForwards.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    String command = "LF down"; // Spin left wheel forwards
                    client.sendString(command);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    String command = "LF up";  //Stop
                    client.sendString(command);
                }
                return true;
            }

        });

        buttonControlLeftBackwards.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    String command = "LB down"; // Spin left wheel backwards
                    client.sendString(command);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    String command = "LB up";  // stop
                    client.sendString(command);
                }
                return true;
            }
        });


        buttonControlRightForwards.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    String command = "RF down"; // Spin right wheel backwards
                    client.sendString(command);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    String command = "RF up"; //Stop
                    client.sendString(command);
                }
                return true;
            }
        });

        buttonControlRightBackwards.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    String command = "RB down"; // Spin right wheel backwards
                    client.sendString(command);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    String command = "RB up"; //Stop
                    client.sendString(command);
                }
                return true;
            }
        });

    }
    public String localIpAddress() {
        WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        //copy pasta von stack overflow
        String local_ip = String.format(Locale.getDefault(),"%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return local_ip;
    }

}
