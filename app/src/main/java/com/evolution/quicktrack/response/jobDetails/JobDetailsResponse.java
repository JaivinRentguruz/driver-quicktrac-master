package com.evolution.quicktrack.response.jobDetails;

import com.google.gson.annotations.SerializedName;

public class JobDetailsResponse{

	@SerializedName("data")
	private JobDetailsData data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(JobDetailsData data){
		this.data = data;
	}

	public JobDetailsData getData(){
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
			"JobDetailsResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}