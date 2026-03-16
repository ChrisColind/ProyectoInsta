/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;
 
import java.io.*;
import java.util.*;
/**
 *
 * @author Rogelio
 */
public class GestorArchivos {
 
    public static final String RAIZ              = "INSTA_RAIZ/";
    public static final String STICKERS_GLOBALES = RAIZ + "stickers_globales/";
 
    private static final String[] STICKERS_DEFAULT = {
        "Feliz.png", "Triste.png", "Corazon.png", "Risa.png", "Aplauso.png"
    };
 
    public static void inicializar() {
        crearCarpeta(RAIZ);
        crearCarpeta(STICKERS_GLOBALES);
        crearArchivo(RAIZ + "users.ins");
        for (String s : STICKERS_DEFAULT) {
            File f = new File(STICKERS_GLOBALES + s);
            if (!f.exists()) try { f.createNewFile(); } catch (IOException e) { }
        }
    }
 
    public static void crearEstructuraUsuario(String username) {
        String base = RAIZ + username.toLowerCase() + "/";
        crearCarpeta(base);
        crearCarpeta(base + "imagenes/");
        crearCarpeta(base + "folders_personales/");
        crearCarpeta(base + "stickers_personales/");
        crearCarpeta(base + "publicaciones/");
        crearArchivo(base + "followers.ins");
        crearArchivo(base + "following.ins");
        crearArchivo(base + "insta.ins");
        crearArchivo(base + "inbox.ins");
        crearArchivo(base + "stickers.ins");
    }
 
    public static boolean registrarUsername(String username) {
        String ruta = RAIZ + "users.ins";
        List<String> lista = leerLineas(ruta);
        if (lista.contains(username.toLowerCase())) return false;
        escribirLinea(ruta, username.toLowerCase());
        return true;
    }
 
    public static boolean usuarioExiste(String username) {
        return leerLineas(RAIZ + "users.ins").contains(username.toLowerCase());
    }
 
    public static List<String> getTodosLosUsuarios() {
        return leerLineas(RAIZ + "users.ins");
    }
 
    // guarda un usuario usando RandomAccessFile (binario)
    public static void guardarUsuarioBinario(String username, String datos) {
        String ruta = RAIZ + username.toLowerCase() + "/insta.ins";
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "rw")) {
            raf.setLength(0);
            raf.writeUTF(datos);
        } catch (IOException e) {
            System.out.println("Error guardando usuario: " + e.getMessage());
        }
    }
 
    public static String leerUsuarioBinario(String username) {
        String ruta = RAIZ + username.toLowerCase() + "/insta.ins";
        File f = new File(ruta);
        if (!f.exists()) return null;
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            return raf.readUTF();
        } catch (IOException e) {
            return null;
        }
    }
 
    // guarda una publicacion usando RandomAccessFile
    public static void guardarPublicacionBinaria(String username, String id, String datos) {
        String carpeta = RAIZ + username.toLowerCase() + "/publicaciones/";
        new File(carpeta).mkdirs();
        try (RandomAccessFile raf = new RandomAccessFile(carpeta + id + ".pub", "rw")) {
            raf.setLength(0);
            raf.writeUTF(datos);
        } catch (IOException e) {
            System.out.println("Error guardando publicacion: " + e.getMessage());
        }
    }
 
    public static String leerPublicacionBinaria(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) return null;
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            return raf.readUTF();
        } catch (IOException e) {
            return null;
        }
    }
 
    // recorre recursivamente carpetas buscando archivos con cierta extension
    public static List<File> buscarArchivosRecursivo(File carpeta, String extension) {
        List<File> resultado = new ArrayList<>();
        if (carpeta == null || !carpeta.exists()) return resultado;
        File[] hijos = carpeta.listFiles();
        if (hijos == null) return resultado;
        for (File f : hijos) {
            if (f.isDirectory()) resultado.addAll(buscarArchivosRecursivo(f, extension));
            else if (f.getName().endsWith(extension)) resultado.add(f);
        }
        return resultado;
    }
 
    public static List<String> getStickersPorDefecto() {
        return new ArrayList<>(Arrays.asList(STICKERS_DEFAULT));
    }
 
    public static List<String> getStickersUsuario(String username) {
        List<String> lista = getStickersPorDefecto();
        for (String s : leerLineas(RAIZ + username + "/stickers.ins"))
            if (!lista.contains(s)) lista.add(s);
        return lista;
    }
 
    public static void importarSticker(String username, String nombre) {
        String ruta = RAIZ + username + "/stickers.ins";
        if (!leerLineas(ruta).contains(nombre)) escribirLinea(ruta, nombre);
    }
 
    public static List<String> leerLineas(String ruta) {
        List<String> lista = new ArrayList<>();
        File f = new File(ruta);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null)
                if (!linea.trim().isEmpty()) lista.add(linea.trim());
        } catch (IOException e) { }
        return lista;
    }
 
    public static void escribirLinea(String ruta, String contenido) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            bw.write(contenido);
            bw.newLine();
        } catch (IOException e) { }
    }
 
    public static void sobreescribir(String ruta, List<String> lineas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, false))) {
            for (String l : lineas) { bw.write(l); bw.newLine(); }
        } catch (IOException e) { }
    }
 
    private static void crearCarpeta(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) f.mkdirs();
    }
 
    private static void crearArchivo(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) try { f.createNewFile(); } catch (IOException e) { }
    }
}
