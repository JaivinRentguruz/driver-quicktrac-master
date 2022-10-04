package com.evolution.quicktrack.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.callback.Recycler_AcceptedJobClickCallBack;
import com.evolution.quicktrack.response.fleetres.FleetResponse;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 4/3/2018.
 */

public class DeliveryToDepoFleetList_Adepter extends RecyclerView.Adapter<DeliveryToDepoFleetList_Adepter.ViewHolder> {
    List<FleetResponse> dataItems;

    Context context;

    Recycler_AcceptedJobClickCallBack callBack;

    public DeliveryToDepoFleetList_Adepter(Context context, List<FleetResponse> myDataset, Recycler_AcceptedJobClickCallBack callBack) {
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
                inflater.inflate(R.layout.deliverytodepo_fleetlist, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        FleetResponse dataitem = dataItems.get(position);

        if(QuickTrackAplication.IsNotNull(dataitem))
        {
            if(QuickTrackAplication.IsNotNull(dataitem.getFleet_name())) {
                holder.txt_fleetName.setText(dataitem.getFleet_name());
            }
            else
            {
                holder.txt_fleetName.setText("--");
            }


            if(QuickTrackAplication.IsNotNull(dataitem.getAddress())) {
                holder.txt_fleetAddres.setText(dataitem.getAddress());
            }
            else
            {
                holder.txt_fleetAddres.setText("--");
            }


            if(QuickTrackAplication.IsNotNull(dataitem.isSelected()))
            {
                if(dataitem.isSelected())
                {
                    holder.rb_depo.setImageResource(R.drawable.ic_checkradiobtn);
                }
                else
                {
                    holder.rb_depo.setImageResource(R.drawable.ic_uncheckradiobtn);
                }

            }



        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.OnItemClick(position);
            }
        });



    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.txt_fleetName)
        TextView txt_fleetName;

        @BindView(R.id.txt_fleetAddres)
        TextView txt_fleetAddres;


        @BindView(R.id.rsdio_depo)
        ImageView rb_depo;



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