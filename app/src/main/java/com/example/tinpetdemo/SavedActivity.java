package com.example.tinpetdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class SavedActivity extends AppCompatActivity {
    String Appid = "application-0-erjbs";
    SharedPreferences sharedPreferences;
    String userId = "";
    App app;
    ImageView petImageVIew;
    TextView petNameTV, petAgeTV, petTypeTV;
    Button removeBtn;
    LottieAnimationView animationView;
    LinearLayout layoutCat;
    TextView noPetSaved;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        // loading animation
        animationView = findViewById(R.id.animation_cat);
        layoutCat = findViewById(R.id.layoutCat);
        animationView.setVisibility(View.VISIBLE);
        layoutCat.setVisibility(View.VISIBLE);
        animationView.playAnimation();

        //Text
        noPetSaved = (TextView)findViewById(R.id.noPetSaved);


        sharedPreferences = getSharedPreferences("currentUser", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("userId")) {
            userId = sharedPreferences.getString("userId", "");
            System.out.println(userId);
        }

        LinearLayout lLayout = (LinearLayout) findViewById(R.id.petInfolayout);

        //call Realm func- getSavedPets
        Realm.init(getApplicationContext());
        app = new App(new AppConfiguration.Builder(Appid).build());
        Credentials credentials = Credentials.apiKey("AQDP5P1BoCs6rTldZB5eAJDxtcNJMTuaStmerw1bSDvCq5tSMIagp2xX36qVUwcT");


        app.loginAsync(credentials, it -> {
            if (it.isSuccess()) {
                User user = app.currentUser();
                assert user != null;
                Functions functionsManager = app.getFunctions(user);
                List<Object> args = Arrays.asList(userId);
                functionsManager.callFunctionAsync("getSavedPets", args, Document.class, result -> {
                    System.out.println("Call Function ---->");
                    if (result.isSuccess()) {
                        result.get().forEach((prop, value) -> {
                            try {
                                JSONObject res = new JSONObject(value.toString());
                                String petId = res.getString("userId");
                                String petName = res.getString("petName");
                                String petType = res.getString("petType");
                                String petAge = res.getString("petAge");
                                String petGender = res.getString("petGender");
                                String petImage = res.getString("petImage");

                                TextView textView = new TextView(this);
                                petNameTV = new TextView(SavedActivity.this);
                                petAgeTV = new TextView(SavedActivity.this);
                                petTypeTV = new TextView(SavedActivity.this);
                                petImageVIew = new ImageView(SavedActivity.this);
                                removeBtn = new Button(SavedActivity.this);
                                LinearLayout baselayout = new LinearLayout(this);


//                                final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.gravity = Gravity.CENTER;
                                params.setMargins(40, 5, 40, 5);
                                baselayout.setLayoutParams(params);
                                baselayout.setBackgroundColor(Color.parseColor("#92B4EC"));
                                baselayout.setOrientation(LinearLayout.VERTICAL);
                                baselayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);

                                LinearLayout L1 = new LinearLayout(this);
                                final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources().getDisplayMetrics());
                                LinearLayout.LayoutParams paramsL1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                                paramsL1.weight = 1.0f;
                                paramsL1.gravity = Gravity.CENTER;
                                L1.setLayoutParams(paramsL1);
                                L1.setOrientation(LinearLayout.VERTICAL);

                                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                petImageVIew.setLayoutParams(imageParams);
                                petImageVIew.setImageBitmap(base64ToImage(petImage));
                                L1.addView(petImageVIew);
                                baselayout.addView(L1);

                                LinearLayout L2 = new LinearLayout(this);
                                LinearLayout.LayoutParams paramsL2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                paramsL2.weight = 3.0f;
                                L2.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                L2.setLayoutParams(paramsL2);
                                L2.setOrientation(LinearLayout.HORIZONTAL);

                                petNameTV.setText(petName);
                                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params2.weight = 1.0f;
                                petNameTV.setLayoutParams(params2);
                                petNameTV.setTypeface(Typeface.DEFAULT_BOLD);
                                petNameTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                petNameTV.setGravity(Gravity.CENTER_HORIZONTAL);
                                petNameTV.setTextColor(Color.parseColor("#FFFFFFFF"));

                                petAgeTV.setText(petAge);
                                params2.weight = 1.0f;
                                petAgeTV.setLayoutParams(params2);
                                petAgeTV.setTypeface(Typeface.DEFAULT_BOLD);
                                petAgeTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                petAgeTV.setGravity(Gravity.CENTER_HORIZONTAL);
                                petAgeTV.setTextColor(Color.parseColor("#FFFFFFFF"));

                                petTypeTV.setText(petType);
                                params2.weight = 1.0f;
                                petTypeTV.setLayoutParams(params2);
                                petTypeTV.setTypeface(Typeface.DEFAULT_BOLD);
                                petTypeTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                                petTypeTV.setGravity(Gravity.CENTER_HORIZONTAL);
                                petTypeTV.setTextColor(Color.parseColor("#FFFFFFFF"));
                                L2.addView(petNameTV);
                                L2.addView(petAgeTV);
                                L2.addView(petTypeTV);
                                baselayout.addView(L2);
                                // remove button
                                LinearLayout L3 = new LinearLayout(this);
                                LinearLayout.LayoutParams paramsL3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                paramsL3.weight = 3.0f;
                                L3.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                L3.setLayoutParams(paramsL3);
                                L3.setOrientation(LinearLayout.HORIZONTAL);

                                removeBtn.setText("remove");
                                removeBtn.setLayoutParams(params);
                                removeBtn.setTextColor(Color.WHITE);
                                removeBtn.setBackgroundColor(Color.parseColor("#FFF44336"));
                                L3.addView(removeBtn);
                                baselayout.addView(L3);

                                // pet img on click Layout ->petInfo
                                baselayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(SavedActivity.this, PetinfoActivity.class);
                                        intent.putExtra("petId", petId);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                removeBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        app.loginAsync(credentials, it -> {
                                            if (it.isSuccess()) {
                                                User user = app.currentUser();
                                                assert user != null;
                                                Functions functionsManager = app.getFunctions(user);
                                                List<Object> args = Arrays.asList(userId, petId);
                                                functionsManager.callFunctionAsync("deletePet", args, String.class, result -> {
                                                    if (result.isSuccess()) {
                                                        Log.v("EXAMPLE", "REsult: " + result.get());
                                                        Intent intent = getIntent();
                                                        finish();
                                                        startActivity(intent);
                                                    } else {
                                                        Log.e("EXAMPLE", "failed to call sum function with: " + result.getError());
                                                    }
                                                });
                                            } else {
                                                Log.e("EXAMPLE", "Error logging into the Realm app. Make sure that anonymous authentication is enabled. Error: " + it.getError());
                                            }
                                        });

                                    }
                                });

                                noPetSaved.setVisibility(View.GONE);
                                lLayout.addView(baselayout);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });

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
    }

    public static Bitmap base64ToImage(String imageText){
        byte[] imageBytes = Base64.decode(imageText, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodeImage;
    }

}