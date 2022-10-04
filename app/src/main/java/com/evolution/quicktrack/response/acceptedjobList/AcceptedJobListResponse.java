package com.evolution.quicktrack.response.acceptedjobList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class AcceptedJobListResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<AcceptedJob> data = null;

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

    public List<AcceptedJob> getData() {
        return data;
    }

    public void setData(List<AcceptedJob> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("message", message).append("data", data).toString();
    }

}
