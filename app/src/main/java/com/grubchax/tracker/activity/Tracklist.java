package com.grubchax.tracker.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.grubchax.tracker.R;
import com.grubchax.tracker.adapter.TrackCursorAdapter;
import com.grubchax.tracker.db.DBHelper;


public class Tracklist extends ActionBarActivity {

    public static final String ACTIVITY_NAME = "Tracklist";
    ListView listView;
    TrackCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracklist);
        listView = (ListView) findViewById(R.id.tracklist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), Info.class);
                intent.putExtra("activity", ACTIVITY_NAME);
                intent.putExtra("track_id", item.getLong(item.getColumnIndex("_id")));
                intent.putExtra("name", item.getString(item.getColumnIndex(DBHelper.TrackHeader.TRACK_NAME)));
                intent.putExtra("starttime", item.getLong(item.getColumnIndex(DBHelper.TrackHeader.START_TIME)));
                intent.putExtra("endtime", item.getLong(item.getColumnIndex(DBHelper.TrackHeader.END_TIME)));
                startActivityForResult(intent, 6);
            }
        });

        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6 && resultCode == 5) {
            cursorAdapter = new TrackCursorAdapter(this, getCursor());
            listView.setAdapter(cursorAdapter);
        }
    }

    private void initData() {
        if (cursorAdapter == null) {
            cursorAdapter = new TrackCursorAdapter(this, getCursor());
            listView.setAdapter(cursorAdapter);
        } else {
            cursorAdapter.swapCursor(getCursor());
            cursorAdapter.notifyDataSetChanged();
        }
    }

    private Cursor getCursor() {
        DBHelper dbHelper = new DBHelper(Tracklist.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(DBHelper.TrackHeader.TABLE, null, null, null, null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracklist, menu);
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
