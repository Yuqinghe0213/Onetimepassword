package com.example.administrator.qrcode_reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

public class mobileId extends AppCompatActivity {
    TextView txtnm, immeicode;
    ImageView image;
    String aes_key;
    String hashcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_id);
        txtnm = (TextView) findViewById(R.id.txtnm);
        image = (ImageView) findViewById(R.id.image);
        immeicode = (TextView) findViewById(R.id.immeicode);
        Intent intentrecv = getIntent();
        String name = intentrecv.getStringExtra("username");
        txtnm.setText("Hello, user " + name +", Please scan QR code by terminal");
        TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        this.hashcode = GenPassword.md5(imei);
        immeicode.setText(hashcode);

        try {
            aes_key = getRandomString();
            String dataMI = AESUtil.encrypt(hashcode , aes_key);
            RSAPublicKey pk = getPublicKeyFromAsset();
            String keyMI = RSAUtil.encryptByPublicKey(aes_key,pk);
            String msg = keyMI + " " + dataMI;
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(msg, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    public static String getRandomString() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    private RSAPublicKey getPublicKeyFromAsset(){
        try{
            InputStream in = getAssets().open("publicKey.key");
            File targetFile = new File(getFilesDir(), "pk");
            FileOutputStream out = new FileOutputStream(targetFile);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            in.close();
            out.write(bytes);
            out.flush();
            out.close();
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(targetFile));
            Object key = oin.readObject();
            oin.close();
            return (RSAPublicKey) key;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else{
                String context = result.getContents();
                String idcode = AESUtil.decrypt(context,aes_key);
                String[] contexts = idcode.split("/");
                if(contexts.length < 2)
                {
                    new AlertDialog.Builder(mobileId.this).setTitle("Warning")
                            .setMessage("The code is wrong, please scan again")
                            .setPositiveButton("OK", null)
                            .show();
                }
                else{
                    if (!contexts[1].equals(hashcode)) {
                        new AlertDialog.Builder(mobileId.this).setTitle("Warning")
                                .setMessage("The Data has been changed")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        String seed = contexts[0];
                        immeicode.setText(seed);
                        SharedPreferences shPre = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = shPre.edit();
                        edit.putString("seed", seed);
                        edit.apply();
                    }
                }

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    final Activity activity = this;
    public void scancode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }
}
