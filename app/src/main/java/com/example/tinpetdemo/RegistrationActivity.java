package com.example.tinpetdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class RegistrationActivity extends AppCompatActivity {
    String Appid = "application-0-erjbs";
    EditText emailedt2, passwordedt2;
    TextView logingTextview;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerBtn = (Button) findViewById(R.id.registerBtn1);
        emailedt2 = (EditText) findViewById(R.id.emailedt2);
        passwordedt2 = (EditText) findViewById(R.id.passwordedt2);
        logingTextview = (TextView) findViewById(R.id.loginTextview);

        Realm.init(getApplicationContext());

        App app = new App(new AppConfiguration.Builder(Appid).build());

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Credentials credentials = Credentials.emailPassword(emailedt2.getText().toString(), passwordedt2.getText().toString());
                app.getEmailPassword().registerUserAsync(emailedt2.getText().toString(), passwordedt2.getText().toString(), it->{
                    if(it.isSuccess())
                    {
                        Log.v("User", "Register with email successfully");
                        Toast.makeText(RegistrationActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        Log.v("User", "Register Failed");
                        Toast.makeText(RegistrationActivity.this, "Register failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        logingTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}