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
public class Buscador {
 
    public static List<String> buscarUsuarios(String texto) {
        return GestorUsuarios.buscarPorCoincidencia(texto);
    }
 
    public static List<Publicacion> buscarHashtag(String tag) {
        String tagLimpio = tag.startsWith("#") ? tag : "#" + tag;
        return GestorPublicaciones.buscarPorHashtag(tagLimpio);
    }
 
    public static Usuario getPerfilCompleto(String username) {
        return Usuario.cargarDesdeArchivo(username);
    }
}