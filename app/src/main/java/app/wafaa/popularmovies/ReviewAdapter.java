package app.wafaa.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import app.wafaa.popularmovies.data.MovieContract;


public class ReviewAdapter extends CursorAdapter{

    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView authorView = (TextView) view.findViewById(R.id.review_author);
        TextView contentView = (TextView) view.findViewById(R.id.review_content);


        int authorIndex = cursor.getColumnIndex(MovieContract.COL_R_AUTHOR);
        int contentIndex = cursor.getColumnIndex(MovieContract.COL_R_CONTENT);

        String author = cursor.getString(authorIndex);
        String content = cursor.getString( contentIndex );


        authorView.setText(author);
        contentView.setText(content);
    }
}
