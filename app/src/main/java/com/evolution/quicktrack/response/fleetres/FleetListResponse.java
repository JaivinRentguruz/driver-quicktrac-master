package com.evolution.quicktrack.response.fleetres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class FleetListResponse {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<FleetResponse> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FleetResponse> getData() {
        return data;
    }

    public void setData(List<FleetResponse> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("message", message).append("data", data).toString();
    }
}
