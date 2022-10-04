package com.evolution.quicktrack.response.sentmsg;

import com.google.gson.annotations.SerializedName;

public class SentMsgResponse{

	@SerializedName("data")
	private int data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setData(int data){
		this.data = data;
	}

	public int getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"SentMsgResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}