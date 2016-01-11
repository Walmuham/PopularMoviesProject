package app.wafaa.popularmovies.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.wafaa.popularmovies.MoiveUtilities.Constants;
import app.wafaa.popularmovies.MovieAdapter;
import app.wafaa.popularmovies.R;
import app.wafaa.popularmovies.MoiveUtilities.Utils;
import app.wafaa.popularmovies.Sync.MovieSyncAdapter;
import app.wafaa.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieActivityFragment.class.getSimpleName();

    // This is the Adapter being used to display the Movie posters.
    private MovieAdapter movieAdapter;
    private GridView posterGridView;

    private int mPosition = GridView.INVALID_POSITION;

    private final int POP_MOVIE_LOADER_ID=0;
    private final int HIGH_RAT_LOADER_ID=1;
    private final int FAVORITES_LOADER_ID=2;

    MenuItem show_popMovie;
    MenuItem show_highRated ;
    MenuItem show_myFavorite ;

    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;


    private static final String MOVIES_KEY = "movies";


    /**
     * related to what user will select for sort purpose, by default movie posters are sorted by Popularity
     */
    public static String SortBy=Constants.popSort;




    @Override
    public void onResume() {
        super.onResume();
    }



    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        updateMovieView(SortBy);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie, menu);

        show_popMovie = menu.findItem(R.id.sort_by_popularity);
        show_highRated = menu.findItem(R.id.sort_by_high_rating);
        show_myFavorite = menu.findItem(R.id.sort_by_favorites);
//
        if (SortBy.contentEquals(Constants.popSort)) {
            if (!show_popMovie.isChecked()) {

                show_popMovie.setChecked(true);
                SortBy=Constants.popSort;
            }
        }
      else if (!SortBy.contentEquals(Constants.highRateSort)) {
            if (!show_highRated.isChecked()) {
                show_highRated .setChecked(true);
                SortBy=Constants.highRateSort;

            }
        }
       else if (!SortBy.contentEquals(Constants.favoriteSort)) {
            if (!show_myFavorite.isChecked()) {
                show_myFavorite.setChecked(true);
                SortBy=Constants.favoriteSort;
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch (id) {
            case R.id.sort_by_popularity: {
                updateMenuItem(item);
                updateMovieView(Constants.popSort);
                getLoaderManager().restartLoader(POP_MOVIE_LOADER_ID, null, this);

                return true;
            }
            case R.id.sort_by_high_rating: {
                updateMenuItem(item);
                updateMovieView(Constants.highRateSort);
                getLoaderManager().restartLoader(HIGH_RAT_LOADER_ID, null, this);

                return true;
            }
            case R.id.sort_by_favorites: {
                updateMenuItem(item);
                getLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mCallbacks=this;
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(POP_MOVIE_LOADER_ID, null, mCallbacks);
        getLoaderManager().initLoader(HIGH_RAT_LOADER_ID, null, mCallbacks);
        getLoaderManager().initLoader(FAVORITES_LOADER_ID, null,mCallbacks);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Bundle bundle);
    }



    private void updateMenuItem(MenuItem item){
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_movie, container, false);


        if(movieAdapter==null) {
            // Create an empty adapter we will use to display the loaded data.
            movieAdapter = new MovieAdapter(getActivity(), null, 0);
        }

        // Get a reference to the GridView, and attach this adapter to it.
        posterGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        posterGridView.setAdapter(movieAdapter);





        posterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // CursorAdapter returns a cursor at the correct position for getItem(), or null
                    // if it cannot seek to that position.

                    Cursor cursor =(Cursor) adapterView.getItemAtPosition(position);

                    if (cursor != null) {
                        Bundle bundle= new Bundle();
                        bundle.putString(MovieContract.COL_MOVIE_ID,
                                cursor.getString(cursor.getColumnIndex(MovieContract.COL_MOVIE_ID)));
                        bundle.putString(MovieContract.COL_POSTER_PATH,
                                cursor.getString(cursor.getColumnIndex(MovieContract.COL_POSTER_PATH)));
                        bundle.putString(MovieContract.COL_TITLE,
                                cursor.getString(cursor.getColumnIndex(MovieContract.COL_TITLE)));
                        bundle.putString(MovieContract.COL_RELEASE_DATE,
                                cursor.getString(cursor.getColumnIndex(MovieContract.COL_RELEASE_DATE)));
                        bundle.putString(MovieContract.COL_VOTE_AVERAGE,
                                cursor.getString(cursor.getColumnIndex(MovieContract.COL_VOTE_AVERAGE)));
                        bundle.putString(MovieContract.COL_PLOT,
                                cursor.getString(cursor.getColumnIndex(MovieContract.COL_PLOT)));
                        bundle.putString(MovieContract.COL_BACkDROP_PATH,
                                cursor.getString(cursor.getColumnIndex(MovieContract.COL_BACkDROP_PATH)));

                        ((Callback) getActivity()).onItemSelected(bundle);

                    }
                    mPosition = position;
                }
            });


        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_KEY)) {
            // The GridView probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(MOVIES_KEY);
        }

        return rootView;
    }




    private void updateMovieView(String sortType) {
        MovieSyncAdapter.syncImmediately(getActivity(), sortType);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(MOVIES_KEY, mPosition);
        }

        super.onSaveInstanceState(outState);
    }


    @Override
      public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        CursorLoader loader=null;

        switch (loaderId) {
            case POP_MOVIE_LOADER_ID: {
                loader = new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieEntry._ID,
                                MovieContract.COL_POSTER_PATH, MovieContract.COL_MOVIE_ID,
                                MovieContract.COL_TITLE, MovieContract.COL_RELEASE_DATE,
                                MovieContract.COL_VOTE_AVERAGE, MovieContract.COL_PLOT,
                                MovieContract.COL_BACkDROP_PATH},
                        null,
                        null,
                        MovieContract.COL_POPULARITY + " DESC");
                return loader;
            }
            case HIGH_RAT_LOADER_ID: {
                loader = new CursorLoader(getActivity(), MovieContract.HighRatedMovieEntry.CONTENT_URI,
                        new String[]{MovieContract.HighRatedMovieEntry._ID,
                                MovieContract.COL_POSTER_PATH, MovieContract.COL_MOVIE_ID,
                                MovieContract.COL_TITLE, MovieContract.COL_RELEASE_DATE,
                                MovieContract.COL_VOTE_AVERAGE, MovieContract.COL_PLOT,
                                MovieContract.COL_BACkDROP_PATH},
                        null,
                        null,
                        MovieContract.COL_VOTE_AVERAGE + " DESC");
                return loader;
            }
            case FAVORITES_LOADER_ID: {
                loader = new CursorLoader(getActivity(), MovieContract.FavoriteMovieEntry.CONTENT_URI,
                        new String[]{MovieContract.FavoriteMovieEntry._ID,
                                MovieContract.COL_POSTER_PATH, MovieContract.COL_MOVIE_ID,
                                MovieContract.COL_TITLE, MovieContract.COL_RELEASE_DATE,
                                MovieContract.COL_VOTE_AVERAGE, MovieContract.COL_PLOT,
                                MovieContract.COL_BACkDROP_PATH},
                        null,
                        null,
                        MovieContract.COL_MOVIE_ID + " ASC");
                return loader;
            }
            default:
                return null;
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }


    // Set the cursor in our CursorAdapter once the Cursor is loaded
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if( movieAdapter != null ) {
            switch (loader.getId()) {
                case POP_MOVIE_LOADER_ID: {
                    Log.d(LOG_TAG, "Cursor loaded, " + cursor.getCount() + " rows fetched");
                    movieAdapter.swapCursor(cursor);
                    break;
                }
                case HIGH_RAT_LOADER_ID: {
                    movieAdapter.swapCursor(cursor);
                    break;
                }
                case FAVORITES_LOADER_ID: {
                    movieAdapter.swapCursor(cursor);
                    break;
                }
            }
            if (mPosition != GridView.INVALID_POSITION)
                posterGridView.setSelection(mPosition);
        }
        }
}
