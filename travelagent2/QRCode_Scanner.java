package com.example.oscar.travelagent2;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Calendar;

public class QRCode_Scanner extends AppCompatActivity {


    private SurfaceView mycameraview;
    private boolean unkownSource = false;
    private boolean isURL = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode__scanner);
        final BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();
        final CameraSource cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();
        mycameraview = (SurfaceView) findViewById(R.id.camera_view);
        mycameraview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    checkLocationPermission();
                    cameraSource.start(mycameraview.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    String word = barcodes.valueAt(0).displayValue;
                    if(checkStringOK(word)) {
                        if(isURL){
                            Uri uri = Uri.parse(barcodes.valueAt(0).displayValue); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }else{
                            Intent go_back_mainsearch = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("place", word);
                            go_back_mainsearch.putExtras(bundle);
                            go_back_mainsearch.setClass(QRCode_Scanner.this, SearchMainPage.class);
                            startActivity(go_back_mainsearch);
                        }
                    }else{
                        unkownSource = true;
                    }
                }
            }
        });
        if(isUnkownSource()){
            Toast.makeText(QRCode_Scanner.this, "此非符合QR_Code!", Toast.LENGTH_LONG).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkLocationPermission() {
        // 如果使用者的 Android 版本低於 6.0 ，直接回傳 True (在安裝時已授權)
        int api_version = Build.VERSION.SDK_INT;    //API版本
        String android_version = Build.VERSION.RELEASE;    //Android版本
        if(api_version < Build.VERSION_CODES.M && !android_version.matches("(6)\\..+")) return true;

        return (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean checkStringOK(String str){
        boolean pass = true;
        boolean isNumber = true;
        int num=0;
        String[] words = str.split("|");
        String web_type1="",web_type2="";

        for(String word:words){
            try{
                System.out.println(Integer.parseInt(word));
            } catch(NumberFormatException e){
               isNumber = false;
            }
            num++;
        }

        if(num>7){
            web_type1 = words[0]+words[1]+words[2]+words[3]+words[4]+words[5]+words[6];
            web_type2 = words[0]+words[1]+words[2]+words[3]+words[4]+words[5]+words[6]+words[7];
        }

        if(web_type1.equals("http://") || web_type2.equals("https://")){
            isURL = true;
            pass = true;
        }else if(isNumber){
            pass = false;
        }else if(num>=10){
            pass = false;
        }
        return pass;
    }
    private boolean isUnkownSource(){
        return unkownSource;
    }
}
