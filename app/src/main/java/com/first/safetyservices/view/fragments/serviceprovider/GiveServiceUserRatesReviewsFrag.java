package com.first.safetyservices.view.fragments.serviceprovider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.first.safetyservices.R;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiveServiceUserRatesReviewsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiveServiceUserRatesReviewsFrag extends Fragment {
    List<String> recommendationsAndRating;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GiveServiceUserRatesReviewsFrag() {
        // Required empty public constructor
    }
    public GiveServiceUserRatesReviewsFrag(List<String>ratingAndRecommenations) {
        this.recommendationsAndRating=ratingAndRecommenations;
        if(recommendationsAndRating==null||ratingAndRecommenations.get(1).equals("0")){//first rate
            recommendationsAndRating=new LinkedList<String>();
            recommendationsAndRating.add("0");
            recommendationsAndRating.add("0");
            recommendationsAndRating.add("No Reviews yet");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiveServiceUserRatesReviewsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GiveServiceUserRatesReviewsFrag newInstance(String param1, String param2) {
        GiveServiceUserRatesReviewsFrag fragment = new GiveServiceUserRatesReviewsFrag();
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
        View view =inflater.inflate(R.layout.fragment_give_service_user_rates_reviews, container, false);
        TextView text=(TextView)view.findViewById(R.id.retRev);
        RatingBar ratingBar=(RatingBar)view.findViewById(R.id.ratingBar2);
        ratingBar.setRating(Float.parseFloat(recommendationsAndRating.get(0))/Float.parseFloat(recommendationsAndRating.get(1)));
        for(int i=0;i<2;i++)
        {
            recommendationsAndRating.remove(0);
        }
        StringBuilder builder = new StringBuilder();
        for (String recommandation: recommendationsAndRating) {
            if (recommendationsAndRating.size()==1&&recommandation.equals("No Reviews yet"))
                builder.append(recommandation);
            else builder.append("''"+recommandation+"''");
            builder.append("\n\n");
        }
        text.setText(builder.toString());
        return view;
    }
}