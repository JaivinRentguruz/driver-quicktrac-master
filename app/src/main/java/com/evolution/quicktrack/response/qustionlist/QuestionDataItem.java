package com.evolution.quicktrack.response.qustionlist;

import com.google.gson.annotations.SerializedName;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

public class QuestionDataItem {

	@SerializedName("checklist_question_name")
	private String checklistQuestionName;

	@SerializedName("id")
	private String id;







	public void setChecklistQuestionName(String checklistQuestionName){
		this.checklistQuestionName = checklistQuestionName;
	}

	public String getChecklistQuestionName(){
		return checklistQuestionName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"QuestionDataItem{" +
			"checklist_question_name = '" + checklistQuestionName + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}





	/*private String answer="";*/
	private String comment="";
	private String isSelected="";
	private ArrayList<Image> images=new ArrayList<>();
	private boolean isRight=false;
	private boolean isFail =false;
	private boolean isNA=false;

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

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

	public boolean isFail() {
		return isFail;
	}

	public void setFail(boolean fail) {
		isFail = fail;
	}

	public boolean isNA() {
		return isNA;
	}

	public void setNA(boolean NA) {
		isNA = NA;
	}



	/*public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}*/

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}



}