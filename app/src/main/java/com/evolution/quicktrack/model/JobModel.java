package com.evolution.quicktrack.model;

/**
 * Created by user on 7/27/2018.
 */

public class JobModel  {



    private   long id;
    private   String txtClientName;
    private   String txtPickupAddres;
    private   String txtDeliveryAddres;
    private   String discription;
    private   double distance;
    private  String sorting;
    private boolean ispickup;
    private   String status;
    private boolean isEnable;
    private boolean isChecked;
    private String deliveryName;
    private long subjobId;
    private String type;

    public String getCollectionCompany() {
        return collectionCompany;
    }

    public void setCollectionCompany(String collectionCompany) {
        this.collectionCompany = collectionCompany;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    private String collectionCompany;
    private String deliveryCompany;


    public JobModel() {
    }

    public JobModel(long id, String txtClientName, String txtPickupAddres, String txtDeliveryAddres, String discription, double distance, String sorting, boolean ispickup, String status, boolean isEnable, boolean isChecked, String deliveryName, long subjobId,String type) {
        this.id = id;
        this.txtClientName = txtClientName;
        this.txtPickupAddres = txtPickupAddres;
        this.txtDeliveryAddres = txtDeliveryAddres;
        this.discription = discription;
        this.distance = distance;
        this.sorting = sorting;
        this.ispickup = ispickup;
        this.status = status;
        this.isEnable = isEnable;
        this.isChecked = isChecked;
        this.deliveryName = deliveryName;
        this.subjobId = subjobId;
        this.type = type;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isIspickup() {
        return ispickup;
    }

    public void setIspickup(boolean ispickup) {
        this.ispickup = ispickup;
    }



    public String getTxtClientName() {
        return txtClientName;
    }

    public void setTxtClientName(String txtClientName) {
        this.txtClientName = txtClientName;
    }

    public String getTxtPickupAddres() {
        return txtPickupAddres;
    }

    public void setTxtPickupAddres(String txtPickupAddres) {
        this.txtPickupAddres = txtPickupAddres;
    }

    public String getTxtDeliveryAddres() {
        return txtDeliveryAddres;
    }

    public void setTxtDeliveryAddres(String txtDeliveryAddres) {
        this.txtDeliveryAddres = txtDeliveryAddres;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSubjobId() {
        return subjobId;
    }

    public void setSubjobId(long subjobId) {
        this.subjobId = subjobId;
    }
}
