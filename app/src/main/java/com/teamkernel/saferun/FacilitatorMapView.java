package com.teamkernel.saferun;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FacilitatorMapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Chronometer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitator_map_view);

        timer = (Chronometer) findViewById(R.id.chronometer1);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.setFormat("Elapsed Time: %s");
        timer.start();

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void endRunConfirmation(View view){


            long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();
            Intent intent = new Intent(this, FacilitatorEndRunConfirmation.class);
            Bundle b = new Bundle();
            b.putLong("elapsed_time", elapsedMillis);
            intent.putExtras(b);
            startActivity(intent);
    }

    public void restartTime(View view){
        timer.setBase(SystemClock.elapsedRealtime());
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("ss", "onKeyDown Called");

            Intent intent = new Intent(this, FacilitatorExitRunConfirmation.class);
            startActivity(intent);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(40.809527, -73.960269);
        mMap.addMarker(new MarkerOptions().position(sydney).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
    }
}
