package com.games.jclundy.quoridor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.games.jclundy.quoridor.board.BoardFragment;


public class QuoridorActivity extends Activity {

    private BoardFragment boardFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            Intent previous = getIntent();
            int numPlayers = previous.getIntExtra(getString(R.string.NUM_PLAYERS), 2);

            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.NUM_PLAYERS), numPlayers);
            boardFrag = new BoardFragment();
            boardFrag.setArguments(bundle);
        }
        setContentView(R.layout.activity_quoridor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quoridor, menu);
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
