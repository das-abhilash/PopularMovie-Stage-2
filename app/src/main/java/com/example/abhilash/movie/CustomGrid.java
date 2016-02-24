package com.example.abhilash.movie;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomGrid extends CursorAdapter {

    public CustomGrid(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid, parent, false);
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView imageview = (ImageView) view.findViewById(R.id.grid_image);
        int idx_poster = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);

        Picasso.with(context).load((cursor.getString(idx_poster))).into(imageview);
    }
}
