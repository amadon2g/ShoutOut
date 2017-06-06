package com.example.amado.shoutout;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {


    private EditText eTitle, eDesc, eLocation, eSD, eST, eED, eET;
    private View v;
    private Button eBtn;


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
        eLocation = (EditText) v.findViewById(R.id.eventLocation);
        //eSD = (EditText) v.findViewById(R.id.eventTitle);
        //eTitle = (EditText) v.findViewById(R.id.eventTitle);
        //eTitle = (EditText) v.findViewById(R.id.eventTitle);
        //eTitle = (EditText) v.findViewById(R.id.eventTitle);

        eBtn = (Button) v.findViewById(R.id.createEventBtn);
        eBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                EventsMapFragment fragment= new EventsMapFragment();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.mainLayout, fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });
        return v;
    }
}
