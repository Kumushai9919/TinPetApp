package com.example.tinpetdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import io.realm.mongodb.User;

public class MainActivity extends AppCompatActivity {
    String Appid = "application-0-erjbs";
    Button loginBtn;
    EditText emailedt, passwordedt;
    TextView newaccTview;
    User user;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtn1);
        emailedt = (EditText) findViewById(R.id.emailedt);
        passwordedt = (EditText) findViewById(R.id.passwordedt);
        newaccTview = (TextView) findViewById(R.id.newaccTview);
        sharedPreferences = getSharedPreferences("currentUser", Context.MODE_PRIVATE);

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        // 사용자 아이디 있는 경우
        if (sharedPreferences.contains("userId")) {
            Intent intent = new Intent(MainActivity.this, PageActivity.class);
            startActivity(intent);
        }


        //Realm Init and login
        Realm.init(getApplicationContext()); //1.Realm database init
        App app = new App(new AppConfiguration.Builder(Appid).build()); //2.create Realm instance with Realm Appid
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Credentials credentials = Credentials.emailPassword(emailedt.getText().toString(), passwordedt.getText().toString());
                app.loginAsync(credentials, new App.Callback<User>() {      //3. we're initializing the app and login
                    @Override
                    public void onResult(App.Result<User> result) {
                        if (result.isSuccess()) {
                            user = app.currentUser();
                            Log.v("User", "Logged " + user.getId());

                            // Shared Preferences 이용하여 사용자의 계정 ID를 저장한다.
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", user.getId().toString());
                            editor.apply();
                            Toast.makeText(MainActivity.this, "Logged", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, PageActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.v("User", "Failed" + result.getError().toString());
                            Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //Register
        newaccTview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }
}