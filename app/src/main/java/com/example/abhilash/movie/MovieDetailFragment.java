package com.example.abhilash.movie;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.ContentValues;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.support.v4.content.CursorLoader;
import android.widget.Toast;

//import com.example.abhilash.movie.dummy.DummyContent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 2;


    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." +
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_ABOUT,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR,
            MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT,
            MovieContract.MovieEntry.COLUMN_TRAILER_NAME,
            MovieContract.MovieEntry.COLUMN_VIDEO_PATH,
            MovieContract.MovieEntry.COLUMN_SORT_KEY
    };


    private static final int COL_MOVIE_ID = 0;

    private static final int COL_MOVIE_TITLE = 1;

    private static final int COL_MOVIE_POSTER = 2;

    private static final int COL_ABOUT = 3;

    private static final int COL_RELEASE_DATE = 4;

    private static final int COL_RATING = 5;

    private static final int COL_REVIEW_AUTHOR = 6;

    private static final int COL_REVIEW_CONTENT = 7;

    private static final int COL_TRAILER_NAME = 8;

    private static final int COL_VIDEO_PATH = 9;

    private static final int COL_SORT_KEY = 10;


    private ListView TList;

    private ListView RList;

    private TextView aboutText;

    private TextView detailText;

    private TextView ratingText;

    private TextView releaseText;

    private ImageView picassoImage;

    private Button favBtn;

    private boolean FAVORITE;

    private boolean twoPane;
    public static  String Share_movie_name = null;

    public static  String Share_trailer = null;

    public static final String ARG_ITEM_ID = "item_id";
    public static final String twopane = "twopane";
    public Uri uri;
    FloatingActionButton fab;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fabShare);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String uurl = (getArguments().getString(ARG_ITEM_ID));
            twoPane = (getArguments().getBoolean(twopane));
            String sortSetting = Utility.getPreferredSort(getContext());
            uri = MovieContract.MovieEntry.buildMovieSortWithMovieId(
                    sortSetting, uurl);
            /*Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }*/
        }
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        if (twoPane)
        setHasOptionsMenu(true);
        TList = (ListView) rootView.findViewById(R.id.list);
        RList = (ListView) rootView.findViewById(R.id.list_review);
        detailText = (TextView) rootView.findViewById(R.id.title);
        aboutText = (TextView) rootView.findViewById(R.id.about);
        ratingText = (TextView) rootView.findViewById(R.id.rating);
        releaseText = (TextView) rootView.findViewById(R.id.release);
        picassoImage = (ImageView) rootView.findViewById(R.id.picassoImage);
        favBtn = (Button) rootView.findViewById(R.id.favBtn);

//        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabShare);

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                              *//* Snackbar.make(view, "Replace ", Snackbar.LENGTH_LONG)
159                         .setAction("Action", null).show();*//*


            }


        });*/

        // Show the dummy content as text in a TextView.


        return rootView;
    }
/*public static void Shareintent(){
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    //  String shareBody = "This is the first Trailer";

    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Movie Name : " + Share_movie_name);
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Share_trailer);
    startActivity(Intent.createChooser(sharingIntent, "Share via"));
}*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share)
       {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "This is the first Trailer";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Movie Name : " + Share_movie_name);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Share_trailer);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
return false;
       // return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Intent intent = getActivity().getIntent();


        //  Uri uri = intent.getData();
        // if (intent == null) {

        return new CursorLoader(
                getActivity(),
                uri,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );

    }

    @Override


    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;

        }
        int count = data.getCount();
        final String Mtitle = data.getString(COL_MOVIE_TITLE);
        Share_movie_name = Mtitle;
        detailText.setText(Mtitle);
        detailText.setFocusableInTouchMode(true);
        detailText.requestFocus();


        final String about = data.getString(COL_ABOUT);
        aboutText.setText(about);


        final String rating = data.getString(COL_RATING);
        ratingText.setText(rating);


        final String SortKey = data.getString(COL_SORT_KEY);


        final String release = data.getString(COL_RELEASE_DATE);
        releaseText.setText(Utility.parseDateToddMMyyyy(release));


        final String image = data.getString(COL_MOVIE_POSTER);
        Picasso.with(getActivity()).load(image).into(picassoImage);


        final String review = data.getString(COL_REVIEW_AUTHOR);
        final String content = data.getString(COL_REVIEW_CONTENT);


        ArrayList<review> re = Utility.ConvertStringToArrayReview(review, content);


        Displayreview(re);


        final String tra = data.getString(COL_TRAILER_NAME);
        final String path = data.getString(COL_VIDEO_PATH);
        ArrayList<trailer> trlr = Utility.ConvertStringToArrayTrailer(tra, path);
        Share_trailer = trlr.get(0).getKey();
        TrailerOnClick(trlr);
        final String Movie_id = data.getString(COL_MOVIE_ID);
        String favorite_key = String.valueOf(Utility.getSort("favorite", getActivity()));

        Cursor cur = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_SORT_KEY + " = ? AND " + MovieContract.MovieEntry.COLUMN_MOVIE_ID
                        + "= ?", new String[]{favorite_key, Movie_id}, null);
        //  MovieContract.MovieEntry.COLUMN_MOVIE_ID
        //    + "= ?", new String[]{Movie_id}, null);
        int i = cur.getCount();
        int j = 0;
        // if (i == 2) {
        if (i != 0) {
            FAVORITE = true;
            favBtn.setText("Remove Fav");

        } else {
            FAVORITE = false;
            favBtn.setText("Set Fav");

        }


        favBtn.setOnClickListener(
                new Button.OnClickListener() {


                    @Override


                    public void onClick(View v) {


                        long fav_key = (Utility.getSort("favorite", getActivity()));
                        if (!FAVORITE) {
                            ContentValues movieValues = new ContentValues();
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, Movie_id);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, Mtitle);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, image);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_ABOUT, about);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_SORT_KEY, Utility.getSort("favorite", getActivity()));
                            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT, content);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR, review);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_NAME, tra);
                            movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO_PATH, path);

                            getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieValues);
                            FAVORITE = true;
                            favBtn.setText("Remove Fav");

                            Toast.makeText(getActivity(), "Marked As Favorite", Toast.LENGTH_SHORT).show();

                        } else {


                            int Del_Num = getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                    MovieContract.MovieEntry.COLUMN_SORT_KEY + " = ? AND " + MovieContract.MovieEntry.COLUMN_MOVIE_ID
                                            + "= ?", new String[]{String.valueOf(fav_key), Movie_id});


                            FAVORITE = false;
                            favBtn.setText("Set Fav");

                            Toast.makeText(getActivity(), "Removed From Favorite", Toast.LENGTH_SHORT).show();

                        }


                    }


                }
        );

    }

    @Override


    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void TrailerOnClick(final ArrayList<trailer> tr) {
        ListAdapter myAdapter = new CustomAdapter(getContext(), tr);


        TList.setAdapter(myAdapter);
        // TList.setFocusable(true);


        TList.postDelayed(new Runnable() {
            public void run() {
                Utility.setListViewHeightBasedOnChildren(TList);
            }
        }, 400);
        //  Utility.setListViewHeightBasedOnChildren(TList);
        //  TList.setExpand(true);


        TList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        trailer tra = tr.get(position);
                        String video_path = tra.getKey();


                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_path));
                        startActivity(intent);
                    }
                });
    }

    public void Displayreview(final ArrayList<review> re) {


        if (re != null) {
            ListAdapter myAdapter = new CustomAdapterReview(getContext(), re);


            RList.setAdapter(null);
            RList.setAdapter(myAdapter);
            //RList.setFocusable(true);
            RList.postDelayed(new Runnable() {
                public void run() {
                    Utility.setListViewHeightBasedOnChildren(RList);
                }
            }, 400);
            //  Utility.setListViewHeightBasedOnChildren(RList);


        }
    }


}
