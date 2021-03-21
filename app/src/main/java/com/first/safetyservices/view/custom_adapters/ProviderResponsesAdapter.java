package com.first.safetyservices.view.custom_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.safetyservices.R;
import com.first.safetyservices.model.ServiceResponse;


import java.util.List;
//This Class Uses for the presentation of the service providers users positive responses
//Only Accepted requests will show up here

public class ProviderResponsesAdapter extends RecyclerView.Adapter<ProviderResponsesAdapter.ViewHolder>{
         private List<String> responses;
        ViewGroup parent;
        public ProviderResponsesAdapter(List<String> responses){
            this.responses=responses;
        }

        @NonNull
        @Override
        public ProviderResponsesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row_requests,parent,false);
            this.parent=parent;
            return new ProviderResponsesAdapter.ViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceResponse res=new ServiceResponse();
        res.fromStringToResponse(responses.get(position));
        holder.fullname.setText(res.getClientName());
        holder.service.setText(res.getService());
        holder.date.setText(res.getDate());
    }


        @Override
        public int getItemCount() {
            return responses.size();
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

