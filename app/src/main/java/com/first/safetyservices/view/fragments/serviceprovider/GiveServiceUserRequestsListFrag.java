package com.first.safetyservices.view.fragments.serviceprovider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.first.safetyservices.R;
import com.first.safetyservices.view.custom_adapters.RequestsAdapter;

import java.util.LinkedList;
import java.util.List;


public class GiveServiceUserRequestsListFrag extends Fragment {
    //private List<ServiceRequest> requests;
    private List<String> requests;

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

    public GiveServiceUserRequestsListFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiveServiceUSerDefFrag.
     */
    // TODO: Rename and change types and number of parameters
   /* public static GiveServiceUserDefFrag newInstance(String param1, String param2) {
        GiveServiceUserDefFrag fragment = new GiveServiceUserDefFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
  /*  public void setRequestsList(List<ServiceRequest> requests){
        this.requests=new LinkedList<ServiceRequest>();
        this.requests=requests;
    }*/
    public void setRequestsList(List<String> requests){
        this.requests=new LinkedList<String>();
        this.requests=requests;
    }
   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_give_service_user_requests_list, container, false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_2) ;      // UsersAdapter adapter = new UsersAdapter(getActivity(), users);;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=new RequestsAdapter(requests);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}