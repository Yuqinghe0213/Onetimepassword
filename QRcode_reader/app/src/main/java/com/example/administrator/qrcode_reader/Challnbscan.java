package com.example.administrator.qrcode_reader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Challnbscan extends AppCompatActivity {

    EditText challenge,crtpassord;
    ImageView image;
    final Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challnbscan);
        challenge = (EditText) findViewById(R.id.challenge);
        crtpassord = (EditText) findViewById(R.id.crtpassord);
        image = (ImageView) findViewById(R.id.image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else{
                try {
                    String context = result.getContents();
                    challenge.setText(context);
                    SharedPreferences sharePre = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String seed = sharePre.getString("seed", "");
                    String password = GenPassword.getTOTP(context, seed);
                    crtpassord.setText(password);
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    BitMatrix bitMatrix = multiFormatWriter.encode(crtpassord.getText().toString(), BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void scanstring(View view) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public void logoutact(View view) {
        Intent intentmsg1 = new Intent(this, ReaderActivity.class);
        startActivity(intentmsg1);
    }
}
