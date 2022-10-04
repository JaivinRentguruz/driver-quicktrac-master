package com.evolution.quicktrack.response.acceptedjobList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcceptedJob implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("collection_location")
    @Expose
    private String collectionLocation;
    @SerializedName("collection_latitude")
    @Expose
    private String collectionLatitude;
    @SerializedName("collection_longitude")
    @Expose
    private String collectionLongitude;
    @SerializedName("collection_postalcodes")
    @Expose
    private String collectionPostalcodes;
    @SerializedName("collection_zone")
    @Expose
    private String collectionZone;
    @SerializedName("collection_contatperson")
    @Expose
    private String collectionContatperson;
    @SerializedName("collection_contactno")
    @Expose
    private String collectionContactno;
    @SerializedName("collection_companyname")
    @Expose
    private String collectionCompanyname;
    @SerializedName("pickup_from_dateTime")
    @Expose
    private String pickupFromDateTime;
    @SerializedName("pickup_to_dateTime")
    @Expose
    private String pickupToDateTime;
    @SerializedName("delivery_location")
    @Expose
    private String deliveryLocation;
    @SerializedName("delivery_latitude")
    @Expose
    private String deliveryLatitude;
    @SerializedName("delivery_longitude")
    @Expose
    private String deliveryLongitude;
    @SerializedName("delivery_postalcodes")
    @Expose
    private String deliveryPostalcodes;
    @SerializedName("delivery_zone")
    @Expose
    private String deliveryZone;
    @SerializedName("delivery_from_dateTime")
    @Expose
    private String deliveryFromDateTime;
    @SerializedName("delivery_to_dateTime")
    @Expose
    private String deliveryToDateTime;
    @SerializedName("delivery_contatperson")
    @Expose
    private String deliveryContatperson;
    @SerializedName("delivery_companyname")
    @Expose
    private String deliveryCompanyname;
    @SerializedName("delivery_contactno")
    @Expose
    private String deliveryContactno;
    @SerializedName("purchase_order")
    @Expose
    private String purchaseOrder;
    @SerializedName("docket_number")
    @Expose
    private String docketNumber;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("cubic_weight")
    @Expose
    private String cubicWeight;
    @SerializedName("dimension")
    @Expose
    private String dimension;
    @SerializedName("measurement")
    @Expose
    private String measurement;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusname")
    @Expose
    private String statusname;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("requested_by")
    @Expose
    private Object requestedBy;
    @SerializedName("delivery_name")
    @Expose
    private String deliveryName;
    @SerializedName("pickup_distance")
    @Expose
    private String pickupDistance;
    @SerializedName("delivery_distance")
    @Expose
    private String deliveryDistance;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("subjobid")
    @Expose
    private long subjobid;

    private boolean isChecked = false;

    public AcceptedJob() {
    }

    public AcceptedJob(String id, String vehicleId, String collectionLocation, String collectionLatitude, String collectionLongitude, String collectionPostalcodes, String collectionZone, String collectionContatperson, String collectionContactno, String collectionCompanyname, String pickupFromDateTime, String pickupToDateTime, String deliveryLocation, String deliveryLatitude, String deliveryLongitude, String deliveryPostalcodes, String deliveryZone, String deliveryFromDateTime, String deliveryToDateTime, String deliveryContatperson, String deliveryCompanyname, String deliveryContactno, String purchaseOrder, String docketNumber, String description, String quantity, String weight, String cubicWeight, String dimension, String measurement, String status, String statusname, String customerName, Object requestedBy, String deliveryName, String pickupDistance, String deliveryDistance, String type, long subjobid) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.collectionLocation = collectionLocation;
        this.collectionLatitude = collectionLatitude;
        this.collectionLongitude = collectionLongitude;
        this.collectionPostalcodes = collectionPostalcodes;
        this.collectionZone = collectionZone;
        this.collectionContatperson = collectionContatperson;
        this.collectionContactno = collectionContactno;
        this.collectionCompanyname = collectionCompanyname;
        this.pickupFromDateTime = pickupFromDateTime;
        this.pickupToDateTime = pickupToDateTime;
        this.deliveryLocation = deliveryLocation;
        this.deliveryLatitude = deliveryLatitude;
        this.deliveryLongitude = deliveryLongitude;
        this.deliveryPostalcodes = deliveryPostalcodes;
        this.deliveryZone = deliveryZone;
        this.deliveryFromDateTime = deliveryFromDateTime;
        this.deliveryToDateTime = deliveryToDateTime;
        this.deliveryContatperson = deliveryContatperson;
        this.deliveryCompanyname = deliveryCompanyname;
        this.deliveryContactno = deliveryContactno;
        this.purchaseOrder = purchaseOrder;
        this.docketNumber = docketNumber;
        this.description = description;
        this.quantity = quantity;
        this.weight = weight;
        this.cubicWeight = cubicWeight;
        this.dimension = dimension;
        this.measurement = measurement;
        this.status = status;
        this.statusname = statusname;
        this.customerName = customerName;
        this.requestedBy = requestedBy;
        this.deliveryName = deliveryName;
        this.pickupDistance = pickupDistance;
        this.deliveryDistance = deliveryDistance;
        this.type = type;
        this.subjobid = subjobid;
    }


    protected AcceptedJob(Parcel in) {
        id = in.readString();
        vehicleId = in.readString();
        collectionLocation = in.readString();
        collectionLatitude = in.readString();
        collectionLongitude = in.readString();
        collectionPostalcodes = in.readString();
        collectionZone = in.readString();
        collectionContatperson = in.readString();
        collectionContactno = in.readString();
        collectionCompanyname = in.readString();
        pickupFromDateTime = in.readString();
        pickupToDateTime = in.readString();
        deliveryLocation = in.readString();
        deliveryLatitude = in.readString();
        deliveryLongitude = in.readString();
        deliveryPostalcodes = in.readString();
        deliveryZone = in.readString();
        deliveryFromDateTime = in.readString();
        deliveryToDateTime = in.readString();
        deliveryContatperson = in.readString();
        deliveryCompanyname = in.readString();
        deliveryContactno = in.readString();
        purchaseOrder = in.readString();
        docketNumber = in.readString();
        description = in.readString();
        quantity = in.readString();
        weight = in.readString();
        cubicWeight = in.readString();
        dimension = in.readString();
        measurement = in.readString();
        status = in.readString();
        statusname = in.readString();
        customerName = in.readString();
        deliveryName = in.readString();
        pickupDistance = in.readString();
        deliveryDistance = in.readString();
        type = in.readString();
        subjobid = in.readLong();
    }

    public static final Creator<AcceptedJob> CREATOR = new Creator<AcceptedJob>() {
        @Override
        public AcceptedJob createFromParcel(Parcel in) {
            return new AcceptedJob(in);
        }

        @Override
        public AcceptedJob[] newArray(int size) {
            return new AcceptedJob[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCollectionLocation() {
        return collectionLocation;
    }

    public void setCollectionLocation(String collectionLocation) {
        this.collectionLocation = collectionLocation;
    }

    public String getCollectionLatitude() {
        return collectionLatitude;
    }

    public void setCollectionLatitude(String collectionLatitude) {
        this.collectionLatitude = collectionLatitude;
    }

    public String getCollectionLongitude() {
        return collectionLongitude;
    }

    public void setCollectionLongitude(String collectionLongitude) {
        this.collectionLongitude = collectionLongitude;
    }

    public String getCollectionPostalcodes() {
        return collectionPostalcodes;
    }

    public void setCollectionPostalcodes(String collectionPostalcodes) {
        this.collectionPostalcodes = collectionPostalcodes;
    }

    public String getCollectionZone() {
        return collectionZone;
    }

    public void setCollectionZone(String collectionZone) {
        this.collectionZone = collectionZone;
    }

    public String getCollectionContatperson() {
        return collectionContatperson;
    }

    public void setCollectionContatperson(String collectionContatperson) {
        this.collectionContatperson = collectionContatperson;
    }

    public String getCollectionContactno() {
        return collectionContactno;
    }

    public void setCollectionContactno(String collectionContactno) {
        this.collectionContactno = collectionContactno;
    }

    public String getCollectionCompanyname() {
        return collectionCompanyname;
    }

    public void setCollectionCompanyname(String collectionCompanyname) {
        this.collectionCompanyname = collectionCompanyname;
    }

    public String getPickupFromDateTime() {
        return pickupFromDateTime;
    }

    public void setPickupFromDateTime(String pickupFromDateTime) {
        this.pickupFromDateTime = pickupFromDateTime;
    }

    public String getPickupToDateTime() {
        return pickupToDateTime;
    }

    public void setPickupToDateTime(String pickupToDateTime) {
        this.pickupToDateTime = pickupToDateTime;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public String getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(String deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public String getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(String deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    public String getDeliveryPostalcodes() {
        return deliveryPostalcodes;
    }

    public void setDeliveryPostalcodes(String deliveryPostalcodes) {
        this.deliveryPostalcodes = deliveryPostalcodes;
    }

    public String getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(String deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public String getDeliveryFromDateTime() {
        return deliveryFromDateTime;
    }

    public void setDeliveryFromDateTime(String deliveryFromDateTime) {
        this.deliveryFromDateTime = deliveryFromDateTime;
    }

    public String getDeliveryToDateTime() {
        return deliveryToDateTime;
    }

    public void setDeliveryToDateTime(String deliveryToDateTime) {
        this.deliveryToDateTime = deliveryToDateTime;
    }

    public String getDeliveryContatperson() {
        return deliveryContatperson;
    }

    public void setDeliveryContatperson(String deliveryContatperson) {
        this.deliveryContatperson = deliveryContatperson;
    }

    public String getDeliveryCompanyname() {
        return deliveryCompanyname;
    }

    public void setDeliveryCompanyname(String deliveryCompanyname) {
        this.deliveryCompanyname = deliveryCompanyname;
    }

    public String getDeliveryContactno() {
        return deliveryContactno;
    }

    public void setDeliveryContactno(String deliveryContactno) {
        this.deliveryContactno = deliveryContactno;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCubicWeight() {
        return cubicWeight;
    }

    public void setCubicWeight(String cubicWeight) {
        this.cubicWeight = cubicWeight;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Object getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Object requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getPickupDistance() {
        return pickupDistance;
    }

    public void setPickupDistance(String pickupDistance) {
        this.pickupDistance = pickupDistance;
    }

    public String getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(String deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSubjobid() {
        return subjobid;
    }

    public void setSubjobid(long subjobid) {
        this.subjobid = subjobid;
    }


    @Override
    public String toString() {
        return "AcceptedJob{" +
                "id='" + id + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", collectionLocation='" + collectionLocation + '\'' +
                ", collectionLatitude='" + collectionLatitude + '\'' +
                ", collectionLongitude='" + collectionLongitude + '\'' +
                ", collectionPostalcodes='" + collectionPostalcodes + '\'' +
                ", collectionZone='" + collectionZone + '\'' +
                ", collectionContatperson='" + collectionContatperson + '\'' +
                ", collectionContactno='" + collectionContactno + '\'' +
                ", collectionCompanyname='" + collectionCompanyname + '\'' +
                ", pickupFromDateTime='" + pickupFromDateTime + '\'' +
                ", pickupToDateTime='" + pickupToDateTime + '\'' +
                ", deliveryLocation='" + deliveryLocation + '\'' +
                ", deliveryLatitude='" + deliveryLatitude + '\'' +
                ", deliveryLongitude='" + deliveryLongitude + '\'' +
                ", deliveryPostalcodes='" + deliveryPostalcodes + '\'' +
                ", deliveryZone='" + deliveryZone + '\'' +
                ", deliveryFromDateTime='" + deliveryFromDateTime + '\'' +
                ", deliveryToDateTime='" + deliveryToDateTime + '\'' +
                ", deliveryContatperson='" + deliveryContatperson + '\'' +
                ", deliveryCompanyname='" + deliveryCompanyname + '\'' +
                ", deliveryContactno='" + deliveryContactno + '\'' +
                ", purchaseOrder='" + purchaseOrder + '\'' +
                ", docketNumber='" + docketNumber + '\'' +
                ", description='" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                ", weight='" + weight + '\'' +
                ", cubicWeight='" + cubicWeight + '\'' +
                ", dimension='" + dimension + '\'' +
                ", measurement='" + measurement + '\'' +
                ", status='" + status + '\'' +
                ", statusname='" + statusname + '\'' +
                ", customerName='" + customerName + '\'' +
                ", requestedBy=" + requestedBy +
                ", deliveryName='" + deliveryName + '\'' +
                ", pickupDistance='" + pickupDistance + '\'' +
                ", deliveryDistance='" + deliveryDistance + '\'' +
                ", type='" + type + '\'' +
                ", subjobid=" + subjobid +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(vehicleId);
        dest.writeString(collectionLocation);
        dest.writeString(collectionLatitude);
        dest.writeString(collectionLongitude);
        dest.writeString(collectionPostalcodes);
        dest.writeString(collectionZone);
        dest.writeString(collectionContatperson);
        dest.writeString(collectionContactno);
        dest.writeString(collectionCompanyname);
        dest.writeString(pickupFromDateTime);
        dest.writeString(pickupToDateTime);
        dest.writeString(deliveryLocation);
        dest.writeString(deliveryLatitude);
        dest.writeString(deliveryLongitude);
        dest.writeString(deliveryPostalcodes);
        dest.writeString(deliveryZone);
        dest.writeString(deliveryFromDateTime);
        dest.writeString(deliveryToDateTime);
        dest.writeString(deliveryContatperson);
        dest.writeString(deliveryCompanyname);
        dest.writeString(deliveryContactno);
        dest.writeString(purchaseOrder);
        dest.writeString(docketNumber);
        dest.writeString(description);
        dest.writeString(quantity);
        dest.writeString(weight);
        dest.writeString(cubicWeight);
        dest.writeString(dimension);
        dest.writeString(measurement);
        dest.writeString(status);
        dest.writeString(statusname);
        dest.writeString(customerName);
        dest.writeString(deliveryName);
        dest.writeString(pickupDistance);
        dest.writeString(deliveryDistance);
        dest.writeString(type);
        dest.writeLong(subjobid);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
