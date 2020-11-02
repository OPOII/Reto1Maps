package com.example.prueba;

import com.google.android.gms.maps.model.LatLng;

public class Hueco {

   public String lugar;
    public String latitud;
    public String longitud;
    public LatLng pos;
    public Hueco(){

    }
    public Hueco(String lugar, String latitud, String longitud){
        this.lugar=lugar;
        this.latitud=latitud;
        this.longitud=longitud;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public void retornarPos(){
        pos=new LatLng(Double.parseDouble(latitud),Double.parseDouble(longitud));
    }

    public LatLng getPos() {
        return pos;
    }

    public void setPos(LatLng pos) {
        this.pos = pos;
    }
}
