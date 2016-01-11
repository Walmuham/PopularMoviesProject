package app.wafaa.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;


import app.wafaa.popularmovies.Activities.PopularMovies;
import app.wafaa.popularmovies.MoiveUtilities.Constants;
import app.wafaa.popularmovies.MoiveUtilities.Utils;

import app.wafaa.popularmovies.data.MovieContract;


public class MovieAdapter extends CursorAdapter {

    private static final String LOG_TAG =MovieAdapter.class.getSimpleName();



    /**
     * Cache of the children views for a movie poster grid item.
     */

    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.poster_image);
        }
    }

    public  MovieAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        Log.d(LOG_TAG, "MovieAdapter");
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return 0 ;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        Log.d(LOG_TAG, "In new View");


        View view =LayoutInflater.from(context).inflate(R.layout.movie_images, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.prograss_bar);

        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "In bind View");


        int posterIndex = cursor.getColumnIndex(MovieContract.COL_POSTER_PATH);
        String poster = cursor.getString(posterIndex);

        String posterUri= Utils.getPosterURL(poster, Constants.poster_Size_w185);
        Log.i(LOG_TAG, "Image reference extracted: " +  posterUri);

        progressBar.setVisibility(View.VISIBLE);
        ImageLoader imageLoader = PopularMovies.getInstance().getImageLoader();

// If you are using normal ImageView
        imageLoader.get(posterUri, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(LOG_TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    progressBar.setVisibility(View.GONE);
                    // load image into image view
                    viewHolder.imageView.setImageBitmap(response.getBitmap());
                }
            }
        });

    }
}
