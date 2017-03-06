package app.radiouser.gstavrinos.radio_user_app;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.ros.address.InetAddressFactory;
import org.ros.android.MasterChooser;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.util.Iterator;
import java.util.Vector;

public class RoomsActivity extends RosActivity {

    NodeMainExecutor nme;
    GoalPublisher node;
    RoomSubscriber node2;
    //String masterURI = "http://172.11.20.101:11311";
    Vector<Room> rooms;

    protected RoomsActivity(){
        super("Robot connection", "Robot connection", "http://172.17.20.102:11311");
        //startNodeExecutor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        //SharedPreferences prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        //masterURI= prefs.getString("master_uri", "http://172.11.20.101:11311");

        Button back_button  = (Button)findViewById(R.id.back_button);
        Button kitchen_button  = (Button)findViewById(R.id.kitchen_button);
        Button myroom_button  = (Button)findViewById(R.id.myroom_button);
        Button common_area_button  = (Button)findViewById(R.id.common_area_button);

        rooms = new Vector<>();

        Thread thread = new Thread() {
            @Override
            public void run() {

                while (true) {
                    try {
                        sleep(500);
                        if (node2.rooms.compareTo("") != 0) { // keep asking for the rooms until you get them!
                            fillRooms();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for(Room r : rooms){
                                        Log.e(":)",r.getRoom());
                                    }
                                    // TODO create the buttons here
                                }
                            });
                            break;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();

        myroom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                node.x = 1;
                node.y = 1;
                node.z = 16;
                node.new_goal = true;
            }
        });

        kitchen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                node.x = 2;
                node.y = 2;
                node.z = 4;
                node.new_goal = true;
            }
        });

        common_area_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                node.x = 3;
                node.y = 3;
                node.z = 1.777;
                node.new_goal = true;
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomsActivity.this.onBackPressed();
            }
        });

        /*
        * TODO Instead of reading the rooms from the xml, I will have to listen to a ros topic coming from the server, and create the rooms dynamically inside the scrollview!
        */

    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        nme = nodeMainExecutor;
        initNode();
    }

    private void initNode() {
        node = new GoalPublisher();
        node2 = new RoomSubscriber();
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());
        nme.execute(node, nodeConfiguration);
        nme.execute(node2, nodeConfiguration);
    }

    private void fillRooms(){
        try {
            String r[] = node2.rooms.split(",", -1);
            for (int i = 0; i < r.length-4; i++) {
                rooms.add(new Room(r[i],Float.parseFloat(r[i+1]), Float.parseFloat(r[i+2]), Float.parseFloat(r[i+3]), r[i+4]));
            }
        }
        catch(Exception e){
            //TODO show an error message to the user (toast)?
        }
    }
}
