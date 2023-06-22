package com.example.ruhe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //estos son los permisos que se usaron, el fine location es una mas fina pero siempre va acompañada de la otra
    final static String[]PERMISSIONS ={Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    final static int PERMISSION_ALL = 1;
    private static ArrayList<Ruta> rutas = new ArrayList<Ruta>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                //show message que se quisiera mostrar por eso es should
            }else{
                ActivityCompat.requestPermissions(this, PERMISSIONS,PERMISSION_ALL);
            }
        }

        Ruta ruta1 = new Ruta("Restaurante", "4.6356580948948745, -74.08240211685772", "4.648896532622788, -74.07757715905059","tas bien?","30","antrianaa@unal.edu.co");
        Ruta ruta2 = new Ruta("Apto","4.6356580948948745, -74.08240211685772", "4.648716, -74.095209","Tass bien?","20","antrianaa@unal.edu.co");
        Ruta ruta3 = new Ruta("Pareja","4.6356580948948745, -74.08240211685772", "4.702341, -74.143039 ","Tas bienn?","10","antrianaa@unal.edu.co");
        Ruta ruta4 = new Ruta("Ex pareja","4.6356580948948745, -74.08240211685772", "4.610145862143372, -74.0528045943292","Tas Bien?","15","antrianaa@unal.edu.co");
        Ruta ruta5 = new Ruta("Universidad","4.6356580948948745, -74.08240211685772", "4.597949468065427, -74.07606702229843","Tas bien?","25","antrianaa@unal.edu.co");

        rutas.add(ruta1);
        rutas.add(ruta2);
        rutas.add(ruta4);
        rutas.add(ruta5);
        rutas.add(ruta3);
    }

    public static void añadirRuta(Ruta ruta){
        rutas.add(ruta);
    }

    public void irIniciar(View v){
        Intent i = new Intent(this, iniciarSesion.class);
        startActivity(i);
    }

    public void irRegistro(View v){
        Intent i = new Intent(this, registrarse.class);
        startActivity(i);
    }

    //ESTO SE VAAAAA
//    public void irPruebaConAlertaConConfirmacion(View v) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Confirmation")
//                .setMessage("There are some options that you can only see\nAre you sure to make this action?")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // La acción se realiza cuando el usuario presiona "OK"
//                        Intent i = new Intent(MainActivity.this, enAccion.class);
//                        startActivity(i);
//                    }
//                })
//                .setNegativeButton("Cancel", null) // No realiza ninguna acción si el usuario presiona "Cancelar"
//                .show();
//    }

    public static ArrayList<Ruta> getRutas() {
        return rutas;
    }

    public static void setRutas(ArrayList<Ruta> rutasNuevas) {
        rutas = rutasNuevas;
    }
}