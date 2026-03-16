/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.*;
/**
 *
 * @author Rogelio
 */
public class GestorPublicaciones {
 
    public static void publicar(Publicacion p) {
        p.guardar();
    }
 
    public static List<Publicacion> getPublicacionesDeUsuario(String username) {
        return Publicacion.cargarTodasDeUsuario(username);
    }
 
    // devuelve publicaciones de los usuarios que sigo para el feed
    public static List<Publicacion> getFeed(String username) {
        List<Publicacion> feed = new ArrayList<>();
        for (String seguido : GestorUsuarios.getFollowing(username)) {
            if (GestorUsuarios.puedeVer(username, seguido))
                feed.addAll(getPublicacionesDeUsuario(seguido));
        }
        feed.addAll(getPublicacionesDeUsuario(username));
        return feed;
    }
 
    // busca en todas las publicaciones de todos los usuarios activos por hashtag
    public static List<Publicacion> buscarPorHashtag(String tag) {
        ListaPublicaciones todas = new ListaPublicaciones();
        for (String u : GestorArchivos.getTodosLosUsuarios()) {
            Usuario usr = Usuario.cargarDesdeArchivo(u);
            if (usr == null || !usr.esActivo()) continue;
            for (Publicacion p : getPublicacionesDeUsuario(u))
                todas.agregar(p);
        }
        return todas.buscarHashtag(tag, todas.getCabeza(), new ArrayList<>());
    }
 
    public static List<Publicacion> buscarMenciones(String username) {
        List<Publicacion> resultado = new ArrayList<>();
        Set<String> vistos = new HashSet<>();
        for (String u : GestorArchivos.getTodosLosUsuarios()) {
            Usuario usr = Usuario.cargarDesdeArchivo(u);
            if (usr == null || !usr.esActivo()) continue;
            for (Publicacion p : getPublicacionesDeUsuario(u))
                if (p.mencionaA(username) && vistos.add(p.getId()))
                    resultado.add(p);
        }
        return resultado;
    }
}
