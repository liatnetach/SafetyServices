package com.first.safetyservices.view.fragments.serviceprovider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.first.safetyservices.R;
import com.first.safetyservices.model.UpdateUser;
import com.first.safetyservices.view.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiveServiceUserSummaryFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiveServiceUserSummaryFrag extends Fragment {
    EditText selfSummary;
    String userSummary;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GiveServiceUserSummaryFrag() {
        // Required empty public constructor
    }
    public  GiveServiceUserSummaryFrag(String summary){
        userSummary=summary;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiveServiceUserSummaryFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GiveServiceUserSummaryFrag newInstance(String param1, String param2) {
        GiveServiceUserSummaryFrag fragment = new GiveServiceUserSummaryFrag();
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
        View view= inflater.inflate(R.layout.fragment_give_service_user_summary, container, false);
        TextView done=(TextView)view.findViewById(R.id.successVector);
        done.setVisibility(View.GONE);
        selfSummary=(EditText) view.findViewById(R.id.selfSummary);
        selfSummary.setText(userSummary);
        Button editSummary=(Button)view.findViewById(R.id.editSummaryBtn);
        editSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity=(MainActivity) getActivity();
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                UpdateUser userToUpdate=new UpdateUser(uid);
                userToUpdate.updateProviderSummary(selfSummary.getText().toString());
                done.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}