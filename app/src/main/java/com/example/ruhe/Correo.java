package com.example.ruhe;

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
    private static String contacto, destinoUsuario, ubicacion;

    public static void enviarCorreo(String ubicacion) {
        ArrayList<Ruta> rutas = new ArrayList<>(MainActivity.getRutas());
        contacto = rutas.get(Ingresado.getIndex()).getContacto();
        destinoUsuario = rutas.get(Ingresado.getIndex()).getDestino();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        try {
            session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo, contra);
                }
            });

            if (session != null) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(correo));
                message.setSubject("EMERGENCIA: SU CONTACTO NO RESPONDE");
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(contacto));
                String url = "https://www.google.com/maps?q=" + ubicacion;
                String LinkUbicacion = "<a href=\"" + url + "\">" + url + "</a>";
                String url2 = "https://www.google.com/maps?q=" + destinoUsuario;
                String LinkDestino = "<a href=\"" + url2 + "\">" + url2 + "</a>";
                message.setContent("Hola, somos de la aplicación Ruhe, el envío de este mensaje se debe a que una persona que lo seleccionó a usted como contacto de emergencia, se encuentra en peligro, esta es la ubicación de la persona: \n" + LinkUbicacion + "\nY se dirigía a estas coordenadas: \n" + LinkDestino, "text/html; charset=utf-8");
                Transport.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
