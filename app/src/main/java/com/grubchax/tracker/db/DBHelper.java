package com.grubchax.tracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Serg on 3/25/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_tracker";
    private static int version = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL;

        SQL = "create table " + TrackHeader.TABLE + " (" +
                "_id integer primary key, " +
                TrackHeader.TRACK_NAME + " text, " +
                TrackHeader.START_TIME + " integer, " +
                TrackHeader.END_TIME + " integer)";
        db.execSQL(SQL);

        SQL = "create table " + TrackItem.TABLE + " (" +
                "_id integer primary key, " +
                TrackItem.TRACK_ID + " integer, " +
                TrackItem.POINT_TIME + " integer, " +
                TrackItem.LONGITUDE + " integer, " +
                TrackItem.LATITUDE + " integer)";
        db.execSQL(SQL);

        SQL = "create table " + CurrentTrack.TABLE + " (" +
                CurrentTrack.POINT_TIME + " integer, " +
                CurrentTrack.LONGITUDE + " integer, " +
                CurrentTrack.LATITUDE + " integer)";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // to be implemented
    }

    public static class TrackHeader {
        public static String TABLE = "track_header";
        public static String TRACK_NAME = "track_name";
        public static String START_TIME = "start_time";
        public static String END_TIME = "end_time";
    }

    public static class TrackItem {
        public static String TABLE = "track_item";
        public static String TRACK_ID = "track_id";
        public static String POINT_TIME = "point_time";
        public static String LONGITUDE = "longitude";
        public static String LATITUDE = "latitude";
    }

    public static class CurrentTrack {
        public static String TABLE = "current_track";
        public static String POINT_TIME = "point_time";
        public static String LONGITUDE = "longitude";
        public static String LATITUDE = "latitude";
    }
}
