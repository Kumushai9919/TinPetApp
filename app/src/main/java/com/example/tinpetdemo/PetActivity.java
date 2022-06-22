package com.example.tinpetdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.bson.Document;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.options.UpdateOptions;

public class PetActivity extends AppCompatActivity implements LocationListener, AdapterView.OnItemSelectedListener  {
    String Appid = "application-0-erjbs";
    String PetLocationString = "";
    String currentPetType = "";
    ImageButton petImage;
    Spinner petTypeSpinner;
    EditText petName, petAge, petGender;
    TextView petLocation;
    Button petSavebtn, setLocationbtn;
    User user;
    App app;
    String stringImage;
    String userId = "";
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    int SELECT_PICTURE = 200;
    boolean dataIsNull = true;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;
    LottieAnimationView animationView;
    LinearLayout layoutCat;
    Double l1, l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        // loading animation
        animationView = findViewById(R.id.animation_cat);
        layoutCat = findViewById(R.id.layoutCat);
        animationView.setVisibility(View.VISIBLE);
        layoutCat.setVisibility(View.VISIBLE);
        animationView.playAnimation();




        petImage = (ImageButton) findViewById(R.id.petImg);
        petName = (EditText) findViewById(R.id.petName);
        petTypeSpinner = (Spinner) findViewById(R.id.petTypeSpinner);
        petAge = (EditText) findViewById(R.id.petAge);
        petGender = (EditText) findViewById(R.id.petGender);
        petLocation = (TextView) findViewById(R.id.petLocationTextView);
        petSavebtn = (Button) findViewById(R.id.petSavebtn);
        setLocationbtn = (Button) findViewById(R.id.setLocationbtn);


        // Adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.petTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petTypeSpinner.setAdapter(adapter);
        petTypeSpinner.setOnItemSelectedListener(this);

        //Realm init and login
        Realm.init(getApplicationContext());
        sharedPreferences = getSharedPreferences("currentUser", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("userId")){
            userId = sharedPreferences.getString("userId", "");
            System.out.println(userId);
        }
        Document findUserById = new Document().append("userId", userId);
        app = new App(new AppConfiguration.Builder(Appid).build());
        Credentials credentials = Credentials.apiKey("AQDP5P1BoCs6rTldZB5eAJDxtcNJMTuaStmerw1bSDvCq5tSMIagp2xX36qVUwcT");

        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess()) {
                    Log.v("User", "USerID ");
                    user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("petdatas");
                    mongoCollection = mongoDatabase.getCollection("pets");
                    mongoCollection.findOne(findUserById).getAsync(userData -> {
                        if (userData.isSuccess())
                        {
                            Log.v("user", "data found");
                            try {
                                Document uData = userData.get();
                                System.out.println(uData.getString("petName"));
                                try {
                                    petName.setText(uData.getString("petName"));
                                    petAge.setText(uData.getString("petAge"));
//                                    petType.setText(uData.getString("petType"));
                                    petGender.setText(uData.getString("petGender"));
                                    stringImage = uData.getString("petImage");
                                    if (uData.getString("petImage") != null)
                                    {
                                        petImage.setImageBitmap(base64ToImage(stringImage));
                                    }
//                                    if(uData.getDouble("long") != null && uData.getDouble("lat") != null)
//                                    {
//                                        locationConverter(uData.getDouble("long"), uData.getDouble("lat"));
////                                        petLocation.setText(PetLocationString);
//                                    }
                                    System.out.println("Data is nullllll -0---------");
                                    dataIsNull = false;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            animationView.setVisibility(View.GONE);
                            layoutCat.setVisibility(View.GONE);
                            animationView.cancelAnimation();
                        }
                        else
                        {
                            Log.v("User", "Failed" + result.getError().toString());
                        }
                    });
                }
            }
        });

        petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropActivity();
            }
        });

        setLocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Runtime permissions
                if (ContextCompat.checkSelfPermission(PetActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PetActivity.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }
                getLocation();
            }
        });

        //  Save Button- = Data inserting
        petSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Document document = new Document().append("userId", userId).append("petName", petName.getText().toString()).append("petType", currentPetType).append("petAge", petAge.getText().toString()).append("petGender", petGender.getText().toString()).append("petImage", stringImage);
                UpdateOptions options = new UpdateOptions().upsert(true);
                Document query = new Document().append("userId", userId);

                if (dataIsNull)
                {   //if there is no data, we're inserting the data-insertOne();
                    mongoCollection.insertOne(document).getAsync(result -> {
                        if (result.isSuccess())
                        {
                            Log.v("Data", "Data inserted successfully");
                            Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            Log.v("Data", "Failed to insert " + result.getError().toString());
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {   //if there is data, we're updating the data-updateOne();
                    mongoCollection.updateOne(query, document, options).getAsync(result -> {
                        if (result.isSuccess())
                        {
                            Log.v("Data", "Data updated successfully");
                        } else
                        {
                            Log.v("Data", "Failed to update" + result.getError());
                        }
                    });
                }

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Set Location
    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,PetActivity.this);
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
                onLocationChanged(location);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onLocationChanged(Location location) {
        if(location != null){
            try{
                Geocoder geocoder = new Geocoder(PetActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                String address = addresses.get(0).getAddressLine(0);
                petLocation.setText(address);
                l1 = location.getLongitude();
                l2 = location.getLatitude();
                System.out.println("Long-> " + l1 + "\n Lat->" + l2);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Location Converter
    public void locationConverter(Double l1, Double l2){
        try{
            Geocoder geocoder = new Geocoder(PetActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(l2,l1,1);
            PetLocationString = addresses.get(0).getAddressLine(0);
            System.out.println(PetLocationString + "---------- Location Converter");
            petLocation.setText(PetLocationString);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startCropActivity(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(100,300)
                .setMaxCropResultSize(1000,3000)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (null != resultUri) {
                    // update the preview image in the layout
                    //Initialize Bitmap
                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);//Initialize byte stream
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //Compress bitmap
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                        //Initialize byte array
                        byte[] bytes = stream.toByteArray();
                        //Get base64 encoded string
                        stringImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                        petImage.setImageURI(resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static Bitmap base64ToImage(String imageText) {
        byte[] imageBytes = Base64.decode(imageText, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodeImage;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currentPetType = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(PetActivity.this, currentPetType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}