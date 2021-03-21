package com.first.safetyservices.model;

public class ServiceResponse extends ServiceRequest{
    boolean userAnswer;
    String provideruid;
    public ServiceResponse(){}
    public ServiceResponse(ServiceRequest req,boolean answer) {
        super(req.getUid(), req.getClientName(), req.getService(),req.getDate(), req.getProviderName());
        userAnswer=answer;
    }
    public void fromStringToResponse(String summery,boolean answer){
        fromStringToRequest(summery);//converting the request from string to data
        userAnswer=answer;//updating the answer
    }

    public void fromStringToResponse(String summery) {
        String[] words = summery.split("\\s+"); // splits by whitespace
        int i=-1;
        String answer="";
        for (String word : words) {
            if(word.contains("date:")) i=1;
            else if(word.contains("service:")) i=2;
            else if(word.contains("providerUid:")) i=3;
            else if(word.contains("response:")) i=4;
            else if(word.contains("providerName")) i=5;
            else if(word.contains("client")) i=6;
            else{
               if(i==1){
                   date=word;
                   i=-1;
               }
                else if(i==2){
                    service=word;
                    i=-1;
               }
                else if(i==3) {
                    provideruid=word;
                    i=-1;
               }
                else if(i==4)
                {
                    answer=word;
                    i=-1;
                }
                else if(i==5){
                    providerName=word;
                    i=-1;
               }
               else if(i==6){
                   clientName=word;
                   i=-1;
               }
            }
        }
        if(answer.equals("Accepted")) userAnswer=true;
        else if(answer.equals("Declined"))userAnswer=false;
    }

    public void setProvideruid(String provideruid) {
        this.provideruid = provideruid;
    }

    public String getProvideruid() {
        return provideruid;
    }

    public boolean getUserAnswer() {
        return userAnswer;
    }
    public String serviceRequestSummary()
    {
        String response;
        if(userAnswer) response="Accepted";
        else response="Declined";
        return ("provider response: "+response+" ,date: "+date+" ,service: "+ service+ " ,providerUid: "+provideruid+" ,providerName: "+ providerName+ " ,client: "+ clientName);
    }
}
