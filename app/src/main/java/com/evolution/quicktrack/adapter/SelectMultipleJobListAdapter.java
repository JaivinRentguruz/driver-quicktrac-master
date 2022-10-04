package com.evolution.quicktrack.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJob;

import java.util.ArrayList;

public class SelectMultipleJobListAdapter extends RecyclerView.Adapter<SelectMultipleJobListAdapter.ViewHolder> {


    public ArrayList<AcceptedJob> responseDataTemp;
    private String comesFrom;
    private View.OnClickListener onClickListener;
    private String selectedId;

    public SelectMultipleJobListAdapter(ArrayList<AcceptedJob> responseDataTemp,String comesFrom,View.OnClickListener onClickListener,String selectedId) {
        this.responseDataTemp = responseDataTemp;
        this.comesFrom = comesFrom;
        this.onClickListener = onClickListener;
        this.selectedId = selectedId;
    }

    public void addData(ArrayList<AcceptedJob> responseDataTemp){
       // this.responseDataTemp.addAll(responseDataTemp);
        for(int i=0;i<responseDataTemp.size();i++){
            this.responseDataTemp.add(responseDataTemp.get(i));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_multiple_joblist_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txt_clientName.setText(responseDataTemp.get(i).getCustomerName());
        viewHolder.txt_deliveryAddres.setText(responseDataTemp.get(i).getDeliveryCompanyname());
        viewHolder.txt_pickupAddres.setText(responseDataTemp.get(i).getCollectionLocation());
        if (responseDataTemp.get(i).getDescription() != null)
            viewHolder.txt_note.setText(responseDataTemp.get(i).getDescription());
        viewHolder.txt_jobId.setText(responseDataTemp.get(i).getId());

        viewHolder.txt_count.setText(String.valueOf(i+1));

        if (comesFrom.equals(Constants.Job.PICKED_JOB)){
            viewHolder.txt_jobtype.setText("Pickup Job");
            viewHolder.txt_pickupAddres.setVisibility(View.GONE);
            viewHolder.txt_pickupAddres.setVisibility(View.VISIBLE);
        }else{
            viewHolder.txt_jobtype.setText("Delivery Job");
            viewHolder.txt_pickupAddres.setVisibility(View.GONE);
            viewHolder.txt_pickupAddres.setVisibility(View.VISIBLE);
        }

        if(responseDataTemp.get(i).isChecked()||responseDataTemp.get(i).getId().equalsIgnoreCase(selectedId)) {
            viewHolder.chkbox_depo.setImageResource(R.drawable.ic_checkcheckbox);
            responseDataTemp.get(i).setChecked(true);
        }
        else {
            viewHolder.chkbox_depo.setImageResource(R.drawable.ic_uncheckcheckbox);
            responseDataTemp.get(i).setChecked(false);
        }


        viewHolder.itemView.setTag(String.valueOf(i));
        viewHolder.itemView.setTag(R.id.adjust_height,viewHolder.chkbox_depo);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(v.getTag().toString());
                if(responseDataTemp.get(position).getId().equalsIgnoreCase(selectedId)){
                    return;
                }
                ImageView imageView = (ImageView) v.getTag(R.id.adjust_height);
                if(responseDataTemp.get(position).isChecked()){
                    imageView.setImageResource(R.drawable.ic_uncheckcheckbox);
                    responseDataTemp.get(position).setChecked(false);
                }else {
                    imageView.setImageResource(R.drawable.ic_checkcheckbox);
                    responseDataTemp.get(position).setChecked(true);
                }
                onClickListener.onClick(v);
            }
        });


    }

    @Override
    public int getItemCount() {
        return responseDataTemp.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_count;
        TextView txt_jobtype;
        TextView txt_jobId;
        TextView txt_note;
        ImageView chkbox_depo;
        TextView txt_clientName;
        TextView txt_pickupAddres;
        TextView txt_deliveryAddres;
        TextView txt_comment;
        Button btn_startJob;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_jobtype = itemView.findViewById(R.id.txt_jobtype);
            txt_jobId = itemView.findViewById(R.id.txt_jobId);
            txt_note = itemView.findViewById(R.id.txt_note);
            chkbox_depo = itemView.findViewById(R.id.chkbox_depo);
            txt_clientName = itemView.findViewById(R.id.txt_clientName);
            txt_pickupAddres = itemView.findViewById(R.id.txt_pickupAddres);
            txt_deliveryAddres = itemView.findViewById(R.id.txt_deliveryAddres);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            btn_startJob = itemView.findViewById(R.id.btn_startJob);
        }
    }
}
