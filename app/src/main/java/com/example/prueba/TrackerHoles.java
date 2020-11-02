package com.example.prueba;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TrackerHoles extends Thread {
   private FirebaseFirestore db;
   private MapsActivity mapsActivity;
   private boolean isAlive;
   public TrackerHoles(MapsActivity activity){
      mapsActivity=activity;
      isAlive=true;
      db=FirebaseFirestore.getInstance();
   }

   @Override
   public void run() {
      ArrayList<Hueco> huecos=new ArrayList<>();
      while (isAlive){
         delay(5000);
         db.collection("huecos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                  for (QueryDocumentSnapshot document : task.getResult()) {
                     Hueco n=document.toObject(Hueco.class);
                     huecos.add(n);
                  }
               } else {
                  Log.d("SSSS", "Error getting documents: ", task.getException());
               }
            }
         });
         mapsActivity.buscarHuecos(huecos);
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
