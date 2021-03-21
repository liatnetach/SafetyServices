package com.first.safetyservices.view.custom_adapters;

import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.safetyservices.R;
import com.first.safetyservices.model.ServiceResponse;

import java.util.GregorianCalendar;
import java.util.List;
//This Class Uses for the presentation of the service providers users responses about the regular users requests
//both Accepted and Declined requests will show up here
//if the response Approved- the user can tap on it and save it to his calender
public class ResponsesAdapter  extends RecyclerView.Adapter<ResponsesAdapter.ViewHolder> {
    private List<String> responses;
    ViewGroup parent;
    public ResponsesAdapter(List<String> responses){
        this.responses=responses;
    }
    @NonNull
    @Override
    public ResponsesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row_providers_responses,parent,false);
        this.parent=parent;
        return new ResponsesAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ResponsesAdapter.ViewHolder holder, int position) {
        ServiceResponse res=new ServiceResponse();
        res.fromStringToResponse(responses.get(position));
        holder.name.setText(res.getProviderName());
        holder.service.setText(res.getService());
        holder.date.setText(res.getDate());
        String answer;
        if(res.getUserAnswer()) answer="Accepted";
        else answer="Declined";
        holder.answer.setText(answer);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer.equals("Accepted")){//if the request Accepted- the user can add it to his calender
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, res.getService());
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "safety services");
                int[]arr=res.getGenerateDate();
                GregorianCalendar calDate = new GregorianCalendar(arr[2], (arr[1]-1), arr[0]);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());
                parent.getContext().startActivity(calIntent);
                }
            }});
    }

    @Override
    public int getItemCount() {
        return responses.size();    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,service,date,answer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.providerIDCVPR);
            service=itemView.findViewById(R.id.serviceCVPR);
            date=itemView.findViewById(R.id.dateCVPR);
            answer=itemView.findViewById(R.id.answerCVPR);

        }
    }
}
