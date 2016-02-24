package com.example.abhilash.movie;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Utility {

    public static String getPreferredSort(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default));
    }

    public static long getSort(String sortSetting, Context context) {

        long sortId;

        Cursor sortCursor = context.getContentResolver().query(
                MovieContract.SortEntry.CONTENT_URI,
                new String[]{MovieContract.SortEntry._ID},
                MovieContract.SortEntry.COLUMN_SORT_SETTING + " = ?",
                new String[]{sortSetting},
                null);

        if (sortCursor.moveToFirst()) {
            int locationIdIndex = sortCursor.getColumnIndex(MovieContract.SortEntry._ID);
            sortId = sortCursor.getLong(locationIdIndex);
        } else {
            ContentValues sortValues = new ContentValues();
            sortValues.put(MovieContract.SortEntry.COLUMN_SORT_SETTING, sortSetting);
            Uri insertedUri = context.getContentResolver().insert(
                    MovieContract.SortEntry.CONTENT_URI,
                    sortValues
            );
            sortId = ContentUris.parseId(insertedUri);
        }

        sortCursor.close();
        return sortId;
    }

    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy ";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth,
                    View.MeasureSpec.makeMeasureSpec(desiredWidth, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static ArrayList<trailer> ConvertStringToArrayTrailer(String tra, String path) {
        ArrayList<trailer> Trl = new ArrayList<trailer>();

        String[] TrailerString = tra.split("`");
        String[] PathString = path.split("`");
        for (int i = 1; i < TrailerString.length; i++) {
            trailer trl = new trailer();
            trl.setName(TrailerString[i]);
            trl.setKey(PathString[i]);
            Trl.add(trl);
        }

        return Trl;
    }

    public static ArrayList<review> ConvertStringToArrayReview(String review, String content) {
        ArrayList<review> rev = new ArrayList<review>();

        String[] ReviewString = review.split("`");
        String[] ContentString = content.split("`");

        for (int i = 1; i < ReviewString.length; i++) {
            review revw = new review();
            if (ReviewString[i] == "") continue;
            revw.setAuthor(ReviewString[i]);
            revw.setContent(ContentString[i]);
            rev.add(revw);
        }

        return rev;
    }


}
