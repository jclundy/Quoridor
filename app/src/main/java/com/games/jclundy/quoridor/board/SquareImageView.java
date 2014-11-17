package com.games.jclundy.quoridor.board;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

import com.games.jclundy.quoridor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devfloater65 on 11/9/14.
 */
public class SquareImageView extends ImageView {
    private int row;
    private int col;
    private int xSize;
    private int ySize;
    private Context context;
    private int backgroundID;
    private int pieceID;
    private List<Integer> resourceIDs;

    public SquareImageView(Context context, int row, int col) {
        super(context);
        this.context = context;
        this.row = row;
        this.col = col;
        backgroundID = R.drawable.yellowsquare;
        resourceIDs = new ArrayList<Integer>();
        resourceIDs.add(backgroundID);
        Drawable d = getResources().getDrawable(backgroundID);
        setImageDrawable(d);
        xSize = this.getWidth();
        ySize = this.getHeight();
        pieceID = -1;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public void setPiece(int resId) {
        Drawable[] layers = new Drawable[2];
        layers[0] = getResources().getDrawable(backgroundID);
        layers[1] = getResources().getDrawable(resId);
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        setImageDrawable(layerDrawable);
        pieceID = resId;
    }
    public void reset() {
        backgroundID = R.drawable.yellowsquare;
        pieceID = -1;
        Drawable d = getResources().getDrawable(backgroundID);
        setImageDrawable(d);
    }

    public void removePiece() {
        Drawable d = getResources().getDrawable(backgroundID);
        setImageDrawable(d);
        pieceID = -1;
    }

    public void placeWall(int resID) {
        resourceIDs.add(resID);
        int arraySize = resourceIDs.size();
        Drawable[] imgLayers = new Drawable[arraySize];

        for(int i = 0; i < arraySize; i++){
            imgLayers[i] = getResources().getDrawable(resourceIDs.get(i));
        }
        LayerDrawable background = new LayerDrawable(imgLayers);
        if (pieceID != -1) {
            Drawable[] layers = new Drawable[2];
            layers[0] = background;
            layers[1] = getResources().getDrawable(pieceID);
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            setImageDrawable(layerDrawable);
        }
        else {
            setImageDrawable(background);
        }

    }
}
