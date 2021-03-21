package com.first.safetyservices.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class UpdateUser extends Observable {
    private DatabaseReference regularUsersRef,serviceProvidersUsersRef;
    private StorageReference idStorageRef, selfieStorageRef;
    private String uid;
    public UpdateUser(String uid){
        this.uid=uid;
        regularUsersRef=FirebaseDatabase.getInstance().getReference().child("Regular Users");
        serviceProvidersUsersRef=FirebaseDatabase.getInstance().getReference().child("Service Provider Users");
        idStorageRef= FirebaseStorage.getInstance().getReference().child("images/"+uid+"/id.jpg");
        selfieStorageRef= FirebaseStorage.getInstance().getReference().child("images/"+uid+"/selfie.jpg");
    }
    public void updateProviderPhone(String phone){
        serviceProvidersUsersRef.child(uid).child("phone").setValue(phone);
    }
    public void updateProviderSummary(String selfSummary){
        serviceProvidersUsersRef.child(uid).child("selfSummary").setValue(selfSummary);
    }

    public void updateProviderServices(List<String> services){
        serviceProvidersUsersRef.child(uid).child("serviceType").setValue(services);
    }

    public void  updateProviderValidate(boolean isValidate){
        serviceProvidersUsersRef.child(uid).child("validate").setValue(isValidate);
    }
    public void updateRatingArr(float stars, String review)
    {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Service Provider Users");
        Query query= ref.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                        GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                        List<String> myList=user.getRecommendationsAndRating();
                        if(myList==null){//first rate
                            myList=new LinkedList<String>();
                            myList.add("0");
                            myList.add("0");
                            myList.set(0,String.valueOf(stars));
                            myList.set(1,"1");
                        }
                        else{
                            Float rate=Float.parseFloat(myList.get(0));
                            myList.set(0,String.valueOf(rate+stars));
                            Float numOfRates=Float.parseFloat(myList.get(1));
                            numOfRates+=1;
                            myList.set(1,String.valueOf(numOfRates));

                        }
                        myList.add(review);
                        serviceProvidersUsersRef.child(uid).child("recommendationsAndRating").setValue(myList);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void updateIDFile(Uri file)
    {
        idStorageRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();    // Get a URL to the uploaded content
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                });
    }
    public void updateSelfieFile(Bitmap imageBitmap){
        int byteSize=imageBitmap.getRowBytes()*imageBitmap.getHeight();
        ByteBuffer byteBuffer=ByteBuffer.allocate(byteSize);
        imageBitmap.copyPixelsToBuffer(byteBuffer);
        byte[]byteArr=byteBuffer.array();
        ByteArrayInputStream bs=new ByteArrayInputStream(byteArr);
        selfieStorageRef.putStream(bs).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();    // Get a URL to the uploaded content
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                });
    }
    public void updateResponse( ServiceResponse myResponse)
    {
        if(myResponse.getUserAnswer())
        {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Service Provider Users");
            Query query= ref.orderByChild("uid").equalTo(myResponse.getProvideruid());
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
                            myList.add(myResponse.serviceRequestSummary());
                            serviceProvidersUsersRef.child(user.getUid()).child("myResponses").setValue(myList);
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
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Regular Users");
        Query query= ref.orderByChild("uid").equalTo(myResponse.getUid());
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
                        myList.add(myResponse.serviceRequestSummary());
                        regularUsersRef.child(uid).child("myResponses").setValue(myList);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void updateRequest(List<String> requests)
    {
        serviceProvidersUsersRef.child(uid).child("requests").setValue(requests);
    }
    public void addRequest(ServiceRequest request) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Service Provider Users");
        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        GiveServiceUser user = productSnapshot.getValue(GiveServiceUser.class);
                        List<String> myList = user.getRequests();
                        if (myList == null) {
                            myList = new LinkedList<String>();
                        }
                        myList.add(request.getSummary());
                        ref.child(uid).child("requests").setValue(myList);
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
