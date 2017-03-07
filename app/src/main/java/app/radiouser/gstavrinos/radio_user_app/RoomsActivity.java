package app.radiouser.gstavrinos.radio_user_app;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ros.address.InetAddressFactory;
import org.ros.android.MasterChooser;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Vector;

public class RoomsActivity extends RosActivity {

    NodeMainExecutor nme;
    GoalPublisher node;
    RoomSubscriber node2;
    //String masterURI = "http://172.11.20.101:11311";
    Vector<Room> rooms;

    protected RoomsActivity(){
        super("Robot connection", "Robot connection", "http://192.168.1.111:11311"); // let's assume that the main controller has this IP.
        //startNodeExecutor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        //SharedPreferences prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        //masterURI= prefs.getString("master_uri", "http://172.11.20.101:11311");

        Button back_button  = (Button)findViewById(R.id.back_button);
        //Button kitchen_button  = (Button)findViewById(R.id.kitchen_button);
        //Button myroom_button  = (Button)findViewById(R.id.myroom_button);
        //Button common_area_button  = (Button)findViewById(R.id.common_area_button);

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
                                    try {
                                        Resources res = getResources();
                                        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, res.getDisplayMetrics());
                                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.button_layout);
                                        int btn_cnt = 0;
                                        Vector<Integer> v = new Vector<>();
                                        findViewById(R.id.loading_txt).setVisibility(View.INVISIBLE);
                                        for (Room r : rooms) {
                                            Log.e(":)", r.getRoom());
                                            final Button b = new Button(getApplicationContext());
                                            b.setWidth(px);
                                            b.setHeight(px);
                                            v.add(View.generateViewId());
                                            b.setId(v.elementAt(btn_cnt));
                                            b.setTextSize(14);
                                            Field fieldID = R.drawable.class.getDeclaredField(r.getImage());
                                            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            p.setMargins(0, px/6, 0, 0);
                                            if (btn_cnt > 1){
                                                p.addRule(RelativeLayout.BELOW, v.elementAt(btn_cnt-2));
                                            }
                                            if (btn_cnt % 2 == 0){
                                                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            }
                                            else{
                                                p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                            }
                                            b.setLayoutParams(p);
                                            b.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(getApplicationContext(), fieldID.getInt(fieldID)), null, null);
                                            b.setBackgroundColor(Color.argb(0, 255, 255, 255));
                                            b.setTextColor(Color.rgb(0, 0, 0));
                                            b.setText(r.getName().toUpperCase());
                                            b.setPadding(0, 0, 0, 0);
                                            b.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                                                        b.setTextColor(Color.rgb(51, 187, 51));
                                                    }
                                                    else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                                                        b.setTextColor(Color.rgb(0, 0, 0));
                                                    }
                                                    return false;
                                                }
                                            });
                                            final Room r_ = r;
                                            b.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    node.x = r_.getX();
                                                    node.y = r_.getY();
                                                    node.z = r_.getZ();
                                                    node.new_goal = true;
                                                }
                                            });


                                            rl.addView(b);
                                            btn_cnt++;
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
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

        /*myroom_button.setOnClickListener(new View.OnClickListener() {
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
        });*/

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomsActivity.this.onBackPressed();
            }
        });

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
            for (int i = 0; i < r.length-4; i+=5) {
                rooms.add(new Room(r[i],Float.parseFloat(r[i+1]), Float.parseFloat(r[i+2]), Float.parseFloat(r[i+3]), r[i+4]));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
