package com.example.tinpetdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.bson.Document;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class PetinfoActivity extends AppCompatActivity {
    String Appid = "application-0-erjbs";
    App app;
    Button back;
    Credentials credentials;
    ImageView petImageView;
    LinearLayout layoutCat;
    TextView petNameTV, petAgeTV, petTypeTV, ownersNameTV, petGenderTV, phoneNumberTV;
    LottieAnimationView animationView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petinfo);

        // loading animation
        animationView = findViewById(R.id.animation_cat);
        layoutCat = findViewById(R.id.layoutCat);
        animationView.setVisibility(View.VISIBLE);
        layoutCat.setVisibility(View.VISIBLE);
        animationView.playAnimation();


        petNameTV = (TextView) findViewById(R.id.petNameTV);
        petAgeTV = (TextView) findViewById(R.id.petAgeTV);
        petTypeTV = (TextView) findViewById(R.id.petTypeTV);
        petImageView = (ImageView) findViewById(R.id.petImgView);
        ownersNameTV = (TextView) findViewById(R.id.ownersName);
        petGenderTV = (TextView) findViewById(R.id.petGenderTV);
        phoneNumberTV = (TextView) findViewById(R.id.petPhoneNumberTV);
        back = (Button) findViewById(R.id.backBtn);
        app = new App(new AppConfiguration.Builder(Appid).build());
        credentials = Credentials.apiKey("AQDP5P1BoCs6rTldZB5eAJDxtcNJMTuaStmerw1bSDvCq5tSMIagp2xX36qVUwcT");

        String petId = getIntent().getStringExtra("petId");
        System.out.println("petID ---->" + petId);
        app.loginAsync(credentials, it ->{
            if(it.isSuccess()){
                User user = app.currentUser();
                assert user != null;
                Functions functionsManager = app.getFunctions(user);
                List<Object> args = Arrays.asList(petId);
                functionsManager.callFunctionAsync("getPetInfo", args, Document.class, result -> {
                    if (result.isSuccess()) {
                        Log.v("EXAMPLE", "Result: " + result.get());
                        System.out.println("Username -->" + result.get().getString("username"));
                        String ownersName = result.get().getString("username");
                        String phoneNumber = result.get().getString("phoneNumber");
                        String petName = result.get().getString("petName");
                        String petType = result.get().getString("petType");
                        String petAge = result.get().getString("petAge");
                        String petGender = result.get().getString("petGender");
                        String petImage = result.get().getString("petImage");
                        ownersNameTV.setText(ownersNameTV.getText() + ownersName);
                        petNameTV.setText(petNameTV.getText() + petName);
                        petAgeTV.setText(petAgeTV.getText() + petAge);
                        petGenderTV.setText(petGenderTV.getText() + petGender);
                        petTypeTV.setText(petTypeTV.getText() + petType);
                        phoneNumberTV.setText(phoneNumberTV.getText() + phoneNumber);
                        petImageView.setImageBitmap(base64ToImage(petImage));


                        animationView.setVisibility(View.GONE);
                        layoutCat.setVisibility(View.GONE);
                        animationView.cancelAnimation();
                    } else {
                        Log.e("EXAMPLE", "failed to call sum function with: " + result.getError());
                    }
                });

            } else {
                Log.e("EXAMPLE", "Error logging into the Realm app. Make sure that anonymous authentication is enabled. Error: " + it.getError());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetinfoActivity.this, PageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static Bitmap base64ToImage(String imageText){
        byte[] imageBytes = Base64.decode(imageText, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodeImage;
    }
}