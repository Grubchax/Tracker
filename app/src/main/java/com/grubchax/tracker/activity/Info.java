package com.grubchax.tracker.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.grubchax.tracker.R;
import com.grubchax.tracker.db.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Info extends ActionBarActivity {

    private long track_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        initControls(intent);

        ((Button) findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSave();
            }
        });
        ((Button) findViewById(R.id.display)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonDisplay();
            }
        });
        ((Button) findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonDelete();
            }
        });
    }

    private void initControls(Intent _intent) {
        EditText editText;
        DateFormat dateFormat;

        if (_intent.hasExtra("activity")) {
            String activity_name = _intent.getStringExtra("activity");
            switch (activity_name) {
                case MainActivity.ACTIVITY_NAME:
                    findViewById(R.id.delete).setEnabled(false);
                    findViewById(R.id.display).setEnabled(false);
                    break;
                case Tracklist.ACTIVITY_NAME:
                    findViewById(R.id.save).setEnabled(false);
            }
        }

        if (_intent.hasExtra("name")) {
            editText = (EditText) findViewById(R.id.name);
            editText.setText(_intent.getStringExtra("name"));
        }

        if (_intent.hasExtra("track_id")) {
            track_id = _intent.getLongExtra("track_id", 0L);
        }

        dateFormat = new SimpleDateFormat();
        if (_intent.hasExtra("starttime")) {
            editText = (EditText) findViewById(R.id.start);
            long time = _intent.getLongExtra("starttime", 0L);
            editText.setText(dateFormat.format(time));
        }
        if (_intent.hasExtra("endtime")) {
            editText = (EditText) findViewById(R.id.end);
            long time = _intent.getLongExtra("endtime", 0L);
            editText.setText(dateFormat.format(time));
        }
    }

    private void onButtonSave() {
        long rowId;
        int idPointTime;
        int idLongitude;
        int idLatitude;

        DBHelper dbHelper = new DBHelper(Info.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Cursor cursor;

        EditText editText;
        editText = (EditText) findViewById(R.id.name);
        cv.put(DBHelper.TrackHeader.TRACK_NAME, editText.getText().toString());
        editText = (EditText) findViewById(R.id.start);
        cv.put(DBHelper.TrackHeader.START_TIME, editText.getText().toString());
        editText = (EditText) findViewById(R.id.end);
        cv.put(DBHelper.TrackHeader.END_TIME, editText.getText().toString());
        rowId = db.insert(DBHelper.TrackHeader.TABLE, null, cv);

        cursor = db.query(DBHelper.CurrentTrack.TABLE, null, null, null, null, null, null);
        idPointTime = cursor.getColumnIndex(DBHelper.CurrentTrack.POINT_TIME);
        idLongitude = cursor.getColumnIndex(DBHelper.CurrentTrack.LONGITUDE);
        idLatitude = cursor.getColumnIndex(DBHelper.CurrentTrack.LATITUDE);

        cv.clear();
        while (cursor.moveToNext()) {
            cv.put(DBHelper.TrackItem.TRACK_ID, rowId);
            cv.put(DBHelper.TrackItem.POINT_TIME, cursor.getLong(idPointTime));
            cv.put(DBHelper.TrackItem.LONGITUDE, cursor.getFloat(idLongitude));
            cv.put(DBHelper.TrackItem.LATITUDE, cursor.getFloat(idLatitude));
            db.insert(DBHelper.TrackItem.TABLE, null, cv);
            cv.clear();
        }
        cursor.close();
        db.close();
        finish();
    }

    private void onButtonDisplay() {
        int idLatitude;
        int idLongitude;

        DBHelper dbHelper = new DBHelper(Info.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        String condition;

        condition = "track_id = " + track_id;
        MapsActivity.coordinates.clear();

        cursor = db.query(DBHelper.TrackItem.TABLE, null, condition, null, null, null, null);
        idLatitude = cursor.getColumnIndex(DBHelper.TrackItem.LATITUDE);
        idLongitude = cursor.getColumnIndex(DBHelper.TrackItem.LONGITUDE);
        while (cursor.moveToNext()) {
            LatLng latLng = new LatLng(cursor.getFloat(idLatitude), cursor.getFloat(idLongitude));
            MapsActivity.coordinates.add(latLng);
        }
        cursor.close();
        db.close();
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    private void onButtonDelete() {
        long rowId;
        String condition;

        DBHelper dbHelper = new DBHelper(Info.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        condition = "track_id = " + track_id;
        db.delete(DBHelper.TrackItem.TABLE, condition, null);

        condition = "_id = " + track_id;
        db.delete(DBHelper.TrackHeader.TABLE, condition, null);

        db.close();
        setResult(5);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
