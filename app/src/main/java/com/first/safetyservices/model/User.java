package com.first.safetyservices.model;

import java.util.LinkedList;
import java.util.List;

public class User {
    protected String fullName, email, geographicalArea,uid;
    List<String> myResponses;//responses

    public User(){}
    public User(String fullName,String email, String area,String uid) {
        this.email = email;
        this.fullName = fullName;
        this.geographicalArea = area;
        this.uid = uid;
        myResponses = new LinkedList<String>();

    }

    public List<String> getMyResponses() {
         return myResponses;
    }
    public void newResponse(ServiceResponse response){
        myResponses.add(response.getSummary());
    }
    public void newResponse(String response){
        myResponses.add(response);
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid){
        this.uid=uid;
    }
    public String getEmail() {
        return email;
    }
    public String getFullName() {
        return fullName;
    }
    public String getGeographicalArea() {
        return geographicalArea;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGeographicalArea(String geographicalArea) {
        this.geographicalArea = geographicalArea;
    }


}
