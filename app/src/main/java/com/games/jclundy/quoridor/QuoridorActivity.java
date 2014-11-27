package com.games.jclundy.quoridor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

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
    private HashMap<Integer, String> playerNameMap;
    private Session session;
    private Switch toggle;
    private int numPlayers;
    private TextView gameStatusLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quoridor);
        Intent previous = getIntent();
        numPlayers = previous.getIntExtra(getString(R.string.NUM_PLAYERS), 2);

        initializeGame();
    }

    private void initializeGame()
    {
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

        playerNameMap = new HashMap<Integer, String>();
        playerNameMap.put(GameRuleConstants.PLAYER_IDS[0], "BLUE PLAYER");
        playerNameMap.put(GameRuleConstants.PLAYER_IDS[1], "ORANGE PLAYER");
        playerNameMap.put(GameRuleConstants.PLAYER_IDS[2], "ORANGE PLAYER");
        playerNameMap.put(GameRuleConstants.PLAYER_IDS[3], "RED PLAYER");

        gameStatusLabel = (TextView) findViewById(R.id.gameStatusLabel);
        handleNextMove();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quoridor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.quit:
                quitApp();
                return true;
            case R.id.restart:
                recreate();
                return true;
            case R.id.newGame:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void quitApp()
    {
        Intent intent = new Intent(QuoridorActivity.this, StartMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
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

                        handleNextMove();
                    }
                    return true;
                }
                return false;
            }
        };
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
        String winningPlayerName = playerNameMap.get(winningPlayer);
        String label = winningPlayerName + " WINS!  GAME OVER";
        gameStatusLabel.setText(label);
    }

    private void handleNextMove()
    {
        int nextPlayerPosition = session.getCurrentPlayerPosition();
        squaresTable.highlightSquare(nextPlayerPosition);
        int nextPlayer = session.getCurrentPlayerID();
        String nextPlayerName = playerNameMap.get(nextPlayer);
        String labelText = nextPlayerName + " TO MOVE";
        gameStatusLabel.setText(labelText);
    }

    private void handleInvalidMove()
    {
        gameStatusLabel.setText("INVALID MOVE");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}