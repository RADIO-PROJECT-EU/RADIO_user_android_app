package app.radiouser.gstavrinos.radio_user_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.ros.node.NodeConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button smart_home_button = (Button)findViewById(R.id.smart_home_button);
        Button facebook_button = (Button)findViewById(R.id.facebook_button);
        Button exit_button = (Button)findViewById(R.id.exit_button);
        Button guide_me_button = (Button)findViewById(R.id.guide_me_button);

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

    }

    private void startWebViewActivity(String url){
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
