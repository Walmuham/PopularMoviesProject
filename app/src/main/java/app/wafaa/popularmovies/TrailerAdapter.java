package app.wafaa.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import app.wafaa.popularmovies.data.MovieContract;


public class TrailerAdapter extends CursorAdapter {

    public TrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.trail_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView trailer_textView = (TextView) view.findViewById(R.id.trail_text);

        int trailerNameIndex = cursor.getColumnIndex(MovieContract.COL_T_NAME);

        String trailerName = cursor.getString(trailerNameIndex );
        trailer_textView.setText( trailerName);

    }
}
