package com.example.prueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private Button loginBoton;
    private Button singUpBotton;
    private EditText username;
    private EditText password;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //poner algo de cargar en caso de que haya un usuario logeado
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION},1
        );



        auth = FirebaseAuth.getInstance();
        //auth.signOut();
        loginBoton= findViewById(R.id.loginButton);
        singUpBotton= findViewById(R.id.signUpButton);
        username=findViewById(R.id.usuarioText);
        password=findViewById(R.id.contrasenaText);



        loginBoton.setOnClickListener(
                (v)->{

                    String usuario=username.getText().toString().trim();
                    String contra=password.getText().toString().trim();
                    if(TextUtils.isEmpty(usuario)){
                        username.setError("Es necesario que llenes este campo");
                    }
                    if(!(username.getText().toString().contains("@"))){
                        username.setError("El usuario es el correo electronico");
                        return;
                    }
                    if(TextUtils.isEmpty(contra)){
                        password.setError("Es necesario que llenes este campo");
                    }
                    if(password.getText().toString().length()<6){
                        password.setError("La clave debe de tener al menos 6 caracteres");
                        return;
                    }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(Login.this,MapsActivity.class));
                            }else{

                            }
                        }
                    });

                }
        );
        singUpBotton.setOnClickListener(
                (v)->{
                    startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(Login.this,MapsActivity.class));
            finish();
        }

    }
}