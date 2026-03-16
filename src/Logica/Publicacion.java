/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Interfaces.InteractuarUser;
import PEnums.Enums.TipoMultimedia;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rogelio
 */
public class Publicacion implements InteractuarUser {

    protected String id;
    protected String autorUsername;
    protected String fecha;
    protected String hora;
    protected String contenido;
    protected String hashtags;
    protected String menciones;
    protected String rutaImagen;
    protected TipoMultimedia tipoMultimedia;
    protected String proporcion;

    public Publicacion(String autor, String contenido, String hashtags,
            String menciones, String rutaImagen, TipoMultimedia tipoMultimedia, String proporcion) {
        this.autorUsername = autor.toLowerCase();
        this.contenido = contenido;
        this.hashtags = hashtags;
        this.menciones = menciones;
        this.rutaImagen = rutaImagen;
        this.tipoMultimedia = tipoMultimedia;
        this.proporcion = proporcion;
        this.fecha = LocalDate.now().toString();
        this.hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.id = autorUsername + "_" + fecha + "_" + hora.replace(":", "");
    }

    public Publicacion() {
    }

    public String getId() {
        return id;
    }

    public String getAutor() {
        return autorUsername;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getContenido() {
        return contenido;
    }

    public String getHashtags() {
        return hashtags;
    }

    public String getMenciones() {
        return menciones;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public TipoMultimedia getTipoMultimedia() {
        return tipoMultimedia;
    }

    public String getProporcion() {
        return proporcion;
    }

    public boolean contieneHashtag(String tag) {
        if (hashtags == null || hashtags.isEmpty()) {
            return false;
        }
        return hashtags.toLowerCase().contains(tag.toLowerCase());
    }

    public boolean mencionaA(String username) {
        if (menciones == null || menciones.isEmpty()) {
            return false;
        }
        return menciones.toLowerCase().contains("@" + username.toLowerCase());
    }

    // implementacion de Interactuable
    @Override
    public void comentar(String username, String texto) {
        String ruta = GestorArchivos.RAIZ + autorUsername + "/publicaciones/" + id + ".comentarios";
        GestorArchivos.escribirLinea(ruta, username + ": " + texto);
    }

    @Override
    public void mencionar(String username) {
        if (menciones == null) {
            menciones = "";
        }
        if (!menciones.contains("@" + username.toLowerCase())) {
            menciones += " @" + username.toLowerCase();
        }
    }

    @Override
    public void reaccionar(String username) {
        String ruta = GestorArchivos.RAIZ + autorUsername + "/publicaciones/" + id + ".likes";
        List<String> likes = GestorArchivos.leerLineas(ruta);
        if (!likes.contains(username.toLowerCase())) {
            GestorArchivos.escribirLinea(ruta, username.toLowerCase());
        }
    }

    public List<String> getComentarios() {
        return GestorArchivos.leerLineas(
                GestorArchivos.RAIZ + autorUsername + "/publicaciones/" + id + ".comentarios"
        );
    }

    public int getTotalLikes() {
        return GestorArchivos.leerLineas(
                GestorArchivos.RAIZ + autorUsername + "/publicaciones/" + id + ".likes"
        ).size();
    }

    public void guardar() {
        String carpeta = GestorArchivos.RAIZ + autorUsername + "/publicaciones/";
        new File(carpeta).mkdirs();
        String datos = "id=" + id + "\n"
                + "autor=" + autorUsername + "\n"
                + "fecha=" + fecha + "\n"
                + "hora=" + hora + "\n"
                + "contenido=" + contenido + "\n"
                + "hashtags=" + (hashtags != null ? hashtags : "") + "\n"
                + "menciones=" + (menciones != null ? menciones : "") + "\n"
                + "rutaImagen=" + (rutaImagen != null ? rutaImagen : "") + "\n"
                + "tipoMultimedia=" + tipoMultimedia.name() + "\n"
                + "proporcion=" + proporcion;
        GestorArchivos.guardarPublicacionBinaria(autorUsername, id, datos);
    }

    public static Publicacion cargar(String ruta) {
        String raw = GestorArchivos.leerPublicacionBinaria(ruta);
        if (raw == null) {
            return null;
        }
        Publicacion p = new Publicacion();
        for (String linea : raw.split("\n")) {
            if (linea.startsWith("id=")) {
                p.id = linea.substring(3);
            } else if (linea.startsWith("autor=")) {
                p.autorUsername = linea.substring(6);
            } else if (linea.startsWith("fecha=")) {
                p.fecha = linea.substring(6);
            } else if (linea.startsWith("hora=")) {
                p.hora = linea.substring(5);
            } else if (linea.startsWith("contenido=")) {
                p.contenido = linea.substring(10);
            } else if (linea.startsWith("hashtags=")) {
                p.hashtags = linea.substring(9);
            } else if (linea.startsWith("menciones=")) {
                p.menciones = linea.substring(10);
            } else if (linea.startsWith("rutaImagen=")) {
                p.rutaImagen = linea.substring(11).trim();
            } else if (linea.startsWith("tipoMultimedia=")) {
                p.tipoMultimedia = TipoMultimedia.valueOf(linea.substring(15).trim());
            } else if (linea.startsWith("proporcion=")) {
                p.proporcion = linea.substring(11);
            }
        }
        if (p.rutaImagen != null && p.rutaImagen.isEmpty()) {
            p.rutaImagen = null;
        }
        return p;
    }

    public static List<Publicacion> cargarTodasDeUsuario(String username) {
        List<Publicacion> lista = new ArrayList<>();
        File carpeta = new File(GestorArchivos.RAIZ + username + "/publicaciones/");
        for (File f : GestorArchivos.buscarArchivosRecursivo(carpeta, ".pub")) {
            Publicacion p = cargar(f.getAbsolutePath());
            if (p != null) {
                lista.add(p);
            }
        }
        return lista;
    }
}
