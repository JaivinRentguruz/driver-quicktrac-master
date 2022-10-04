package com.evolution.quicktrack.response.truckdetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TruckDetailsResponse{

	@SerializedName("data")
	private List<TruckData> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(List<TruckData> data){
		this.data = data;
	}

	public List<TruckData> getData(){
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
			"TruckDetailsResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}