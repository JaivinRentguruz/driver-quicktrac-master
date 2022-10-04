package com.evolution.quicktrack.response.vehicle;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VehicleResponse{

	@SerializedName("data")
	private List<VehicleDataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(List<VehicleDataItem> data){
		this.data = data;
	}

	public List<VehicleDataItem> getData(){
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
			"VehicleResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}