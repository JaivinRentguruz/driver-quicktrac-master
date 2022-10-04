package com.evolution.quicktrack.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.response.userlist.UserDataItem;
import com.evolution.quicktrack.util.CircleImageView;
import com.evolution.quicktrack.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class UserList_Adapter extends RecyclerView.Adapter<UserList_Adapter.SingleItemRowHolder> implements Filterable {

    private List<UserDataItem> itemsList;
    private Context mContext;
    private Recycler_ItemClickCallBack listner;
    private List<UserDataItem> contactListFiltered;
    boolean isRecent;


    public UserList_Adapter(Context context, List<UserDataItem> itemsList, Recycler_ItemClickCallBack listner,boolean isRecent) {
        this.itemsList = itemsList;
        this.contactListFiltered = itemsList;
        this.mContext = context;
        this.listner=listner;
        this.isRecent=isRecent;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_userlist, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);


        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int position) {

        final UserDataItem userDataItem = contactListFiltered.get(position);

        holder.txt_username.setText(userDataItem.getFriendName());
        holder.txt_userRole.setText(userDataItem.getRoleName());



        /*TextDrawable drawable = TextDrawable.builder()
               // .buildRect(String.valueOf(userDataItem.getFriendName().charAt(0)), Color.RED);
                .buildRound("A", Color.RED);*/


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
              //  .fontSize(30) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(Utils.printFirstCharacter(userDataItem.getFriendName()), Utils.getRandomMaterialColor(mContext));
        holder.img_txt.setImageDrawable(drawable);



       /* RequestOptions options = new RequestOptions()
                 // .placeholder(drawable)
                //.error(R.drawable.cat_img_1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);
        Glide.with(mContext)
                .load(userDataItem.getFriendPicture())
                .apply(options)
                .into(holder.img_user);*/

        lodImageusr(userDataItem.getFriendPicture(),holder.img_user,holder.img_txt);



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.OnItemClick(position);
            }
        });




    }

    @Override
    public int getItemCount() {
        return (null != contactListFiltered ? contactListFiltered.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

         TextView txt_username,txt_userRole;

        ImageView img_txt;
        CircleImageView img_user;

        View view;

        public SingleItemRowHolder(View view) {
            super(view);

            this.txt_username = (TextView) view.findViewById(R.id.txt_userName);
            this.txt_userRole = (TextView) view.findViewById(R.id.txt_userRole);

            this.img_txt = (ImageView) view.findViewById(R.id.img_txt);
            this.img_user = (CircleImageView) view.findViewById(R.id.img_user);

            this.view=view;


        }

    }


    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_400" , "array",mContext. getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }






    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                    if(isRecent){
                        contactListFiltered = itemsList;
                    }else{

                        //contactListFiltered = itemsList;
                    }

                } else {
                    List<UserDataItem> filteredList = new ArrayList<>();
                    for (UserDataItem row : itemsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFriendName().toLowerCase().contains(charString.toLowerCase()) || row.getAdminRole().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<UserDataItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }





    public void lodImageusr(String imgUrl, ImageView imageView, final ImageView imageViewtxt) {
        LogCustom.logd("xxxxx imge:","imgUrl:"+imgUrl);

        RequestOptions options = new RequestOptions()
                //.placeholder(R.drawable.refresh_icon)
                //.error(R.drawable.refresh_icon)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext)
                .load(imgUrl)
                .apply(options)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageViewtxt.setImageDrawable(mContext.getResources().getDrawable(R.drawable.circle_shape));
                        return false;
                    }
                })

                .into(imageView);


    }


}