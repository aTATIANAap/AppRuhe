package com.example.ruhe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Ingresado extends AppCompatActivity {

    FirebaseAuth auth;
    private ListView listView;
    private ArrayList<String> rutasMostrar;
    private String opcion, encontrada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresado);

        if(!Serial.cargarArchivo()){
            Serial.guardarArchivo(MainActivity.getRutas());
        }

        auth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listViewRutas);
        rutasMostrar=new ArrayList<>();

        for (int i=0;i< MainActivity.getRutas().size();i++){
            rutasMostrar.add(MainActivity.getRutas().get(i).getNombreRuta()+"");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,rutasMostrar);
        listView.setAdapter(adapter);

    }

    public void irAgregarRuta(View v){
        Intent i = new Intent(this, AgregarRuta.class);
        startActivity(i);
    }


    public void cerrarSesion(View v){
        auth.signOut();
        if (auth.getCurrentUser()==null){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    public void escogerRuta(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese una ruta");
        // Inflar el diseño XML del EditText
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        EditText editText = view.findViewById(R.id.editText);

        // Configurar botón "Aceptar"
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                opcion = editText.getText().toString();
                // Guardar el valor ingresado en SharedPreferences
                SharedPreferences preferences = getSharedPreferences("MyPrefs", Ingresado.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userInput", opcion);
                editor.apply();
                // Aquí puedes hacer algo con el dato ingresado por el usuario

                // Cerrar el cuadro de diálogo
                for (Ruta rutaTraida: MainActivity.getRutas())
                { if (rutaTraida.getNombreRuta().equals(opcion)){
                    dialog.dismiss();
                    Intent i = new Intent(Ingresado.this, enAccion.class);
                    startActivity(i);
                    encontrada="Se encontro la ruta.";
                }else{
                    encontrada="No se encontro la ruta.";}
                }
                Toast.makeText(Ingresado.this,encontrada,Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar botón "Cancelar"
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cerrar el cuadro de diálogo sin hacer ninguna acción adicional
                dialog.dismiss();
            }
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
}