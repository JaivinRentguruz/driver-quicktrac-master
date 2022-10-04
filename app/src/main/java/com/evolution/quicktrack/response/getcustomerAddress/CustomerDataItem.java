package com.evolution.quicktrack.response.getcustomerAddress;

import com.google.gson.annotations.SerializedName;

public class CustomerDataItem {

	@SerializedName("address")
	private String address;

	@SerializedName("id")
	private String id;

	@SerializedName("customer_id")
	private String customerId;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	@Override
 	public String toString(){
		return 
			"CustomerDataItem{" +
			"address = '" + address + '\'' + 
			",id = '" + id + '\'' + 
			",customer_id = '" + customerId + '\'' + 
			"}";
		}
}