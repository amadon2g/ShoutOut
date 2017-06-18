package com.example.amado.shoutout;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

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
    int day, month, year, dayFinal, monthFinal, yearFinal;
    private static final int THRESHOLD = 2;
    private ImageView geo_autocomplete_clear;



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
                if(s.length() > 0)
                {
                    geo_autocomplete_clear.setVisibility(View.VISIBLE);
                }
                else
                {
                    geo_autocomplete_clear.setVisibility(View.GONE);
                }
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


    private class CreateEventHandler extends StringRequest {

        private static final String REGISTER_REQUEST_URL = "https://amadon2g.000webhostapp.com/register.php";
        private Map<String, String> params;

        public CreateEventHandler(String name, String email, String location, String gender, String password,
                               Response.Listener<String> listener) {
            super(Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("name", name);
            params.put("email", email);
            params.put("location", location);
            params.put("gender", gender);
            params.put("password", password);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

}
