package com.evolution.quicktrack.response.login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("data")
	private LoginData data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(LoginData data){
		this.data = data;
	}

	public LoginData getData(){
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
			"LoginResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}