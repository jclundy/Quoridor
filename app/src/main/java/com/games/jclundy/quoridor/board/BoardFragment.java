package com.games.jclundy.quoridor.board;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.games.jclundy.quoridor.GameRules.Board;
import com.games.jclundy.quoridor.GameRules.GameRuleConstants;
import com.games.jclundy.quoridor.GameRules.Session;
import com.games.jclundy.quoridor.R;

import java.util.HashMap;

public class BoardFragment extends Fragment implements View.OnTouchListener{
    private SquaresTableView squaresTable;
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private HashMap<Integer, Integer> playerTurns;
    private Session session;
    private Switch toggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_board, container, true);
        squaresTable = (SquaresTableView) v.findViewById(R.id.squaresTable);
        squaresTable.setOnTouchListener(this);
        createBoard();
        up = (Button) v.findViewById(R.id.up);
        down = (Button) v.findViewById(R.id.down);
        left = (Button) v.findViewById(R.id.left);
        right = (Button) v.findViewById(R.id.right);

        View.OnClickListener listener = buttonListener();
        up.setOnClickListener(listener);
        down.setOnClickListener(listener);
        right.setOnClickListener(listener);
        left.setOnClickListener(listener);

        session = new Session(4);

        playerTurns = new HashMap<Integer, Integer>();
        playerTurns.put(GameRuleConstants.PLAYER_IDS[0], R.drawable.bluecircle);
        playerTurns.put(GameRuleConstants.PLAYER_IDS[1], R.drawable.orangecircle);
        playerTurns.put(GameRuleConstants.PLAYER_IDS[2], R.drawable.greencircle);
        playerTurns.put(GameRuleConstants.PLAYER_IDS[3], R.drawable.redcircle);

        toggle = (Switch) v.findViewById(R.id.toggle);
        this.setRetainInstance(true);
        return v;
    }

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
                session.placeWall(position, isVertical);
                squaresTable.placeWall(position, isVertical);
            }
            return true;
        }

        return false;
    }

    public void clearBoard() {
        squaresTable.removeAllViews();
        squaresTable.invalidate();
        createBoard();
    }

    public Bitmap createBitmap() {
        return squaresTable.createBitmap();
    }

    public void setBackground(int resourceId) {
        squaresTable.setBackgroundResource(resourceId);
    }

    private void createBoard() {
        int squareSize = 50;
        int [] vals = squaresTable.disposeSquares(500, 500, squareSize);
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
                }
            }
        };
    }
}
