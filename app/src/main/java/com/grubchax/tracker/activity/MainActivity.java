package com.grubchax.tracker.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.grubchax.tracker.R;
import com.grubchax.tracker.db.DBHelper;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String ACTIVITY_NAME = "MainActivity";

    public static final long UPDATE_INTERVAL = 10000;
    public static final long FASTEST_UPDATE_INTERVAL = 5000;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    protected LocationRequest mLocationRequest;
    boolean isActive = false;
    long startTime;
    long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonStart = (Button) findViewById(R.id.start);
        final Button buttonSave = (Button) findViewById(R.id.save);
        Button buttonDisplay = (Button) findViewById(R.id.display);
        Button buttonTracklist = (Button) findViewById(R.id.tracklist);

        Spinner spinner = (Spinner) findViewById(R.id.interval);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.intervals,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageView imageView = (ImageView) findViewById(R.id.indicator);
        imageView.setImageResource(R.drawable.light_off);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isActive = !isActive;
                if (isActive) {
                    buttonStart.setText("Stop");
                    buttonStart.setTextColor(getResources().getColor(R.color.red_dark));
                    buttonSave.setEnabled(false);
                    startTime = System.currentTimeMillis();

                    startLocationUpdates();

                    turnOnIndicator();

                    startTrackRecording();
                } else {
                    buttonStart.setText("Start");
                    buttonStart.setTextColor(getResources().getColor(R.color.green_dark));
                    buttonSave.setEnabled(true);
                    endTime = System.currentTimeMillis();

                    stopLocationUpdates();

                    stopTrackRecording();
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Info.class);
                intent.putExtra("activity", ACTIVITY_NAME);
                intent.putExtra("starttime", startTime);
                intent.putExtra("endtime", endTime);
                startActivity(intent);
            }
        });

        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idLongitude;
                int idLatitude;

                DBHelper dbHelper = new DBHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor;

                cursor = db.query(DBHelper.CurrentTrack.TABLE, null, null, null, null, null, null);
                idLongitude = cursor.getColumnIndex(DBHelper.CurrentTrack.LONGITUDE);
                idLatitude = cursor.getColumnIndex(DBHelper.CurrentTrack.LATITUDE);

                MapsActivity.coordinates.clear();
                while (cursor.moveToNext()) {
                    LatLng latLng = new LatLng(cursor.getFloat(idLatitude), cursor.getFloat(idLongitude));
                    MapsActivity.coordinates.add(latLng);
                }
                cursor.close();
                db.close();

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        buttonTracklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Tracklist.class);
                startActivity(intent);
            }
        });

        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startTrackRecording() {

        long rowId;
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete(DBHelper.CurrentTrack.TABLE, null, null);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.445333f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.454863f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.445655f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.454716f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.446138f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.454631f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.445977f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.454327f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.445666f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.453511f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.445457f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.452905f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.444813f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.451469f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        cv.put(DBHelper.CurrentTrack.POINT_TIME, System.currentTimeMillis());
        cv.put(DBHelper.CurrentTrack.LONGITUDE, 30.448447f);
        cv.put(DBHelper.CurrentTrack.LATITUDE, 50.450784f);
        rowId = db.insert(DBHelper.CurrentTrack.TABLE, null, cv);

        db.close();
    }

    private void stopTrackRecording() {

    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void turnOnIndicator() {
        final ImageView imageView = (ImageView) findViewById(R.id.indicator);
        imageView.setImageResource(R.drawable.light_on);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.drawable.light_off);
            }
        }, 200L);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                updateUI();
                updateDB();
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        int a = i;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        int i = connectionResult.getErrorCode();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            updateUI();
            updateDB();
        }
    }

    protected void updateUI() {
        ((TextView) findViewById(R.id.latitude)).setText(String.valueOf(mCurrentLocation.getLatitude()));
        ((TextView) findViewById(R.id.longitude)).setText(String.valueOf(mCurrentLocation.getLongitude()));
    }

    protected void updateDB() {

    }

}
