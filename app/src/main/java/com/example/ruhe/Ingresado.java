package com.example.ruhe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Ingresado extends AppCompatActivity {

    private FirebaseAuth auth;
    private ListView listView;
    private static int index;
    private ArrayList<String> rutasMostrar;
    private String opcion, encontrada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresado);
        Toast.makeText(Ingresado.this, "Remember your contact to check their mail", Toast.LENGTH_SHORT).show();

        auth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listViewRutas);
        rutasMostrar = new ArrayList<>();

        for (int i = 0; i < MainActivity.getRutas().size(); i++) {
            rutasMostrar.add(MainActivity.getRutas().get(i).getNombreRuta() + "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, rutasMostrar);
        listView.setAdapter(adapter);

    }

    public void irAgregarRuta(View v) {
        Intent i = new Intent(this, AgregarRuta.class);
        startActivity(i);
    }


    public void cerrarSesion(View v) {
        auth.signOut();
        if (auth.getCurrentUser() == null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    public void escogerRuta(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your route.");
        // Inflar el diseño XML del EditText
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(view);
        EditText editText = view.findViewById(R.id.editText);

        // Configurar botón "Aceptar"
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                opcion = editText.getText().toString();

                // Cerrar el cuadro de diálogo
                for (Ruta rutaTraida : MainActivity.getRutas()) {
                    if (rutaTraida.getNombreRuta().equals(opcion)) {
                        // Guardar el valor ingresado en SharedPreferences
                        index = MainActivity.getRutas().indexOf(rutaTraida);
                        dialog.dismiss();
                        encontrada = "We found your route";
                        Intent i = new Intent(Ingresado.this, enAccion.class);
                        startActivity(i);
                    } else {
                        encontrada = "We did not find your route";
                    }
                }
                Toast.makeText(Ingresado.this, encontrada, Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar botón "Cancelar"
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    public static int getIndex() {
        return index;
    }
}