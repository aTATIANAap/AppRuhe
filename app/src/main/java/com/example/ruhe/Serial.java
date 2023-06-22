package com.example.ruhe;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Serial {
    private static final String nombreArchivo = "archivoRutas";

    public static Boolean cargarArchivo(){
        Boolean cond = true;
        try{
            FileInputStream archivoGuardado = new FileInputStream(nombreArchivo);
            ObjectInputStream archivo = new ObjectInputStream(archivoGuardado);

            ArrayList<Ruta> listaRutasCargadas = new ArrayList<Ruta>();
            Boolean band = true;
            while(band){
                try{
                    Ruta ruta = (Ruta)archivo.readObject();
                    listaRutasCargadas.add(ruta);
                }
                catch(EOFException error){
                    System.out.println(error+" Limite de lista alcanzado");
                    band = false;
                }

            }

            if(listaRutasCargadas.isEmpty()){
                System.out.println("No habian rutas guardados previamente");
            }
            else{
                MainActivity.setRutas(listaRutasCargadas);

                System.out.println("Archivo cargado exitosamente");
            }
        }
        catch(FileNotFoundException e){
            System.out.println(e+" No se encontro un archivo guardado");
            cond = false;
        }
        catch(IOException e){
            System.out.println(e+" No se pudo leer correctamente el archivo");
        }
        catch(ClassNotFoundException e){
            System.out.println(e+" No se encontro la clase del objeto guardado");
        }
        return cond;
    }

    public static String guardarArchivo(ArrayList listaRutasNueva){
        try{
            FileOutputStream archivoGuardado = new FileOutputStream("datosContactos.obj");
            ObjectOutputStream archivo = new ObjectOutputStream(archivoGuardado);

            for(int i=0;i<listaRutasNueva.size();i++){
                archivo.writeObject(listaRutasNueva.get(i));
            }

            archivo.close();
            System.out.println("Archivo guardado exitosamente");
            return "Archivo guardado exitosamente";
        }
        catch(FileNotFoundException e){
            System.out.println(e+" No se pudo guardar el archivo correctamente");
            return "No se pudo guardar el archivo correctamente";
        }
        catch(IOException e){
            System.out.println(e+" No se pudo guardar uno o varios objetos correctamente");
            return "No se pudo guardar uno o varios objetos correctamente";
        }
    }

}