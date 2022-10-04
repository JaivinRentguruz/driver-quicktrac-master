package com.evolution.quicktrack.response.qustionlist;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QuestionListResponse {

	@SerializedName("data")
	private List<QuestionDataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(List<QuestionDataItem> data){
		this.data = data;
	}

	public List<QuestionDataItem> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"QuestionListResponse{" +
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}