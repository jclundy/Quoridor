package com.games.jclundy.quoridor.board;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.games.jclundy.quoridor.GameRules.GameRuleConstants;
import com.games.jclundy.quoridor.R;
import java.util.HashMap;

/**
 * Created by devfloater65 on 11/9/14.
 */
public class SquaresTableView extends ViewGroup {

    private Context context;
    private int parentWidth;
    private int parentHeight;
    private int squareSize;
    private int numRow;
    private int numCol;

    private HashMap<Integer, Integer> positionMap;

    public SquaresTableView(Context context){
        super(context);
        this.context = context;
    }

    public SquaresTableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public SquaresTableView(Context context, AttributeSet attsr) {
        super(context, attsr);
        this.context = context;
    }

    public void disposeSquares(int width, int height, int squareSize, int numPlayers) {

        this.squareSize = squareSize;
        numRow = 9;
        numCol = 9;
        for (int row = 0; row < numRow; row ++) {
            for (int col = 0; col < numCol; col ++) {
                SquareImageView squareImg = new SquareImageView(getContext(), row, col);
                this.addView(squareImg);
            }
        }
        positionMap = new HashMap<Integer, Integer>();

        int [] drawables = new int[] {R.drawable.bluecircle,
                                    R.drawable.orangecircle,
                                    R.drawable.greencircle,
                                    R.drawable.redcircle};

        for(int i = 0; i < numPlayers; i++) {
            positionMap.put(drawables[i], GameRuleConstants.START_POSITIONS[i]);
            placePawn(drawables[i]);
        }

        SquareImageView highlighted = (SquareImageView) getChildAt(GameRuleConstants.START_POSITIONS[0]);
        highlighted.highlightPiece();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth  = MeasureSpec.getSize(widthMeasureSpec) ;
        parentHeight = MeasureSpec.getSize(heightMeasureSpec) ;

        this.setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        int childCount = getChildCount();

        for(int i = 0; i < childCount; i++) {
            SquareImageView squareImage = (SquareImageView) getChildAt(i);
            int left = squareImage.getCol() * squareSize;
            int top = squareImage.getRow() * squareSize;
            int right = left + squareSize;
            int bottom = top + squareSize;

            squareImage.layout(left, top, right, bottom);
        }
    }

    public void movePawn (int newPosition, int resID){
        int oldPosition = getPawnPosition(resID);
        SquareImageView oldSquare = (SquareImageView) getChildAt(oldPosition);
        if(oldSquare != null)
            oldSquare.updateBackgroundToDefault();
            oldSquare.removePiece();
        SquareImageView newSquare = (SquareImageView) getChildAt(newPosition);
        if (newSquare != null) {
            newSquare.setPiece(resID);
            positionMap.put(resID, newPosition);
        }
    }

    public void placePawn(int imgID) {
        int pos = getPawnPosition(imgID);
        SquareImageView newSquare = (SquareImageView) getChildAt(pos);
        if (newSquare != null) {
            newSquare.setPiece(imgID);
        }
    }

    public void placeWall(int newPosition, boolean isVertical) {
        SquareImageView firstSquare = (SquareImageView) getChildAt(newPosition);
        SquareImageView secondSquare;
        int resID;
        if(isVertical){
            resID = R.drawable.vertical_black_line;
            secondSquare = (SquareImageView) getChildAt(newPosition + 9);
        }
        else{
            resID = R.drawable.horizontal_black_line_bottom;
            secondSquare = (SquareImageView) getChildAt(newPosition + 1);
        }
        if (firstSquare != null && secondSquare != null) {
            firstSquare.placeWall(resID);
            secondSquare.placeWall(resID);
        }
    }

    public void highlightSquare(int position) {
        SquareImageView square = (SquareImageView) getChildAt(position);
        square.highlightPiece();
    }

    public void unHighlightSquare(int position) {
        SquareImageView square = (SquareImageView) getChildAt(position);
        square.unHighlightPiece();
    }

    public int getRow(int y) {
        return (int) Math.ceil(y / squareSize);
    }

    public int getColumn(int x) {
        return (int) Math.ceil( x / squareSize);
    }

    public int getPawnPosition(int resID) {
        return positionMap.get(resID);
    }

    public int getPosition(int x, int y)
    {
        int row = getRow(y);
        int col = getColumn(x);
        return  col + row * numCol;
    }
}