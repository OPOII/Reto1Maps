package com.example.prueba;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackUsersWorker extends Thread {
    private MapsActivity actividad;
    private boolean isAlive;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    String prueba;
    public TrackUsersWorker(MapsActivity activity){
        actividad=activity;
        isAlive=true;
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        prueba="";

    }

    @Override
    public void run() {
        prueba="";
        ArrayList<QueryDocumentSnapshot> snaps=new ArrayList<>();
        ArrayList<Position>positions=new ArrayList<>();
        ArrayList<Usuario> lista= new ArrayList<>();

        while(isAlive){
           // String json=r.GETrequest("https://console.firebase.google.com/project/reto1maps/firestore/data~2Fusuarios.json");
            //Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",json);
            delay(1000);
            db.collection("usuarios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("vvvv", document.getId() + " => " + document.getData());
                            Usuario n=document.toObject(Usuario.class);
                            lista.add(n);
                        }
                    } else {
                        Log.d("SSSS", "Error getting documents: ", task.getException());
                    }
                }
            });
            if(lista.size()>0){
                for (int i = 0; i <lista.size() ; i++) {
                    Position n=new Position(Double.parseDouble(lista.get(i).latitud),Double.parseDouble(lista.get(i).longitud));
                    positions.add(n);
                }
            }
            actividad.updateMarker(positions);
        }
    }
    public void delay(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void finish(){
        isAlive=false;

    }
}
