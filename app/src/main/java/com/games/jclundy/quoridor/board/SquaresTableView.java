package com.games.jclundy.quoridor.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.games.jclundy.quoridor.R;

/**
 * Created by devfloater65 on 11/9/14.
 */
public class SquaresTableView extends ViewGroup {

    private Context context;

    private int parentWidth;
    private int parentHeight;
    private int squareSize;
    private int backgroundID;
    private int numRow;
    private int numCol;

    private int currentPosition;

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

    public int[] disposeSquares(int width, int height, int squareSize) {
        this.squareSize = squareSize;
        numRow = 9;
        numCol = 9;

        for (int row = 0; row < numRow; row ++) {
            for (int col = 0; col < numCol; col ++) {
                SquareImageView squareImg = new SquareImageView(getContext(), row, col);
                this.addView(squareImg);
            }
        }
        currentPosition = 4;
        placePawn(currentPosition, R.drawable.pawn);
        return new int[]{numRow, numCol};
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

    public void movePawn(int x, int y, int imgID) {
        int oldPosition = currentPosition;
        SquareImageView oldSquare = (SquareImageView) getChildAt(oldPosition);
        int row = getRow(y);
        int col = getColumn(x);
        int newPosition = col + row * numCol;
        SquareImageView newSquare = (SquareImageView) getChildAt(newPosition);
        if (newSquare != null) {
            newSquare.setPiece(imgID);
            oldSquare.removePiece();
            currentPosition = newPosition;

        }
    }

    public void movePawn (int position, int resID){
        int oldPosition = currentPosition;
        SquareImageView oldSquare = (SquareImageView) getChildAt(oldPosition);
        if(oldSquare != null)
            oldSquare.removePiece();

        int newposition = position;
        SquareImageView newSquare = (SquareImageView) getChildAt(newposition);
        if (newSquare != null) {
            newSquare.setPiece(resID);
            currentPosition = newposition;
        }
    }

    public void placePawn(int pos, int imgID) {
        SquareImageView newSquare = (SquareImageView) getChildAt(pos);
        if (newSquare != null) {
            newSquare.setPiece(imgID);
            currentPosition = pos;
        }
    }

    public void placeWall(int x, int y, boolean isVertical) {
        int row = getRow(y);
        int col = getColumn(x);
        int newPosition = col + row * numCol;
        SquareImageView newSquare = (SquareImageView) getChildAt(newPosition);
        if (newSquare != null) {
            newSquare.changeBackground(R.drawable.singlewall);
        }
    }

    public int getRow(int y) {
        return (int) Math.ceil(y / squareSize);
    }

    public int getColumn(int x) {
        return (int) Math.ceil( x / squareSize);
    }

    public Bitmap createBitmap() {
        Bitmap b = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        this.draw(c);
        return b;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}