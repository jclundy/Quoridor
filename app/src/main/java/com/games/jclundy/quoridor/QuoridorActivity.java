package com.games.jclundy.quoridor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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


public class QuoridorActivity extends Activity
{
    private SquaresTableView squaresTable;
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private HashMap<Integer, Integer> playerImageMap;
    private Session session;
    private Switch toggle;
    private int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quoridor);
        Intent previous = getIntent();
        numPlayers = previous.getIntExtra(getString(R.string.NUM_PLAYERS), 2);

        createBoard();
        createButtons();
        setButtonsListener(buttonListener());

        session = new Session(numPlayers);

        playerImageMap = new HashMap<Integer, Integer>();
        playerImageMap.put(GameRuleConstants.PLAYER_IDS[0], R.drawable.bluecircle);
        playerImageMap.put(GameRuleConstants.PLAYER_IDS[1], R.drawable.orangecircle);
        playerImageMap.put(GameRuleConstants.PLAYER_IDS[2], R.drawable.greencircle);
        playerImageMap.put(GameRuleConstants.PLAYER_IDS[3], R.drawable.redcircle);

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

    private View.OnTouchListener generateBoardListener()
    {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)
                {
                    boolean isVertical = toggle.isChecked();
                    int position = squaresTable.getPosition(x, y);
                    position = Board.getValidSquareForWall(position);
                    if(session.canPlaceWall(position, isVertical))
                    {
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
        };
    }

    public void clearBoard()
    {
        squaresTable.removeAllViews();
        squaresTable.invalidate();
        createBoard();
    }

    private void createBoard()
    {
        squaresTable = (SquaresTableView) findViewById(R.id.squaresTable);
        squaresTable.setOnTouchListener(generateBoardListener());
        int squareSize = 50;
        squaresTable.disposeSquares(500, 500, squareSize, numPlayers);
    }

    private void createButtons()
    {
        up = (Button) findViewById(R.id.up);
        down = (Button) findViewById(R.id.down);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
    }

    private void setButtonsListener(View.OnClickListener listener)
    {
        up.setOnClickListener(listener);
        down.setOnClickListener(listener);
        right.setOnClickListener(listener);
        left.setOnClickListener(listener);
    }

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                handleMove(newPosition);
            }
        };
    }

    private void handleMove(int move)
    {
        int currentPlayer = session.getCurrentPlayerID();
        if(session.isMoveValid(move))
        {
            session.makeMove(move);
            int playerImg = playerImageMap.get(currentPlayer);
            squaresTable.movePawn(session.getPlayerPosition(currentPlayer), playerImg);
            handleGameStatus(currentPlayer);
        }
        else
        {
            handleInvalidMove();
        }
    }

    private void handleGameStatus(int lastPlayerToMove)
    {
        if(session.playerHasWon(lastPlayerToMove))
            handleVictory(lastPlayerToMove);
        else
            handleNextMove();
    }

    private void handleVictory(int winningPlayer)
    {
        squaresTable.highlightSquare(session.getPlayerPosition(winningPlayer));
        squaresTable.setOnTouchListener(null);
        setButtonsListener(null);
        Log.d("VICTORY", "Game is Over");
    }

    private void handleNextMove()
    {
        int nextPlayerPosition = session.getCurrentPlayerPosition();
        squaresTable.highlightSquare(nextPlayerPosition);
        Log.d("VALID MOVE", "Next player to move");
    }

    private void handleInvalidMove()
    {
        Log.d("INVALID MOVE", "Please redo move");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
