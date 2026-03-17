/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import PEnums.Enums.EstadoCuenta;
import PEnums.Enums.Genero;
import PEnums.Enums.TipoCuenta;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Rogelio
 */
public class Usuario extends EntidadBase {

    public Usuario(String username, String nombreCompleto, String genero,
            String password, int edad, String tipoCuenta, String rutaFoto) {
        this.username = username.toLowerCase();
        this.nombreCompleto = nombreCompleto;
        this.genero = genero.equalsIgnoreCase("M") ? Genero.M : Genero.F;
        this.password = password;
        this.edad = edad;
        this.fechaCreacion = LocalDate.now().toString();
        this.estado = EstadoCuenta.ACTIVO;
        this.tipoCuenta = tipoCuenta.equalsIgnoreCase("Publica") ? TipoCuenta.PUBLICA : TipoCuenta.PRIVADA;
        this.rutaFoto = rutaFoto;
        this.carpeta = GestorArchivos.RAIZ + this.username + "/";
    }

    public Usuario() {
    }

    @Override
    public String toResumen() {
        return username + " | " + nombreCompleto + " | " + tipoCuenta + " | " + estado;
    }

    public void guardar() { 
        GestorArchivos.crearEstructuraUsuario(username);
        String datos = "username=" + username + "\n"
                + "nombre=" + nombreCompleto + "\n"
                + "genero=" + genero.name() + "\n"
                + "password=" + password + "\n"
                + "edad=" + edad + "\n"
                + "fechaCreacion=" + fechaCreacion + "\n"
                + "estado=" + estado.name() + "\n"
                + "tipoCuenta=" + tipoCuenta.name() + "\n"
                + "rutaFoto=" + (rutaFoto != null ? rutaFoto : "");
        GestorArchivos.guardarUsuarioBinario(username, datos);
    }

    public static Usuario cargarDesdeArchivo(String username) {
        String raw = GestorArchivos.leerUsuarioBinario(username);
        if (raw == null) {
            return null;
        }
        Usuario u = new Usuario();
        for (String linea : raw.split("\n")) {
            if (linea.startsWith("username=")) {
                u.username = linea.substring(9);
            } else if (linea.startsWith("nombre=")) {
                u.nombreCompleto = linea.substring(7);
            } else if (linea.startsWith("genero=")) {
                u.genero = Genero.valueOf(linea.substring(7).trim());
            } else if (linea.startsWith("password=")) {
                u.password = linea.substring(9);
            } else if (linea.startsWith("edad=")) {
                u.edad = Integer.parseInt(linea.substring(5).trim());
            } else if (linea.startsWith("fechaCreacion=")) {
                u.fechaCreacion = linea.substring(14);
            } else if (linea.startsWith("estado=")) {
                u.estado = EstadoCuenta.valueOf(linea.substring(7).trim());
            } else if (linea.startsWith("tipoCuenta=")) {
                u.tipoCuenta = TipoCuenta.valueOf(linea.substring(11).trim());
            } else if (linea.startsWith("rutaFoto=")) {
                u.rutaFoto = linea.substring(9).trim();
            }
        }
        u.carpeta = GestorArchivos.RAIZ + u.username + "/";
        if (u.rutaFoto != null && !u.rutaFoto.isEmpty()) {
            String nombreArchivo = new File(u.rutaFoto).getName();
            u.rutaFoto = GestorArchivos.RAIZ + u.username + "/imagenes/" + nombreArchivo;
        } else {
            u.rutaFoto = null;
        }
        return u;
    }

    public List<String> getFollowers() {
        return leerLineas(carpeta + "followers.ins");
    }

    public List<String> getFollowing() {
        return leerLineas(carpeta + "following.ins");
    }

    public boolean sigueA(String otro) {
        return getFollowing().contains(otro.toLowerCase());
    }
    
    public void setRutaFoto(String ruta) {
        this.rutaFoto = ruta;
    }
    public boolean puedeVerA(Usuario otro) {
        if (!otro.esActivo()) {
            return false;
        }
        if (otro.esPublico()) {
            return true;
        }
        return sigueA(otro.getUsername()) && otro.sigueA(this.username);
    }
    
    public String getFechaCreacion() {
        return fechaCreacion;
    }
}
