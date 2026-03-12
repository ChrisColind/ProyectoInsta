/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 *
 * @author Rogelio
 */
public class Publicacion {
 
    protected String id;
    protected String autorUsername;
    protected String fecha;
    protected String hora;
    protected String contenido;
    protected String hashtags;
    protected String menciones;
    protected String rutaImagen;
    protected String tipoMultimedia;
    protected String proporcion;
 
    public Publicacion(String autor, String contenido, String hashtags,
                       String menciones, String rutaImagen, String tipoMultimedia, String proporcion) {
        this.autorUsername  = autor.toLowerCase();
        this.contenido      = contenido;
        this.hashtags       = hashtags;
        this.menciones      = menciones;
        this.rutaImagen     = rutaImagen;
        this.tipoMultimedia = tipoMultimedia;
        this.proporcion     = proporcion;
        this.fecha          = LocalDate.now().toString();
        this.hora           = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.id             = autorUsername + "_" + fecha + "_" + hora.replace(":", "");
    }
 
    public Publicacion() {}
 
    public String getId()             { return id; }
    public String getAutor()          { return autorUsername; }
    public String getFecha()          { return fecha; }
    public String getHora()           { return hora; }
    public String getContenido()      { return contenido; }
    public String getHashtags()       { return hashtags; }
    public String getMenciones()      { return menciones; }
    public String getRutaImagen()     { return rutaImagen; }
    public String getTipoMultimedia() { return tipoMultimedia; }
    public String getProporcion()     { return proporcion; }
 
    public boolean contieneHashtag(String tag) {
        if (hashtags == null || hashtags.isEmpty()) return false;
        return hashtags.toLowerCase().contains(tag.toLowerCase());
    }
 
    public boolean mencionaA(String username) {
        if (menciones == null || menciones.isEmpty()) return false;
        return menciones.toLowerCase().contains("@" + username.toLowerCase());
    }
 
    public void guardar() {
        String carpeta = SistemaArchivos.RAIZ + autorUsername + "/publicaciones/";
        new File(carpeta).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(carpeta + id + ".pub"))) {
            bw.write("id="             + id);             bw.newLine();
            bw.write("autor="          + autorUsername);  bw.newLine();
            bw.write("fecha="          + fecha);          bw.newLine();
            bw.write("hora="           + hora);           bw.newLine();
            bw.write("contenido="      + contenido);      bw.newLine();
            bw.write("hashtags="       + hashtags);       bw.newLine();
            bw.write("menciones="      + menciones);      bw.newLine();
            bw.write("rutaImagen="     + (rutaImagen != null ? rutaImagen : "")); bw.newLine();
            bw.write("tipoMultimedia=" + tipoMultimedia); bw.newLine();
            bw.write("proporcion="     + proporcion);     bw.newLine();
        } catch (IOException e) {
            System.out.println("Error guardando publicacion: " + e.getMessage());
        }
    }
 
    public static Publicacion cargar(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) return null;
        Publicacion p = new Publicacion();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if      (linea.startsWith("id="))             p.id             = linea.substring(3);
                else if (linea.startsWith("autor="))          p.autorUsername  = linea.substring(6);
                else if (linea.startsWith("fecha="))          p.fecha          = linea.substring(6);
                else if (linea.startsWith("hora="))           p.hora           = linea.substring(5);
                else if (linea.startsWith("contenido="))      p.contenido      = linea.substring(10);
                else if (linea.startsWith("hashtags="))       p.hashtags       = linea.substring(9);
                else if (linea.startsWith("menciones="))      p.menciones      = linea.substring(10);
                else if (linea.startsWith("rutaImagen="))     p.rutaImagen     = linea.substring(11);
                else if (linea.startsWith("tipoMultimedia=")) p.tipoMultimedia = linea.substring(15);
                else if (linea.startsWith("proporcion="))     p.proporcion     = linea.substring(11);
            }
        } catch (IOException e) { return null; }
        return p;
    }
 
    // recursividad: recorre subcarpetas buscando archivos .pub
    public static List<Publicacion> cargarTodasDeUsuario(String username) {
        List<Publicacion> lista = new ArrayList<>();
        File carpeta = new File(SistemaArchivos.RAIZ + username + "/publicaciones/");
        for (File f : SistemaArchivos.leerArchivosRecursivo(carpeta, ".pub")) {
            Publicacion p = cargar(f.getAbsolutePath());
            if (p != null) lista.add(p);
        }
        return lista;
    }
 
    public static List<Publicacion> buscarPorHashtag(String tag) {
        List<Publicacion> resultado = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (String u : SistemaArchivos.getTodosLosUsuarios()) {
            Usuario usr = Usuario.cargarDesdeArchivo(u);
            if (usr == null || !usr.esActivo()) continue;
            for (Publicacion p : cargarTodasDeUsuario(u)) {
                if (p.contieneHashtag(tag) && ids.add(p.getId()))
                    resultado.add(p);
            }
        }
        return resultado;
    }
 
    public static List<Publicacion> buscarMenciones(String username) {
        List<Publicacion> resultado = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        for (String u : SistemaArchivos.getTodosLosUsuarios()) {
            Usuario usr = Usuario.cargarDesdeArchivo(u);
            if (usr == null || !usr.esActivo()) continue;
            for (Publicacion p : cargarTodasDeUsuario(u)) {
                if (p.mencionaA(username) && ids.add(p.getId()))
                    resultado.add(p);
            }
        }
        return resultado;
    }
}
