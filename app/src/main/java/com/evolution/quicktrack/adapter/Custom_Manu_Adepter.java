package com.evolution.quicktrack.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.R;

/**
 * Created by user on 4/3/2018.
 */

public class Custom_Manu_Adepter extends RecyclerView.Adapter<Custom_Manu_Adepter.ViewHolder> {
    private String[] mDataset;
    String[] values;
    Context mContext;

    int manuImge[]={R.drawable.manu1,R.drawable.manu2,R.drawable.manu3,R.drawable.manu4,
            R.drawable.manu5,R.drawable.manu6,R.drawable.manu7,R.drawable.manu8,R.drawable.manu9};


    public Custom_Manu_Adepter(Context context, String[] myDataset) {
        values = myDataset;
        mContext=context;
    }

    @Override
    public Custom_Manu_Adepter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.custom_manu_adepter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txt_manuName.setText("Footer: " );
        holder.img_manu.setImageResource(manuImge[position]);

        Log.d("xxx","sss adapter");
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txt_manuName;
        public ImageView img_manu;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txt_manuName = (TextView) v.findViewById(R.id.txt_manuName);
            img_manu = (ImageView) v.findViewById(R.id.img_manu);
        }
    }

}