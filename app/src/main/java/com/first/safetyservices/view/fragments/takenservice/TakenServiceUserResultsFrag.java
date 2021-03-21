package com.first.safetyservices.view.fragments.takenservice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.first.safetyservices.R;
import com.first.safetyservices.model.GiveServiceUser;
import com.first.safetyservices.view.custom_adapters.UsersAdapter;

import java.util.ArrayList;

public class TakenServiceUserResultsFrag extends Fragment {
    private ArrayList<GiveServiceUser> users;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String service,fullname;
    //private static String LOG_TAG = "CardViewActivity";*/

  //
    public TakenServiceUserResultsFrag(){

    }
    public void setUsersList(ArrayList<GiveServiceUser> users){
        this.users=new ArrayList<GiveServiceUser>();
        this.users=users;
    }
    public void updateRequestedService(String service)
    {
     this.service=service;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_taken_service_user_results, container, false);
        //System.out.println(users);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_view) ;      // UsersAdapter adapter = new UsersAdapter(getActivity(), users);;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=new UsersAdapter(users,service,fullname);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void updateFullname(String fullname) {
        this.fullname=fullname;
    }
}