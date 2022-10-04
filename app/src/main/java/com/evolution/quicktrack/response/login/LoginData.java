package com.evolution.quicktrack.response.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

	@SerializedName("driver_id")
	private String driverId;

	@SerializedName("d_contact_no")
	private String dContactNo;

	@SerializedName("d_licence_number")
	private String dLicenceNumber;

	@SerializedName("d_email")
	private String dEmail;

	@SerializedName("d_last_name")
	private String dLastName;

	@SerializedName("d_first_name")
	private String dFirstName;


	@SerializedName("image")
	private String image;

	@SerializedName("login_token")
	private String login_token;

	@SerializedName("fcm_token")
	private String fcm_token;

	@SerializedName("login_flag")
	private int login_flag;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setDriverId(String driverId){
		this.driverId = driverId;
	}

	public String getDriverId(){
		return driverId;
	}

	public void setDContactNo(String dContactNo){
		this.dContactNo = dContactNo;
	}

	public String getDContactNo(){
		return dContactNo;
	}

	public void setDLicenceNumber(String dLicenceNumber){
		this.dLicenceNumber = dLicenceNumber;
	}

	public String getDLicenceNumber(){
		return dLicenceNumber;
	}

	public void setDEmail(String dEmail){
		this.dEmail = dEmail;
	}

	public String getDEmail(){
		return dEmail;
	}

	public void setDLastName(String dLastName){
		this.dLastName = dLastName;
	}

	public String getDLastName(){
		return dLastName;
	}

	public void setDFirstName(String dFirstName){
		this.dFirstName = dFirstName;
	}

	public String getDFirstName(){
		return dFirstName;
	}

	public String getLogin_token() {
		return login_token;
	}

	public void setLogin_token(String login_token) {
		this.login_token = login_token;
	}

	public String getFcm_token() {
		return fcm_token;
	}

	public void setFcm_token(String fcm_token) {
		this.fcm_token = fcm_token;
	}

	public int getLogin_flag() {
		return login_flag;
	}

	public void setLogin_flag(int login_flag) {
		this.login_flag = login_flag;
	}

	@Override
 	public String toString(){
		return 
			"LoginData{" +
			"driver_id = '" + driverId + '\'' + 
			",d_contact_no = '" + dContactNo + '\'' + 
			",d_licence_number = '" + dLicenceNumber + '\'' + 
			",d_email = '" + dEmail + '\'' + 
			",d_last_name = '" + dLastName + '\'' + 
			",d_first_name = '" + dFirstName + '\'' +
			",login_token = '" + login_token + '\'' +
			",fcm_token = '" + fcm_token + '\'' +
			",login_flag = '" + login_flag + '\'' +
			"}";
		}
}