/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;


import java.io.*;
import java.time.LocalDate;
import java.util.*;
import Absrtact.CuentaBase;
/**
 *
 * @author Rogelio
 */
public class Usuario extends CuentaBase {
 
    private String estado;
 
    public Usuario(String username, String nombreCompleto, String genero,
                   String password, int edad, String tipoCuenta, String rutaFoto) {
        this.username       = username.toLowerCase();
        this.nombreCompleto = nombreCompleto;
        this.genero         = genero;
        this.password       = password;
        this.edad           = edad;
        this.fechaRegistro  = LocalDate.now().toString();
        this.estado         = "Activo";
        this.tipoCuenta     = tipoCuenta;
        this.rutaFoto       = rutaFoto;
        this.carpeta        = SistemaArchivos.RAIZ + this.username + "/";
    }
 
    // constructor minimo para cargar desde archivo
    public Usuario(String username, String password) {
        this.username = username.toLowerCase();
        this.password = password;
        this.carpeta  = SistemaArchivos.RAIZ + this.username + "/";
    }
 
    public String getEstado()  { return estado; }
    public boolean esActivo()  { return "Activo".equalsIgnoreCase(estado); }
 
    @Override
    public String toResumen() {
        return username + " | " + nombreCompleto + " | " + tipoCuenta + " | " + estado;
    }
 
    public void guardar() {
        SistemaArchivos.crearEstructuraUsuario(username);
        String ruta = carpeta + "insta.ins";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("username="     + username);       bw.newLine();
            bw.write("nombre="       + nombreCompleto); bw.newLine();
            bw.write("genero="       + genero);         bw.newLine();
            bw.write("password="     + password);       bw.newLine();
            bw.write("edad="         + edad);           bw.newLine();
            bw.write("fechaRegistro="+ fechaRegistro);  bw.newLine();
            bw.write("estado="       + estado);         bw.newLine();
            bw.write("tipoCuenta="   + tipoCuenta);     bw.newLine();
            bw.write("rutaFoto="     + (rutaFoto != null ? rutaFoto : "")); bw.newLine();
        } catch (IOException e) {
            System.out.println("Error guardando usuario: " + e.getMessage());
        }
    }
 
    public static Usuario cargarDesdeArchivo(String username) {
        String ruta = SistemaArchivos.RAIZ + username.toLowerCase() + "/insta.ins";
        File f = new File(ruta);
        if (!f.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String user = "", nombre = "", genero = "", pass = "";
            String fecha = "", estado = "", tipo = "", foto = "";
            int edad = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                if      (linea.startsWith("username="))     user   = linea.substring(9);
                else if (linea.startsWith("nombre="))       nombre = linea.substring(7);
                else if (linea.startsWith("genero="))       genero = linea.substring(7);
                else if (linea.startsWith("password="))     pass   = linea.substring(9);
                else if (linea.startsWith("edad="))         edad   = Integer.parseInt(linea.substring(5).trim());
                else if (linea.startsWith("fechaRegistro="))fecha  = linea.substring(14);
                else if (linea.startsWith("estado="))       estado = linea.substring(7);
                else if (linea.startsWith("tipoCuenta="))   tipo   = linea.substring(11);
                else if (linea.startsWith("rutaFoto="))     foto   = linea.substring(9);
            }
            Usuario u = new Usuario(user, nombre, genero, pass, edad, tipo, foto.isEmpty() ? null : foto);
            u.fechaRegistro = fecha;
            u.estado        = estado;
            return u;
        } catch (IOException e) { return null; }
    }
 
    public List<String> getFollowers() { return leerLineas(carpeta + "followers.ins"); }
    public List<String> getFollowing() { return leerLineas(carpeta + "following.ins"); }
 
    public boolean sigueA(String otro) { return getFollowing().contains(otro.toLowerCase()); }
 
    public void seguir(String otro) {
        String t = otro.toLowerCase();
        agregarSiNoExiste(carpeta + "following.ins", t);
        agregarSiNoExiste(SistemaArchivos.RAIZ + t + "/followers.ins", username);
    }
 
    public void dejarDeSeguir(String otro) {
        String t = otro.toLowerCase();
        eliminarLinea(carpeta + "following.ins", t);
        eliminarLinea(SistemaArchivos.RAIZ + t + "/followers.ins", username);
    }
 
    // verifica si puede ver el perfil/mensajes de otro usuario
    public boolean puedeVerA(Usuario otro) {
        if (!otro.esActivo()) return false;
        if (otro.esPublico()) return true;
        return sigueA(otro.getUsername()) && otro.sigueA(this.username);
    }
}