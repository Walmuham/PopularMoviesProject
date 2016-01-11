package app.wafaa.popularmovies.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import app.wafaa.popularmovies.MoiveUtilities.Constants;
import app.wafaa.popularmovies.MoiveUtilities.Utils;
import app.wafaa.popularmovies.R;
import app.wafaa.popularmovies.ReviewAdapter;
import app.wafaa.popularmovies.TrailerAdapter;
import app.wafaa.popularmovies.data.MovieContract;

import com.linearlistview.LinearListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();

    static final String MOVIE_DETAIL = "DETAIL";

    Bundle extras;


    ImageLoader imageLoader;

    LinearListView reviewListView;
    LinearListView trailerListView;


    String movie_id;
    String title;
    String released_day;
    String poster_path;
    String backdrop_path;
    String rating;
    String plot;


    boolean isFavorite=false;

    public MovieDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_movie_details, container, false);


        //Bundle extras= getActivity().getIntent().getBundleExtra(MovieDetailsFragment.MOVIE_DETAIL);

        Bundle arguments = getArguments();
        if (arguments != null) {
            extras = arguments.getBundle(MovieDetailsFragment.MOVIE_DETAIL);
        }
        if(extras==null) return null;

        movie_id=extras.getString(MovieContract.COL_MOVIE_ID);
        title=extras.getString(MovieContract.COL_TITLE);
        poster_path=extras.getString(MovieContract.COL_POSTER_PATH);
        backdrop_path=extras.getString(MovieContract.COL_BACkDROP_PATH);
        rating=extras.getString(MovieContract.COL_VOTE_AVERAGE);
        released_day=extras.getString(MovieContract.COL_RELEASE_DATE);
        plot=extras.getString(MovieContract.COL_PLOT);

////////////////////////////////////////////////////////////////////////////////////////
        final ImageView backdrop_image = (ImageView) rootView.findViewById(R.id.movie_backdrop);
        String backdropUri= Utils.getPosterURL(backdrop_path, Constants.poster_Size_w500);
        displayMovieImage(backdropUri,backdrop_image);

        final ImageView poster_image = (ImageView)rootView.findViewById(R.id.movie_poster);
        String posterUri= Utils.getPosterURL(poster_path, Constants.poster_Size_w154);
        displayMovieImage(posterUri, poster_image);
////////////////////////////////////////////////////////////////////////////////////////
        TextView movie_title=(TextView)rootView.findViewById(R.id.movie_title);
        movie_title.setText(title);

        TextView  movie_rating = (TextView)rootView.findViewById(R.id.movie_rating);
        movie_rating.setText(String.valueOf(rating + "/10"));

        TextView movie_releaseDate = (TextView)rootView.findViewById(R.id.movie_released);
        String released=Utils.getDate(released_day);
        movie_releaseDate.setText(("null".equals(released) ? "N/A" : released));

        TextView movie_plot=(TextView)rootView.findViewById(R.id.movie_plot);
        movie_plot.setText(plot);
////////////////////////////////////////////////////////////////////////////////////////
        //fetch trailer and review lists from DB:

        trailerListView = (LinearListView) rootView.findViewById(R.id.trail_list_view);
        reviewListView = (LinearListView) rootView.findViewById(R.id.review_list_view);



        Cursor trailersCursor = getActivity().getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                MovieContract.COL_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie_id)},
                null);

        final TrailerAdapter tAdapter = new TrailerAdapter(getActivity(), trailersCursor, 0);
        trailerListView.setAdapter(tAdapter);
        trailerListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {
                Cursor cursor = tAdapter.getCursor();
                int keyIndex = cursor.getColumnIndex(MovieContract.COL_T_KEY);
                String youtubeKey = cursor.getString(keyIndex);
                Uri trailerUrl = Uri.parse(Constants.YOUTUBE_URL + youtubeKey);
                Intent playIntent = new Intent(Intent.ACTION_VIEW, trailerUrl );
                startActivity(playIntent);

            }
        });

        Cursor reviewCursor = getActivity().getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                MovieContract.COL_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie_id)},
                null);
        final ReviewAdapter rAdapter=new ReviewAdapter(getActivity(),reviewCursor, 0);
        reviewListView.setAdapter(rAdapter);

////////////////////////////////////////////////////////////////////////////////////////////////


        //is favorite button:
        final Button favoriteBtn = (Button) rootView.findViewById(R.id.fbtn);
        if (isFavorite(movie_id)) {
            favoriteBtn.setBackgroundResource(R.drawable.favorite_sel);
        } else {
            favoriteBtn.setBackgroundResource(R.drawable.ic_favorite);
        }
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorite(movie_id)) {
                    favoriteBtn.setBackgroundResource(R.drawable.ic_favorite);
                    deleteFromFavorite();
                    Toast.makeText(getActivity(), title +
                            " has been removed from your favorites", Toast.LENGTH_SHORT).show();

                }else{
                    favoriteBtn.setBackgroundResource(R.drawable.favorite_sel);
                    addToFavorite();
                    Toast.makeText(getActivity(), title + " has been added to your favorites", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return rootView;
    }


    private void displayMovieImage(String imageUri, final ImageView imageView)
    {
        imageLoader= PopularMovies.getInstance().getImageLoader();
        imageLoader.get(imageUri, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into image view
                    imageView.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    private void addToFavorite()
    {
        ContentValues mVal=new ContentValues();
        mVal.put(MovieContract.COL_MOVIE_ID, movie_id);
        mVal.put(MovieContract.COL_TITLE, title);
        mVal.put(MovieContract.COL_RELEASE_DATE,released_day);
        mVal.put(MovieContract.COL_VOTE_AVERAGE,rating);
        mVal.put(MovieContract.COL_POSTER_PATH, poster_path);
        mVal.put(MovieContract.COL_BACkDROP_PATH, backdrop_path);
        mVal.put(MovieContract.COL_PLOT, plot);

        getActivity().getContentResolver().insert(
                MovieContract.FavoriteMovieEntry.CONTENT_URI, mVal);
    }

    private void  deleteFromFavorite() {
        getActivity().getContentResolver().delete(
                MovieContract.FavoriteMovieEntry.CONTENT_URI,
                MovieContract.COL_MOVIE_ID + " = ?",
                new String[]{movie_id}
        );
    }

    public boolean isFavorite(String movie_id) {
        Cursor cursor = getActivity().getContentResolver()
                .query(MovieContract.FavoriteMovieEntry.CONTENT_URI,
                        null,
                        MovieContract.COL_MOVIE_ID+ " = ?",
                        new String[] {movie_id},
                        null);
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
