package app.radiouser.gstavrinos.radio_user_app;

import android.support.v7.app.AppCompatActivity;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;
import android.media.ToneGenerator;
import android.media.AudioManager;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.URLUtil;
import android.widget.Button;
import android.view.Window;
import android.view.View;
import android.os.Bundle;
import android.os.Build;
import android.net.Uri;

public class WebviewActivity extends AppCompatActivity {

    ToneGenerator toneG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_webview);


        WebView webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);


        webview.setWebViewClient(new WebViewClient() {

            @RequiresApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                final Uri uri = request.getUrl();
                return false;
            }


            @RequiresApi(Build.VERSION_CODES.KITKAT)
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // we handle the url ourselves if it's a network url (http / https)
                return ! URLUtil.isNetworkUrl(url);
            }
        });
        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        String url = getIntent().getStringExtra("url");
        webview.loadUrl(url);

        Button back_button  = (Button)findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toneG.startTone(ToneGenerator.TONE_PROP_NACK, 600);
                WebviewActivity.this.onBackPressed();
            }
        });

    }
}