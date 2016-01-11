package app.wafaa.popularmovies.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import app.wafaa.popularmovies.MoiveUtilities.Constants;
import app.wafaa.popularmovies.MoiveUtilities.Utils;
import app.wafaa.popularmovies.R;
import app.wafaa.popularmovies.Sync.MovieSyncAdapter;


public class MovieActivity extends AppCompatActivity implements MovieActivityFragment.Callback{


    private final String LOG_TAG = MovieActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {

            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            if (savedInstanceState == null) {
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailsFragment(),
                                MovieDetailsFragment.LOG_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }


        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onItemSelected(Bundle bundle) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putBundle(MovieDetailsFragment.MOVIE_DETAIL, bundle);

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MovieDetailsFragment.LOG_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetails.class);
            intent.putExtra(MovieDetailsFragment.MOVIE_DETAIL, bundle);
            startActivity(intent);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_movie, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
