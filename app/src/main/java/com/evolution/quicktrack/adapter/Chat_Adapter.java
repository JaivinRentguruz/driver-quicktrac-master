package com.evolution.quicktrack.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.callback.Chat_ItemClickCallBack;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.model.UserNameImageModel;
import com.evolution.quicktrack.response.chatlist.MessagesItem;
import com.evolution.quicktrack.util.CircleImageView;
import com.evolution.quicktrack.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.SingleItemRowHolder>  {



    private List<MessagesItem> itemsList;
    private Context mContext;
    private Chat_ItemClickCallBack listner;
    private  UserNameImageModel   userNameImageModel;


    public Chat_Adapter(Context context, List<MessagesItem> itemsList, UserNameImageModel   userNameImageModel, Chat_ItemClickCallBack listner) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.listner = listner;
        this.userNameImageModel = userNameImageModel;

    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_chatlist, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);


        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int position) {

        final MessagesItem messagesItem = itemsList.get(position);

//        Date  date=new Date(messagesItem.getDateTimestamp());
//

        if (!userNameImageModel.getMyId().equals(messagesItem.getToUserId())) {

            holder.linerRecive.setVisibility(View.VISIBLE);
            holder.linerSender.setVisibility(View.GONE);

            if (messagesItem.getMessage().equals("")) {
                holder.txtRecivermsg.setVisibility(View.GONE);
                holder.relReciveImg.setVisibility(View.VISIBLE);
                lodImage(messagesItem.getImage(), holder.pgRecive, holder.imgRecive);
            } else {
                holder.txtRecivermsg.setVisibility(View.VISIBLE);
                holder.relReciveImg.setVisibility(View.GONE);
                holder.txtRecivermsg.setText(messagesItem.getMessage());
            }
            holder.txt_sendertime.setVisibility(View.GONE);
            holder.txt_recivertime.setVisibility(View.VISIBLE);
            holder.txt_recivertime.setText(getFormattedDateAus(Long.parseLong(messagesItem.getDateTimestamp())));


            setUsertxtImage(userNameImageModel.getMyName(),holder.imgTxtrecive);
            lodImageusr(userNameImageModel.getMyImage(),holder.imgUserrecive,holder.imgTxtrecive);

        } else {

            holder.linerSender.setVisibility(View.VISIBLE);
            holder.linerRecive.setVisibility(View.GONE);

            if (messagesItem.getMessage().equals("")) {
                holder.txtSendermsg.setVisibility(View.GONE);
                holder.relSenderImg.setVisibility(View.VISIBLE);
                lodImage(messagesItem.getImage(), holder.pgSender, holder.imgSender);

            } else {
                holder.relSenderImg.setVisibility(View.GONE);
                holder.txtSendermsg.setVisibility(View.VISIBLE);
                holder.txtSendermsg.setText(messagesItem.getMessage());

            }
            holder.txt_recivertime.setVisibility(View.GONE);
            holder.txt_sendertime.setVisibility(View.VISIBLE);

            holder.txt_sendertime.setText(getFormattedDateAus(Long.parseLong(messagesItem.getDateTimestamp())));
            setUsertxtImage(userNameImageModel.getSenderName(),holder.imgTxtsender);
            lodImageusr(userNameImageModel.getSenderImage(),holder.imgUsersender,holder.imgTxtsender);

        }





        holder.imgRecive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.OnImageClick(position);
            }
        });

        holder.imgSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.OnImageClick(position);
            }
        });
        holder.imgUserrecive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.OnUserImageClick(position,true);
            }
        });
        holder.imgUsersender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.OnUserImageClick(position,true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_sendertime)
        TextView txt_sendertime;
        @BindView(R.id.txt_recivertime)
        TextView txt_recivertime;

        @BindView(R.id.img_txtsender)
        ImageView imgTxtsender;
        @BindView(R.id.img_usersender)
        CircleImageView imgUsersender;
        @BindView(R.id.img_sender)
        ImageView imgSender;
        @BindView(R.id.pg_sender)
        ProgressBar pgSender;
        @BindView(R.id.rel_senderImg)
        RelativeLayout relSenderImg;
        @BindView(R.id.txt_sendermsg)
        TextView txtSendermsg;

        @BindView(R.id.liner_sender)
        LinearLayout linerSender;
        @BindView(R.id.img_recive)
        ImageView imgRecive;
        @BindView(R.id.pg_recive)
        ProgressBar pgRecive;
        @BindView(R.id.rel_reciveImg)
        RelativeLayout relReciveImg;
        @BindView(R.id.txt_recivermsg)
        TextView txtRecivermsg;

        @BindView(R.id.img_txtrecive)
        ImageView imgTxtrecive;
        @BindView(R.id.img_userrecive)
        CircleImageView imgUserrecive;
        @BindView(R.id.liner_recive)
        LinearLayout linerRecive;

        View view;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

    }


   /* private String getDate(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public String getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
        }
        return "";
    }
*/

    public String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        TimeZone tz = TimeZone.getDefault();
        smsTime.setTimeInMillis(smsTimeInMilis * 1000);
        smsTime.add(Calendar.MILLISECOND, tz.getOffset(smsTime.getTimeInMillis()));


        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd-MM-yyyy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }

    public String getFormattedDateAus(long smsTimeInMilis) {

        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        TimeZone.setDefault(TimeZone.getTimeZone("Australia/Sydney"));
        TimeZone tz = TimeZone.getDefault();
        smsTime.setTimeInMillis(smsTimeInMilis * 1000);
        smsTime.add(Calendar.MILLISECOND, tz.getOffset(smsTime.getTimeInMillis()));


        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd-MM-yyyy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }


    public void lodImage(String imgUrl, final ProgressBar bar, ImageView imageView) {


        RequestOptions options = new RequestOptions()
                //.placeholder(R.drawable.refresh_icon)
                //.error(R.drawable.refresh_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext)
                .load(imgUrl)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        bar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        bar.setVisibility(View.GONE);
                        return false;
                    }
                })

                .into(imageView);


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



    public void setUsertxtImage(String name,ImageView image){

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                //  .fontSize(30) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(Utils.printFirstCharacter(name), Utils.getRandomMaterialColor(mContext));
        image.setImageDrawable(drawable);


    }





}