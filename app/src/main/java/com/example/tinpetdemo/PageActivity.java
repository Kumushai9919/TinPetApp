package com.example.tinpetdemo;

import static com.example.tinpetdemo.PetActivity.base64ToImage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.Edits;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.options.UpdateOptions;

public class PageActivity extends AppCompatActivity {
    String Appid = "application-0-erjbs";
    private static final String TAG = "MainActivity";
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollectionPets;
    Button likedBtn, savedBtn, nextBtn;
    ImageView petImageView;
    TextView petNameTV, petAgeTV, petTypeTV;
    String userId;
    SharedPreferences sharedPreferences;
    App app;
    Credentials credentials;
    LottieAnimationView animationView;
    LinearLayout layoutCat;
    Boolean dogFilter = true;
    Boolean catFilter = true;
    List<Pets> items = new ArrayList<>();
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);



        // animation cat
        animationView = findViewById(R.id.animation_cat);
        layoutCat = findViewById(R.id.layoutCat);
        animationView.setVisibility(View.VISIBLE);
        layoutCat.setVisibility(View.VISIBLE);
        animationView.playAnimation();

        savedBtn = (Button) findViewById(R.id.savedBtn);

        petNameTV = (TextView) findViewById(R.id.petNameTV);
        petAgeTV = (TextView) findViewById(R.id.petAgeTV);
        petTypeTV = (TextView) findViewById(R.id.petTypeTV);
        petImageView = (ImageView) findViewById(R.id.petImgView);



        Realm.init(getApplicationContext());
        sharedPreferences = getSharedPreferences("currentUser", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("userId")) {
            userId = sharedPreferences.getString("userId", "");
            System.out.println(userId);
        }

        if(sharedPreferences.contains("catBool")){
            catFilter = sharedPreferences.getBoolean("catBool", true);
        }
        if(sharedPreferences.contains("dogBool")){
            dogFilter = sharedPreferences.getBoolean("dogBool", true);
        }

        app = new App(new AppConfiguration.Builder(Appid).build());
        credentials = Credentials.apiKey("AQDP5P1BoCs6rTldZB5eAJDxtcNJMTuaStmerw1bSDvCq5tSMIagp2xX36qVUwcT");

        app.loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if (result.isSuccess()) {
                    Log.v("User", "UserID");
                    final User[] user = {app.currentUser()};
                    mongoClient = user[0].getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("petdatas");
                    mongoCollectionPets = mongoDatabase.getCollection("pets");

                    RealmResultTask<MongoCursor<Document>> findTask = mongoCollectionPets.find().iterator();
                    findTask.getAsync(task -> {
                        if (task.isSuccess()) {
                            MongoCursor<Document> uData = task.get();
                            if (!uData.hasNext()) {
                                Log.v("Result", "Couldn't find data");
                            }

                            while (uData.hasNext())
                            {
                                Document currentDoc = uData.next(); // pet Object

                                if (dogFilter && currentDoc.getString("petType").equals("Dog")) {
                                    String petName = currentDoc.getString("petName");
                                    String petType = currentDoc.getString("petType");
                                    String petId = currentDoc.getString("userId");
                                    String petGender = currentDoc.getString("petGender");
                                    System.out.println("PetType ----->" + petType);
                                    String petAge = currentDoc.getString("petAge");
                                    System.out.println("PetAge ----->" + petAge);
                                    String petImage = currentDoc.getString("petImage");

                                    //bitmap on drawable
                                    Bitmap bitmap = base64ToImage(petImage);
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                    items.add(new Pets(petId, petName, petType, petGender, bitmap, petAge));
                                }
                                if (catFilter && currentDoc.getString("petType").equals("Cat")) {
                                    String petName = currentDoc.getString("petName");
                                    String petType = currentDoc.getString("petType");
                                    String petId = currentDoc.getString("userId");
                                    String petGender = currentDoc.getString("petGender");
                                    System.out.println("PetType ----->" + petType);
                                    String petAge = currentDoc.getString("petAge");
                                    System.out.println("PetAge ----->" + petAge);
                                    String petImage = currentDoc.getString("petImage");

                                    //bitmap on drawable
                                    Bitmap bitmap = base64ToImage(petImage);
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                    items.add(new Pets(petId, petName, petType, petGender, bitmap, petAge));
                                }
                            }

                            Bitmap bitmapNoPet = BitmapFactory.decodeResource(getResources(), R.drawable.nopet);
                            items.add(new Pets("notFound", "No Pet","", "", bitmapNoPet, ""));
                            animationView.setVisibility(View.GONE);
                            layoutCat.setVisibility(View.GONE);
                            animationView.cancelAnimation();

                        } else {
                            Log.v("task error", task.getError().toString());
                        }
                    });

                } else {
                    Log.v("User", "Failed" + result.getError().toString());
                }
            }

        });


        CardStackView cardStackView = findViewById(R.id.card_stack_view);

        savedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                startActivity(intent);
            }
        });


        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right) {
                    String currentPetId = items.get(manager.getTopPosition() - 1).getPetId();
                    if(currentPetId.equals("notFound")){
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                    }
                    app.loginAsync(credentials, it -> {
                        if (it.isSuccess()) {
                            User user = app.currentUser();
                            assert user != null;
                            Functions functionsManager = app.getFunctions(user);
                            List<Object> args = Arrays.asList(userId, currentPetId);
                            functionsManager.callFunctionAsync("likedPets", args, String.class, result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Result: " + result.get());

                                    Toast.makeText(PageActivity.this, "Like Pet", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("EXAMPLE", "failed to call sum function with: " + result.getError());
                                }
                            });
                        } else {
                            Log.e("EXAMPLE", "Error logging into the Realm app. Make sure that anonymous authentication is enabled. Error: " + it.getError());
                        }
                    });

                }
                if (direction == Direction.Top) {
                    String currentPetId = items.get(manager.getTopPosition() - 1).getPetId();
                    if(currentPetId.equals("notFound")) {
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(PageActivity.this, "Direction Top", Toast.LENGTH_SHORT).show();
                }

                if (direction == Direction.Left) {
                    String currentPetId = items.get(manager.getTopPosition() - 1).getPetId();
                    if(currentPetId.equals("notFound")) {
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(PageActivity.this, "Direction Left", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Bottom) {
                    String currentPetId = items.get(manager.getTopPosition() - 1).getPetId();
                    if(currentPetId.equals("notFound")) {
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(PageActivity.this, "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 1) {
                    paginate();
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);

                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardStackAdapter(items);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.CatItem).setChecked(catFilter);
        menu.findItem(R.id.DogItem).setChecked(dogFilter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent updatePetType = getIntent();
        switch (item.getItemId()) {
            case R.id.profile:
                Intent NewActivity = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(NewActivity);
                break;
            case R.id.logOut:
                editor.remove("userId");
                editor.apply();
                Intent intent = new Intent(PageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.CatItem:
                if(item.isChecked()){
                    catFilter = false;
                    editor.putBoolean("catBool", catFilter);
                    editor.apply();
                    item.setChecked(false);
                    Toast.makeText(this, "Cat Item is False", Toast.LENGTH_SHORT).show();
                    startActivity(updatePetType);
                    finish();
                }else{
                    catFilter = true;
                    editor.putBoolean("catBool", catFilter);
                    editor.apply();
                    item.setChecked(true);
                    Toast.makeText(this, "Cat Item is True", Toast.LENGTH_SHORT).show();
                    startActivity(updatePetType);
                    finish();
                }
                break;
            case R.id.DogItem:

                if(item.isChecked()){
                    dogFilter = false;
                    editor.putBoolean("dogBool", dogFilter);
                    editor.apply();
                    item.setChecked(false);
                    Toast.makeText(this, "Dog Item is False", Toast.LENGTH_SHORT).show();
                    startActivity(updatePetType);
                    finish();
                }else{
                    dogFilter = true;
                    editor.putBoolean("dogBool", dogFilter);
                    editor.apply();
                    item.setChecked(true);
                    Toast.makeText(this, "Dog Item is True", Toast.LENGTH_SHORT).show();
                    startActivity(updatePetType);
                    finish();
                }
                break;
        }

        return true;
    }

    public static Bitmap base64ToImage(String imageText) {
        byte[] imageBytes = Base64.decode(imageText, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodeImage;
    }


    private void paginate() {
        List<Pets> old = adapter.getItems();
        List<Pets> baru = new ArrayList<>(items);
        CardStackCallBack callback = new CardStackCallBack(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);

        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }
}