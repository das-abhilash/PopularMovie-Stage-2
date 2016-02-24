package com.example.abhilash.movie;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<trailer> {

   /* private ArrayList<trailer> tr;*/

    public CustomAdapter(Context context, ArrayList<trailer> tr) {
        super(context, R.layout.list_layout, tr);
       // this.tr = tr;
    }

    private static class ViewHolder {
        private TextView trailerText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.list_layout, parent, false);

            viewHolder.trailerText = (TextView) convertView.findViewById(R.id.trailerText);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        trailer trlr = getItem(position);
        if (trlr != null) {
            viewHolder.trailerText.setText(trlr.getName());
        }
        return convertView;

    }

    }
