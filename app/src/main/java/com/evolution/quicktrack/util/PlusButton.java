package com.evolution.quicktrack.util;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.evolution.quicktrack.R;

public class PlusButton extends AppCompatImageView {
    public PlusButton(Context context) {
        super(context);
        init();
    }

    public PlusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setImageResource(R.drawable.ic_plus_menu);
    }
}
