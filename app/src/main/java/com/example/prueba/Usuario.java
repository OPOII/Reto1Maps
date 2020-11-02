package com.example.prueba;

public class Usuario {
    public String apellidos;
    public String correo;
    public String fechaNacimiento;
    public String latitud;
    public String longitud;
    public String nombre;
    public String telefono;

    public Usuario(String apellidos, String correo, String fechaNacimiento, String latitud, String longitud, String nombre, String telefono){
    this.apellidos=apellidos;
    this.correo=correo;
    this.fechaNacimiento=fechaNacimiento;
    this.latitud=latitud;
    this.longitud=longitud;
    this.nombre=nombre;
    this.telefono=telefono;
    }
    public Usuario(){

    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
