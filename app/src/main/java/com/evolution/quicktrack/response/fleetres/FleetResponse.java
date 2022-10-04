package com.evolution.quicktrack.response.fleetres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FleetResponse  {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("fleet_name")
    @Expose
    private String fleet_name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("fleet_latitude")
    @Expose
    private String fleet_latitude;

    @SerializedName("fleet_longitude")
    @Expose
    private String fleet_longitude;

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("contact_no")
    @Expose
    private String contact_no;

    @SerializedName("suburb")
    @Expose
    private String suburb;

    @SerializedName("postal_code")
    @Expose
    private String postal_code;

    @SerializedName("zone_name")
    @Expose
    private String zone_name;

    private  boolean isSelected = false;

    public FleetResponse() {
    }

    public FleetResponse(int id, String fleet_name, String address, String fleet_latitude, String fleet_longitude, String company_name, String contact_no, String suburb, String postal_code, String zone_name, boolean isSelected) {
        this.id = id;
        this.fleet_name = fleet_name;
        this.address = address;
        this.fleet_latitude = fleet_latitude;
        this.fleet_longitude = fleet_longitude;
        this.company_name = company_name;
        this.contact_no = contact_no;
        this.suburb = suburb;
        this.postal_code = postal_code;
        this.zone_name = zone_name;
        this.isSelected = isSelected;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFleet_name() {
        return fleet_name;
    }

    public void setFleet_name(String fleet_name) {
        this.fleet_name = fleet_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFleet_latitude() {
        return fleet_latitude;
    }

    public void setFleet_latitude(String fleet_latitude) {
        this.fleet_latitude = fleet_latitude;
    }

    public String getFleet_longitude() {
        return fleet_longitude;
    }

    public void setFleet_longitude(String fleet_longitude) {
        this.fleet_longitude = fleet_longitude;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "FleetResponse{" +
                "id=" + id +
                ", fleet_name='" + fleet_name + '\'' +
                ", address='" + address + '\'' +
                ", fleet_latitude='" + fleet_latitude + '\'' +
                ", fleet_longitude='" + fleet_longitude + '\'' +
                ", company_name='" + company_name + '\'' +
                ", contact_no='" + contact_no + '\'' +
                ", suburb='" + suburb + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", zone_name='" + zone_name + '\'' +
                '}';
    }
}
