package com.example.amado.shoutout;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.amado.shoutout.R.id.eventLocation;
import static com.example.amado.shoutout.R.id.locationSearch;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private View v;
    private final Double RADIUS = 6371.01;
    private final double DISTANCE = 1000;
    private Marker mCurrLocationMarker;


    public EventsMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(EventsMapFragment.this);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(5000);
        //mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {

        //get current user location and move camera to thea location
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        LatLng latLng = new LatLng(lat, lng);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

        EventSearch eventSearch = new EventSearch();
        eventSearch.execute(lat,lng, RADIUS);



    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private class EventSearch extends AsyncTask<Double, Integer, List<Event>> {

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
        }

        @Override
        protected List<Event> doInBackground(Double... params) {
            Log.e("parameters", Double.toString(params[0])+ params[1]);


            Response.Listener<String> stringListener = new Response.Listener<String>() {


                /**
                 * Called when a response is received.
                 *
                 * @param response
                 */
                @Override
                public void onResponse(String response) {
                    Log.e("E", "response" + response);

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jResponse = jsonArray.getJSONObject(i);
                            Boolean success = jResponse.getBoolean("success");

                            if (success) {

                                Double lat = Double.parseDouble(jResponse.getString("latitude"));
                                Double lng = Double.parseDouble(jResponse.getString("longitude"));
                                GeoLocation loc = GeoLocation.fromRadians(lat, lng);
                                lat = loc.getLatitudeInDegrees();
                                lng = loc.getLongitudeInDegrees();

                                LatLng mye = new LatLng(lat, lng);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(mye);
                                markerOptions.title("Current Position");
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                            } else {

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };

            GeoLocation location = GeoLocation.fromDegrees(params[0], params[1]);

            GeoLocation[] boundingCoordinates =
                    location.boundingCoordinates(DISTANCE, RADIUS);
            boolean meridian180WithinDistance =
                    boundingCoordinates[0].getLongitudeInRadians() >
                            boundingCoordinates[1].getLongitudeInRadians();

            String latitude = Double.toString(location.getLatitudeInRadians());
            String longitude = Double.toString(location.getLongitudeInRadians());
            String latMin = Double.toString(boundingCoordinates[0].getLatitudeInRadians());
            String latMax = Double.toString(boundingCoordinates[1].getLatitudeInRadians());
            String lngMin = Double.toString(boundingCoordinates[0].getLongitudeInRadians());
            String lngMax = Double.toString(boundingCoordinates[1].getLongitudeInRadians());
            String r = Double.toString(DISTANCE/RADIUS);

            Log.e ("R value", "it " + r);


            ArrayList<Event> nearByEvents = new ArrayList<Event>();

            EventsMapFragment.EventsHandler loginHandler = new EventsMapFragment.EventsHandler
                    (latitude, longitude, latMin, latMax, lngMin, lngMax, r, stringListener);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(loginHandler);

            Log.e("Response", "JSONArray" + nearByEvents);
            return nearByEvents;
        }
    }



    private class EventsHandler extends StringRequest {

        private static final String REGISTER_REQUEST_URL = "https://amadon2g.000webhostapp.com/Events.php";
        private Map<String, String> params;

        public EventsHandler(String latitude, String longitude, String latMin, String latMax,
                             String lngMin, String lngMax, String r, Response.Listener<String> listener) {
            super(Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("latitude", latitude);
            params.put("longitude", longitude);
            params.put("latMin", latMin);
            params.put("latMax", latMax);
            params.put("lngMin", lngMin);
            params.put("lngMax", lngMax);
            params.put("r", r);
        }
        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }


}
