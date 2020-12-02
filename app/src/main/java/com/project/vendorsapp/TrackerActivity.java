package com.project.vendorsapp;

import android.graphics.Bitmap;

import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.vendorsapp.GoogleClasses.DataParser;
import com.project.vendorsapp.GoogleClasses.FetchURL;
import com.project.vendorsapp.GoogleClasses.TaskLoadedCallback;

import java.util.HashMap;
import java.util.List;



public class TrackerActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private static final String TAG = TrackerActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private GoogleMap mMap;
    private FirebaseAuth mauth;
    String Email,mobileno,id,mlatitude,mlongitude;
    String[] deliveryboyname;
    Marker marker;
    private Polyline currentPolyline;
    LatLng location1,location;
    MarkerOptions place1,place2;
    double lat1,lng1,lat2,lng2;
    TextView txtview_distance,txtview_time,txtview_dboyname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        FirebaseApp.initializeApp(this);
        txtview_distance=findViewById(R.id.txtview_distance);
        txtview_time=findViewById(R.id.txtview_time);
        txtview_dboyname=findViewById(R.id.txtview_dboyname);
        deliveryboyname=getIntent().getStringExtra("dboyname").split(",");
        Email=getIntent().getStringExtra("email");
        mobileno=getIntent().getStringExtra("mobileno");
       id=getIntent().getStringExtra("firebaseid");
       txtview_dboyname.setText(deliveryboyname[0].replace("Assigned to",""));
       mlatitude=getIntent().getStringExtra("latitude");
       mlongitude=getIntent().getStringExtra("longitude");
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(TrackerActivity.this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
      //  mMap.setMaxZoomPreference(15);
      //  mMap.setMinZoomPreference(12);
        lat1 = Double.parseDouble(mlatitude);
        lng1 = Double.parseDouble(mlongitude);
        location1 = new LatLng(lat1, lng1);
        place1 = new MarkerOptions().position(location1);
       mMap.addMarker(new MarkerOptions().title("Delivery Address").position(location1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        loginToFirebase();
    }

    private void loginToFirebase() {
        //  String email = "rouf.shaikbasha@gmail.com";
        final String email = Email;
        String password = mobileno;

        //  subscribeToUpdates();
        // Authenticate with Firebase and subscribe to updates
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    subscribeToUpdates();
                    Log.d(TAG, "firebase auth success");
                } else {
                    Log.d(TAG, "firebase auth failed");
                }
            }
        });
    }


    private void subscribeToUpdates() {


        // Functionality coming next step

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userid=user.getUid();

        DatabaseReference ref = null;
        ref = FirebaseDatabase.getInstance().getReference("locations");  //(getString(R.string.firebase_path));







        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {


                setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // Functionality coming next step
        // boundaries required to show them all on the map at once

        String key = dataSnapshot.getKey();
        if (key.equalsIgnoreCase(id)) {
            HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
             lat2 = Double.parseDouble(value.get("latitude").toString());
             lng2 = Double.parseDouble(value.get("longitude").toString());
            location = new LatLng(lat2, lng2);
            place2 = new MarkerOptions().position(location);
            draw_Route();
            if (!mMarkers.containsKey(key)) {
             mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.box))));

            } else {
                mMarkers.get(key).setPosition(location);
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers.values()) {
                builder.include(marker.getPosition());
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
                   15));







          //  mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));

           // createDashedLine(mMap,location,location1,R.color.black);
          //  Draw_Polylines();

        //  marker.getPosition();

          //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
         //   animateMarker(mMap,mMarkers, location, false);

        }
     /*   mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final LatLng startPosition = marker.getPosition();
                final LatLng finalPosition = new LatLng(12.7801569, 77.4148528);
                final Handler handler = new Handler();
                final long start = SystemClock.uptimeMillis();
                final Interpolator interpolator = new AccelerateDecelerateInterpolator();
                final float durationInMs = 3000;
                final boolean hideMarker = false;
                bearingBetweenLocations(SomePos,finalPosition);

                handler.post(new Runnable() {
                    long elapsed;
                    float t;
                    float v;

                    @Override
                    public void run() {
                        // Calculate progress using interpolator
                        elapsed = SystemClock.uptimeMillis() - start;
                        t = elapsed / durationInMs;

                        LatLng currentPosition = new LatLng(
                                startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                                startPosition.longitude * (1 - t) + finalPosition.longitude * t);

                        marker.setPosition(currentPosition);

                        // Repeat till progress is complete.
                        if (t < 1) {
                            // Post again 16ms later.
                            handler.postDelayed(this, 16);
                        } else {
                            if (hideMarker) {
                                marker.setVisible(false);
                            } else {
                                marker.setVisible(true);
                            }
                        }
                    }
                });
                return true;
            }
        });*/
    }

    public static void createDashedLine(GoogleMap map, LatLng latLngOrig, LatLng latLngDest, int color){
        double difLat = latLngDest.latitude - latLngOrig.latitude;
        double difLng = latLngDest.longitude - latLngOrig.longitude;

        double zoom = map.getCameraPosition().zoom;

        double divLat = difLat / (zoom * 2);
        double divLng = difLng / (zoom * 2);

        LatLng tmpLatOri = latLngOrig;

        for(int i = 0; i < (zoom * 2); i++){
            LatLng loopLatLng = tmpLatOri;

            if(i > 0){
                loopLatLng = new LatLng(tmpLatOri.latitude + (divLat * 0.25f), tmpLatOri.longitude + (divLng * 0.25f));
            }

            Polyline polyline = map.addPolyline(new PolylineOptions()
                    .add(loopLatLng)
                    .add(new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng))
                    .color(color)
                    .width(5f));

            tmpLatOri = new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng);
        }
    }

    public void Draw_Polylines()
    {
        	mMap.addPolyline((new PolylineOptions())
            .add(location,location1).width(5).color(Color.BLUE)
            .geodesic(true));
        // move camera to zoom on map
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
                12));
    }

    public void draw_Route()
    {
       // String url=getUrl(place1.getPosition(),place2.getPosition(),"driving");
        //new FetchURL(TrackerActivity.this).execute(url,"driving");
        new FetchURL(TrackerActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        GetTime();


    }

    public  void GetTime()
    {
        String distance= DataParser.Distance;
        String time=DataParser.Duration;
        txtview_distance.setText(distance);
        txtview_time.setText(time);
    }

}
