package com.evolution.quicktrack.callback;

/**
 * Created by user on 4/16/2018.
 */

public interface QuestionClickCallBack {


    public void OnItemClick(int position);

    public void rightClick(int position,boolean isSelected);
    public void commentClick(int position,String comment);
    public void failClick(int position, boolean isSelected);
    public void naClick(int position,boolean isSelected);

    public void imageClick(int position);

    public void imageremoveClick(int position,int no);



}
