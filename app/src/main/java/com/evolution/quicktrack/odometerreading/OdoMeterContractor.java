package com.evolution.quicktrack.odometerreading;

public interface OdoMeterContractor {
    interface View{
        void onCheckOdoMeterSuccess(OdoMeterCheckRequestEntity odoMeterCheckRequestEntity,String message);
        void onCheckOdoMeterError(String message);

        void addDriverToVehicleSuccess(String message);
        void addDriverToVehicleError(String message);
    }
    interface Presenter{
        void checkOdoMeter(OdoMeterCheckRequestEntity odoMeterCheckRequestEntity);
        void addDriverToVehicle(OdoMeterCheckRequestEntity odoMeterCheckRequestEntity);
    }
}
