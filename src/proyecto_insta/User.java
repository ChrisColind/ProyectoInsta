package proyecto_insta;

import Absrtact.ABSinstru;
import Interfaces.InteractuarUser;
import PEnums.Enums.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 *
 * @author Rogelio
 */
public class User extends ABSinstru implements InteractuarUser {

    private static final long serialVersionUID = 1L;

    public static final String RAIZ          = "INSTA_RAIZ/";
    public static final String ARCHIVO_USERS = RAIZ + "users.ins";

    private String       nombreCompleto;
    private Genero       genero;          
    private String       username;        
    private String       password;
    private String       fechaRegistro;   
    private int          edad;
    private EstadoCuenta estado;          
    private String       fotoPerfil;      
    private TipoCuenta   tipoCuenta;      

    public User(String nombreCompleto, Genero genero, String username,
                String password, int edad, String fotoPerfil, TipoCuenta tipoCuenta) {

        super(RAIZ + username + "/insta.ins");

        this.nombreCompleto = nombreCompleto;
        this.genero = genero;
        this.username = username;
        this.password = password;
        this.edad = edad;
        this.fotoPerfil = fotoPerfil;
        this.tipoCuenta = tipoCuenta;

        this.fechaRegistro  = LocalDate.now().toString();
        this.estado         = EstadoCuenta.ACTIVO;
    }

    protected boolean validar() {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()){ 
            return false;
        }
        
        if (username == null || username.trim().isEmpty()){
            return false;
        }
        
        if (password == null || password.trim().isEmpty()){
            return false;
        }
        
        if (edad <= 0){
            return false;
        }
        
        if (genero == null){
            return false;
        }
        
        if (tipoCuenta == null){
            return false;
        }
        
        return true;
    }

    @Override
    public void GuardarCuenta() throws Exception {
        if (!validar()) throw new Exception("Datos del usuario inválidos.");

        File carpeta = new File(RAIZ + username);
        if (!carpeta.exists()) carpeta.mkdirs();  

        new File(RAIZ + username + "/imagenes").mkdirs();
        new File(RAIZ + username + "/stickers_personales").mkdirs();
        new File(RAIZ + username + "/folders_personales").mkdirs();

        // Serialización del objeto actual
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(this);  
        }

        // Registro en lista global
        ArrayList<String> usuarios = cargarListaUsuarios();
        if (!usuarios.contains(username)) {
            usuarios.add(username);
            guardarListaUsuarios(usuarios);
        }
    }

    @Override
    public void Cargar() throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            User cargado = (User) ois.readObject(); 

            this.nombreCompleto = cargado.nombreCompleto;
            this.genero         = cargado.genero;
            this.password       = cargado.password;
            this.fechaRegistro  = cargado.fechaRegistro;
            this.edad           = cargado.edad;
            this.estado         = cargado.estado;
            this.fotoPerfil     = cargado.fotoPerfil;
            this.tipoCuenta     = cargado.tipoCuenta;
        }
    }

    @Override
    public void Eliminar() throws Exception {
        eliminarCarpeta(new File(RAIZ + username));

        ArrayList<String> usuarios = cargarListaUsuarios();
        usuarios.remove(username);
        guardarListaUsuarios(usuarios);
    }

    @Override
    public void Seguir(String usernameDestino) throws Exception {
        if (usernameDestino.equals(this.username)) return;
        if (!usuarioExiste(usernameDestino)) throw new Exception("Usuario no encontrado.");

        String rutaFollowing = RAIZ + username + "/following.ins";
        ArrayList<String> following = cargarLista(rutaFollowing);
        if (!following.contains(usernameDestino)) {
            following.add(usernameDestino);
            guardarLista(rutaFollowing, following);
        }

        String rutaFollowers = RAIZ + usernameDestino + "/followers.ins";
        ArrayList<String> followers = cargarLista(rutaFollowers);
        if (!followers.contains(this.username)) {
            followers.add(this.username);
            guardarLista(rutaFollowers, followers);
        }
    }

    @Override
    public void NoSeguir(String usernameDestino) throws Exception {
        String rutaFollowing = RAIZ + username + "/following.ins";
        ArrayList<String> following = cargarLista(rutaFollowing);
        following.remove(usernameDestino);
        guardarLista(rutaFollowing, following);

        String rutaFollowers = RAIZ + usernameDestino + "/followers.ins";
        ArrayList<String> followers = cargarLista(rutaFollowers);
        followers.remove(this.username);
        guardarLista(rutaFollowers, followers);
    }

    @Override
    public void EnviarMensaje(String receptor, String contenido, TipoMensaje tipo) throws Exception {
        if (contenido.length() > 300) throw new Exception("Mensaje excede 300 caracteres.");

        User usuarioReceptor = buscarPorUsername(receptor);
        if (usuarioReceptor == null) throw new Exception("Usuario receptor no encontrado.");

        if (usuarioReceptor.getTipoCuenta() == TipoCuenta.PRIVADA) {
            boolean yoLoSigo  = SigueA(receptor);
            boolean elMeSigue = usuarioReceptor.SigueA(this.username);
            if (!yoLoSigo || !elMeSigue) {
                throw new Exception("No puedes enviar mensajes a una cuenta privada sin ser amigos.");
            }
        }
        
    }

    @Override
    public boolean SigueA(String usernameDestino) throws Exception {
        String rutaFollowing = RAIZ + username + "/following.ins";
        ArrayList<String> following = cargarLista(rutaFollowing);
        return following.contains(usernameDestino);
    }

    public static User buscarPorUsername(String username) {
        try {
            String ruta = RAIZ + username + "/insta.ins";
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
                return (User) ois.readObject();
            }
        } catch (Exception e) {
            return null; 
        }
    }

    public static boolean usuarioExiste(String username) {
        return cargarListaUsuarios().contains(username);
    }

    public static User login(String username, String password) {
        User u = buscarPorUsername(username);
        if (u == null) return null;
        if (u.getEstado() == EstadoCuenta.INACTIVO) return null;
        if (!u.getPassword().equals(password)) return null;
        return u;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> cargarListaUsuarios() {
        try {
            new File(RAIZ).mkdirs();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_USERS))) {
                return (ArrayList<String>) ois.readObject();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static void guardarListaUsuarios(ArrayList<String> lista) throws Exception {
        new File(RAIZ).mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USERS))) {
            oos.writeObject(lista);
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> cargarLista(String ruta) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return (ArrayList<String>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void guardarLista(String ruta, ArrayList<String> lista) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
        }
    }

    private void eliminarCarpeta(File carpeta) {
        if (carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    eliminarCarpeta(archivo);
                }
            }
        }
        carpeta.delete();
    }

    public void desactivar() throws Exception {
        this.estado = EstadoCuenta.INACTIVO;
        GuardarCuenta(); 
    }

    public void activar() throws Exception {
        this.estado = EstadoCuenta.ACTIVO;
        GuardarCuenta();
    }

    // --- GETTERS Y SETTERS ---

    public String getNombreCompleto() { 
        return nombreCompleto; 
    }
    public Genero getGenero() { 
        return genero;
    }
    public String getUsername() { 
        return username; 
    }
    public String getPassword() { 
        return password; 
    }
    public String getFechaRegistro() { 
        return fechaRegistro;
    }
    public int getEdad() { 
        return edad; 
    }
    public EstadoCuenta getEstado() { 
        return estado; 
    }
    public String getFotoPerfil() { 
        return fotoPerfil; 
    }
    public TipoCuenta getTipoCuenta() { 
        return tipoCuenta; 
    }

    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil; 
    }
    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    @Override
    public String toString() {
        return "Usuario{" + "username='" + username + '\'' + ", nombre='" + nombreCompleto + '\'' +
               ", estado=" + estado + ", tipo=" + tipoCuenta + '}';
    }
}