package com.first.safetyservices.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class SearchUser extends Observable {
    private String userT;

    public SearchUser() {
    }

    public void searchProviderUser(String uid) {
        String[] dataArr = new String[3];
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Service Provider Users");
        Query query= ref.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                        GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                        if (user.getValidate()) {
                            userT = "provider";
                        } else {
                            userT = "providerNV";
                        }
                        dataArr[0] = userT;
                        dataArr[1] = uid;
                        dataArr[2]=user.getFullName();
                        setChanged();
                        notifyObservers(dataArr);
                        break;
                    }
                }
                else{
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Regular Users");
                    Query query= ref.orderByChild("uid").equalTo(uid);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                                    User user = productSnapshot.getValue(User.class);
                                    userT = "regular";
                                    dataArr[0] = userT;
                                    dataArr[1] = uid;
                                    dataArr[2]=user.getFullName();
                                    setChanged();
                                    notifyObservers(dataArr);
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void filter(String selectedArea, String selectedService) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Service Provider Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<GiveServiceUser> users = new ArrayList<GiveServiceUser>();
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                    if (user.getValidate()&&user.getGeographicalArea().equals(selectedArea) &&user.getServiceType().contains(selectedService))
                    {
                        users.add(user);
                    }
                }
                setChanged();
                notifyObservers(users);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void getUserRequestList(String uid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Service Provider Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                    if (user.getUid()!=null&&user.getUid().equals(uid))
                    {
                        List<String> myList=user.getRequests();

                        if (myList==null)
                        {
                            myList=new LinkedList<String>();
                        }
                        ResponsesList result=new ResponsesList();
                        result.setType("Request");
                        result.setList(myList);
                        setChanged();
                        notifyObservers(result);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void getUserResponseList(String uid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Service Provider Users");
        Query query= ref.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                        GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                        List<String> myList=user.getMyResponses();
                        if (myList==null)
                        {
                            myList=new LinkedList<String>();
                        }
                        ResponsesList result=new ResponsesList();
                        result.setType("Response");
                        result.setList(myList);
                        setChanged();
                        notifyObservers(result);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void getProvidersResponsesList(String uid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Regular Users");
        Query query= ref.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                        User user = productSnapshot.getValue(User.class);
                        List<String> myList=user.getMyResponses();
                        if (myList==null)
                        {
                            myList=new LinkedList<String>();
                        }
                        ResponsesList result=new ResponsesList();
                        result.setType("ProvidersResponses");
                        result.setList(myList);
                        setChanged();
                        notifyObservers(result);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void finsuserSummary(String uid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Service Provider Users");
        Query query= ref.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                        GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                        Summary summary=new Summary(user.getSelfSummary());
                        setChanged();
                        notifyObservers(summary);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void finsuserRateAndReviews(String uid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Service Provider Users");
        Query query= ref.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                        GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                        Rating ratesAndReviews=new Rating(user.getRecommendationsAndRating());
                        setChanged();
                        notifyObservers(ratesAndReviews);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    //inner clases in order to send the data to the observer
    public class ResponsesList{
        List<String>list;
        String type;
        public ResponsesList(){
            list=new LinkedList<>();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }
    public class Summary{
        String summary;
        public Summary(String summary){this.summary=summary;}
        public String get_summary(){
            return summary;
        }
    }
    public class Rating{
        List<String> ratingAndrecommendations;
        public Rating(List<String>recommendationsAndRecommendations){
            this.ratingAndrecommendations=recommendationsAndRecommendations;
        }

        public List<String> getRatingRecommendations() {
            return ratingAndrecommendations;
        }

    }


}


