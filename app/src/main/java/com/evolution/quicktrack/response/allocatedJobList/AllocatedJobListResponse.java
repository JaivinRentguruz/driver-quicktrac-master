package com.evolution.quicktrack.response.allocatedJobList;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllocatedJobListResponse{

	@SerializedName("data")
	private List<JobItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(List<JobItem> data){
		this.data = data;
	}

	public List<JobItem> getData(){
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
			"AllocatedJobListResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}