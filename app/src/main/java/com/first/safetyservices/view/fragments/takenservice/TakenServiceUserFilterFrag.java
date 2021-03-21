package com.first.safetyservices.view.fragments.takenservice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.first.safetyservices.R;
import com.first.safetyservices.view.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TakenServiceUserFilterFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakenServiceUserFilterFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String selectedArea;
    private String selectedService;
    private String mParam1,mParam2;

    public TakenServiceUserFilterFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TakenServiceUserFilterFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static TakenServiceUserFilterFrag newInstance(String param1, String param2) {
        TakenServiceUserFilterFrag fragment = new TakenServiceUserFilterFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_taken_service_user_filter, container, false);

        Spinner area=(Spinner) view.findViewById(R.id.areaSpinner);
        Spinner services=(Spinner) view.findViewById(R.id.serviceSpinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(), R.array.Services,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        services.setAdapter(adapter);
        services.setSelection(0);
        ArrayAdapter<CharSequence> areaadapter= ArrayAdapter.createFromResource(getActivity(), R.array.Area,android.R.layout.simple_spinner_item);
        areaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area.setAdapter(areaadapter);
        area.setSelection(0);
        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                  selectedArea = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedService = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button filter=(Button)view.findViewById(R.id.filteringBtn);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity=(MainActivity) getActivity();
                mainActivity.filter(selectedArea,selectedService);
            }
        });

        return view;
    }
}