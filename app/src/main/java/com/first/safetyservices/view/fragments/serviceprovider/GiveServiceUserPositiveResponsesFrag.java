package com.first.safetyservices.view.fragments.serviceprovider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.first.safetyservices.R;
import com.first.safetyservices.view.custom_adapters.ProviderResponsesAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiveServiceUserPositiveResponsesFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiveServiceUserPositiveResponsesFrag extends Fragment {
    private List<String> responses;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GiveServiceUserPositiveResponsesFrag() {
        // Required empty public constructor
    }
    public void setResponsesList(List<String> responses){
        this.responses=new LinkedList<String>();
        this.responses=responses;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiveServiceUserMsgFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GiveServiceUserPositiveResponsesFrag newInstance(String param1, String param2) {
        GiveServiceUserPositiveResponsesFrag fragment = new GiveServiceUserPositiveResponsesFrag();
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
        View view= inflater.inflate(R.layout.fragment_give_service_user_positive_responses, container, false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_3) ;
        mRecyclerView.setHasFixedSize(true);
       // MainActivity mainActivity=(MainActivity) getActivity();
        //mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=new ProviderResponsesAdapter(responses);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}