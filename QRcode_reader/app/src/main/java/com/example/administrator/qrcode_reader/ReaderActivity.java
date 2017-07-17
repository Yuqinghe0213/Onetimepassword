package com.example.administrator.qrcode_reader;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ReaderActivity extends AppCompatActivity {

    Button loginbtn, register;
    EditText username, password;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        result = (TextView)findViewById(R.id.result);
    }

    public void register(View view){
        if(username.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please input username", Toast.LENGTH_LONG).show();
        }
        else if(password.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please input password", Toast.LENGTH_LONG).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReaderActivity.this);

            builder.setMessage("Do your want to create a new account?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharePre = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharePre.edit();
                            edit.putString("username", username.getText().toString());
                            edit.putString("password", password.getText().toString());
                            edit.apply();
                            Toast.makeText(ReaderActivity.this, "New user created", Toast.LENGTH_LONG).show();
                            Intent intentmsg1 = new Intent(ReaderActivity.this, mobileId.class);
                            intentmsg1.putExtra("username", username.getText().toString());
                            startActivity(intentmsg1);
                        }
                    })
                    .setNegativeButton("No",null);
            AlertDialog alert = builder.create();
            alert.show();

        }

    }

    public void loginact(View view) {
        SharedPreferences sharePre = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = sharePre.getString("username", "");
        String passwordstor = sharePre.getString("password", "");
        if(username.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please input username", Toast.LENGTH_LONG).show();
        }
        else if(password.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please input password", Toast.LENGTH_LONG).show();
        }
        else if(name.equals(username.getText().toString()) && passwordstor.equals(password.getText().toString()) ) {
            Toast.makeText(this, "Login succeed", Toast.LENGTH_LONG).show();
            Intent intentmsg2 = new Intent(this, Challnbscan.class);
            intentmsg2.putExtra("username", name);
            startActivity(intentmsg2);
        }
        else{
            Toast.makeText(this, "username or password wrong", Toast.LENGTH_LONG).show();
        }
    }
}
