package com.evolution.quicktrack.response.jobDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobDetailsData {

	public String getCollectionContactPerson() {
		return collectionContactPerson;
	}

	public void setCollectionContactPerson(String collectionContactPerson) {
		this.collectionContactPerson = collectionContactPerson;
	}

	public String getCollectionContactNumner() {
		return collectionContactNumner;
	}

	public void setCollectionContactNumner(String collectionContactNumner) {
		this.collectionContactNumner = collectionContactNumner;
	}

	public String getDeliveryContactPerson() {
		return deliveryContactPerson;
	}

	public void setDeliveryContactPerson(String deliveryContactPerson) {
		this.deliveryContactPerson = deliveryContactPerson;
	}

	public String getDeliveryContactNumner() {
		return deliveryContactNumner;
	}

	public void setDeliveryContactNumner(String deliveryContactNumner) {
		this.deliveryContactNumner = deliveryContactNumner;
	}

	@SerializedName("collection_contatperson")
	private String collectionContactPerson;

	@SerializedName("collection_contactno")
	private String collectionContactNumner;


	@SerializedName("delivery_contatperson")
	private String deliveryContactPerson;

	@SerializedName("delivery_contactno")
	private String deliveryContactNumner;




	@SerializedName("driver_id")
	private String driverId;

	@SerializedName("requestname")
	private String requestname;

	@SerializedName("distance")
	private String distance;

	@SerializedName("collection_longitude")
	private String collectionLongitude;

	public String getCollectionCompanyname() {
		return collectionCompanyname;
	}

	public void setCollectionCompanyname(String collectionCompanyname) {
		this.collectionCompanyname = collectionCompanyname;
	}

	public String getDeliveryCompanyname() {
		return deliveryCompanyname;
	}

	public void setDeliveryCompanyname(String deliveryCompanyname) {
		this.deliveryCompanyname = deliveryCompanyname;
	}

	@SerializedName("collection_companyname")
	@Expose
	private String collectionCompanyname;

	@SerializedName("delivery_companyname")
	@Expose
	private String deliveryCompanyname;


	@SerializedName("description")
	private String description;

	@SerializedName("delivery_location")
	private String deliveryLocation;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("contact_person")
	private String contactPerson;


	@SerializedName("delivery_to_dateTime")
	private String deliveryToDateTime;

	@SerializedName("id")
	private String id;

	@SerializedName("collection_postalcodes")
	private String collectionPostalcodes;

	@SerializedName("vehicle_id")
	private String vehicleId;

	@SerializedName("pickup_from_dateTime")
	private String pickupFromDateTime;

	@SerializedName("dimension")
	private String dimension;

	@SerializedName("delivery_latitude")
	private String deliveryLatitude;

	@SerializedName("delivery_postalcodes")
	private String deliveryPostalcodes;

	@SerializedName("zonenames")
	private String zonenames;

	@SerializedName("collection_latitude")
	private String collectionLatitude;

	@SerializedName("collection_location")
	private String collectionLocation;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("weight")
	private String weight;

	@SerializedName("statusname")
	private String statusname;

	@SerializedName("zonename")
	private String zonename;

	@SerializedName("pickup_to_dateTime")
	private String pickupToDateTime;

	@SerializedName("cubic_weight")
	private String cubicWeight;

	@SerializedName("collection_subrb")
	private String collectionSubrb;

	@SerializedName("customer_id")
	private String customerId;

	@SerializedName("delivery_from_dateTime")
	private String deliveryFromDateTime;

	@SerializedName("delivery_longitude")
	private String deliveryLongitude;

	@SerializedName("delivery_type")
	private String deliveryType;

	@SerializedName("status")
	private String status;

	@SerializedName("special_instructions")
	private String specialinstructions;

	@SerializedName("collection_location_id")
	private String collection_location_id;

	@SerializedName("requested_by")
	private String requested_by;


	public void setDriverId(String driverId){
		this.driverId = driverId;
	}

	public String getDriverId(){
		return driverId;
	}

	public void setRequestname(String requestname){
		this.requestname = requestname;
	}

	public String getRequestname(){
		return requestname;
	}

	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getDistance(){
		return distance;
	}

	public void setCollectionLongitude(String collectionLongitude){
		this.collectionLongitude = collectionLongitude;
	}

	public String getCollectionLongitude(){
		return collectionLongitude;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setDeliveryLocation(String deliveryLocation){
		this.deliveryLocation = deliveryLocation;
	}

	public String getDeliveryLocation(){
		return deliveryLocation;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public void setDeliveryToDateTime(String deliveryToDateTime){
		this.deliveryToDateTime = deliveryToDateTime;
	}

	public String getDeliveryToDateTime(){
		return deliveryToDateTime;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCollectionPostalcodes(String collectionPostalcodes){
		this.collectionPostalcodes = collectionPostalcodes;
	}

	public String getCollectionPostalcodes(){
		return collectionPostalcodes;
	}

	public void setVehicleId(String vehicleId){
		this.vehicleId = vehicleId;
	}

	public String getVehicleId(){
		return vehicleId;
	}

	public void setPickupFromDateTime(String pickupFromDateTime){
		this.pickupFromDateTime = pickupFromDateTime;
	}

	public String getPickupFromDateTime(){
		return pickupFromDateTime;
	}

	public String getTimeOnlyFromDateTime() {
		if(pickupFromDateTime != null && !pickupFromDateTime.isEmpty()) {

		}

		return "";
	}

	public void setDimension(String dimension){
		this.dimension = dimension;
	}

	public String getDimension(){
		return dimension;
	}

	public void setDeliveryLatitude(String deliveryLatitude){
		this.deliveryLatitude = deliveryLatitude;
	}

	public String getDeliveryLatitude(){
		return deliveryLatitude;
	}

	public void setDeliveryPostalcodes(String deliveryPostalcodes){
		this.deliveryPostalcodes = deliveryPostalcodes;
	}

	public String getDeliveryPostalcodes(){
		return deliveryPostalcodes;
	}

	public void setZonenames(String zonenames){
		this.zonenames = zonenames;
	}

	public String getZonenames(){
		return zonenames;
	}

	public void setCollectionLatitude(String collectionLatitude){
		this.collectionLatitude = collectionLatitude;
	}

	public String getCollectionLatitude(){
		return collectionLatitude;
	}

	public void setCollectionLocation(String collectionLocation){
		this.collectionLocation = collectionLocation;
	}

	public String getCollectionLocation(){

		//if(deliveryType)
		return collectionLocation;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setWeight(String weight){
		this.weight = weight;
	}

	public String getWeight(){
		return weight;
	}

	public void setStatusname(String statusname){
		this.statusname = statusname;
	}

	public String getStatusname(){
		return statusname;
	}

	public void setZonename(String zonename){
		this.zonename = zonename;
	}

	public String getZonename(){
		return zonename;
	}

	public void setPickupToDateTime(String pickupToDateTime){
		this.pickupToDateTime = pickupToDateTime;
	}

	public String getPickupToDateTime(){
		return pickupToDateTime;
	}

	public void setCubicWeight(String cubicWeight){
		this.cubicWeight = cubicWeight;
	}

	public String getCubicWeight(){
		return cubicWeight;
	}

	public void setCollectionSubrb(String collectionSubrb){
		this.collectionSubrb = collectionSubrb;
	}

	public String getCollectionSubrb(){
		return collectionSubrb;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public void setDeliveryFromDateTime(String deliveryFromDateTime){
		this.deliveryFromDateTime = deliveryFromDateTime;
	}

	public String getDeliveryFromDateTime(){
		return deliveryFromDateTime;
	}

	public void setDeliveryLongitude(String deliveryLongitude){
		this.deliveryLongitude = deliveryLongitude;
	}

	public String getDeliveryLongitude(){
		return deliveryLongitude;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public String getSpecialinstructions() {
		return specialinstructions;
	}

	public void setSpecialinstructions(String specialinstructions) {
		this.specialinstructions = specialinstructions;
	}

	public String getCollection_location_id() {
		return collection_location_id;
	}

	public void setCollection_location_id(String collection_location_id) {
		this.collection_location_id = collection_location_id;
	}

	@Override
 	public String toString(){
		return 
			"JobDetailsData{" +
			"driver_id = '" + driverId + '\'' + 
			",requestname = '" + requestname + '\'' + 
			",distance = '" + distance + '\'' + 
			",collection_longitude = '" + collectionLongitude + '\'' + 
			",description = '" + description + '\'' + 
			",delivery_location = '" + deliveryLocation + '\'' +
			",company_name = '" + companyName + '\'' +
			",contact_person = '" + contactPerson	 + '\'' +
			",delivery_to_dateTime = '" + deliveryToDateTime + '\'' + 
			",id = '" + id + '\'' + 
			",collection_postalcodes = '" + collectionPostalcodes + '\'' + 
			",vehicle_id = '" + vehicleId + '\'' + 
			",pickup_from_dateTime = '" + pickupFromDateTime + '\'' + 
			",dimension = '" + dimension + '\'' + 
			",delivery_latitude = '" + deliveryLatitude + '\'' + 
			",delivery_postalcodes = '" + deliveryPostalcodes + '\'' + 
			",zonenames = '" + zonenames + '\'' + 
			",collection_latitude = '" + collectionLatitude + '\'' + 
			",collection_location = '" + collectionLocation + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",weight = '" + weight + '\'' + 
			",statusname = '" + statusname + '\'' + 
			",zonename = '" + zonename + '\'' + 
			",pickup_to_dateTime = '" + pickupToDateTime + '\'' + 
			",cubic_weight = '" + cubicWeight + '\'' + 
			",collection_subrb = '" + collectionSubrb + '\'' + 
			",customer_id = '" + customerId + '\'' + 
			",delivery_from_dateTime = '" + deliveryFromDateTime + '\'' + 
			",delivery_longitude = '" + deliveryLongitude + '\'' +
			",delivery_type = '" + deliveryType + '\'' +
			",status = '" + status + '\'' +
			",special_instructions = '" + specialinstructions + '\'' +
			",collection_location_id = '" + collection_location_id + '\'' +
			"}";
		}

	public String getRequested_by() {
		return requested_by;
	}

	public void setRequested_by(String requested_by) {
		this.requested_by = requested_by;
	}
}