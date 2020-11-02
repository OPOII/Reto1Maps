package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText nombre;
    private EditText apellidos;
    private EditText correo;
    private EditText password;
    private EditText confirmPassword;
    private EditText fechaNacimiento;
    private EditText telefono;
    private Button confirmar;
    public final String NOMBRE="nombre";
    public final String APELLIDOS="apellidos";
    public final String CORREO="correo";
    public final String FECHA_NACIMIENTO="fechaNacimiento";
    public final String TELEFONO="telefono";
    public final String LATITUD="latitud";
    public final String LONGITUD="longitud";
    FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nombre=findViewById(R.id.nombreText);
        apellidos=findViewById(R.id.apellidosText);
        correo=findViewById(R.id.emailText);
        password=findViewById(R.id.passwordText);
        confirmPassword=findViewById(R.id.repeatPassword);
        fechaNacimiento=findViewById(R.id.dateText);
        telefono=findViewById(R.id.numberText);
        confirmar=findViewById(R.id.registerButton);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
        confirmar.setOnClickListener(
                (v)->{
                    if(TextUtils.isEmpty(nombre.getText().toString())){
                        nombre.setError("Por favor rellene el campo del nombre");
                    }
                    if(TextUtils.isEmpty(apellidos.getText().toString())){
                        apellidos.setError("Por favor rellene el campo del apellidos");
                    }
                    if(TextUtils.isEmpty(correo.getText().toString())){
                        correo.setError("Por favor rellene el campo del nombre");
                    }
                    if(!(correo.getText().toString().contains("@"))){
                        correo.setError("Por favor, ponga una direccion de correo electronico valida");
                    }
                    if(password.getText().toString().length()<6){
                        password.setError("La contraseña debe de tener al menos 6 caracteres");
                    }
                    if(TextUtils.isEmpty(fechaNacimiento.getText().toString())){
                        fechaNacimiento.setError("Debes de ingresar una fecha de nacimiento");
                    }
                    if(TextUtils.isEmpty(confirmPassword.getText().toString())){
                        confirmPassword.setError("Las contraseñas no coinciden");
                    }
                    if(TextUtils.isEmpty(telefono.getText().toString())){
                        telefono.setError("Por favor ingrese un numero celular");
                    }
                    if(telefono.getText().toString().length()<10){
                        telefono.setError("Por favor ingrese un numero valido");
                    }
                    auth.createUserWithEmailAndPassword(correo.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"User created Successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                            }else{
                                Toast.makeText(SignUpActivity.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Map<String,Object> note=new HashMap<>();
                    note.put(NOMBRE,nombre.getText().toString());
                    note.put(APELLIDOS,apellidos.getText().toString());
                    note.put(CORREO,correo.getText().toString());
                    note.put(FECHA_NACIMIENTO,fechaNacimiento.getText().toString());
                    note.put(TELEFONO,telefono.getText().toString());
                    note.put(LATITUD,0);
                    note.put(LONGITUD,0);

                    auth.signInWithEmailAndPassword(correo.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"Logged in Succesfully",Toast.LENGTH_SHORT).show();
                                db.collection("usuarios").document(auth.getUid()).set(note);
                                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                            }else{
                                Toast.makeText(SignUpActivity.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        );

    }
}