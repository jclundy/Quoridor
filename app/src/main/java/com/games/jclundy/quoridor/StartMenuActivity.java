package com.games.jclundy.quoridor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;


public class StartMenuActivity extends Activity {

    private Button startButton;
    private NumberPicker numberPicker;
    private Switch aiSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        startButton = (Button) findViewById(R.id.startButton);
        numberPicker = (NumberPicker) findViewById(R.id.numPlayersPicker);
        aiSwitch = (Switch) findViewById(R.id.aiSwitch);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartMenuActivity.this, QuoridorActivity.class);
                intent.putExtra(getString(R.string.NUM_PLAYERS), numberPicker.getValue());
                startActivity(intent);
            }
        });

        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(4);
        numberPicker.setEnabled(true);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
