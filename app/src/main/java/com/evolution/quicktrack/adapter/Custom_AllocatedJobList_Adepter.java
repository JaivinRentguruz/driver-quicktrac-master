package com.evolution.quicktrack.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.callback.Recycler_AllocatedJobClickCallBack;
import com.evolution.quicktrack.response.allocatedJobList.JobItem;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 4/3/2018.
 */

public class Custom_AllocatedJobList_Adepter extends RecyclerView.Adapter<Custom_AllocatedJobList_Adepter.ViewHolder> {
    List<JobItem> dataItems;

    Context context;

    Recycler_AllocatedJobClickCallBack callBack;

    public Custom_AllocatedJobList_Adepter(Context context, List<JobItem> myDataset, Recycler_AllocatedJobClickCallBack callBack) {
        dataItems = myDataset;
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.custom_allocated_joblist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        JobItem dataItem = dataItems.get(position);

        if (QuickTrackAplication.IsNotNull(dataItem)) {
            if (QuickTrackAplication.IsNotNull(dataItem.getDeliveryName())) {
                if (dataItem.getDeliveryName().equals("Hurricane Delivery")) {
                    holder.main_layout.setBackgroundColor(Color.parseColor("#FF0000"));
                }
            }
            holder.txtClientName.setText(dataItem.getCustomerName());
            holder.txtDeliveryAddres.setText(dataItem.getDeliveryLocation());
            holder.txtPickupAddres.setText(dataItem.getCollectionLocation());
            holder.txtComment.setText(dataItem.getDescription());
            holder.txt_jobId.setText("JOB ID : " + dataItem.getId());
            holder.txtCollectionCompany.setText(dataItem.getCollection_companyname());
            holder.txtDeliveryCompany.setText(dataItem.getDelivery_companyname());

            /*if (dataItem.getType().equals(Constants.Job.JOB_0)){
                holder.txt_jobId.setText("JOB ID : " + dataItem.getId());
            }else if (dataItem.getType().equals(Constants.Job.JOB_1)){
                holder.txt_jobId.setText("JOB ID : " + dataItem.getJobid());
            }*/

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    callBack.OnAcceptItemClick(position);
                }
            });

            holder.btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.OnDeclineItemClick(position);

                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.txt_clientName)
        TextView txtClientName;
        @BindView(R.id.txt_jobId)
        TextView txt_jobId;

        @BindView(R.id.allocated_mainLayout)
        LinearLayout main_layout;

        @BindView(R.id.txt_pickupAddres)
        TextView txtPickupAddres;
        @BindView(R.id.txt_deliveryAddres)
        TextView txtDeliveryAddres;
        @BindView(R.id.txt_comment)
        TextView txtComment;
        @BindView(R.id.txtCollectionCompany)
        TextView txtCollectionCompany;
        @BindView(R.id.txtDeliveryCompany)
        TextView txtDeliveryCompany;
        @BindView(R.id.btn_decline)
        Button btnDecline;
        @BindView(R.id.btn_accept)
        Button btnAccept;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);


        }
    }

    private void Load_Image(String Url, final ImageView img) {

        Glide.with(context)
                .load(Url)
                .into(img);
    }


    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

}