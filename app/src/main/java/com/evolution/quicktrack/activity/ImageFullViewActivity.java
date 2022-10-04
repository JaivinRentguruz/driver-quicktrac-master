package com.evolution.quicktrack.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.evolution.quicktrack.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 7/7/2018.
 */

public class ImageFullViewActivity extends AppCompatActivity {


    @BindView(R.id.img_fullimge)
    GestureImageView imgFullimge;
    @BindView(R.id.img_close)
    ImageView imgClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefullview);
        ButterKnife.bind(this);

        Load_Image(getIntent().getStringExtra("Image"),imgFullimge);


    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        onBackPressed();

    }



    private void Load_Image(String Url, final ImageView img) {

        Glide.with(getApplicationContext())
                .load(Url)
                .into(img);
    }

}
