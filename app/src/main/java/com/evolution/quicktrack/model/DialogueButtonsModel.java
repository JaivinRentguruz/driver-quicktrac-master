package com.evolution.quicktrack.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DialogueButtonsModel implements Parcelable {
    public DialogueButtonsModel(){

    }
    protected DialogueButtonsModel(Parcel in) {
        btnOneText = in.readString();
        btnTwoText = in.readString();
        btnThreeText = in.readString();
        btnFourText = in.readString();
        btnFiveText = in.readString();
        imgOne = in.readInt();
        imgTwo = in.readInt();
        imgThree = in.readInt();
        imgFour = in.readInt();
        imgFive = in.readInt();
        noOFButtonVisibility = in.readInt();
    }

    public static final Creator<DialogueButtonsModel> CREATOR = new Creator<DialogueButtonsModel>() {
        @Override
        public DialogueButtonsModel createFromParcel(Parcel in) {
            return new DialogueButtonsModel(in);
        }

        @Override
        public DialogueButtonsModel[] newArray(int size) {
            return new DialogueButtonsModel[size];
        }
    };

    public String getBtnOneText() {
        return btnOneText;
    }

    public void setBtnOneText(String btnOneText) {
        this.btnOneText = btnOneText;
    }

    public String getBtnTwoText() {
        return btnTwoText;
    }

    public void setBtnTwoText(String btnTwoText) {
        this.btnTwoText = btnTwoText;
    }

    public String getBtnThreeText() {
        return btnThreeText;
    }

    public void setBtnThreeText(String btnThreeText) {
        this.btnThreeText = btnThreeText;
    }

    public String getBtnFourText() {
        return btnFourText;
    }

    public void setBtnFourText(String btnFourText) {
        this.btnFourText = btnFourText;
    }

    public String getBtnFiveText() {
        return btnFiveText;
    }

    public void setBtnFiveText(String btnFiveText) {
        this.btnFiveText = btnFiveText;
    }

    public int getImgOne() {
        return imgOne;
    }

    public void setImgOne(int imgOne) {
        this.imgOne = imgOne;
    }

    public int getImgTwo() {
        return imgTwo;
    }

    public void setImgTwo(int imgTwo) {
        this.imgTwo = imgTwo;
    }

    public int getImgThree() {
        return imgThree;
    }

    public void setImgThree(int imgThree) {
        this.imgThree = imgThree;
    }

    public int getImgFour() {
        return imgFour;
    }

    public void setImgFour(int imgFour) {
        this.imgFour = imgFour;
    }

    public int getImgFive() {
        return imgFive;
    }

    public void setImgFive(int imgFive) {
        this.imgFive = imgFive;
    }

    public int getNoOFButtonVisibility() {
        return noOFButtonVisibility;
    }

    public void setNoOFButtonVisibility(int noOFButtonVisibility) {
        this.noOFButtonVisibility = noOFButtonVisibility;
    }

    private String btnOneText;
    private String btnTwoText;
    private String btnThreeText;
    private String btnFourText;
    private String btnFiveText;


    private int imgOne;
    private int imgTwo;
    private int imgThree;
    private int imgFour;
    private int imgFive;

    private int noOFButtonVisibility;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(btnOneText);
        dest.writeString(btnTwoText);
        dest.writeString(btnThreeText);
        dest.writeString(btnFourText);
        dest.writeString(btnFiveText);
        dest.writeInt(imgOne);
        dest.writeInt(imgTwo);
        dest.writeInt(imgThree);
        dest.writeInt(imgFour);
        dest.writeInt(imgFive);
        dest.writeInt(noOFButtonVisibility);
    }
}
