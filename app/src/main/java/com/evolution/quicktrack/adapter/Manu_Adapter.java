package com.evolution.quicktrack.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.R;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;

public class Manu_Adapter extends Adapter<Manu_Adapter.SingleItemRowHolder> {

    String[] itemsList;
    Context mContext;
    Recycler_ItemClickCallBack listner;


    int manuImge[]={R.drawable.manu1,R.drawable.manu2,R.drawable.manu3,R.drawable.manu4,
            R.drawable.manu5,R.drawable.manu6,R.drawable.manu7,R.drawable.manu8,R.drawable.manu9};



    public Manu_Adapter(Context context, String[] itemsList,Recycler_ItemClickCallBack recycler_itemClickCallBack) {
        this.itemsList = itemsList;
        this.mContext = context;
        listner=recycler_itemClickCallBack;
    }

    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SingleItemRowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_manu_adepter, viewGroup, false));
    }

    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {

        holder.txt_noofstar.setText(itemsList[i]);
        holder.img_manu.setImageResource(manuImge[i]);

        holder.img_manu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.OnItemClick(i);
            }
        });


    }

    public int getItemCount() {
        return this.itemsList != null ? this.itemsList.length : 0;
    }

    public class SingleItemRowHolder extends ViewHolder {
        public ImageView img_manu;

        protected TextView txt_noofstar;


        public SingleItemRowHolder(View view) {
            super(view);
            txt_noofstar = (TextView) view.findViewById(R.id.txt_manuName);
            img_manu = (ImageView) view.findViewById(R.id.img_manu);

        }
    }
}
