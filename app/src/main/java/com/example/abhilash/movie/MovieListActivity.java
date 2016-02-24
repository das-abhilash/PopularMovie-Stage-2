package com.example.abhilash.movie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView;

public class MovieListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    GridView gridview;
    private CustomGrid mygrid;
    private static int Movie_Loader = 4;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        mygrid = new CustomGrid(this, null, 0);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(mygrid);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        getLoaderManager().initLoader(Movie_Loader, null, this);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSort);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, cursor.getString(1));
                        arguments.putBoolean(MovieDetailFragment.twopane, mTwoPane);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, cursor.getString(1));
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String SortSetting = Utility.getPreferredSort(this);
        String sortOrder = MovieContract.MovieEntry._ID + " DESC";
        Uri movieForSortUri = MovieContract.MovieEntry.buildSortUri(SortSetting);

        return new CursorLoader(this,
                movieForSortUri,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int ii = data.getCount();
        mygrid.notifyDataSetChanged();
        mygrid.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        onSortChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    private void updateMovie() {
        String Sort = Utility.getPreferredSort(this);
        if (!"favorite".equals(Sort))
            new Downloadtask(this).execute(Sort);
    }

    void onSortChanged() {
        updateMovie();
        getLoaderManager().restartLoader(Movie_Loader, null, this);
    }
}
