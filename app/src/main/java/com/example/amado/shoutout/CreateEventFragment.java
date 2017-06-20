package com.example.amado.shoutout;


import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private EditText eTitle, eDesc, eSD, eST, eED, eET;
    private DelayAutoCompleteTextView eLocation;
    private View v;
    private Button eBtn, cBtn;
    private static final int THRESHOLD = 2;
    double lat;
    double lng;


    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_create_event, container, false);


        eTitle = (EditText) v.findViewById(R.id.eventTitle);
        eDesc = (EditText) v.findViewById(R.id.eventDescription);
        eLocation = (DelayAutoCompleteTextView) v.findViewById(R.id.eventLocation);
        eLocation.setThreshold(THRESHOLD);
        eLocation.setAdapter(new GeoAutoCompleteAdapter(getActivity()));


        eLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
                lat = result.getLat();
                lng = result.getLng();
                eLocation.setText(result.getAddress());
            }
        });


        eLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        eSD = (EditText) v.findViewById(R.id.startDate);
        eSD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new FromDatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                    return true;
                }
                return false;
            }
        });


        eST = (EditText) v.findViewById(R.id.startTime);
        eST.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new FromTimePickerFragment();
                    newFragment.show(getFragmentManager(), "timePicker");
                    return true;
                }
                return false;
            }
        });


        eED = (EditText) v.findViewById(R.id.endDate);
        eED.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new ToDatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                    return true;
                }
                return false;
            }
        });


        eET = (EditText) v.findViewById(R.id.endTime);
        eET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    DialogFragment newFragment = new ToTimePickerFragment();
                    newFragment.show(getFragmentManager(), "timePicker");
                    return true;
                }
                return false;
            }
        });


        eBtn = (Button) v.findViewById(R.id.createEventBtn);
        eBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                final String title = eTitle.getText().toString();
                final String address = eLocation.getText().toString();
                GeoLocation location = GeoLocation.fromDegrees(lat, lng);
                String latitude = Double.toString(location.getLatitudeInRadians());
                String longitude = Double.toString(location.getLongitudeInRadians());
                Log.e("LatAndLng" +"two ", latitude);
                final String desc = eDesc.getText().toString();
                final String sDate = eSD.getText().toString();
                final String sTime = eST.getText().toString();
                final String eDate = eSD.getText().toString();
                final String eTime = eST.getText().toString();


                Response.Listener<String> stringListener = new Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        Log.e("e", "RESPONSE" + response);
                        try {
                            JSONObject jResponse = new JSONObject(response);
                            Boolean success = jResponse.getBoolean("success");

                            if(success) {
                                EventsMapFragment fragment= new EventsMapFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                // Replace whatever is in the fragment_container view with this fragment,
                                // and add the transaction to the back stack so the user can navigate back
                                transaction.replace(R.id.mainLayout, fragment);
                                transaction.addToBackStack(null);

                                // Commit the transaction
                                transaction.commit();

                                Toast.makeText(getContext(), "Event created", Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG ).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                CreateEventFragment.CreateEventHandler createEventHandler = new CreateEventFragment
                        .CreateEventHandler(title, latitude, longitude, address, desc, sDate, sTime, eDate, eTime, stringListener);
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(createEventHandler);

            }
        });



        return v;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    public boolean validate() {
//        boolean valid = true;
//
//        final String title = eTitle.getText().toString();
//        final String address = eLocation.getText().toString();
//        GeoLocation location = GeoLocation.fromDegrees(lat, lng);
//        String latitude = Double.toString(location.getLatitudeInRadians());
//        String longitude = Double.toString(location.getLongitudeInRadians());
//        final String desc = eDesc.getText().toString();
//        final String sDate = eSD.getText().toString();
//        final String sTime = eST.getText().toString();
//        final String eDate = eSD.getText().toString();
//        final String eTime = eST.getText().toString();
//
//        if (title.isEmpty())) {
//            eTitle.setError("Enter event title");
//            valid = false;
//        } else {
//            eTitle.setError(null);
//        }
//
//        if (address.isEmpty()) {
//            eLocation.setError("Enter an Address");
//            valid = false;
//        } else if {
//
//        }
//        else {
//            eLocation.setError(null);
//        }
//
//        return valid;
//    }


    private class CreateEventHandler extends StringRequest {

        private static final String REGISTER_REQUEST_URL = "https://amadon2g.000webhostapp.com/createEvent.php";
        private Map<String, String> params;

        public CreateEventHandler(String title, String latitude, String longitude, String address, String description, String fromDate,
                                  String fromTime, String toDate, String toTime, Response.Listener<String> listener) {
            super(Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("title", title);
            params.put("latitude", latitude);
            params.put("longitude", longitude);
            params.put("address", address);
            params.put("description", description);
            params.put("fromDate", fromDate);
            params.put("fromTime", fromTime);
            params.put("toDate", toDate);
            params.put("toTime", toTime);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

}
