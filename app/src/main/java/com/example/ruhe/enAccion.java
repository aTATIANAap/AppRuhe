package com.example.ruhe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    Session session;
    String correo = "ruheaplicacion@gmail.com";
    String contra = "gbqdomxhuovibywj";
    LocationManager locationManager;
    ArrayList<Ruta> rutas;
    String pregunta, opcion, ubicacion;
    TextView cronometro;
    int tiempo, ruta=1;
    FirebaseAuth auth;


    private static final int GPS_TIME_INTERVAL = 1000 * 60 * 5; // get gps location every 1 min
    private static final int GPS_DISTANCE = 1000; // set the distance value in meter
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Ingresado ingresado = new Ingresado();
        opcion = ingresado.getOpcion();

        MainActivity principal = new MainActivity();
        rutas = new ArrayList<>();
        rutas = principal.getRutas();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_accion);
        auth = FirebaseAuth.getInstance();
        cronometro = findViewById(R.id.cronometro);

        if (auth.getCurrentUser()!=null){
            pregunta=rutas.get(ruta).getPregunta();
            tiempo=Integer.parseInt(rutas.get(ruta).getTiempo());
            cronometro(tiempo,pregunta);

        }else{
            tiempo=20;
            pregunta="Estás bien?";
            cronometro(tiempo,pregunta);
        }

    }

    //codigo que se llamara cada que se reciba el location update
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this,"Got Location",Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación")
                .setMessage(pregunta)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cronometro(tiempo, pregunta);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestLocation();
                        //metodo de enviar ubi al contacto
                    }
                })
                .show();
    }

    public void cronometro(int tiempo,String pregunta){
        countDownTimer = new CountDownTimer(tiempo*1000, 1000) {
            // 60000 milisegundos (60 segundos), intervalo de 1000 milisegundos (1 segundo)

            public void onTick(long millisUntilFinished) {
                // Se llama cada segundo mientras el cronómetro está en marcha
                long segundos = millisUntilFinished / 1000;
                // Actualizar la interfaz de usuario con el tiempo restante
                cronometro.setText("Tiempo restante: " + segundos + " segundos");
            }

            public void onFinish() {
                // Se llama cuando el cronómetro llega a cero
                cronometro.setText("¡Cronómetro finalizado!");
                preguntar(pregunta);
            }
        };
        countDownTimer.start(); // Iniciar el cronómetro
    }

    public void emergencia(View view){
        requestLocation();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        try{
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo, contra);
                }
            });

            if(session != null){
                LocationManager locationManager = (LocationManager) enAccion.this.getSystemService (Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(@NonNull Location location) {
                        ubicacion = (""+location.getLatitude()+" "+location.getLongitude());
                    }
                };
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("EMERGENCIA: SU CONTACTO NO RESPONDE");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("david-felipe-2000@hotmail.com"));
                message.setContent("Hola, somos de la aplicación Ruhe, el envío de este mensaje se debe a que una persona que lo seleccionó a usted com contacto de emergencia, se encuentra en peligro, esta es la ubicación de la persona: "+ubicacion, "text/html; charset=utf-8");

                Transport.send(message);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Toast.makeText(this,"Got it",Toast.LENGTH_SHORT).show();

        //metodo de enviar ubi al contacto

    }
}