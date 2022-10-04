package com.evolution.quicktrack.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.evolution.quicktrack.R;
import com.evolution.quicktrack.model.DialogueButtonsModel;


public class ShowCommonDialogu extends DialogFragment implements  View.OnClickListener {


    View llOne;

    View llTwo;

    View llThree;

    View llFour;

    View llFive;


    ImageView imgOne;

    ImageView imgTwo;

    ImageView imgThree;

    ImageView imgFour;

    ImageView imgFive;


    TextView txtOne;

    TextView txtTwo;

    TextView txtThree;

    TextView txtFour;

    TextView txtFive;







    public static int btnClicked = 0;

    public static String DATA_BUNDLE="data_bundle";

    private DialogueButtonsModel showCommonDialogu;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            //int width = ViewGroup.LayoutParams.MATCH_PARENT;
            //int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //dialog.getWindow().setLayout(width, 400);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        btnClicked = 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showCommonDialogu = getArguments().getParcelable(DATA_BUNDLE);
    }

    public static ShowCommonDialogu getInstance(DialogueButtonsModel dialogueButtonsModel){
        ShowCommonDialogu showCommonDialogu = new ShowCommonDialogu();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DATA_BUNDLE,dialogueButtonsModel);
        showCommonDialogu.setArguments(bundle);
        return showCommonDialogu;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_buttons_dialogu,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        llOne = view.findViewById(R.id.llOne);
        llTwo = view.findViewById(R.id.llTwo);
        llThree = view.findViewById(R.id.llThree);
        llFour = view.findViewById(R.id.llFour);
        llFive = view.findViewById(R.id.llFive);


        txtOne = view.findViewById(R.id.txtOne);
        txtTwo = view.findViewById(R.id.txtTwo);
        txtThree = view.findViewById(R.id.txtThree);
        txtFour = view.findViewById(R.id.txtFour);
        txtFive = view.findViewById(R.id.txtFive);

        imgOne = view.findViewById(R.id.imgOne);
        imgTwo = view.findViewById(R.id.imgTwo);
        imgThree = view.findViewById(R.id.imgThree);
        imgFour = view.findViewById(R.id.imgFour);
        imgFive = view.findViewById(R.id.imgFive);

        llOne.setOnClickListener(this);
        llTwo.setOnClickListener(this);
        llThree.setOnClickListener(this);
        llFour.setOnClickListener(this);
        llFive.setOnClickListener(this);

        switch (showCommonDialogu.getNoOFButtonVisibility()){
            case 1:
                txtOne.setText(showCommonDialogu.getBtnOneText());
                imgOne.setImageResource(showCommonDialogu.getImgOne());
                llOne.setVisibility(View.VISIBLE);
                break;
            case 2:
                txtOne.setText(showCommonDialogu.getBtnOneText());
                imgOne.setImageResource(showCommonDialogu.getImgOne());
                llOne.setVisibility(View.VISIBLE);
                txtTwo.setText(showCommonDialogu.getBtnTwoText());
                imgTwo.setImageResource(showCommonDialogu.getImgTwo());
                llTwo.setVisibility(View.VISIBLE);
                break;

            case 3:
                txtOne.setText(showCommonDialogu.getBtnOneText());
                imgOne.setImageResource(showCommonDialogu.getImgOne());
                llOne.setVisibility(View.VISIBLE);
                txtTwo.setText(showCommonDialogu.getBtnTwoText());
                imgTwo.setImageResource(showCommonDialogu.getImgTwo());
                llTwo.setVisibility(View.VISIBLE);
                txtThree.setText(showCommonDialogu.getBtnThreeText());
                imgThree.setImageResource(showCommonDialogu.getImgThree());
                llThree.setVisibility(View.VISIBLE);
                break;
            case 4:
                txtOne.setText(showCommonDialogu.getBtnOneText());
                imgOne.setImageResource(showCommonDialogu.getImgOne());
                llOne.setVisibility(View.VISIBLE);
                txtTwo.setText(showCommonDialogu.getBtnTwoText());
                imgTwo.setImageResource(showCommonDialogu.getImgTwo());
                llTwo.setVisibility(View.VISIBLE);
                txtThree.setText(showCommonDialogu.getBtnThreeText());
                imgThree.setImageResource(showCommonDialogu.getImgThree());
                llThree.setVisibility(View.VISIBLE);
                txtFour.setText(showCommonDialogu.getBtnFourText());
                imgFour.setImageResource(showCommonDialogu.getImgFour());
                llFour.setVisibility(View.VISIBLE);

                break;

            case 5:
                txtOne.setText(showCommonDialogu.getBtnOneText());
                imgOne.setImageResource(showCommonDialogu.getImgOne());
                llOne.setVisibility(View.VISIBLE);
                txtTwo.setText(showCommonDialogu.getBtnTwoText());
                imgTwo.setImageResource(showCommonDialogu.getImgTwo());
                llTwo.setVisibility(View.VISIBLE);
                txtThree.setText(showCommonDialogu.getBtnThreeText());
                imgThree.setImageResource(showCommonDialogu.getImgThree());
                llThree.setVisibility(View.VISIBLE);
                txtFour.setText(showCommonDialogu.getBtnFourText());
                imgFour.setImageResource(showCommonDialogu.getImgFour());
                llFour.setVisibility(View.VISIBLE);
                txtFive.setText(showCommonDialogu.getBtnFiveText());
                imgFive.setImageResource(showCommonDialogu.getImgFive());
                llFive.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llOne:
                btnClicked = 1;
                break;

            case R.id.llTwo:
                btnClicked = 2;
                break;

            case R.id.llThree:
                btnClicked = 3;
                break;

            case R.id.llFour:
                btnClicked = 4;
                break;

            case R.id.llFive:
                btnClicked = 5;
                break;
        }
        dismiss();
    }
}
