package com.example.ruhe;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AgregarRuta extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{
    GoogleMap mMap;
    private EditText textNombre, textDest, textTiempo, textPregunta, textContacto;
    LatLng usuario;
    private String userUbi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ruta);

        textNombre = findViewById(R.id.txt_Nombre);
        textDest = findViewById(R.id.txt_Direccion);
        textContacto = findViewById(R.id.Telefono);
        textPregunta = findViewById(R.id.txt_pregunta);
        textTiempo = findViewById(R.id.txt_Time);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    //Metodo Presionar
    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    //Metodo mantener presionado
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        LatLng destino = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(destino).title("Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destino));

        textDest.setText(String.valueOf(latLng.latitude+" "+latLng.longitude));

    }

    //Metodo abrir mapa
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this,"Use the icon on the right side of the map to center.",Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) AgregarRuta.this.getSystemService (Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(@NonNull Location location) {
                usuario = new LatLng(location.getLatitude(), location.getLongitude());
                //textUbi.setText(String.valueOf(usuario.latitude+" "+usuario.longitude));
                userUbi = String.valueOf(usuario.latitude+" "+usuario.longitude);
            }
            public void onStatusChanges(String provider, int status, Bundle extras){}
            public void onProviderEnabled(String provider){}
            public void onProviderDisabled(String provider){}
        };

        int permissionCheck = ContextCompat.checkSelfPermission(AgregarRuta.this, ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }
    public void crearRuta(Ruta ruta) {
        MainActivity.añadirRuta(ruta);
        Toast.makeText(this,"Route added",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, Ingresado.class);
        startActivity(i);

    }
    public void agregarRuta(View view){
        String nombreRuta = String.valueOf(textNombre.getText());
        String destino = String.valueOf(textDest.getText());
        String contacto = (String.valueOf(textContacto.getText()));
        String pregunta = String.valueOf(textPregunta.getText());
        String  tiempo = String.valueOf(textTiempo.getText());
        if (nombreRuta.equals("") || destino.equals("") || pregunta.equals("")|| contacto.equals("") || tiempo.equals("")) {
            Toast.makeText(this,"Fill all the empty spaces",Toast.LENGTH_SHORT).show();
        } else {
            Ruta datosUsuario = new Ruta(nombreRuta, userUbi, destino, pregunta,tiempo,contacto);
            crearRuta(datosUsuario);
        }
    }
}