/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Absrtact;


import Logica.GestorArchivos;
import java.util.*;

/**
 *
 * @author Rogelio
 */
public abstract class CuentaBase {
 
    protected String username;
    protected String nombreCompleto;
    protected String genero;
    protected String password;
    protected int    edad;
    protected String fechaRegistro;
    protected String tipoCuenta;
    protected String rutaFoto;
    protected String carpeta;
 
    public String getUsername()    { return username; }
    public String getNombre()      { return nombreCompleto; }
    public String getGenero()      { return genero; }
    public int    getEdad()        { return edad; }
    public String getFecha()       { return fechaRegistro; }
    public String getTipoCuenta()  { return tipoCuenta; }
    public String getRutaFoto()    { return rutaFoto; }
 
    public boolean esPublico()     { return "Publica".equalsIgnoreCase(tipoCuenta); }
 
    public boolean verificarPassword(String pass) { return this.password.equals(pass); }
 
    // polimorfismo — cada subclase define como se muestra en resumen
    public abstract String toResumen();
 
    protected List<String> leerLineas(String ruta) {
        return GestorArchivos.leerLineas(ruta);
    }
 
    protected void agregarSiNoExiste(String ruta, String valor) {
        List<String> lista = leerLineas(ruta);
        if (!lista.contains(valor.toLowerCase())) {
            GestorArchivos.escribirLinea(ruta, valor.toLowerCase());
        }
    }
 
    protected void eliminarLinea(String ruta, String valor) {
        List<String> lista = leerLineas(ruta);
        lista.remove(valor.toLowerCase());
        GestorArchivos.sobreescribir(ruta, lista);
    }
}
