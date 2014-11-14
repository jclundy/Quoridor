package com.games.jclundy.quoridor.board;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.games.jclundy.quoridor.R;

import java.util.HashMap;

public class BoardFragment extends Fragment implements View.OnTouchListener{
    private SquaresTableView squaresTable;
    private int count;
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private HashMap<Integer, Integer> playerTurns;

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

        playerTurns = new HashMap<Integer, Integer>();
        playerTurns.put(1, R.drawable.blackpawn);
        playerTurns.put(2, R.drawable.bluepawn);
        playerTurns.put(3, R.drawable.whitepawn);

        count = 0;
        this.setRetainInstance(true);
        return v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                squaresTable.placeWall(x, y, true);
                count ++;
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
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        int squareSize = 50;
        int [] vals = squaresTable.disposeSquares(500, 500, squareSize);
    }

    private View.OnClickListener buttonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPlayerNumber = count % 3 + 1;
                int playerImg = playerTurns.get(currentPlayerNumber);
                int currentPosition = squaresTable.getPawnPosition(playerImg);
                int newPosition = currentPosition;
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
                if (newPosition >= 0 && newPosition < 81){
                    squaresTable.movePawn(newPosition, playerImg);
                    count ++;
                }
            }
        };
    }
}
