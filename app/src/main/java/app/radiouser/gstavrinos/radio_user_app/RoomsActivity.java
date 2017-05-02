package app.radiouser.gstavrinos.radio_user_app;

import android.support.v4.content.ContextCompat;
import org.ros.address.InetAddressFactory;
import android.content.DialogInterface;
import org.ros.node.NodeConfiguration;
import android.content.res.Resources;
import org.ros.node.NodeMainExecutor;
import android.media.ToneGenerator;
import org.ros.android.RosActivity;
import android.media.AudioManager;
import android.widget.TableLayout;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.widget.TableRow;
import android.app.AlertDialog;
import android.util.TypedValue;
import java.lang.reflect.Field;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.Window;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.util.Vector;

public class RoomsActivity extends RosActivity {

    NodeMainExecutor nme;
    GoalPublisher node;
    RoomSubscriber node2;
    //String masterURI = "http://172.11.20.101:11311";
    Vector<Room> rooms;
    ToneGenerator toneG;

    protected RoomsActivity(){
        super("Robot connection", "Robot connection", "http://172.21.13.111:11311"); // let's assume that the main controller has this IP.
        //startNodeExecutor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_rooms);

        //SharedPreferences prefs = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        //masterURI= prefs.getString("master_uri", "http://172.11.20.101:11311");

        Button back_button  = (Button)findViewById(R.id.back_button);
        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

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
                                        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, res.getDisplayMetrics());
                                        TableLayout rl = (TableLayout) findViewById(R.id.button_layout);
                                        int btn_cnt = 0;
                                        TableRow tr = new TableRow(getApplicationContext());
                                        Vector<Integer> v = new Vector<>();
                                        TableRow.LayoutParams trp = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT);
                                        trp.setMargins(0, 0, 0, (int) Math.floor(px/7.5));
                                        findViewById(R.id.loading_txt).setVisibility(View.INVISIBLE);
                                        for (Room r : rooms) {
                                            Log.e(":)", r.getRoom());
                                            final Button b = new Button(getApplicationContext());
                                            v.add(View.generateViewId());
                                            b.setId(v.elementAt(btn_cnt));
                                            b.setTextSize(20);
                                            Field fieldID = R.drawable.class.getDeclaredField(r.getImage());
                                            TableRow.LayoutParams p = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                            b.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(getApplicationContext(), fieldID.getInt(fieldID)), null, null);
                                            b.setBackgroundColor(Color.argb(0, 255, 255, 255));
                                            b.setTextColor(Color.rgb(0, 0, 0));
                                            b.setText(r.getName());
                                            b.setAllCaps(false);
                                            b.setPadding(0, 0, 0, 0);
                                            if(btn_cnt % 2 == 0){
                                                tr = new TableRow(getApplicationContext());
                                                tr.setLayoutParams(trp);
                                                p.setMargins(0, 0, px/6, 0);
                                            }
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
                                                    btnClick(r_);
                                                }
                                            });
                                            b.setLayoutParams(p);

                                            tr.addView(b);
                                            if(btn_cnt %  2 == 1){
                                                rl.addView(tr, (int)Math.floor(btn_cnt/2));
                                            }
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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toneG.startTone(ToneGenerator.TONE_PROP_NACK, 600);
                RoomsActivity.this.onBackPressed();
            }
        });

    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        nme = nodeMainExecutor;
        try {
            initNode();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
            for (int i = 0; i < r.length-5; i+=6) {
                rooms.add(new Room(r[i],Float.parseFloat(r[i+1]), Float.parseFloat(r[i+2]), Float.parseFloat(r[i+3]), Float.parseFloat(r[i+4]), r[i+5]));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void btnClick(final Room r_){
        toneG.startTone(ToneGenerator.TONE_PROP_NACK, 600);
        new AlertDialog.Builder(RoomsActivity.this)
                .setTitle(R.string.sure_en)
                .setMessage(R.string.robot_check_en)
                .setPositiveButton(R.string.yes_en, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        node.x = r_.getX();
                        node.y = r_.getY();
                        node.z = r_.getZ();
                        node.z = r_.getW();
                        node.new_goal = true;
                        toneG.startTone(ToneGenerator.TONE_PROP_NACK, 600);
                        new AlertDialog.Builder(RoomsActivity.this)
                                .setMessage(R.string.robot_coming_en)
                                .setPositiveButton(R.string.ok_en, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        toneG.startTone(ToneGenerator.TONE_PROP_NACK, 600);
                                    }
                                })
                                .show();
                    }
                })
                .setNegativeButton(R.string.no_en, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        toneG.startTone(ToneGenerator.TONE_PROP_NACK, 600);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
