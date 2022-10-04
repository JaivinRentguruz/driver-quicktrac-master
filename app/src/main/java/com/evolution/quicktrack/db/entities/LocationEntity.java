package com.evolution.quicktrack.db.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class LocationEntity {

    @ColumnInfo(name = "latitude")
    public double latitude ;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "driver_id")
    public String driverID;
}
