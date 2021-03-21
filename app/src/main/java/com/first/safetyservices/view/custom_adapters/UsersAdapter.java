package com.first.safetyservices.view.custom_adapters;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.safetyservices.R;
import com.first.safetyservices.model.GiveServiceUser;
import com.first.safetyservices.model.ServiceRequest;
import com.first.safetyservices.model.UpdateUser;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
//This Class Uses for the presentation of the filtered service providers users
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private ArrayList<GiveServiceUser> users;
    private UpdateUser updateUser;
    private String service,fullname;
    ViewGroup parent;
    public UsersAdapter(ArrayList<GiveServiceUser> users,String requestedService,String fullname)
    {
        this.fullname=fullname;
        this.service=requestedService;
        this.users=users;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row,parent,false);
        this.parent=parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        holder.fullname.setText(users.get(position).getFullName());
        if(users.get(position).getSelfSummary()!=null)
            holder.summary.setText("''"+users.get(position).getSelfSummary()+"''");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView txtclose,username, services;
            Button dial,sendRequest, rate,showRatingReviews;
            Dialog myDialog = new Dialog(parent.getContext());
            myDialog.setContentView(R.layout.given_service_user_popup_results);
            username=(TextView) myDialog.findViewById(R.id.userNameTxt);
            username.setText(users.get(position).getFullName());
            showRatingReviews=(Button)myDialog.findViewById(R.id.showUserRatingsBtn);
            if(users.get(position).getRecommendationsAndRating()==null||users.get(position).getRecommendationsAndRating().get(1).equals("0")) {//When user have no rating yet we set this button to unable
                showRatingReviews.setEnabled(false);
                showRatingReviews.setText("There are no reviews and ratings yet");
            }
            txtclose = (TextView) myDialog.findViewById(R.id.txtclosing);
            txtclose.setText("X");
            dial = (Button) myDialog.findViewById(R.id.dialBtn);
            sendRequest=(Button) myDialog.findViewById(R.id.sendRequestBtn);
            rate=(Button) myDialog.findViewById(R.id.rateUserBtn);
            rate.setOnClickListener(new View.OnClickListener() {//When the user press on Rate button we pop new pop-up
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                    TextView txtclose,errorMsg;
                    Button submit;
                    RatingBar ratingBar;
                    Dialog dialog = new Dialog(parent.getContext());
                    dialog.setContentView(R.layout.rating_popup);
                    txtclose=(TextView) dialog.findViewById(R.id.txtclosing);;
                    txtclose.setText("X");
                    ratingBar= (RatingBar) dialog.findViewById(R.id.ratingBar);
                    ratingBar.setNumStars(5); // set total number of stars
                    errorMsg=(TextView)dialog.findViewById(R.id.errorMsg);
                    errorMsg.setText("");
                    EditText review;
                    review=(EditText)dialog.findViewById(R.id.popupEditTxt);
                    submit=(Button)dialog.findViewById(R.id.sendBtn);
                    txtclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    submit.setOnClickListener(new View.OnClickListener() {//submit review- in order to do so- the user MUST enter both stars rating and text review
                        @Override
                        public void onClick(View v) {
                            if(review.getText().toString().isEmpty())
                                errorMsg.setText("You need to add review about your experience");
                            else if (ratingBar.getRating()==0)
                                errorMsg.setText("You need to choose at least 1 star");
                            else
                            {
                                updateUser=new UpdateUser(users.get(position).getUid());
                                updateUser.updateRatingArr(ratingBar.getRating(),review.getText().toString());
                                //update user rating array and add the review to the recommendations list
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });
            showRatingReviews.setOnClickListener(new View.OnClickListener() {//show the current rating and review thr user have
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                    TextView txtclose,ratingReviews;
                    RatingBar ratingBar;
                    Dialog dialog2 = new Dialog(parent.getContext());
                    dialog2.setContentView(R.layout.users_rating_reviews_popup);
                    txtclose=(TextView) dialog2.findViewById(R.id.txtclosing);;
                    txtclose.setText("X");
                    ratingReviews=(TextView)dialog2.findViewById(R.id.retRev);
                    ratingBar= (RatingBar) dialog2.findViewById(R.id.ratingBar2);
                    ratingBar.setNumStars(5); // set total number of stars
                    ratingBar.setRating(Float.parseFloat(users.get(position).getRecommendationsAndRating().get(0))/Float.parseFloat(users.get(position).getRecommendationsAndRating().get(1)));
                    StringBuilder builder = new StringBuilder();
                    List<String> recommendations=users.get(position).getRecommendationsAndRating();
                    for(int i=0;i<2;i++)
                    {
                        recommendations.remove(0);
                    }
                    for (String recommandation: recommendations) {
                        builder.append("''"+recommandation+"''");
                        builder.append("\n\n");
                    }
                    ratingReviews.setText(builder.toString());
                    txtclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog2.show();
                }
            });
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            dial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri usernumber = Uri.parse("tel:" + users.get(position).getPhone());
                    Intent dialNum = new Intent(Intent.ACTION_DIAL, usernumber);
                    try
                    {
                        parent.getContext().startActivity(dialNum);
                    }
                    catch (SecurityException s)
                    {
                        System.out.println("Error Occoured");
                    }
                }
            });
            sendRequest.setOnClickListener(new View.OnClickListener() {//Send request to the selected service provider user
                @Override
                public void onClick(View v) {
                    DatePicker picker;
                    picker=(DatePicker)myDialog.findViewById(R.id.datePicker);
                    String date=picker.getDayOfMonth()+"/"+(picker.getMonth()+1)+"/"+picker.getYear();
                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    ServiceRequest request=new ServiceRequest(uid, fullname,service,date,users.get(position).getFullName());//DATE from picker, SERVICE from the search, NAME from the users list
                    updateUser=new UpdateUser(users.get(position).getUid());
                    updateUser.addRequest(request);
                    myDialog.dismiss();
                }

            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
    });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname,summary;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname=itemView.findViewById(R.id.fullname);
            summary=itemView.findViewById(R.id.summary);
        }
    }
}