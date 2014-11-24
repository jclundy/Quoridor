package com.games.jclundy.quoridor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.games.jclundy.quoridor.GameRules.Board;
import com.games.jclundy.quoridor.GameRules.GameRuleConstants;
import com.games.jclundy.quoridor.GameRules.Session;
import com.games.jclundy.quoridor.board.SquaresTableView;

import java.util.HashMap;


public class QuoridorActivity extends Activity {
    private SquaresTableView squaresTable;
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private HashMap<Integer, Integer> playerTurns;
    private Session session;
    private Switch toggle;
    private int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quoridor);
        Intent previous = getIntent();
        numPlayers = previous.getIntExtra(getString(R.string.NUM_PLAYERS), 2);

        squaresTable = (SquaresTableView) findViewById(R.id.squaresTable);
        squaresTable.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    boolean isVertical = toggle.isChecked();
                    int position = squaresTable.getPosition(x, y);
                    position = Board.getValidSquareForWall(position);
                    if(session.canPlaceWall(position, isVertical)){

                        int currentPlayerPosition = session.getCurrentPlayerPosition();
                        squaresTable.unHighlightSquare(currentPlayerPosition);

                        session.placeWall(position, isVertical);
                        squaresTable.placeWall(position, isVertical);

                        int nextPlayerPosition = session.getCurrentPlayerPosition();
                        squaresTable.highlightSquare(nextPlayerPosition);
                    }
                    return true;
                }

                return false;
            }
        });
        createBoard();
        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);

        View.OnClickListener listener = buttonListener();
        up.setOnClickListener(listener);
        down.setOnClickListener(listener);
        right.setOnClickListener(listener);
        left.setOnClickListener(listener);

        session = new Session(numPlayers);

        playerTurns = new HashMap<Integer, Integer>();
        playerTurns.put(GameRuleConstants.PLAYER_IDS[0], R.drawable.bluecircle);
        playerTurns.put(GameRuleConstants.PLAYER_IDS[1], R.drawable.orangecircle);
        playerTurns.put(GameRuleConstants.PLAYER_IDS[2], R.drawable.greencircle);
        playerTurns.put(GameRuleConstants.PLAYER_IDS[3], R.drawable.redcircle);

        toggle = (Switch) findViewById(R.id.toggle);
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
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearBoard() {
        squaresTable.removeAllViews();
        squaresTable.invalidate();
        createBoard();
    }

    private void createBoard() {
        int squareSize = 50;
        squaresTable.disposeSquares(500, 500, squareSize, numPlayers);
    }

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPlayer = session.getCurrentPlayerID();
                int currentPlayerPosition = session.getCurrentPlayerPosition();
                int newPosition = currentPlayerPosition;
                switch (view.getId()) {
                    case R.id.up:
                        newPosition -= 9;
                        break;
                    case R.id.down:
                        newPosition += 9;
                        break;
                    case R.id.left:
                        newPosition -= 1;
                        break;
                    case R.id.right:
                        newPosition += 1;
                        break;
                    default:
                        break;
                }
                session.makeMove(newPosition);
                if(session.getCurrentPlayerID() != currentPlayer){
                    int playerImg = playerTurns.get(currentPlayer);
                    squaresTable.movePawn(session.getPlayerPosition(currentPlayer), playerImg);
                    int nextPlayerPosition = session.getCurrentPlayerPosition();
                    squaresTable.highlightSquare(nextPlayerPosition);
                }
            }
        };
    }
}
