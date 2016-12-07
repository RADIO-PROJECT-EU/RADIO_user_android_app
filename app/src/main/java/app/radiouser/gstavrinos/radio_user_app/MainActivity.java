package app.radiouser.gstavrinos.radio_user_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        int wifi_result = checkWifiOnAndConnected();

        if(wifi_result < 0 && !Build.FINGERPRINT.startsWith("generic")){
            String message = "";
            if(wifi_result == -2){
                message = "Wi-Fi is not enabled. Press Ok to enter the Wi-Fi settings, or Cancel to exit the application.";
            }
            else if(wifi_result == -1){
                message = "Wi-Fi is not connected to a network. Press Ok to enter the Wi-Fi settings, or Cancel to exit the application.";
            }
            else{
                message = "Unknown Wi-Fi problem. Press Ok to enter the Wi-Fi settings, or Cancel to exit the application.";
            }
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Wi-Fi Problem!")
                    .setMessage(message)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finishAffinity();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        Button smart_home_button = (Button)findViewById(R.id.smart_home_button);
        Button facebook_button = (Button)findViewById(R.id.facebook_button);
        Button exit_button = (Button)findViewById(R.id.exit_button);
        Button guide_me_button = (Button)findViewById(R.id.guide_me_button);
        //Button settings_button = (Button)findViewById(R.id.settings_button);

        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWebViewActivity("http://www.facebook.com");
            }
        });

        smart_home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWebViewActivity("http://dev.nassist-test.com");
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.onBackPressed();
            }
        });

        guide_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RoomsActivity.class);
                startActivity(intent);
            }
        });

        /*settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });*/

    }

    private void startWebViewActivity(String url){
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    private int checkWifiOnAndConnected() {
        try {
            WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

                if (wifiInfo.getNetworkId() == -1) {
                    return -1; // Not connected to an access point
                }
                return 1; // Connected to an access point
            } else {
                return -2; // Wi-Fi adapter is OFF
            }
        }
        catch (Exception e){
            return -3;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            Intent intent_ = getIntent();
            finish();
            startActivity(intent_);
        }
    }

}
