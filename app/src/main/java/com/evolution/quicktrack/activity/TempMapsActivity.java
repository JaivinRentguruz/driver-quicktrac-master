package com.evolution.quicktrack.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.location.GPSTracker;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiCilentDistanceCalculator;
import com.evolution.quicktrack.util.JSONParserTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TempMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    double currentdestination = 0;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    GPSTracker gpsTracker;
    GetLocation getLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);

        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocation = new GetLocation(TempMapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        buildGoogleApiClient();


        getLocation = new GetLocation(TempMapsActivity.this);

        LatLng source = new LatLng(getLocation.getcurrntLataLong().getLatitude(),getLocation.getcurrntLataLong().getLongitude());
        //LatLng desti = new LatLng(Double.parseDouble(destinationLatitude),Double.parseDouble(destinationLongitude));
        //LatLng desti = new LatLng(22.288633,70.771360);
        LatLng desti = new LatLng(22.267973, 70.782341);

        //mGoogleMap.addMarker(new MarkerOptions().position(source).title("PickUpPoint"));
        mMap.addMarker(new MarkerOptions().position(desti).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Destination Point"));
        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(source));

        UpdateMap();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    void UpdateMap()
    {
        LatLng source = new LatLng(getLocation.getcurrntLataLong().getLatitude(), getLocation.getcurrntLataLong().getLongitude());
        ///LatLng desti = new LatLng(22.288633,70.771360);
        LatLng desti = new LatLng(22.267973, 70.782341);
        String str_origin = "origin=" + source.latitude + "," + source.longitude;
        String str_dest = "destination=" + desti.latitude + "," + desti.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        FetchUrl FetchUrl = new FetchUrl();
        FetchUrl.execute(url);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 16));
        /*try {
            Toast.makeText(this, "LAt : "+String.valueOf(getLocation.getcurrntLataLong().getLatitude()) + "\n Long : "+String.valueOf(getLocation.getcurrntLataLong().getLongitude()), Toast.LENGTH_SHORT).show();
            getDistance(String.valueOf(getLocation.getcurrntLataLong().getLatitude()),String.valueOf(getLocation.getcurrntLataLong().getLongitude()),String.valueOf(desti.latitude),String.valueOf(desti.longitude));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        mMap.setMyLocationEnabled(true);
    }

    private double getDistance(final String currentLat,final String currentLong, final String disLat,final String disLong) throws Exception {

        ApiCallInterface apiService = ApiCilentDistanceCalculator.getClient().create(ApiCallInterface.class);

        String Origins = currentLat + "," + currentLong;
        String Destination = disLat + "," + disLong;

        Toast.makeText(this, "origins : "+Origins, Toast.LENGTH_SHORT).show();

        Call<JsonElement> call = apiService.getDistance(Origins, Destination);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (QuickTrackAplication.IsNotNull(response.body())) {
                    try {


                        JsonObject jsonObject = response.body().getAsJsonObject();
                        ArrayList<Object> rows = new ArrayList<Object>();

                        JsonArray rowsData = jsonObject.getAsJsonArray("rows");
                        JsonObject jsonObject1 = rowsData.get(0).getAsJsonObject();
                        JsonArray jsonArray = jsonObject1.getAsJsonArray("elements");
                        JsonObject jsonObject2 = jsonArray.get(0).getAsJsonObject();
                        JsonObject jsonObject3 = jsonObject2.getAsJsonObject("distance");
                       /* JsonArray elements = rowsData.get(0).getAsJsonArray();
                        JsonObject distanceObject = elements.get(0).getAsJsonObject();*/
                        currentdestination  = Double.parseDouble(jsonObject3.get("value").toString());
                        Toast.makeText(TempMapsActivity.this, ""+currentdestination, Toast.LENGTH_SHORT).show();

                        double matchdesti = 500;
                        try {
                            if(currentdestination < matchdesti)
                            {
                                Toast.makeText(TempMapsActivity.this, "E Hali Gyu", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(TempMapsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(PickupLocationActivity.this, "cur"+currentdestination, Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(TempMapsActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

        return  currentdestination;
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng desti = new LatLng(22.267973, 70.782341);
        try {
            getDistance(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),String.valueOf(desti.latitude),String.valueOf(desti.longitude));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onConnected( Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(20000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        //liveLocation  = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);



    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    ///////////Location Line draw /////////////////////////////////

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result----",result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                JSONParserTask parser = new JSONParserTask();
                Log.d("ParserTask", parser.toString());
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
}
