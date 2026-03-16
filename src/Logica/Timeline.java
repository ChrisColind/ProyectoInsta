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
public class Timeline {
 
    private String username;
 
    public Timeline(String username) {
        this.username = username;
    }
 
    public List<Publicacion> getFeed() {
        return GestorPublicaciones.getFeed(username);
    }
 
    public List<Publicacion> getMenciones() {
        return GestorPublicaciones.buscarMenciones(username);
    }
}