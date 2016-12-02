package app.radiouser.gstavrinos.radio_user_app;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.ros.android.MasterChooser;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

public class RoomsActivity extends RosActivity {

    NodeMainExecutor nme;

    protected RoomsActivity(){
        super("Robot connection","Robot connection");
        //TODO we need a way to bypass the MasterChooser activity (or just modify the library)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms); Button back_button  = (Button)findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO we also need to close the ros node here
                RoomsActivity.this.onBackPressed();
            }
        });

        /*
        * TODO Instead of reading the rooms from the xml, I will have to listen to a ros topic coming from the server, and create the rooms dynamically inside the scrollview!
        */

    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

    }
}
