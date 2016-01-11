package app.wafaa.popularmovies.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import app.wafaa.popularmovies.R;

public class MovieDetails extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putBundle(MovieDetailsFragment.MOVIE_DETAIL,
                    getIntent().getBundleExtra(MovieDetailsFragment.MOVIE_DETAIL));

            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();


        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

  }

}
