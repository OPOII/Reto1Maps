package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener,GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private  LocationManager manager;
    private FloatingActionButton agregar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String addres;
    private LocationWorker locationWorker;
    private TrackUsersWorker usersWorker;
    //private Marker me;
    private String usuario;
    private FirebaseAuth auth;
    private Position currentPosition;
    private ArrayList<Marker>markers;
    private Button confirmar;
    private Button aviso;
    private TrackerHoles holes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        auth=FirebaseAuth.getInstance();
        usuario=auth.getCurrentUser().getUid().toString();
        Log.d("Probar", "onCreate: "+usuario);

         manager= (LocationManager)getSystemService(LOCATION_SERVICE);
         agregar=findViewById(R.id.botonAgregar);
         confirmar=findViewById(R.id.butttonConfirmar);
         confirmar.setEnabled(false);
         aviso=findViewById(R.id.bottonAviso);
         aviso.setVisibility(View.GONE);
         markers=new ArrayList<>();


    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        setInicialPos();
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,this);
        locationWorker=new LocationWorker(this);
        locationWorker.start();

        usersWorker=new TrackUsersWorker(this);
        usersWorker.start();

        holes=new TrackerHoles(this);
        holes.start();


    }

    @Override
    protected void onDestroy() {
        locationWorker.finish();
        usersWorker.finish();
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    public void setInicialPos(){
     Location location= manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
     if(location!=null){
         LatLng myPos = new LatLng(location.getLatitude(),location.getLongitude());
         Log.d("Posicion", myPos+"");
         mMap.animateCamera(CameraUpdateFactory.newLatLng(myPos));
         currentPosition=new Position(location.getLatitude(),location.getLongitude());
     }

    }
    public void updateMyLocation(Location location){
    LatLng myPos=new LatLng(location.getLatitude(),location.getLongitude());
    /*
    if(me==null){
        me=mMap.addMarker(new MarkerOptions().position(myPos).title("Estas aqui"));
    }else{
        me.setPosition(myPos);
    }
     */
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos,15));
    currentPosition=new Position(location.getLatitude(),location.getLongitude());
        agregar.setOnClickListener(
                (v)->{
                    LatLng posActual= myPos;
                    Geocoder geocoder= new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses= geocoder.getFromLocation(posActual.latitude,posActual.longitude,1);
                        if(addresses.size()>0){
                            Address fetchedAddress = addresses.get(0);
                            StringBuilder stringBuilder= new StringBuilder();
                            for(int i=0; i<=fetchedAddress.getMaxAddressLineIndex(); i++){
                                stringBuilder.append(fetchedAddress.getAddressLine(i)).append("");
                            }
                            addres=stringBuilder.toString();
                            Log.d("mylog","Direccion: ->>>>"+ addres);
                        }

                    } catch (IOException e) {
                        addres="No se ha podido encontrar la dirección";
                    }

                    Map<String, Object> data=new HashMap<>();
                    data.put("Coordenadas",addres);
                    data.put("latitud", posActual.latitude+"");
                    data.put("longitud",posActual.longitude+"");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Agregar un hueco");
                    builder.setMessage("Coordenadas :"+"\n"+posActual.toString()+"\n"+"Direccion: "+"\n"+addres);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("huecos").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("My log", "DocumentSnapshot written with ID: " + documentReference.getId());
                                    LatLng hueco=posActual;

                                    mMap.addMarker(new MarkerOptions().position(hueco).title("Aqui hay un hueco").icon(bitmapDescriptor(getApplicationContext(),R.drawable.hueco)));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("My log again", "Error adding document", e);
                                }
                            });
                        }
                    });
                    builder.show();
                }
        );
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
       updateMyLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    public Position getCurrentPosition(){
        return currentPosition;
    }
    public String getUser(){
        return usuario;
    }
    public void updateMarker(ArrayList<Position>positions){
        runOnUiThread(

                ()->{
                    for (int i = 0; i <markers.size() ; i++) {
                        Marker m=markers.get(i);
                        m.remove();
                    }
                    markers.clear();
                    for (int i = 0; i <positions.size() ; i++) {
                        Position pos=positions.get(i);
                        LatLng latLng=new LatLng(pos.getLat(),pos.getLang());
                        Marker m=mMap.addMarker(new MarkerOptions().position(latLng));
                        markers.add(m);
                    }
                }
        );
    }
    public BitmapDescriptor bitmapDescriptor(Context context, int v){
        Drawable vector= ContextCompat.getDrawable(context,v);
        vector.setBounds(0,0,vector.getIntrinsicWidth(),vector.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vector.getIntrinsicWidth(),vector.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vector.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void buscarHuecos(ArrayList<Hueco>huecos){
        runOnUiThread(
                ()->{
                    aviso.setVisibility(View.GONE);
                    confirmar.setEnabled(false);
                    for (int i = 0; i <huecos.size() ; i++) {
                        huecos.get(i).retornarPos();
                        mMap.addMarker(new MarkerOptions().position(huecos.get(i).pos).title("Aqui hay un hueco").icon(bitmapDescriptor(getApplicationContext(),R.drawable.hueco)));
                    }
                    for (int i = 0; i <huecos.size() ; i++) {
                        LatLng hueco=huecos.get(i).pos;
                        LatLng me=new LatLng(currentPosition.getLat(),currentPosition.getLang());
                        Log.d("PROBAR DISTANCIA", me.toString()+"");
                        double metros= SphericalUtil.computeDistanceBetween(hueco,me);
                        Log.d("PROBAR DISTANCIA", metros+"");
                        if(metros<200){

                            aviso.setVisibility(View.VISIBLE);
                            aviso.setText("Hay un HUECO a "+metros+" suyo");
                            confirmar.setEnabled(true);
                        }

                    }
                    confirmar.setOnClickListener(
                            (v)->{
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Confirmar un Hueco");
                                builder.setMessage("El hueco se encuentra en :"+addres+"\n"+"¿Desea confirmar el hueco?");
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        aviso.setVisibility(View.GONE);
                                        confirmar.setEnabled(false);
                                        Toast.makeText(MapsActivity.this,"You confirmed a whole. Thanks for your help :)",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                    );
                }
        );
        
    }
}