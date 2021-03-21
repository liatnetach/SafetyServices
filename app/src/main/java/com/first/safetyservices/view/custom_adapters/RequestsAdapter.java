package com.first.safetyservices.view.custom_adapters;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.safetyservices.R;
import com.first.safetyservices.model.ServiceRequest;
import com.first.safetyservices.model.ServiceResponse;
import com.first.safetyservices.model.UpdateUser;
import com.google.firebase.auth.FirebaseAuth;

import java.util.GregorianCalendar;
import java.util.List;
//This Class Uses for the presentation of the existing requests that has not answered yet
//Press on Accept/Declined will remove the event from the list
//if the user Approved the request- the calender will open at the relevant Date
public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
    private List<String> requests;
    ViewGroup parent;
    public RequestsAdapter(List<String> requests){
        this.requests=requests;
    }

    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row_requests,parent,false);
        this.parent=parent;
        return new RequestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRequest req=new ServiceRequest();
        req.fromStringToRequest(requests.get(position));
        holder.fullname.setText(req.getClientName());
        holder.service.setText(req.getService());
        holder.date.setText(req.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtclose,requestDtails;
                Button yes,no;
                Dialog myDialog = new Dialog(parent.getContext());
                myDialog.setContentView(R.layout.request_popup);
                requestDtails=(TextView) myDialog.findViewById(R.id.popupTitle);
                requestDtails.setText(req.toString());//present the request summary as defined in the ServiceRequest Class
                txtclose = (TextView) myDialog.findViewById(R.id.txtcloseR);
                txtclose.setText("X");
                yes=(Button)myDialog.findViewById(R.id.yesBtn);
                no=(Button)myDialog.findViewById(R.id.noBtn);
                txtclose.setOnClickListener(new View.OnClickListener() {//Close the popup
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {//Approve the current Request
                    @Override
                    public void onClick(View v) {
                        ServiceResponse response=new ServiceResponse();
                        response.fromStringToResponse(requests.get(position),true);
                        myDialog.dismiss();
                        afterAnswer(position,response);//call the function that update the DATABASE according to the user's response
                        Intent calIntent = new Intent(Intent.ACTION_INSERT);//Open Calender
                        calIntent.setType("vnd.android.cursor.item/event");
                        calIntent.putExtra(CalendarContract.Events.TITLE, req.getService());
                        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "safety services");
                        int[]arr=req.getGenerateDate();
                        GregorianCalendar calDate = new GregorianCalendar(arr[2], (arr[1]-1), arr[0]);
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                calDate.getTimeInMillis());
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                                calDate.getTimeInMillis());
                        parent.getContext().startActivity(calIntent);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {//Declined the current Request
                    @Override
                    public void onClick(View v) {
                        ServiceResponse response=new ServiceResponse();
                        response.fromStringToResponse(requests.get(position),false);
                        myDialog.dismiss();
                        afterAnswer(position,response);//call the function that update the DATABASE according to the user's response
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });
    }

    public void afterAnswer(int position,ServiceResponse answer){//Called when user answer to the Request
        ServiceRequest request=new ServiceRequest();
        request.fromStringToRequest(requests.get(position));
        UpdateUser sendRes=new UpdateUser(request.getUid());
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        UpdateUser updatedList=new UpdateUser(uid);
        answer.setProvideruid(uid);
        requests.remove(position);//Remove the answered request from the unanswered requests list
        sendRes.updateResponse(answer);//update the user that sent the request about the response
        updatedList.updateRequest(requests);//Update the requests List in the Database
        notifyDataSetChanged();//Refresh the view to show the updated List
    }
    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname,service,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname=itemView.findViewById(R.id.clientFullnameCVR);
            service=itemView.findViewById(R.id.serviceCVR);
            date=itemView.findViewById(R.id.dateCVR);

        }
    }

}
