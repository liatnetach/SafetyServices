package com.first.safetyservices.model;

import java.util.LinkedList;
import java.util.List;

public class GiveServiceUser extends User{
    private List<String> serviceType;
    private boolean isValidate;
    private String phone,selfSummary;
    private List<String> requests;
    private List<String> recommendationsAndRating;// Rating and Reviews
    public GiveServiceUser(String fullName,  String email, String area, String uid) {
        super(fullName, email, area,uid);
        requests = new LinkedList<String>();
        recommendationsAndRating=new LinkedList<String>();
        recommendationsAndRating.add(0,"0");
        recommendationsAndRating.add(1,"0");
    }
    public GiveServiceUser(){}
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setValidate(boolean isValidate) {
        this.isValidate = isValidate;
    }
    public boolean getValidate(){
        return isValidate;
    }

    public List<String> getServiceType() {
        return serviceType;
    }

    public List<String> getRequests() {
       return requests;
   }
    public void sendRequest(ServiceRequest request){
        requests.add(request.getSummary());
    }

    public String getSelfSummary() {
        return selfSummary;
    }
    public void setSelfSummary(String selfSummary)
    {
        this.selfSummary=selfSummary;
    }

    public void setServiceType(List<String> serviceType) {
        this.serviceType = serviceType;
    }

    public float calculateCurrentRating(){
        Float result=(Float.parseFloat(recommendationsAndRating.get(0)))/(Float.parseFloat(recommendationsAndRating.get(1)));
        return result;
    }

    public void setRecommendationsAndRating(List<String>recommendations){
        this.recommendationsAndRating=recommendations;}
    public List<String> getRecommendationsAndRating(){
        return recommendationsAndRating;}

    @Override
    public String toString() {
        return ("User Full Name:"+this.getFullName()+
                " User Email: "+ this.getEmail() +
                " User phone: "+ this.getPhone() +
                " User services : " + this.getServiceType());
    }
}
