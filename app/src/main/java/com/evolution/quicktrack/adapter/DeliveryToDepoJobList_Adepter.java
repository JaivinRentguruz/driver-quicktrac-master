package com.evolution.quicktrack.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.evolution.quicktrack.callback.Recycler_AcceptedJobClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.model.JobModel;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 4/3/2018.
 */

public class DeliveryToDepoJobList_Adepter extends RecyclerView.Adapter<DeliveryToDepoJobList_Adepter.ViewHolder> {
    List<JobModel> dataItems;

    Context context;
    String comesFrom;
    Recycler_AcceptedJobClickCallBack callBack;

    public DeliveryToDepoJobList_Adepter(Context context, List<JobModel> myDataset,
                                         Recycler_AcceptedJobClickCallBack callBack,
                                         String comesFrom) {
        dataItems = myDataset;
        this.context = context;
        this.callBack = callBack;
        this.comesFrom = comesFrom;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.deliverytodepo_joblist, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        JobModel dataitem = dataItems.get(position);

        if(QuickTrackAplication.IsNotNull(dataitem))
        {
            if(QuickTrackAplication.IsNotNull(dataitem.getTxtClientName())) {
                holder.txtClientName.setText(dataitem.getTxtClientName());
            }
            else
            {
                holder.txtClientName.setText("--");
            }

          /*  if(QuickTrackAplication.IsNotNull(dataitem.getDeliveryName()))
            {
                if(dataitem.getDeliveryName().equals("Hurricane Delivery"))
                {
                    holder.txt_deliveryType.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.txt_deliveryType.setVisibility(View.INVISIBLE);
                }
            }
            else
            {*/
                holder.txt_deliveryType.setVisibility(View.INVISIBLE);
            //}


            if(QuickTrackAplication.IsNotNull(dataitem.getDiscription())) {
                holder.txtComment.setText(dataitem.getDiscription());
            }
            else
            {
                holder.txtComment.setText("--");
            }

            if(QuickTrackAplication.IsNotNull(dataitem.getId())) {
                holder.txt_jobId.setText("JOB ID : "+dataitem.getId());
            }
            else
            {
                holder.txt_jobId.setText("--");
            }

            if(QuickTrackAplication.IsNotNull(dataitem.getId())) {
                holder.txt_count.setText(""+(position+1));
            }
            else
            {
                holder.txt_count.setText("--");
            }

            if(QuickTrackAplication.IsNotNull(dataitem.getId())) {
                holder.txt_count.setText(""+(position+1));
            }
            else
            {
                holder.txt_count.setText("--");
            }

            if(QuickTrackAplication.IsNotNull(dataitem.isChecked()))
            {
                if(dataitem.isChecked()) {
                    holder.cb_depo.setImageResource(R.drawable.ic_checkcheckbox);
                }
                else {
                    holder.cb_depo.setImageResource(R.drawable.ic_uncheckcheckbox);
                }
            }

            if(QuickTrackAplication.IsNotNull(dataitem.isIspickup())) {

                if(dataitem.isIspickup())
                {
                    if (comesFrom.equals(Constants.Job.PICKED_JOB)){
                        holder.txt_jobtype.setText("Pickup Job");
                        holder.btn_startJob.setText("Start pickup Job");
                        if(QuickTrackAplication.IsNotNull(dataitem.getTxtPickupAddres())) {
                            holder.txtPickupAddres.setText(dataitem.getTxtPickupAddres());
                            holder.txtDeliveryAddres.setText("--");
                            holder.lay_pickupAddres.setVisibility(View.VISIBLE);
                            holder.lay_deliveryAddres.setVisibility(View.GONE);
                        }
                    }else{
                        holder.itemView.setVisibility(View.GONE);
                    }
                }
                else
                {
                    if (comesFrom.equals(Constants.Job.DILEVERED_JOB)){
                        holder.txt_jobtype.setText("Delivery Job");
                        holder.btn_startJob.setText("Start Delivery Job");
                        if(QuickTrackAplication.IsNotNull(dataitem.getTxtDeliveryAddres())) {
                            holder.txtDeliveryAddres.setText(dataitem.getTxtDeliveryAddres());
                            holder.txtPickupAddres.setText("--");
                            holder.lay_pickupAddres.setVisibility(View.GONE);
                            holder.lay_deliveryAddres.setVisibility(View.VISIBLE);
                        }
                    }else{
                        holder.itemView.setVisibility(View.GONE);
                    }
                }
            }

        }



       // holder.txtComment.setText(dataItems.get(position).getDiscription());
       // holder.txt_jobId.setText("JOB ID : "+dataItems.get(position).getId());


       // holder.txt_count.setText(""+(position+1));




        LogCustom.logd("xxxxxx ","dist adapter:"+":"+dataItems.get(position).getDistance());

        if(dataitem.isEnable()){

            holder.txt_note.setVisibility(View.GONE);
            holder.btn_startJob.setBackground(context.getResources().getDrawable(R.drawable.btn_shape_black));

        }else {

            holder.txt_note.setVisibility(View.VISIBLE);
            holder.btn_startJob.setPaintFlags(holder.btn_startJob.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.btn_startJob.setBackground(context.getResources().getDrawable(R.drawable.btn_shape_gray));
        }

     //   holder.btn_startJob.setText("Start job");

        /*if(dataItems.get(position).getStatus().equals(Constants.Accepted)){

        }else{
            holder.btn_startJob.setText("Continue");

        }
*/



        holder.btn_startJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if(dataItems.get(position).isEnable()) {
                    callBack.OnStartJobItemClick(position);
                }*/
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.OnItemClick(position);
            }
        });



    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.txt_clientName)
        TextView txtClientName;
        @BindView(R.id.txt_jobId)
        TextView txt_jobId;

         @BindView(R.id.txt_note)
        TextView txt_note;

        @BindView(R.id.delivery_type)
        TextView txt_deliveryType;


        @BindView(R.id.lay_pickupAddres)
        LinearLayout lay_pickupAddres;

        @BindView(R.id.lay_deliveryAddres)
        LinearLayout lay_deliveryAddres;

        @BindView(R.id.txt_pickupAddres)
        TextView txtPickupAddres;
        @BindView(R.id.txt_deliveryAddres)
        TextView txtDeliveryAddres;
        @BindView(R.id.txt_comment)
        TextView txtComment;

        @BindView(R.id.txt_count)
        TextView txt_count;
        @BindView(R.id.txt_jobtype)
        TextView txt_jobtype;


        @BindView(R.id.btn_startJob)
        Button btn_startJob;

        @BindView(R.id.chkbox_depo)
        ImageView cb_depo;



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