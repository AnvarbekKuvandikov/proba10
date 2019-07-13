package com.example.proba10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        Switch switchWifi = findViewById(R.id.switchWiFi);
        switchWifi.setChecked(hasWiFi());
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toogleWiFi(isChecked);
            }
        });

        final EditText ip = findViewById(R.id.ipadress);
        ip.setText(loadText());

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveText(ip.getText().toString());
                startMainAct();
                finish();
            }

        });
    }

    String  loadText() {
       SharedPreferences sPref = getPreferences(MODE_PRIVATE);
       return sPref.getString(Const.IP_ADRES_SHARED_PREF, "");
    }

    void saveText(String ipAdress) {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Const.IP_ADRES_SHARED_PREF,ipAdress);
        ed.apply();
    }

    void startMainAct(){
        EditText ip = findViewById(R.id.ipadress);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("getip", ip.getText().toString());
        startActivity(intent);
    }

    boolean hasWiFi(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork !=null && activeNetwork.isConnectedOrConnecting()){
            return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return  false;
    }

    boolean toogleWiFi(boolean status){
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.setWifiEnabled(status);
    }
}
