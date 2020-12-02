package com.project.vendorsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class GetLocationActivity extends FragmentActivity implements OnMapReadyCallback {


    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    private CardView currentlocation_lyout;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private EditText locationSearch;
    double latitude, longitude;
    List<Address> addresses;
    String addressText = "";
    Location currentLocation;
    private LinearLayout serch_lyout;
    private LatLng currentlatlong;
    MarkerOptions markerOptions;
    TextView tview_final_address, pinonmap_txt, address_text;
    String addressStr = "", enteredlocation = "";
    Bundle bundle1;
    Marker locationMarker;
    Typeface fontTypeA;
    ArrayList<Register> registerlist;
    ImageView btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location2);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btn_next = findViewById(R.id.btn_next);
        serch_lyout = findViewById(R.id.serch_lyout);
        currentlocation_lyout = findViewById(R.id.currentlocation_lyout);
        address_text = findViewById(R.id.address_text);
        locationSearch = (EditText) findViewById(R.id.editText);
        serch_lyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredlocation = locationSearch.getText().toString();
                if (!enteredlocation.isEmpty()) {
                    searchLocation(locationSearch.getText().toString());
                } else {
                    Toast.makeText(GetLocationActivity.this, "Please enter your Location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    enteredlocation = locationSearch.getText().toString();
                    if (!enteredlocation.isEmpty()) {
                        searchLocation(locationSearch.getText().toString());
                    } else {
                        Toast.makeText(GetLocationActivity.this, "Please enter your Location", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
        registerlist = getIntent().getParcelableArrayListExtra("list");
        getLastLocation();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        bundle1 = new Bundle();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude != 0.0 && longitude != 0.0) {
                    Intent intent = new Intent(GetLocationActivity.this, Register2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("latitude", String.valueOf(latitude));
                    intent.putParcelableArrayListExtra("list", registerlist);
                    intent.putExtra("longitude", String.valueOf(longitude));
                    startActivity(intent);
                } else {
                    Toast.makeText(GetLocationActivity.this, "Select store location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateLocationUI(Location location) {
        currentLocation = location;
        mapFragment.getMapAsync(this);
        onMapReady(mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.clear();
            currentlatlong = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            searchLocation("");
            markerOptions = new MarkerOptions();
            markerOptions.position(currentlatlong);
            markerOptions.title("Shop"/*addressText*/);
            locationMarker = mMap.addMarker(markerOptions);
            locationMarker.showInfoWindow();
            locationMarker.setDraggable(true);
            mMap.setMinZoomPreference(15f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlatlong));
            mMap.setMaxZoomPreference(50f);
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker arg0) {
                    // TODO Auto-generated method stub
                    currentlatlong = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                }

                @SuppressWarnings("unchecked")
                @Override
                public void onMarkerDragEnd(Marker arg0) {
                    // TODO Auto-generated method stub
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    currentlatlong = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                    locationMarker = mMap.addMarker(new MarkerOptions().position(currentlatlong).title(addressText).draggable(true));
                    currentlatlong = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
//                    GetUserAddress(arg0.getPosition().latitude, arg0.getPosition().longitude);
                    bundle1.putString("FinalAddress", addressStr);
                }

                @Override
                public void onMarkerDrag(Marker arg0) {
                    // TODO Auto-generated method stub
                    Log.i("System out", "onMarkerDrag...");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void searchLocation(String locationSearch) {
        List<Address> addressList = new ArrayList<>();
        if (locationSearch != null && !locationSearch.isEmpty()) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(locationSearch, 1);
                if (addressList.size() != 0) {
                    String Location = convertlocation(addressList.get(0));
                    address_text.setText(Location);
                    LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    mMap.addMarker(new MarkerOptions().position(latLng).title(locationSearch)).setDraggable(true);
//                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    currentlocation_lyout.setVisibility(View.VISIBLE);
                } else {
                    currentlocation_lyout.setVisibility(View.GONE);
                    Toast.makeText(GetLocationActivity.this, "No places found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocation(latitude, longitude, 1);
                if (addressList.size() != 0) {
                    String Location = convertlocation(addressList.get(0));
                    address_text.setText(Location);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(locationSearch)).setDraggable(true);
//                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    currentlocation_lyout.setVisibility(View.VISIBLE);
                } else {
                    currentlocation_lyout.setVisibility(View.GONE);
                    Toast.makeText(GetLocationActivity.this, "No places found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private String convertlocation(Address address) {
        String Usrlocation = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                if (!address.getThoroughfare().equalsIgnoreCase(null)) {
                    stringBuilder.append(address.getThoroughfare());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (!address.getSubThoroughfare().equalsIgnoreCase(null)) {
                    stringBuilder.append(address.getSubThoroughfare() + ",");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                if (!address.getSubLocality().equalsIgnoreCase(null)) {
                    stringBuilder.append(address.getSubLocality() + ",");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                if (!address.getAdminArea().equalsIgnoreCase(null)) {
                    stringBuilder.append(address.getAdminArea() + ",");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                if (!address.getSubAdminArea().equalsIgnoreCase(null)) {
                    stringBuilder.append(address.getSubAdminArea() + ",");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                if (!address.getLocality().equalsIgnoreCase(null)) {
                    stringBuilder.append(address.getLocality() + ",");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (!address.getCountryName().equalsIgnoreCase(null)) {
                    stringBuilder.append(address.getCountryName() + ",");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stringBuilder.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Usrlocation;
        }
    }


//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
//    }


    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    currentLocation = location;
                                    updateLocationUI(location);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            currentLocation = mLastLocation;
            updateLocationUI(currentLocation);
        }
    };

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }


}
