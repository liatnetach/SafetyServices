package com.first.safetyservices.model;

public class ServiceRequest {
    String date;
    String service;
    String uid;
    String clientName;
    String providerName;
    public ServiceRequest(){}
    public ServiceRequest(String uid, String clientName, String service,String date,String providerName)
    {
        this.uid=uid;
        this.clientName=clientName;
        this.service=service;
        this.date=date;
        this.providerName=providerName;
    }
    public void fromStringToRequest(String summery){
        String[] words = summery.split("\\s+"); // splits by whitespace
        int i=-1;
        for (String word : words) {
            if(word.contains("client:")) i=0;
            else if(word.contains("date:")) i=1;
            else if(word.contains("service:")) i=2;
            else if(word.contains("clientUid:")) i=3;
            else if(word.contains("providerName"))i=4;
            else{
                if(i==0){
                    clientName=word;
                    i=-1;
                }
                else if(i==1) {
                    date=word;
                    i=-1;
                }
                else if(i==2){
                    service=word;
                    i=-1;
                }
                else if(i==3){
                    uid=word;
                    i=-1;
                }
                else if(i==4) {
                    providerName=word;
                    i=-1;
                }
            }
        }
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDate() {
        return date;
    }

    public String getClientName() {
        return clientName;
    }

    public String getService() {
        return service;
    }

    public String getUid() {
        return uid;
    }

    public String getSummary() {
        return ("client: "+clientName+" ,date: "+date+" ,service: "+ service+ " ,clientUid: "+uid+" ,providerName: "+ providerName);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client Name: "+getClientName()).append(System.getProperty("line.separator"));
        sb.append("Event Date: "+getDate()).append(System.getProperty("line.separator"));
        sb.append("Service Required: "+ getService());
        return sb.toString();
    }
    public int[] getGenerateDate(){
        int [] datearr=new int[3];
        int i=0;
        String[] words = date.split("/"); // splits by /
        for (String word : words) {
            datearr[i++]=Integer.parseInt(word);
        }
        return datearr;
    }
}
