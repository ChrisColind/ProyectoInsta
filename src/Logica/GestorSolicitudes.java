/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.List;
/**
 *
 * @author Rogelio
 */
public class GestorSolicitudes {

    private static String rutaSolicitudes(String username) {
        return GestorArchivos.RAIZ + username.toLowerCase() + "/solicitudes.ins";
    }

    public static void enviarSolicitud(String de, String para) {
        String ruta = rutaSolicitudes(para);
        List<String> lista = GestorArchivos.leerLineas(ruta);
        if (!lista.contains(de.toLowerCase())) {
            GestorArchivos.escribirLinea(ruta, de.toLowerCase());
        }
    }

    public static void aceptarSolicitud(String yo, String solicitante) {
        rechazarSolicitud(yo, solicitante);
        GestorUsuarios.seguir(solicitante, yo);
    }

    public static void rechazarSolicitud(String yo, String solicitante) {
        String ruta = rutaSolicitudes(yo);
        List<String> lista = GestorArchivos.leerLineas(ruta);
        lista.remove(solicitante.toLowerCase());
        GestorArchivos.sobreescribir(ruta, lista);
    }

    public static List<String> getSolicitudes(String username) {
        return GestorArchivos.leerLineas(rutaSolicitudes(username));
    }

    public static boolean tieneSolicitudPendiente(String de, String para) {
        return GestorArchivos.leerLineas(rutaSolicitudes(para)).contains(de.toLowerCase());
    }
}