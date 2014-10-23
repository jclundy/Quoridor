package com.games.jclundy.quoridor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by devfloater65 on 10/18/14.
 */
public class BoardAdapter extends BaseAdapter{
    private Context context;
    private Integer[] tileImageIds;

    public BoardAdapter(Context c) {
        context = c;
        tileImageIds = new Integer[81];
        for(int i = 0; i < 81; i++)
            tileImageIds[i] = R.drawable.square;
        }

    public int getCount() {
        return tileImageIds.length;
        }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(tileImageIds[position]);
        return imageView;
        }
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

}