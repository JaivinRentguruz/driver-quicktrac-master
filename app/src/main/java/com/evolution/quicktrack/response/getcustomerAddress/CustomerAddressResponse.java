package com.evolution.quicktrack.response.getcustomerAddress;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CustomerAddressResponse{

	@SerializedName("data")
	private List<CustomerDataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setData(List<CustomerDataItem> data){
		this.data = data;
	}

	public List<CustomerDataItem> getData(){
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
			"CustomerAddressResponse{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}