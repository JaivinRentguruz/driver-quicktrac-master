package com.evolution.quicktrack.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.callback.QuestionClickCallBack;
import com.evolution.quicktrack.response.qustionlist.QuestionDataItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Question_Adapter extends Adapter<Question_Adapter.SingleItemRowHolder> {

    List<QuestionDataItem> itemsList;
    Context mContext;
    QuestionClickCallBack listner;



    public Question_Adapter(Context context, List<QuestionDataItem> itemsList, QuestionClickCallBack recycler_itemClickCallBack) {
        this.itemsList = itemsList;
        this.mContext = context;
        listner = recycler_itemClickCallBack;
    }

    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SingleItemRowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_qustionlist, viewGroup, false));
    }

    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {

        String sourceString = "<b> <font color=#e9212121>" + "Question : " + "</font></b> ";
        holder.txtQutn.setText(Html.fromHtml(sourceString + itemsList.get(position).getChecklistQuestionName()));


        holder.imgRight.setSelected(false);
        holder.imgLike.setSelected(false);
        holder.imgNa.setSelected(false);
        holder.imgMsg.setSelected(false);

        if(QuickTrackAplication.IsNotNull(itemsList.get(position).getComment()))
        {
            holder.edit_comment.setText(itemsList.get(position).getComment());
        }
        else
        {
            holder.edit_comment.setText("");
        }




        if(itemsList.get(position).isFail()){

            holder.imgLike.setSelected(true);

        }else {

            holder.imgLike.setSelected(false);

        }
        if(itemsList.get(position).isNA()){
            holder.imgNa.setSelected(true);
        }else {
            holder.imgNa.setSelected(false);
        }
        if(itemsList.get(position).isRight()){
            holder.imgRight.setSelected(true);
        }else {
            holder.imgRight.setSelected(false);
        }


        setImage(position,holder);

        holder.imgNa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.naClick(position, true);
                holder.imgNa.setSelected(true);
                holder.imgRight.setSelected(false);
                holder.imgLike.setSelected(false);
                holder.liner_comment.setVisibility(View.VISIBLE);
            }
        });


        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                listner.failClick(position, true);
                holder.imgLike.setSelected(true);
                holder.imgNa.setSelected(false);
                holder.imgRight.setSelected(false);
                holder.liner_comment.setVisibility(View.VISIBLE);


            }
        });

        holder.imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                listner.rightClick(position, true);
                holder.imgLike.setSelected(false);
                holder.imgNa.setSelected(false);
                holder.imgRight.setSelected(true);
                holder.liner_comment.setVisibility(View.GONE);

            }
        });


        holder.imgMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.liner_comment.setVisibility(View.VISIBLE);

            }
        });


        holder.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listner.imageClick(position);
            }
        });


        holder.imgClose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.imageremoveClick(position,0);
            }
        });
        holder.imgClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.imageremoveClick(position,1);
            }
        });


        holder.edit_comment.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //    if(s.length() != 0)
                listner.commentClick(position, String.valueOf(s));
            }
        });


    }



    public void setImage(int position,SingleItemRowHolder holder){

        if (itemsList.get(position).getImages().size() > 0) {
            Load_Image(itemsList.get(position).getImages().get(0).getPath(),holder. img1);
            holder.relImg1.setVisibility(View.VISIBLE);

        } else {
            holder.relImg1.setVisibility(View.GONE);
        }

        if (itemsList.get(position).getImages().size() > 1) {

            Load_Image(itemsList.get(position).getImages().get(1).getPath(),holder. img2);
            holder.relImg2.setVisibility(View.VISIBLE);
        } else {
            holder.relImg2.setVisibility(View.GONE);
        }


    }



    public int getItemCount() {
        return this.itemsList != null ? this.itemsList.size() : 0;
    }

    public class SingleItemRowHolder extends ViewHolder {
        @BindView(R.id.txt_qutn)
        TextView txtQutn;

        @BindView(R.id.img_right)
        ImageView imgRight;
        @BindView(R.id.img_like)
        ImageView imgLike;
        @BindView(R.id.img_na)
        ImageView imgNa;
        @BindView(R.id.img_msg)
        ImageView imgMsg;
        @BindView(R.id.img_camera)
        ImageView imgCamera;
        @BindView(R.id.liner_comment)
        LinearLayout liner_comment;
        @BindView(R.id.edit_comment)
        EditText edit_comment;

        @BindView(R.id.img_1)
        ImageView img1;
        @BindView(R.id.img_close1)
        ImageView imgClose1;
        @BindView(R.id.rel_img1)
        RelativeLayout relImg1;
        @BindView(R.id.img_2)
        ImageView img2;
        @BindView(R.id.img_close2)
        ImageView imgClose2;
        @BindView(R.id.rel_img2)
        RelativeLayout relImg2;
        @BindView(R.id.liner_image)
        LinearLayout linerImage;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }



    private void Load_Image(String Url, final ImageView img) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.sample_user);
        requestOptions.fitCenter();


        Glide.with(mContext)
                .applyDefaultRequestOptions(requestOptions)
                .load(Url)
                .into(img);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void Update()
    {
        notifyDataSetChanged();
    }

}
