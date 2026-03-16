/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Absrtact.Mensaje;
import java.util.List;

/**
 *
 * @author Rogelio
 */
public class GestorInbox {
 
    public static void enviarTexto(String de, String para, String texto) {
        if (!GestorUsuarios.puedeVer(de, para)) return;
        Mensaje m = new MensajeTexto(de, para, texto);
        new Conversacion(de, para).enviar(m);
    }
 
    public static void enviarSticker(String de, String para, String sticker) {
        if (!GestorUsuarios.puedeVer(de, para)) return;
        Mensaje m = new MensajeSticker(de, para, sticker);
        new Conversacion(de, para).enviar(m);
    }
 
    public static List<Mensaje> getMensajes(String yo, String otro) {
        return new Conversacion(yo, otro).getMensajes();
    }
 
    public static void marcarLeidos(String yo, String otro) {
        new Conversacion(yo, otro).marcarTodosLeidos();
    }
 
    public static void eliminarConversacion(String yo, String otro) {
        new Conversacion(yo, otro).eliminar();
    }
 
    public static int getNoLeidos(String yo, String otro) {
        return new Conversacion(yo, otro).getMensajesNoLeidos();
    }
 
    public static List<String> getConversaciones(String username) {
        return Conversacion.getConversaciones(username);
    }
 
    public static boolean tieneNotificaciones(String username) {
        for (String otro : getConversaciones(username))
            if (getNoLeidos(username, otro) > 0) return true;
        return false;
    }
}