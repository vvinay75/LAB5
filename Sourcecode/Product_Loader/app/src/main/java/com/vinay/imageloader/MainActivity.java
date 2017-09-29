package com.vinay.imageloader;
//Coded By vinay @@@REF:vinay
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vinay.imageloader.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Coded By @vinay Ref::Vinay.c
public class MainActivity extends AppCompatActivity  {
    final String bestBuyKey="w6xsra7wfxbfmk8d2jykg7m8";
    public static final int REQUEST_CAMERA = 100;
    public static final String ALLOW= "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cap=(Button) findViewById(R.id.cap);
        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = { "Capture Image", "Load From Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Capture Image")) {
                                Camera_open();
                        } else if (items[item].equals("Load From Gallery")) {
                                Gallery_open();
                        } else{
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }


    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startInstalledAppDetailsActivity(MainActivity.this);
                    }
                });

        alertDialog.show();
    }
    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
      private void Gallery_open(){
           Intent intent = new Intent();
           intent.setType("image/*");//It shows only images
           intent.setAction(Intent.ACTION_GET_CONTENT);
           startActivityForResult(Intent.createChooser(intent, "Please Select Picture"), 1);
}
    private void Camera_open() {
            // coded by vinay @@@code From Scartch Code Ref: vinay
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (getFromPref(this, ALLOW)) {
                    showSettingsAlert();
                } else if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {
                        showAlert();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA);
                    }
            } else {
                Camera_open();
            }

        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,0);
    }
    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView view = (ImageView) findViewById(R.id.sel_image);
        Bitmap bmp =null;

        if(requestCode==0&& resultCode == RESULT_OK) {
            bmp = (Bitmap) data.getExtras().get("data");
        }
        if (requestCode == 1&& resultCode == RESULT_OK ) {
            Uri uri = data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            view.setImageBitmap(bmp);
            final ClarifaiClient client = new ClarifaiBuilder("fef55f965f114d6186477b44b3cfa485").buildSync();
           final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

new AsyncTask<byte[] , Object, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
    @Override
    protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(byte[]... params) {

        ClarifaiResponse<List<ClarifaiOutput<Concept>>> models =client.getDefaultModels().generalModel().predict()
                .withInputs(ClarifaiInput.forImage(params[0]))
                .executeSync();
//coded By Vinay @@@ REF:vinay
           return models;
    }
    @Override
    protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> feed) {

        List<ClarifaiOutput<Concept>> listResponse=feed.get();
        for(ClarifaiOutput<Concept> item:listResponse){
List<Concept>  cc=item.data();
            Collections.sort(cc, new Comparator<Concept>(){
                public int compare(Concept o1, Concept o2){
                    if((o1.value() - o2.value())>0){
                        return -1;
                    }else{
                        return 1;
                    }
                }
            });
            showResult(cc);
            TextView Proname = (TextView)findViewById(R.id.productName);
            Proname.setText(cc.get(0).name().toUpperCase());
           searchResults("4807511","64109",bestBuyKey);


        }
    }

}.execute(outStream.toByteArray());

    }

    private void showResult(List<Concept>  concept ){
        CustomDialogClass customDialog=new CustomDialogClass(this,concept);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.CYAN));
        customDialog.show();
    }
private List<Store> searchResults(String sku,String pincode,String Key){


    Client_Interface Api_ser=ClientApi.getClient().create(Client_Interface.class);
    Call<Store_Response> call = Api_ser.getResponse(sku,pincode,bestBuyKey);
    call.enqueue(new Callback<Store_Response>() {
        @Override
        public void onResponse(Call<Store_Response>call, Response<Store_Response> response) {
            List<Store> stores = response.body().getStores();
            showStores(stores,getApplicationContext());
        }
        @Override
        public void onFailure(Call<Store_Response>call, Throwable t) {
            // Write For Error

        }
    });
    return null;
}
public  void showStores(List<Store> stores,Context context){
    CustomDialogSearchClass customDialogClass=new CustomDialogSearchClass(this,stores);
    customDialogClass.show();
}
}