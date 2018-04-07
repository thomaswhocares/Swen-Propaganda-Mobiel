package com.example.thomas.myapplication;

import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private TextView textViewConnectionStatus;
    private TextView localeIP;
    private EditText editTextIpAddress, editTextPort;
    protected Button buttonConnect, buttonControlLeft, buttonControlReverse, buttonControlRight;
    private Client client;
    private String serverIP;
    private int serverPort;
    private Handler handeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localeIP = (TextView) findViewById(R.id.textEditLocalIP);
        localeIP.setText(localIpAddress());

        editTextIpAddress = (EditText) findViewById(R.id.textEditServerIP);
        editTextPort = (EditText) findViewById(R.id.textEditServerPort);
        buttonConnect = (Button) findViewById(R.id.buttonServerConnect);
        textViewConnectionStatus = (TextView) findViewById(R.id.textViewConnectionStatus);
        buttonControlLeft = (Button) findViewById(R.id.buttonControlLeft);
        buttonControlReverse = (Button) findViewById(R.id.buttonControlReverse);
        buttonControlRight = (Button) findViewById(R.id.buttonControlRight);

        editTextIpAddress.setText("192.168.2.171");

        //Müsste sich mit dem Looper des UI threads verbinden.
        handeler = new Handler();

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verhindert erneutes drücken des "Verbinden" buttons.

                buttonConnect.setEnabled(false);
                serverIP = editTextIpAddress.getText().toString();
                serverPort = Integer.parseInt(editTextPort.getText().toString());

                //Test ob das Lesen klappt
                Toast.makeText(getApplicationContext(), "Verbinde mit "+ serverIP+" " +serverPort,
                        Toast.LENGTH_SHORT).show();

                //Instanzieren der Client Klasse
                client = new Client(handeler,buttonConnect,buttonControlReverse,buttonControlLeft,buttonControlRight,textViewConnectionStatus);
                client.connectTo(serverIP,serverPort);

            }
        });
        buttonControlReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] msg = new byte[1] ;
                msg[0] = 1;
                client.send(msg);
            }
        });
    }


    public String localIpAddress() {
        WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        //copy pasta von stack overflow
        String local_ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return local_ip;
    }

}
