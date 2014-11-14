package com.games.jclundy.quoridor.board;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

import com.games.jclundy.quoridor.R;

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

    public SquareImageView(Context context, int row, int col) {
        super(context);
        this.context = context;
        this.row = row;
        this.col = col;
        backgroundID = R.drawable.opentile;
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
        backgroundID = R.drawable.opentile;
        Drawable d = getResources().getDrawable(backgroundID);
        setImageDrawable(d);
    }

    public void removePiece() {
        Drawable d = getResources().getDrawable(backgroundID);
        setImageDrawable(d);
        pieceID = -1;
    }

    public void changeBackground(int resID) {
        backgroundID = resID;
        Drawable backgroundImg = getResources().getDrawable(backgroundID);

        if (pieceID != -1) {
            Drawable[] layers = new Drawable[2];
            layers[0] = backgroundImg;
            layers[1] = getResources().getDrawable(pieceID);
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            setImageDrawable(layerDrawable);
        }
        else {
            setImageDrawable(backgroundImg);
        }

    }
}
