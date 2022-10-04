package com.evolution.quicktrack.odometerreading;

public class OdoMeterCheckRequestEntity {
    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOdoMeter() {
        return odoMeter;
    }

    public void setOdoMeter(String odoMeter) {
        this.odoMeter = odoMeter;
    }

    private String loginToken;
    private String apiKey;
    private String truckId;
    private String driverId;
    private String odoMeter;
}
