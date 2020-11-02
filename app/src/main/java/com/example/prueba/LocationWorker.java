package com.example.prueba;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;

import java.util.Map;

public class LocationWorker extends Thread {

    private MapsActivity ref;
    private boolean isAlive;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public LocationWorker(MapsActivity actividad){
        ref=actividad;
        isAlive=true;
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public void run() {


        while (isAlive){
            delay(10000);
                db.collection("usuarios").document(auth.getCurrentUser().getUid()).update("latitud",ref.getCurrentPosition().getLat()+"");
                db.collection("usuarios").document(auth.getCurrentUser().getUid()).update("longitud",ref.getCurrentPosition().getLang()+"");

        }
    }
    public void finish(){
        isAlive=false;
    }
    public void delay(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
