package com.example.ruhe;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class enAccion extends AppCompatActivity implements LocationListener {


    LocationManager locationManager;
    String pregunta, ubicacion;
    TextView cronometro;
    int tiempo;
    FirebaseAuth auth;


    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 5; // get gps location every 1 min
    private static final int GPS_DISTANCE = 1000; // set the distance value in meter
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Ruta> rutas= new ArrayList<>(MainActivity.getRutas());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_accion);
        auth = FirebaseAuth.getInstance();
        cronometro = findViewById(R.id.cronometro);

        if (auth.getCurrentUser()!=null){
            pregunta=rutas.get(Ingresado.getIndex()).getPregunta();
            tiempo=Integer.parseInt(rutas.get(Ingresado.getIndex()).getTiempo());
            cronometro(tiempo,pregunta);
        }else{
            tiempo=20;
            pregunta="Are you okay?";
            cronometro(tiempo,pregunta);
        }

    }

    //codigo que se llamara cada que se reciba el location update
    @Override
    public void onLocationChanged(@NonNull Location location) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ubicacion=location.getLatitude()+","+location.getLongitude();
        Toast.makeText(this,"Got Location.",Toast.LENGTH_SHORT).show();
        Correo.enviarCorreo(ubicacion);
        Intent i = new Intent(enAccion.this, Ingresado.class);
        startActivity(i);
    }

    //metodo que recibe si se aceptaron o no los permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //request location Now
            requestLocation();
        }
    }
    //codigo para pedir la location
    private void requestLocation() {
        if(locationManager == null){
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        GPS_TIME_INTERVAL, GPS_DISTANCE, this);
            }
        }
    }
    private void preguntar(String pregunta){
        // Crear el AlertDialog
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage(pregunta)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cronometro(tiempo, pregunta);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestLocation();
                    }
                })
                .create();

        // Mostrar el AlertDialog
        alertDialog.show();

        // Cerrar el AlertDialog después de un tiempo determinado (ejemplo: 3 segundos)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    requestLocation();
                }
            }
        }, 20000); // Tiempo en milisegundos (20 segundos en este ejemplo)
    }

    public void cronometro(int tiempo,String pregunta){
        countDownTimer = new CountDownTimer(tiempo*1000, 1000) {
            // milisegundos, intervalo de 1000 milisegundos (1 segundo)

            public void onTick(long millisUntilFinished) {
                // Se llama cada segundo mientras el cronómetro está en marcha
                long segundos = millisUntilFinished / 1000;
                // Actualizar la interfaz de usuario con el tiempo restante
                cronometro.setText("Time left: " + segundos + " seconds");
            }

            public void onFinish() {
                // Se llama cuando el cronómetro llega a cero
                cronometro.setText("¡ Timer ended!");
                preguntar(pregunta);
            }
        };
        countDownTimer.start(); // Iniciar el cronómetro
    }

    public void emergencia(View view){
        requestLocation();
        Toast.makeText(this,"Got it",Toast.LENGTH_SHORT).show();
    }
}