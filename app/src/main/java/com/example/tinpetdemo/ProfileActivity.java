package com.example.tinpetdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.bson.Document;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.options.UpdateOptions;

public class ProfileActivity extends AppCompatActivity {
    String Appid = "application-0-erjbs";
    String userId = "";
    EditText nameEdt, fullnameEdt, phoneEdt;
    Button saveBtn, petInfoBtn;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    SharedPreferences sharedPreferences;
    LottieAnimationView animationView;
    LinearLayout layoutCat;
    boolean dataIsNull = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // start Animation
        animationView = findViewById(R.id.animation_cat);
        layoutCat = findViewById(R.id.layoutCat);
        animationView.setVisibility(View.VISIBLE);
        layoutCat.setVisibility(View.VISIBLE);
        animationView.playAnimation();

        saveBtn = (Button) findViewById(R.id.saveBtn);
        petInfoBtn = (Button) findViewById(R.id.petInfoBtn);
        nameEdt = (EditText) findViewById(R.id.nameEdt);
        fullnameEdt = (EditText) findViewById(R.id.fullnameEdt);
        phoneEdt = (EditText) findViewById(R.id.phoneEdt);


        Realm.init(getApplicationContext()); //1.Realm database init
        sharedPreferences = getSharedPreferences("currentUser", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("userId")) {
            userId = sharedPreferences.getString("userId", "");
            System.out.println(userId);
        }
        Document findUserById = new Document().append("userId", userId);
        App app = new App(new AppConfiguration.Builder(Appid).build());
        Credentials credentials = Credentials.apiKey("AQDP5P1BoCs6rTldZB5eAJDxtcNJMTuaStmerw1bSDvCq5tSMIagp2xX36qVUwcT"); //with apiKey they look for current userId

        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess()) {
                    Log.v("User", "UserID");
                    User user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas"); //we use mongodb Atlas service
                    mongoDatabase = mongoClient.getDatabase("petdatas"); //our databaseName
                    mongoCollection = mongoDatabase.getCollection("users");
                    mongoCollection.findOne(findUserById).getAsync(userData -> {  //bringing data if there are in DB
                        if (userData.isSuccess()) {
                            System.out.println(userData.getClass().getName());
                            Document uData = userData.get(); //we're creating the data object of current user
                            try {
                                nameEdt.setText(uData.getString("username"));  //and look for current user's username in our uData object
                                fullnameEdt.setText(uData.getString("fullname"));
                                phoneEdt.setText(uData.getString("phoneNumber"));
                                System.out.println("Data is nullllll -0---------");
                                dataIsNull = false;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            animationView.setVisibility(View.GONE);
                            layoutCat.setVisibility(View.GONE);
                            animationView.cancelAnimation();
                        }
                    });
                } else {
                    Log.v("User", "Failed" + result.getError().toString());
                }
            }
        });


// data inserting
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Document document = new Document().append("userId", userId).append("username", nameEdt.getText().toString()).append("fullname", fullnameEdt.getText().toString()).append("phoneNumber", phoneEdt.getText().toString());
                UpdateOptions options = new UpdateOptions().upsert(true);
                Document query = new Document().append("userId", userId);
                Intent intent = new Intent( ProfileActivity.this, PageActivity.class);
                if (dataIsNull) {                             //if there is no data, we're inserting the data
                    mongoCollection.insertOne(document).getAsync(result -> {
                        if (result.isSuccess()) {
                            Log.v("Data", "Data inserted successfully");
                            Toast.makeText(ProfileActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();

                        } else {
                            Log.v("Data", "Error: " + result.getError().toString());
                        }
                    });
                } else  //if there is data, we're updating the data
                {
                    mongoCollection.updateOne(query, document, options).getAsync(result -> {
                        if (result.isSuccess()) {
                            Log.v("Data", "Data updated successfully");
                            Toast.makeText(ProfileActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            Log.v("Data", "Failed" + result.getError());
                        }
                    });
                }
            }
        });


        petInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PetActivity.class);
                System.out.println(userId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    public static void getUserData(String userId) {

    }
}