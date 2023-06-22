package com.example.ruhe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registrarse extends AppCompatActivity {
    private FirebaseAuth mAuth;

    //se debe crear el email y password que se pide abajo y crearlo es traelo de la info que
    // entra en la pantalla de registro

    private EditText correo;
    private EditText contrasena;
    private EditText contrasenaConfirmacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        correo =findViewById(R.id.correo);
        contrasena =findViewById(R.id.contrasena);
        contrasenaConfirmacion =findViewById(R.id.contrasenaConfir);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser); aun no se ha creado este metodo
    }

    public void registrarUsuario(View view){

        if (contrasena.getText().toString().equals(contrasenaConfirmacion.getText().toString())){
            mAuth.createUserWithEmailAndPassword(correo.getText().toString().trim(),contrasena.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(),"Usuario creado.\n Bienvenid@ a la app que le permitirá llegar seguro a su destino.", Toast.LENGTH_SHORT).show();

                                //Log.d(TAG, "createUserWithEmailAndPassword:success"); se puede omitir
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user); no es necesario

                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);

                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmailAndPassword:failure", task.getException()); no es necesario
                                Toast.makeText(getApplicationContext(),"Authentication failed.", Toast.LENGTH_SHORT).show();
                                //updateUI(null); se puede omitir
                            }
                        }
                    });
        }else{
            Toast.makeText(this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
        }
    }
}