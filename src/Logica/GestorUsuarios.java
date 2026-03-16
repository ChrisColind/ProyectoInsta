/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import PEnums.Enums.EstadoCuenta;
import java.util.*;
/**
 *
 * @author Rogelio
 */
public class GestorUsuarios {
 
    public static boolean registrar(String username, String nombreCompleto, String genero,
                                    String password, int edad, String tipoCuenta, String rutaFoto) {
        if (GestorArchivos.usuarioExiste(username)) return false;
 
        Usuario u = new Usuario(username, nombreCompleto, genero, password, edad, tipoCuenta, rutaFoto);
        u.guardar();
        GestorArchivos.registrarUsername(username);
        return true;
    }
 
    public static Usuario login(String username, String password) {
        Usuario u = Usuario.cargarDesdeArchivo(username);
        if (u == null) return null;
        if (!u.esActivo()) return null;
        if (!u.verificarPassword(password)) return null;
        return u;
    }
 
    public static void seguir(String yo, String otro) {
        String base    = GestorArchivos.RAIZ;
        String myFollow = base + yo.toLowerCase() + "/following.ins";
        String hisFoll  = base + otro.toLowerCase() + "/followers.ins";
 
        List<String> myList = GestorArchivos.leerLineas(myFollow);
        if (!myList.contains(otro.toLowerCase())) {
            GestorArchivos.escribirLinea(myFollow, otro.toLowerCase());
        }
 
        List<String> hisList = GestorArchivos.leerLineas(hisFoll);
        if (!hisList.contains(yo.toLowerCase())) {
            GestorArchivos.escribirLinea(hisFoll, yo.toLowerCase());
        }
    }
 
    public static void dejarDeSeguir(String yo, String otro) {
        String base     = GestorArchivos.RAIZ;
        String myFollow = base + yo.toLowerCase() + "/following.ins";
        String hisFoll  = base + otro.toLowerCase() + "/followers.ins";
 
        List<String> myList = GestorArchivos.leerLineas(myFollow);
        myList.remove(otro.toLowerCase());
        GestorArchivos.sobreescribir(myFollow, myList);
 
        List<String> hisList = GestorArchivos.leerLineas(hisFoll);
        hisList.remove(yo.toLowerCase());
        GestorArchivos.sobreescribir(hisFoll, hisList);
    }
 
    public static boolean sigueA(String yo, String otro) {
        String ruta = GestorArchivos.RAIZ + yo.toLowerCase() + "/following.ins";
        return GestorArchivos.leerLineas(ruta).contains(otro.toLowerCase());
    }
 
    public static List<String> getFollowers(String username) {
        return GestorArchivos.leerLineas(GestorArchivos.RAIZ + username.toLowerCase() + "/followers.ins");
    }
 
    public static List<String> getFollowing(String username) {
        return GestorArchivos.leerLineas(GestorArchivos.RAIZ + username.toLowerCase() + "/following.ins");
    }
 
    public static boolean sonAmigos(String a, String b) {
        return sigueA(a, b) && sigueA(b, a);
    }
 
    public static boolean puedeVer(String yo, String otro) {
        Usuario u = Usuario.cargarDesdeArchivo(otro);
        if (u == null || !u.esActivo()) return false;
        if (u.esPublico()) return true;
        return sonAmigos(yo, otro);
    }
 
    public static List<String> buscarPorCoincidencia(String texto) {
        List<String> resultado = new java.util.ArrayList<>();
        for (String u : GestorArchivos.getTodosLosUsuarios()) {
            Usuario usr = Usuario.cargarDesdeArchivo(u);
            if (usr != null && usr.esActivo() && u.contains(texto.toLowerCase()))
                resultado.add(u);
        }
        return resultado;
    }
 
    public static void activar(String username) {
        Usuario u = Usuario.cargarDesdeArchivo(username);
        if (u != null) { u.setEstado(EstadoCuenta.ACTIVO); u.guardar(); }
    }
 
    public static void desactivar(String username) {
        Usuario u = Usuario.cargarDesdeArchivo(username);
        if (u != null) { u.setEstado(EstadoCuenta.INACTIVO); u.guardar(); }
    }
}