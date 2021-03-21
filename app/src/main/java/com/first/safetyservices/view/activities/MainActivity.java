package com.first.safetyservices.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.first.safetyservices.R;
import com.first.safetyservices.model.SearchUser;
import com.first.safetyservices.view.fragments.ChooseUserRegisterFrag;
import com.first.safetyservices.view.fragments.serviceprovider.GiveServiceUserPositiveResponsesFrag;
import com.first.safetyservices.view.fragments.serviceprovider.GiveServiceUserRatesReviewsFrag;
import com.first.safetyservices.view.fragments.serviceprovider.GiveServiceUserRequestsListFrag;
import com.first.safetyservices.view.fragments.serviceprovider.GiveServiceUserFrag;
import com.first.safetyservices.view.fragments.serviceprovider.GiveServiceUserSummaryFrag;
import com.first.safetyservices.view.fragments.HelloFrag;
import com.first.safetyservices.view.fragments.takenservice.TakenServiceUserFilterFrag;
import com.first.safetyservices.view.fragments.takenservice.TakenServiceUserFrag;
import com.first.safetyservices.view.fragments.takenservice.TakenServiceUserProvidersResponsesFrag;
import com.first.safetyservices.view.fragments.takenservice.TakenServiceUserResultsFrag;
import com.first.safetyservices.model.GiveServiceUser;
import com.first.safetyservices.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean regUser;//indicate if the user gives service or not
    private  Intent register,login;
    private List<User> userList=new ArrayList<User>();
    private String userType,service;
    private SearchUser filterUsers;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("loginPrefs",MODE_PRIVATE);
        filterUsers = new SearchUser();
        filterUsers.addObserver(this);
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
         userType=getIntent().getStringExtra("userType");
         if (userType!=null)//check if user logged in before arrived to main activity
         {
             if (userType.equals("regular")){
                 fragmentTransaction.replace(R.id.mainFrag,new TakenServiceUserFrag()).addToBackStack(null).commit();
             }
            else if (userType.equals("provider")){
                 fragmentTransaction.replace(R.id.mainFrag,new GiveServiceUserFrag()).addToBackStack(null).commit();
            }
            else if(userType.equals("providerNV"))//service provider that didnt validate himself yet
            {
                Toast.makeText(MainActivity.this, "You need to validate yourself.",
                        Toast.LENGTH_LONG).show();
                Intent validation=new Intent(this,UserValidation.class);
                validation.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(validation);
            }
         }
        else {
            if(sharedPreferences.getString("userType", null)!=null)//check if it is potential auto-login user
            {
                if(!(sharedPreferences.getString("userType",null).equals("providerNV"))) {
                    Intent autoLogin=new Intent(this,Login.class);
                    autoLogin.putExtra("action","autologin");//
                    startActivity(autoLogin);
                }
            }
            else//there is no user credential saved for auto-login
            {
                if(getIntent().getStringExtra("action")!=null&&getIntent().getStringExtra("action").equals("moveToRegister"))
                {
                    fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainFrag,new ChooseUserRegisterFrag()).addToBackStack(null).commit();
                }
                else fragmentTransaction.add(R.id.mainFrag,new HelloFrag()).addToBackStack(null).commit();
            }

        }
    }
    //Press on the Login button - move to Login Activity
    public void MoveToLoginScreen(View view) {
        login=new Intent(this,Login.class);
        startActivity(login);
    }
    //Press on the Register button - move to ChooseUserRegisterFrag
    public void MoveToRegistrationScreen(View view) {
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrag,new ChooseUserRegisterFrag()).addToBackStack(null).commit();
    }
    //choose registration type- move to registration activity with relevant data
    public void MoveToRegistrationActivity(View view) {
        switch(view.getId()){
        case R.id.registerRegularUser://press on the regular user btn
            regUser=true;
            break;
        case R.id.registerGiveServiceuUser://press on the give service user btn
            regUser=false;
            break;
    }
        register=new Intent(this,Registration.class);
        register.putExtra("is_reg_user",regUser);//true means regular user, false means give service user
        startActivity(register);
    }

    public void filter(String selectedArea, String selectedService) {
        this.service=selectedService;
        filterUsers.filter(selectedArea,selectedService);
    }

    public void logout(View view) {
        Intent loginDel=new Intent(this,Login.class);
        loginDel.putExtra("action","logout");//
        startActivity(loginDel);
    }
    public void getSummary(String uid){
        filterUsers.finsuserSummary(uid);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof SearchUser){
            if(arg instanceof  List)//
            {
                if (userType.equals("regular"))
                {
                    ArrayList<GiveServiceUser> users = (ArrayList<GiveServiceUser>)arg;
                    fragmentTransaction=fragmentManager.beginTransaction();
                    //move the users list to the results fragment in order to display the list to the user
                    TakenServiceUserResultsFrag resultsFrag = new TakenServiceUserResultsFrag();
                    resultsFrag.updateRequestedService(service);
                    resultsFrag.updateFullname(sharedPreferences.getString("clientname",null));
                    resultsFrag.setUsersList(users);
                    fragmentTransaction.replace(R.id.mainFrag, resultsFrag).addToBackStack(null).commit();
                }
            }
            else if(arg instanceof SearchUser.ResponsesList)
            {
                fragmentTransaction=fragmentManager.beginTransaction();
                SearchUser.ResponsesList result=(SearchUser.ResponsesList)arg;
                if(result.getType().equals("Response")){
                    GiveServiceUserPositiveResponsesFrag responsesFrag= new GiveServiceUserPositiveResponsesFrag();
                    responsesFrag.setResponsesList(result.getList());
                    fragmentTransaction.replace(R.id.mainFrag, responsesFrag).addToBackStack(null).commit();
                }
                else if(result.getType().equals("ProvidersResponses"))
                {
                    TakenServiceUserProvidersResponsesFrag providersResponsesFrag= new TakenServiceUserProvidersResponsesFrag();
                    providersResponsesFrag.setResponsesList(result.getList());
                    fragmentTransaction.replace(R.id.mainFrag, providersResponsesFrag).addToBackStack(null).commit();
                }
                else if(result.getType().equals("Request")){
                    GiveServiceUserRequestsListFrag requestsFrag= new GiveServiceUserRequestsListFrag();
                    requestsFrag.setRequestsList(result.getList());
                    fragmentTransaction.replace(R.id.mainFrag, requestsFrag).addToBackStack(null).commit();
                }
            }

            else if(arg instanceof SearchUser.Summary)
            {
                SearchUser.Summary summary=(SearchUser.Summary)arg;
                fragmentTransaction=fragmentManager.beginTransaction();
                String userSum=summary.get_summary();
                GiveServiceUserSummaryFrag frag=new GiveServiceUserSummaryFrag(userSum);
                fragmentTransaction.replace(R.id.mainFrag, frag).addToBackStack(null).commit();

            }
            else if(arg instanceof  SearchUser.Rating)
            {
                SearchUser.Rating rating=(SearchUser.Rating)arg;
                fragmentTransaction=fragmentManager.beginTransaction();
                List<String> recommendationsAndRating=rating.getRatingRecommendations();
                GiveServiceUserRatesReviewsFrag frag=new GiveServiceUserRatesReviewsFrag(recommendationsAndRating);
                fragmentTransaction.replace(R.id.mainFrag, frag).addToBackStack(null).commit();
            }
        }
    }

    public void showRequests(String uid) {
        filterUsers.getUserRequestList(uid);
    }

    public void showResponses(String uid) {
        filterUsers.getUserResponseList(uid);
    }

    public void showProvidersResponses(String uid) {
        filterUsers.getProvidersResponsesList(uid);
    }

    public void openSearchFragment() {
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrag,new TakenServiceUserFilterFrag()).addToBackStack(null).commit();
    }

    public void getRateAndReviews(String uid) {
        filterUsers.finsuserRateAndReviews(uid);
    }
}