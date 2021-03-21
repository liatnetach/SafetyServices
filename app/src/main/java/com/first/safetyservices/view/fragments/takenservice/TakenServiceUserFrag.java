package com.first.safetyservices.view.fragments.takenservice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.first.safetyservices.R;
import com.first.safetyservices.view.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TakenServiceUserFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TakenServiceUserFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TakenServiceUserFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TakenServiceUserFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static TakenServiceUserFrag newInstance(String param1, String param2) {
        TakenServiceUserFrag fragment = new TakenServiceUserFrag();
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
        View view= inflater.inflate(R.layout.fragment_taken_service_user, container, false);
        Button openResponses=(Button)view.findViewById(R.id.showProvidersResponsesBtn);
        Button openSearch=(Button)view.findViewById(R.id.searchProvidersBtn);
        openResponses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity=(MainActivity) getActivity();
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                mainActivity.showProvidersResponses(uid);
            }
        });
        openSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity=(MainActivity) getActivity();
                mainActivity.openSearchFragment();
            }
        });
        return view;
    }
}