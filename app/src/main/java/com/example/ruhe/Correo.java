package com.example.ruhe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Correo extends AppCompatActivity {
    private static Session session;
    private static String correo = "ruheaplicacion@gmail.com";
    private static String contra = "gbqdomxhuovibywj";

    private static String ubicacion = "ubicacion";
    private static String contacto;

    public static void enviarCorreo(String ubicacion){
        ArrayList<Ruta> rutas= new ArrayList<>(MainActivity.getRutas());
        contacto=rutas.get(Ingresado.getIndex()).getContacto();
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
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("EMERGENCIA: SU CONTACTO NO RESPONDE");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(contacto));
                String url = "https://www.google.com/maps?q="+ubicacion;
                String clickableLink = "<a href=\"" + url + "\">" + url + "</a>";
                message.setContent("Hola, somos de la aplicación Ruhe, el envío de este mensaje se debe a que una persona que lo seleccionó a usted como contacto de emergencia, se encuentra en peligro, esta es la ubicación de la persona: \n"+clickableLink, "text/html; charset=utf-8");

                Transport.send(message);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //metodo de enviar ubi al contacto
    }
}
