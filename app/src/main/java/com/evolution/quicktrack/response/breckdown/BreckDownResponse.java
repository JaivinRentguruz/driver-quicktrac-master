package com.evolution.quicktrack.response.breckdown;

import com.google.gson.annotations.SerializedName;

public class BreckDownResponse{

	@SerializedName("message")
	private String message;

	@SerializedName("key")
	private Object key;

	@SerializedName("status")
	private boolean status;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setKey(Object key){
		this.key = key;
	}

	public Object getKey(){
		return key;
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
			"BreckDownResponse{" + 
			"message = '" + message + '\'' + 
			",key = '" + key + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}