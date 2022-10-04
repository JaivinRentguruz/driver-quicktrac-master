package com.evolution.quicktrack.model;

import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

/**
 * Created by user on 5/8/2018.
 */

public class QuestionModel {

    private String checkListId;
    private String answer;
    private String comment;
    private ArrayList<Image> images;
    private boolean isRight;
    private boolean isLike;
    private boolean isNA;

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isNA() {
        return isNA;
    }

    public void setNA(boolean NA) {
        isNA = NA;
    }

    public String getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(String checkListId) {
        this.checkListId = checkListId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
