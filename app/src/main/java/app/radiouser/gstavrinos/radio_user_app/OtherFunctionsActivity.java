package app.radiouser.gstavrinos.radio_user_app;

import org.ros.address.InetAddressFactory;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import android.content.Intent;
import android.media.ToneGenerator;
import org.ros.android.RosActivity;
import android.media.AudioManager;
import android.view.WindowManager;
import android.widget.Button;
import android.view.Window;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by gstavrinos on 11/8/17.
 */

public class OtherFunctionsActivity extends RosActivity {


        NodeMainExecutor nme;
        IntPublisher node;
        ToneGenerator toneG;

        public OtherFunctionsActivity(){
            super("Robot connection", "Robot connection", /*"http://192.168.2.216:11311"*/"http://172.17.20.116:11311");
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_other);

            Button back_button  = (Button)findViewById(R.id.back_button);
            toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

            Intent intent = getIntent();
            String text = intent.getStringExtra("text");
            TextView t = (TextView) findViewById(R.id.mid_text);
            t.setText(text);

            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toneG.startTone(ToneGenerator.TONE_PROP_NACK, 600);
                    app.radiouser.gstavrinos.radio_user_app.OtherFunctionsActivity.this.onBackPressed();
                }
            });

        }

        @Override
        public void init(NodeMainExecutor nodeMainExecutor) {
            nme = nodeMainExecutor;
            try {
                initNode();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        private void initNode() {
            Intent intent = getIntent();
            int command = intent.getIntExtra("command", -1);
            node = new IntPublisher(command);
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
            nodeConfiguration.setMasterUri(getMasterUri());
            nme.execute(node, nodeConfiguration);
        }

}
