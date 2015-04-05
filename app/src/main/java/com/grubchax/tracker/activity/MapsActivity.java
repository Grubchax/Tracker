package com.grubchax.tracker.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.grubchax.tracker.R;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity {

    public static ArrayList<LatLng> coordinates = new ArrayList<>();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.addPolyline(new PolylineOptions().addAll(coordinates));
                LatLng coordinate = new LatLng(50.454863f, 30.445333f);
                CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                mMap.moveCamera(myLocation);
            }
        });
    }
}

/*        .add(new LatLng(50.454863f, 30.445333f))
        .add(new LatLng(50.454716f, 30.445655f))
        .add(new LatLng(50.454631f, 30.446138f))
        .add(new LatLng(50.454327f, 30.445977f))
        .add(new LatLng(50.453511f, 30.445666f))
        .add(new LatLng(50.452905f, 30.445457f))
        .add(new LatLng(50.451469f, 30.444813f))
        .add(new LatLng(50.450784f, 30.448447f)));*/
