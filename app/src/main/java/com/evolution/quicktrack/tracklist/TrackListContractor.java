package com.evolution.quicktrack.tracklist;

import com.evolution.quicktrack.response.vehicle.VehicleResponse;

public interface TrackListContractor {

    interface View{
        void onGetVehicleSuccess(VehicleResponse vehicleResponse);
        void onGetVehicleError(String message);
    }
    interface Presenter{

        void getVehicle(TrackListRequestEntity trackListRequestEntity);

    }
}
