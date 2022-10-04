package com.evolution.quicktrack.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.R;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.response.vehicle.VehicleDataItem;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 4/3/2018.
 */

public class Custom_TrackList_Adepter extends RecyclerView.Adapter<Custom_TrackList_Adepter.ViewHolder> {
    List<VehicleDataItem> dataItems;

    Context context;

    Recycler_ItemClickCallBack callBack;

    public Custom_TrackList_Adepter(Context context, List<VehicleDataItem> myDataset, Recycler_ItemClickCallBack callBack) {
        dataItems = myDataset;
        this.context = context;
        this.callBack=callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.custom_track_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //     holder.txtFooter.setText("Footer: " + name);
        holder.txt_name.setText("Truck Name:"+dataItems.get(position).getIdentifier());
        holder.txt_class.setText(dataItems.get(position).getMode());

        holder.txtCompny.setText(dataItems.get(position).getTruckCompany());
        holder.txt_capacity.setText(dataItems.get(position).getTonnage());
        holder.txt_licenceplate.setText(dataItems.get(position).getLicencePlate());
        Load_Image(dataItems.get(position).getImage(), holder.imgTrck);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.OnItemClick(position);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.txt_name)        TextView txt_name;
        @BindView(R.id.img_trck)        ImageView imgTrck;
        public View layout;

        @BindView(R.id.txt_compny)        TextView txtCompny;
        @BindView(R.id.txt_capacity)        TextView txt_capacity;
        @BindView(R.id.txt_licenceplate)        TextView txt_licenceplate;
        @BindView(R.id.txt_class)        TextView txt_class;



        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            layout = v;

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
}