package com.evolution.quicktrack.response.vehicle;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleDataItem implements Serializable {

	@SerializedName("registration_expiry")
	private String registrationExpiry;

	@SerializedName("identifier")
	private String identifier;

	@SerializedName("fleet")
	private String fleet;

	@SerializedName("image")
	private String image;

	@SerializedName("odometer")
	private String odometer;

	@SerializedName("truck_company")
	private String truckCompany;

	@SerializedName("chassis_number")
	private String chassisNumber;

	@SerializedName("mode")
	private String mode;

	@SerializedName("decksize1")
	private String decksize1;

	@SerializedName("licence_plate")
	private String licencePlate;

	@SerializedName("axles_setup")
	private String axlesSetup;

	@SerializedName("engine_number")
	private String engineNumber;

	@SerializedName("track_odometer")
	private String trackOdometer;

	@SerializedName("tonnage")
	private String tonnage;

	@SerializedName("id")
	private String id;

	@SerializedName("subcontractor")
	private String subcontractor;

	@SerializedName("decksize2")
	private String decksize2;

	public void setRegistrationExpiry(String registrationExpiry){
		this.registrationExpiry = registrationExpiry;
	}

	public String getRegistrationExpiry(){
		return registrationExpiry;
	}

	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}

	public String getIdentifier(){
		return identifier;
	}

	public void setFleet(String fleet){
		this.fleet = fleet;
	}

	public String getFleet(){
		return fleet;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setOdometer(String odometer){
		this.odometer = odometer;
	}

	public String getOdometer(){
		return odometer;
	}

	public void setTruckCompany(String truckCompany){
		this.truckCompany = truckCompany;
	}

	public String getTruckCompany(){
		return truckCompany;
	}

	public void setChassisNumber(String chassisNumber){
		this.chassisNumber = chassisNumber;
	}

	public String getChassisNumber(){
		return chassisNumber;
	}

	public void setMode(String mode){
		this.mode = mode;
	}

	public String getMode(){
		return mode;
	}

	public void setDecksize1(String decksize1){
		this.decksize1 = decksize1;
	}

	public String getDecksize1(){
		return decksize1;
	}

	public void setLicencePlate(String licencePlate){
		this.licencePlate = licencePlate;
	}

	public String getLicencePlate(){
		return licencePlate;
	}

	public void setAxlesSetup(String axlesSetup){
		this.axlesSetup = axlesSetup;
	}

	public String getAxlesSetup(){
		return axlesSetup;
	}

	public void setEngineNumber(String engineNumber){
		this.engineNumber = engineNumber;
	}

	public String getEngineNumber(){
		return engineNumber;
	}

	public void setTrackOdometer(String trackOdometer){
		this.trackOdometer = trackOdometer;
	}

	public String getTrackOdometer(){
		return trackOdometer;
	}

	public void setTonnage(String tonnage){
		this.tonnage = tonnage;
	}

	public String getTonnage(){
		return tonnage;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSubcontractor(String subcontractor){
		this.subcontractor = subcontractor;
	}

	public String getSubcontractor(){
		return subcontractor;
	}

	public void setDecksize2(String decksize2){
		this.decksize2 = decksize2;
	}

	public String getDecksize2(){
		return decksize2;
	}

	@Override
 	public String toString(){
		return 
			"VehicleDataItem{" +
			"registration_expiry = '" + registrationExpiry + '\'' + 
			",identifier = '" + identifier + '\'' + 
			",fleet = '" + fleet + '\'' + 
			",image = '" + image + '\'' + 
			",odometer = '" + odometer + '\'' + 
			",truck_company = '" + truckCompany + '\'' + 
			",chassis_number = '" + chassisNumber + '\'' + 
			",mode = '" + mode + '\'' + 
			",decksize1 = '" + decksize1 + '\'' + 
			",licence_plate = '" + licencePlate + '\'' + 
			",axles_setup = '" + axlesSetup + '\'' + 
			",engine_number = '" + engineNumber + '\'' + 
			",track_odometer = '" + trackOdometer + '\'' + 
			",tonnage = '" + tonnage + '\'' + 
			",id = '" + id + '\'' + 
			",subcontractor = '" + subcontractor + '\'' + 
			",decksize2 = '" + decksize2 + '\'' + 
			"}";
		}
}