package com.example.tinpetdemo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

public class Pets {
    private String userId;
    Bitmap petImage;
    String petName, petType, petAge, petId, petGender;
    Double l1, l2;

    public Pets() {
    }
    public Pets(String petId, String petName, String petType, String petGender, Bitmap petImage, String petAge) {
        this.petId = petId;
        this.petName = petName;

        this.petImage = petImage;
        this.petType = petType;
        this.petAge = petAge;
        this.petGender = petGender;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetType() {
        return petType;
    }

    public Bitmap getPetImage() {
        return petImage;
    }

    public String getPetAge() {
        return petAge;
    }
    public String getPetId(){
        return petId;
    }
    public String getPetGender(){
        return petGender;
    }

}
