package com.example.pochedex;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class QR_Scan extends AppCompatActivity{

    private static final int PERMISSION_REQUEST_CAMERA = 0;

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private Button qrCodeFoundButton;
    private String qrCode;
    Boolean repeated;
    private static final String FILE_NAME = "poke_list.dat";
    FileOutputStream fileOutputStream;

    //Instance shared resources
    sharedRes shared = new sharedRes();
    //Image list
    Map<String, Integer> images = shared.getImages();
    //Sounds list
    Map<String, Integer> sounds = shared.getSounds();
    //Sound player
    MediaPlayer player;
    //Pochemon instance
    com.example.pochedex.pochemon capturedPochemon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        Context context = getApplicationContext();
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND );
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        previewView = findViewById(R.id.activity_main_previewView);

        qrCodeFoundButton = findViewById(R.id.activity_main_qrCodeFoundButton);
        qrCodeFoundButton.setVisibility(View.INVISIBLE);
        qrCodeFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQR_Info(context);
            }
        });
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        requestCamera();
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(QR_Scan.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCode = _qrCode;
                qrCodeFoundButton.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 1000);
            }

            @Override
            public void qrCodeNotFound() {
                qrCodeFoundButton.setVisibility(View.INVISIBLE);
            }
        }));

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageAnalysis, preview);
    }


    public void openQR_Info(Context context){
        Integer pochemon_number = Integer.parseInt(qrCode);
        List<Integer> pocheList = shared.getPocheList(context);
        repeated = false;
        if (pocheList.size()!=0){
            for ( int i = 0; i < pocheList.size(); i++ ) {
                if (pocheList.get(i).equals(pochemon_number)){
                    repeated = true;
                }
            }
        }
        if (repeated){
            Toast.makeText(getApplicationContext(), "You already have this Pokemon", Toast.LENGTH_SHORT).show();
        }else {
            Log.i(MainActivity.class.getSimpleName(), "Pochemon Found: " + qrCode);
            capturedPochemon = getPochemon(context, pochemon_number);
            writePochemon();
            PochemonDialog(context, capturedPochemon);

        }
    }
    private pochemon getPochemon(Context context, int poche_number) {
        //Get pochedex json file
        String data = shared.getPochedex(context);

        if (!data.isEmpty()) {
            try {
                //Read pokemon database
                JSONObject json = new JSONObject(data);
                JSONArray pochemon = json.getJSONArray("pochemon");
                for (int i = 0; i < pochemon.length(); i++) {
                    JSONObject pocheData = pochemon.getJSONObject(i);

                    //Get pokemon number
                    String strNum = pocheData.getString("num");
                    int number = Integer.parseInt(strNum);

                    if(number == poche_number){
                        //Get pokemon attributes
                        String name = pocheData.getString("name");
                        String size = pocheData.getString("size");
                        String weight = pocheData.getString("weight");
                        String desc = pocheData.getString("description");
                        JSONArray attributes = pocheData.getJSONArray("attrib");
                        JSONArray weaknesses = pocheData.getJSONArray("weak");
                        JSONArray evolutions = pocheData.getJSONArray("evo");
                        //Log.d("type", String.valueOf(attributes.get(0)));

                        //Return pokemon object
                        return new pochemon(number, name, size, weight, desc,
                                attributes, weaknesses, evolutions, images.get(name), sounds.get(name));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //In case of no coincidence a empty pochemon is returned
        return new pochemon(0, null, null, null, null,
                null, null, null, 0, 0);
    }
    private void writePochemon() {
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE | MODE_APPEND);
            fileOutputStream.write((qrCode+"\r\n").getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void PochemonDialog(Context context, pochemon pochemon) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pochemon_dialog);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.pokedex_background);
        Button listDex = (Button)dialog.findViewById(R.id.btn_dex);
        Button stats = (Button)dialog.findViewById(R.id.btn_stats);
        ImageView btnClose = (ImageView)dialog.findViewById(R.id.btn_close);
        TextView pocheName = (TextView)dialog.findViewById(R.id.pocheName);


        ImageView image = (ImageView) dialog.findViewById(R.id.poche_img_dialog);
        image.setImageResource(images.get(pochemon.getName()));
        pocheName.setText(pochemon.getName());

        stats.setOnClickListener(view -> {
            Intent data_activity = new Intent(QR_Scan.this, ShowData.class);
            data_activity.putExtra("num", qrCode);
            startActivity(data_activity);
        });
        listDex.setOnClickListener(view -> {
            Intent main_activity = new Intent(QR_Scan.this, MainActivity.class);
            startActivity(main_activity);
        });
        btnClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

    }
}