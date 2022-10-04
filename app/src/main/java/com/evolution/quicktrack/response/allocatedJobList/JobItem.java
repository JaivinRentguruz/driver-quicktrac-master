package com.evolution.quicktrack.response.allocatedJobList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobItem {

	@SerializedName("job_date")
	private String jobDate;


	@SerializedName("driver_id")
	private String driverId;

	@SerializedName("distance")
	private String distance;

	@SerializedName("delivery_subrb")
	private String deliverySubrb;

	@SerializedName("description")
	private String description;

	@SerializedName("misc_adjustment")
	private String miscAdjustment;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("sub_contractor")
	private String subContractor;

	@SerializedName("delivery_location")
	private String deliveryLocation;

	@SerializedName("base_price_value")
	private String basePriceValue;

	@SerializedName("total")
	private String total;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_by_role")
	private String createdByRole;

	@SerializedName("delivery_type")
	private String deliveryType;

	@SerializedName("delivery_to_dateTime")
	private String deliveryToDateTime;

	@SerializedName("base_price")
	private String basePrice;

	@SerializedName("delivery_location_type")
	private String deliveryLocationType;

	@SerializedName("updated_by_role")
	private String updatedByRole;

	@SerializedName("id")
	private String id;

	@SerializedName("pickup_from_dateTime")
	private String pickupFromDateTime;

	@SerializedName("dimension")
	private String dimension;

	@SerializedName("collection_location")
	private String collectionLocation;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("service_level")
	private String serviceLevel;

	@SerializedName("weight")
	private String weight;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("pickup_to_dateTime")
	private String pickupToDateTime;

	@SerializedName("cubic_weight")
	private String cubicWeight;

	@SerializedName("requested_by")
	private String requestedBy;

	@SerializedName("collection_location_type")
	private String collectionLocationType;

	@SerializedName("collection_subrb")
	private String collectionSubrb;

	@SerializedName("id2")
	private String id2;

	@SerializedName("updated_by")
	private String updatedBy;

	@SerializedName("customer_name")
	private String customerName;

	@SerializedName("delivery_from_dateTime")
	private String deliveryFromDateTime;

	@SerializedName("status")
	private String status;


	@SerializedName("collection_latitude")
	private String collection_latitude;
	@SerializedName("collection_longitude")
	private String collection_longitude;
	@SerializedName("delivery_latitude")
	private String delivery_latitude;
	@SerializedName("delivery_longitude")
	private String delivery_longitude;

	@SerializedName("type")
	private String type;

	@SerializedName("subjobid")
	private String subjobid;

	@SerializedName("delivery_name")
	@Expose
	private String deliveryName;

	@SerializedName("collection_companyname")
	@Expose
	private String collection_companyname;

	@SerializedName("delivery_companyname")
	@Expose
	private String delivery_companyname;


	public String getCollection_latitude() {
		return collection_latitude;
	}

	public void setCollection_latitude(String collection_latitude) {
		this.collection_latitude = collection_latitude;
	}

	public String getCollection_longitude() {
		return collection_longitude;
	}

	public void setCollection_longitude(String collection_longitude) {
		this.collection_longitude = collection_longitude;
	}

	public String getDelivery_latitude() {
		return delivery_latitude;
	}

	public void setDelivery_latitude(String delivery_latitude) {
		this.delivery_latitude = delivery_latitude;
	}

	public String getDelivery_longitude() {
		return delivery_longitude;
	}

	public void setDelivery_longitude(String delivery_longitude) {
		this.delivery_longitude = delivery_longitude;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDistance() {
		return distance;
	}

	public void setDeliverySubrb(String deliverySubrb) {
		this.deliverySubrb = deliverySubrb;
	}

	public String getDeliverySubrb() {
		return deliverySubrb;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setMiscAdjustment(String miscAdjustment) {
		this.miscAdjustment = miscAdjustment;
	}

	public String getMiscAdjustment() {
		return miscAdjustment;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setSubContractor(String subContractor) {
		this.subContractor = subContractor;
	}

	public String getSubContractor() {
		return subContractor;
	}

	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public String getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setBasePriceValue(String basePriceValue) {
		this.basePriceValue = basePriceValue;
	}

	public String getBasePriceValue() {
		return basePriceValue;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotal() {
		return total;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setCreatedByRole(String createdByRole) {
		this.createdByRole = createdByRole;
	}

	public String getCreatedByRole() {
		return createdByRole;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryToDateTime(String deliveryToDateTime) {
		this.deliveryToDateTime = deliveryToDateTime;
	}

	public String getDeliveryToDateTime() {
		return deliveryToDateTime;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setDeliveryLocationType(String deliveryLocationType) {
		this.deliveryLocationType = deliveryLocationType;
	}

	public String getDeliveryLocationType() {
		return deliveryLocationType;
	}

	public void setUpdatedByRole(String updatedByRole) {
		this.updatedByRole = updatedByRole;
	}

	public String getUpdatedByRole() {
		return updatedByRole;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setPickupFromDateTime(String pickupFromDateTime) {
		this.pickupFromDateTime = pickupFromDateTime;
	}

	public String getPickupFromDateTime() {
		return pickupFromDateTime;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getDimension() {
		return dimension;
	}

	public void setCollectionLocation(String collectionLocation) {
		this.collectionLocation = collectionLocation;
	}

	public String getCollectionLocation() {
		return collectionLocation;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeight() {
		return weight;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setPickupToDateTime(String pickupToDateTime) {
		this.pickupToDateTime = pickupToDateTime;
	}

	public String getPickupToDateTime() {
		return pickupToDateTime;
	}

	public void setCubicWeight(String cubicWeight) {
		this.cubicWeight = cubicWeight;
	}

	public String getCubicWeight() {
		return cubicWeight;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setCollectionLocationType(String collectionLocationType) {
		this.collectionLocationType = collectionLocationType;
	}

	public String getCollectionLocationType() {
		return collectionLocationType;
	}

	public void setCollectionSubrb(String collectionSubrb) {
		this.collectionSubrb = collectionSubrb;
	}

	public String getCollectionSubrb() {
		return collectionSubrb;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public String getId2() {
		return id2;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setDeliveryFromDateTime(String deliveryFromDateTime) {
		this.deliveryFromDateTime = deliveryFromDateTime;
	}

	public String getDeliveryFromDateTime() {
		return deliveryFromDateTime;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	/*
	public String getSubJobid() {
		return subjobid;
	}

	public void setSubJobid(String jobid) {
		this.subjobid = jobid;
	}*/


	public String getSubjobid() {
		return subjobid;
	}

	public void setSubjobid(String subjobid) {
		this.subjobid = subjobid;
	}

	public String getCollection_companyname() {
		return collection_companyname;
	}

	public void setCollection_companyname(String collection_companyname) {
		this.collection_companyname = collection_companyname;
	}

	public String getDelivery_companyname() {
		return delivery_companyname;
	}

	public void setDelivery_companyname(String delivery_companyname) {
		this.delivery_companyname = delivery_companyname;
	}

	@Override
	public String toString() {
		return "JobItem{" +
				"driverId='" + driverId + '\'' +
				", distance='" + distance + '\'' +
				", deliverySubrb='" + deliverySubrb + '\'' +
				", description='" + description + '\'' +
				", miscAdjustment='" + miscAdjustment + '\'' +
				", createdAt='" + createdAt + '\'' +
				", subContractor='" + subContractor + '\'' +
				", deliveryLocation='" + deliveryLocation + '\'' +
				", basePriceValue='" + basePriceValue + '\'' +
				", total='" + total + '\'' +
				", updatedAt='" + updatedAt + '\'' +
				", createdByRole='" + createdByRole + '\'' +
				", deliveryType='" + deliveryType + '\'' +
				", deliveryToDateTime='" + deliveryToDateTime + '\'' +
				", basePrice='" + basePrice + '\'' +
				", deliveryLocationType='" + deliveryLocationType + '\'' +
				", updatedByRole='" + updatedByRole + '\'' +
				", id='" + id + '\'' +
				", pickupFromDateTime='" + pickupFromDateTime + '\'' +
				", dimension='" + dimension + '\'' +
				", collectionLocation='" + collectionLocation + '\'' +
				", quantity='" + quantity + '\'' +
				", serviceLevel='" + serviceLevel + '\'' +
				", weight='" + weight + '\'' +
				", createdBy='" + createdBy + '\'' +
				", pickupToDateTime='" + pickupToDateTime + '\'' +
				", cubicWeight='" + cubicWeight + '\'' +
				", requestedBy='" + requestedBy + '\'' +
				", collectionLocationType='" + collectionLocationType + '\'' +
				", collectionSubrb='" + collectionSubrb + '\'' +
				", id2='" + id2 + '\'' +
				", updatedBy='" + updatedBy + '\'' +
				", customerName='" + customerName + '\'' +
				", deliveryFromDateTime='" + deliveryFromDateTime + '\'' +
				", status='" + status + '\'' +
				", collection_latitude='" + collection_latitude + '\'' +
				", collection_longitude='" + collection_longitude + '\'' +
				", delivery_latitude='" + delivery_latitude + '\'' +
				", delivery_longitude='" + delivery_longitude + '\'' +
				", type='" + type + '\'' +
				", subjobid='" + subjobid + '\'' +
				", deliveryName='" + deliveryName + '\'' +
				", collection_companyname='" + collection_companyname + '\'' +
				", delivery_companyname='" + delivery_companyname + '\'' +
				'}';
	}

	public String getJobDate() {
		return jobDate;
	}

	public void setJobDate(String jobDate) {
		this.jobDate = jobDate;
	}
}


