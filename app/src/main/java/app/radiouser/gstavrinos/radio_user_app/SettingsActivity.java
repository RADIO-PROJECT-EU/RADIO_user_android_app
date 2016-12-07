package app.radiouser.gstavrinos.radio_user_app;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Set;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    Button exit_save_buttons = (Button)findViewById(R.id.exit_save_button);
    Button exit_nosave_button = (Button)findViewById(R.id.exit_nosave_button);
    Button add_room_button = (Button)findViewById(R.id.add_room_button);


    final TableLayout tl = (TableLayout)findViewById(R.id.tablelayout);

    add_room_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final TableRow tr = new TableRow(SettingsActivity.this);

        EditText et_room = new EditText(SettingsActivity.this);
        et_room.setHint("Enter Room Name");
        et_room.setSingleLine(true);

        TableLayout minitl = new TableLayout(SettingsActivity.this);
        TableRow minitr = new TableRow(SettingsActivity.this);
        EditText et_x = new EditText(SettingsActivity.this);
        et_x.setHint("Enter X");
        et_x.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
        EditText et_y = new EditText(SettingsActivity.this);
        et_y.setHint("Enter Y");
        et_y.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
        EditText et_z = new EditText(SettingsActivity.this);
        et_z.setHint("Enter Z (orientation)");
        et_z.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
        minitr.addView(et_x);
        minitr.addView(et_y);
        minitr.addView(et_z);
        minitl.addView(minitr);

        Spinner sp = new Spinner(SettingsActivity.this);
        ArrayList<ImageView> spinnerArray = new ArrayList<>();
        ImageView iv = new ImageView(SettingsActivity.this);
        iv.setImageResource(R.drawable.myroom);
        spinnerArray.add(iv);
        iv.setImageResource(R.drawable.restaurant);
        spinnerArray.add(iv);
        iv.setImageResource(R.drawable.common_area);
        spinnerArray.add(iv);
        ArrayAdapter<ImageView> spinnerArrayAdapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        sp.setAdapter(spinnerArrayAdapter);

        Button del_button = new Button(SettingsActivity.this);
        del_button.setText("X");
        del_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            tl.removeView(tr);
          }
        });

        tr.addView(et_room);
        tr.addView(minitl);
        tr.addView(sp);
        tr.addView(del_button);
      }
    });


  }
}
