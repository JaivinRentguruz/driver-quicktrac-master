package com.evolution.quicktrack.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;

public class DownloadActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        String version = getIntent().getExtras().getString("version");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("New version of this app available. Do you want to update?")
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UpdateApp  atualizaApp = new UpdateApp(version);
                        atualizaApp.setContext(DownloadActivity.this);
                        atualizaApp.execute(Constants.DOWNLOAD_URL+version+".apk");
                    }
                });
                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        DownloadActivity.this.finish();
                    }
                });*/

        AlertDialog alert = builder.create();
        alert.setTitle("New Version");
        alert.show();
    }





    private boolean writeResponseBodyToDisk(ResponseBody body,String apkName) {
        try {
            File storageDir =
                    getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath());
            File futureStudioIconFile = File.createTempFile(
                    apkName,  /* prefix */
                    ".apk",         /* suffix */
                    storageDir      /* directory */
            );

            //File futureStudioIconFile = new File(Environment.getExternalStorageDirectory() + File.separator + apkName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("Download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.quicktrac.fileprovider", futureStudioIconFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private ProgressDialog mProgressDialog;
    public class UpdateApp extends AsyncTask<String, String, Void> {

        private String versionName;
        private File file;
        UpdateApp(String versionName){
            this.versionName = versionName;
        }

        private Context context;

        public void setContext(Context contextf) {
            context = contextf;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                int lenghtOfFile = c.getContentLength();
                // String PATH = "/mnt/sdcard/Download/";

                File storageDir =
                        getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath());
                file = File.createTempFile(
                        versionName,  /* prefix */
                        ".apk",         /* suffix */
                        storageDir      /* directory */
                );
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream fos = new FileOutputStream(file);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1; //total = total + len1
                    publishProgress(""+ (int)((total*100)/lenghtOfFile));
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.quicktrac.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
            DownloadActivity.this.finish();
        }


        //our progress bar settings

        private void showProgressBar(){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Downloading file…");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }
    }
}
