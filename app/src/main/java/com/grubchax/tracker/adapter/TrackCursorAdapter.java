package com.grubchax.tracker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.grubchax.tracker.R;
import com.grubchax.tracker.db.DBHelper;

/**
 * Created by Serg on 3/28/2015.
 */
public class TrackCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int idName;
    int idStartTime;
    int idEndTime;
    TextView tvTrackName;
    TextView tvTrackTime;

    public TrackCursorAdapter(Context context, Cursor c) {
        super(context, c);
        init(context);
    }

    public TrackCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        init(context);
    }

    public TrackCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        init(context);
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.activity_track, parent, false);
        tvTrackName = (TextView) view.findViewById(R.id.track_name);
        tvTrackTime = (TextView) view.findViewById(R.id.track_time);
        idName = cursor.getColumnIndex(DBHelper.TrackHeader.TRACK_NAME);
        idStartTime = cursor.getColumnIndex(DBHelper.TrackHeader.START_TIME);
        idEndTime = cursor.getColumnIndex(DBHelper.TrackHeader.END_TIME);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        tvTrackName.setText(cursor.getString(idName));
        tvTrackTime.setText(cursor.getString(idStartTime) + " - " + cursor.getString(idEndTime));
    }
}
