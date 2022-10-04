package com.evolution.quicktrack.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.LogCustom;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class Utils {


    public static boolean isInternetOn(Context mContext) {
        if (mContext != null) {
            NetworkInfo activeNetwork = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (activeNetwork == null) {
                return false;
            }
            if (activeNetwork.getType() == 1 || activeNetwork.getType() == 0) {
                return true;
            }
        }
        return false;
    }







    public static String formatDate(Date dateToFormat) {
        String DATE_TIME_FORMAT_DATE = "dd MMMM, yyyy";
        return new SimpleDateFormat("dd MMMM, yyyy").format(dateToFormat);
    }





    public static void hideKey(Activity context) {
        context.getWindow().setSoftInputMode(3);
    }

    public static void hideKeyview(Activity context, View view) {
        try {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validInternet(Context context) {
        if (isInternetOn(context)) {
            return true;
        }
        LogCustom.Toast(context, context.getResources().getString(R.string.toast_nointernet));
        return false;
    }
    public static String getFileToByte(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }






    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";



    /* * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     *//**//**/

    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }


   /*  * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     *//**//*

    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    */
/**
     * Returns the {@code location} object as a human readable string.
     * @param  @link Location}.
     *//*

    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }


*/



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                   /* AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission Required");
                    alertBuilder.setMessage("Location is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                        }
                    });
                     AlertDialog alert = alertBuilder.create();
                    alert.show();
*/
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            EasyPermissions.requestPermissions((Activity) context, "Please Allow All Permissions",100, perms);


                } else {

                    LogCustom.logd("xxxxxx","permission 1::else");

                    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
                    EasyPermissions.requestPermissions((Activity) context, "Please Allow All Permissions",100, perms);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    final static String KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested";
    final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";
    final static String CHANNEL_ID = "channel_01";

    public static void setRequestingLocationUpdates(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
                .apply();
    }

    public   static boolean getRequestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false);
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    public  static void sendNotification(Context context, String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
      /*  Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.putExtra("from_notification", true);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle("Location update")
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);

            // Channel ID
            builder.setChannelId(CHANNEL_ID);
        }

        // Issue the notification
        mNotificationManager.notify(0, builder.build());*/
    }


    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     *
     * @param context The {@link Context}.
     */
    public  static String getLocationResultTitle(Context context, List<Location> locations) {
        String numLocationsReported = context.getResources().getQuantityString(
                R.plurals.num_locations_reported, locations.size(), locations.size());
        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(new Date());
    }

    /**
     * Returns te text for reporting about a list of  {@link Location} objects.
     *
     * @param locations List of {@link Location}s.
     */
    private static String getLocationResultText(Context context, List<Location> locations) {
        if (locations.isEmpty()) {
            return context.getString(R.string.unknown_location);
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : locations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

    public  static void setLocationUpdatesResult(Context context, List<Location> locations) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle(context, locations)
                        + "\n" + getLocationResultText(context, locations))
                .apply();
    }

    public  static String getLocationUpdatesResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }




    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPhoneState(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_PHONE_STATE)) {


                      String[] perms = {Manifest.permission.READ_PHONE_STATE};
                      EasyPermissions.requestPermissions((Activity) context, "Please Allow All Permissions",101, perms);
                } else {

                    LogCustom.logd("xxxxxx","permission 1::else");

                    String[] perms = {Manifest.permission.READ_PHONE_STATE};
                    EasyPermissions.requestPermissions((Activity) context, "Please Allow All Permissions",101, perms);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }




    public static long getdatedifrnce(String crantdate,String olddate){
        Date startDate =formatStringToDate(olddate); // Set start date
         Date endDate   =formatStringToDate(crantdate); // Set end date

        long duration  = endDate.getTime() - startDate.getTime();

        return duration;
    }

    public static Date formatStringToDate(String formatString){

        String DATE_FOR_DATABSE = "yyyy-MM-dd hh:mm:ss";
        DateFormat df = new SimpleDateFormat(DATE_FOR_DATABSE);
        Date startDate=null;
        try {
            startDate = df.parse(formatString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }





    public static int getRandomMaterialColor(Context context) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_400" , "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }



    public  static String printFirstCharacter(String s)
    {
        String[] words = s.split("\\W+");

        if(words.length>0){
            return String.valueOf(words[0].charAt(0));
        }else {
            return String.valueOf(words[0].charAt(0)+words[1].charAt(0));
        }

    }




    public  static void setNotificationResult(Context context, String id) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("NOTIFICATION", id)

                .apply();
    }

    public  static String getNotificationId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("NOTIFICATION", "");
    }






    public static int distance(double lat1, double lon1, double lat2, double lon2) {
        Log.d("xxxxxxxx calu :",lat1+":"+lon1+":"+lat2+":"+lon2);


        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist / 0.621371;

        int dd= (int) dist;
        Log.d("xxxxxxxx calu :",":"+dist);
        return (dd); // in km
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }





}
