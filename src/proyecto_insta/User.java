package proyecto_insta;

import Interfaces.InteractuarUser;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class User extends ABSinstru implements InteractuarUser {

    private static final long serialVersionUID = 1L;

    public static final String RAIZ          = "INSTA_RAIZ/";
    public static final String ARCHIVO_USERS = RAIZ + "users.ins";

    private String       nombreCompleto;
    private Genero       genero;          // enum: M o F
    private String       username;        // único en todo el sistema
    private String       password;
    private String       fechaRegistro;   // se asigna automáticamente al crear
    private int          edad;
    private EstadoCuenta estado;          // enum: ACTIVO o INACTIVO
    private String       fotoPerfil;      // ruta de la imagen de perfil
    private TipoCuenta   tipoCuenta;      // enum: PUBLICA o PRIVADA

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Crea un nuevo usuario con todos sus datos.
     * La fechaRegistro se asigna automáticamente con la fecha actual.
     * El estado inicia como ACTIVO por defecto.
     *
     * Llama a super(ruta) para inicializar rutaArchivo en EntidadBase.
     *
     * @param nombreCompleto nombre real del usuario
     * @param genero         M o F (enum Genero)
     * @param username       identificador único
     * @param password       contraseña
     * @param edad           edad del usuario
     * @param fotoPerfil     ruta de la imagen de perfil
     * @param tipoCuenta     PUBLICA o PRIVADA (enum TipoCuenta)
     */
    public User(String nombreCompleto, genero, String username,
                   String password, int edad, String fotoPerfil, TipoCuenta tipoCuenta) {

        // Llamamos al constructor de EntidadBase con la ruta del archivo .ins
        // de este usuario específico
        super(RAIZ + username + "/insta.ins");

        this.nombreCompleto = nombreCompleto;
        this.genero         = genero;
        this.username       = username;
        this.password       = password;
        this.edad           = edad;
        this.fotoPerfil     = fotoPerfil;
        this.tipoCuenta     = tipoCuenta;

        // Fecha automática del día de registro
        this.fechaRegistro  = LocalDate.now().toString();

        // Estado activo por defecto al crear la cuenta
        this.estado         = EstadoCuenta.ACTIVO;
    }

    // ── Implementación de validar() (obligatorio por EntidadBase) ──────────

    /**
     * Verifica que los datos del usuario sean válidos antes de guardar.
     * Si alguna validación falla, devuelve false y no se guarda.
     */
    @Override
    protected boolean validar() {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) return false;
        if (username       == null || username.trim().isEmpty())       return false;
        if (password       == null || password.trim().isEmpty())       return false;
        if (edad <= 0)                                                 return false;
        if (genero     == null)                                        return false;
        if (tipoCuenta == null)                                        return false;
        return true;
    }

    // ── Implementación de IGestionable ─────────────────────────────────────

    /**
     * Guarda este usuario en dos lugares:
     * 1. INSTA_RAIZ/username/insta.ins       (datos del perfil)
     * 2. INSTA_RAIZ/users.ins                (lista global de usernames)
     *
     * También crea la carpeta del usuario si no existe.
     */
    @Override
    public void guardar() throws Exception {
        // Primero validamos — si los datos no son válidos, lanzamos error
        if (!validar()) throw new Exception("Datos del usuario inválidos.");

        // Crear la carpeta del usuario si no existe
        // Ejemplo: INSTA_RAIZ/juan123/
        File carpeta = new File(RAIZ + username);
        if (!carpeta.exists()) carpeta.mkdirs();  // mkdirs crea toda la ruta de carpetas

        // Crear subcarpetas del usuario
        new File(RAIZ + username + "/imagenes").mkdirs();
        new File(RAIZ + username + "/stickers_personales").mkdirs();
        new File(RAIZ + username + "/folders_personales").mkdirs();

        // Guardar los datos del perfil en insta.ins
        // ObjectOutputStream convierte el objeto Java a bytes (serialización)
        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(rutaArchivo)
        );
        oos.writeObject(this);  // guarda este objeto completo
        oos.close();

        // Registrar el username en la lista global users.ins
        ArrayList<String> usuarios = cargarListaUsuarios();
        if (!usuarios.contains(username)) {
            usuarios.add(username);
            guardarListaUsuarios(usuarios);
        }
    }

    /**
     * Carga los datos de este usuario desde su insta.ins.
     * Normalmente no se usa directamente — se usa buscarPorUsername().
     */
    @Override
    public void cargar() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream(rutaArchivo)
        );
        // Leemos el objeto y copiamos sus atributos a este objeto
        Usuario cargado = (Usuario) ois.readObject();
        ois.close();

        // Copiamos los datos del objeto cargado a "this"
        this.nombreCompleto = cargado.nombreCompleto;
        this.genero         = cargado.genero;
        this.password       = cargado.password;
        this.fechaRegistro  = cargado.fechaRegistro;
        this.edad           = cargado.edad;
        this.estado         = cargado.estado;
        this.fotoPerfil     = cargado.fotoPerfil;
        this.tipoCuenta     = cargado.tipoCuenta;
    }

    /**
     * Elimina la carpeta completa del usuario y lo quita de users.ins.
     * ¡Cuidado! Esto borra todos sus datos, publicaciones y mensajes.
     */
    @Override
    public void eliminar() throws Exception {
        // Eliminar carpeta del usuario recursivamente
        eliminarCarpeta(new File(RAIZ + username));

        // Quitar el username de la lista global
        ArrayList<String> usuarios = cargarListaUsuarios();
        usuarios.remove(username);
        guardarListaUsuarios(usuarios);
    }

    // ── Implementación de IInteractuable ───────────────────────────────────

    /**
     * Sigue a otro usuario:
     * - Agrega su username a MI following.ins
     * - Agrega MI username a SUS followers.ins
     */
    @Override
    public void seguir(String usernameDestino) throws Exception {
        // No puedes seguirte a ti mismo
        if (usernameDestino.equals(this.username)) return;

        // Verificar que el usuario destino existe
        if (!usuarioExiste(usernameDestino)) throw new Exception("Usuario no encontrado.");

        // Agregar a mi following
        String rutaFollowing = RAIZ + username + "/following.ins";
        ArrayList<String> following = cargarLista(rutaFollowing);
        if (!following.contains(usernameDestino)) {
            following.add(usernameDestino);
            guardarLista(rutaFollowing, following);
        }

        // Agregar mi username a los followers del destino
        String rutaFollowers = RAIZ + usernameDestino + "/followers.ins";
        ArrayList<String> followers = cargarLista(rutaFollowers);
        if (!followers.contains(this.username)) {
            followers.add(this.username);
            guardarLista(rutaFollowers, followers);
        }
    }

    /**
     * Deja de seguir a otro usuario:
     * - Quita su username de MI following.ins
     * - Quita MI username de SUS followers.ins
     */
    @Override
    public void dejarDeSeguir(String usernameDestino) throws Exception {
        // Quitar de mi following
        String rutaFollowing = RAIZ + username + "/following.ins";
        ArrayList<String> following = cargarLista(rutaFollowing);
        following.remove(usernameDestino);
        guardarLista(rutaFollowing, following);

        // Quitar mi username de sus followers
        String rutaFollowers = RAIZ + usernameDestino + "/followers.ins";
        ArrayList<String> followers = cargarLista(rutaFollowers);
        followers.remove(this.username);
        guardarLista(rutaFollowers, followers);
    }

    /**
     * Envía un mensaje a otro usuario, respetando reglas de privacidad:
     * - Si el receptor tiene cuenta PRIVADA → solo si somos amigos (seguimiento mutuo)
     * - Si el receptor tiene cuenta PUBLICA → cualquiera puede escribir
     */
    @Override
    public void enviarMensaje(String receptor, String contenido, TipoMensaje tipo) throws Exception {
        // Validar longitud del mensaje
        if (contenido.length() > 300) throw new Exception("Mensaje excede 300 caracteres.");

        // Cargar el usuario receptor para revisar su privacidad
        Usuario usuarioReceptor = buscarPorUsername(receptor);
        if (usuarioReceptor == null) throw new Exception("Usuario receptor no encontrado.");

        // Revisar regla de privacidad
        if (usuarioReceptor.getTipoCuenta() == TipoCuenta.PRIVADA) {
            // Para enviar mensaje a cuenta privada, debe haber seguimiento mutuo
            boolean yoLoSigo  = sigueA(receptor);
            boolean elMeSigue = usuarioReceptor.sigueA(this.username);
            if (!yoLoSigo || !elMeSigue) {
                throw new Exception("No puedes enviar mensajes a una cuenta privada sin ser amigos.");
            }
        }

        // Crear el mensaje y guardarlo
        Mensaje mensaje = new Mensaje(this.username, receptor, contenido, tipo);
        mensaje.guardar();
    }

    /**
     * Verifica si este usuario sigue al username recibido.
     * Busca en el archivo following.ins si contiene ese username.
     */
    @Override
    public boolean sigueA(String usernameDestino) throws Exception {
        String rutaFollowing = RAIZ + username + "/following.ins";
        ArrayList<String> following = cargarLista(rutaFollowing);
        return following.contains(usernameDestino);
    }

    // ── Métodos estáticos de utilidad ──────────────────────────────────────

    /**
     * Busca un usuario por su username en el sistema de archivos.
     * Lee INSTA_RAIZ/username/insta.ins y devuelve el objeto Usuario.
     *
     * @param  username el username a buscar
     * @return el Usuario encontrado, o null si no existe
     */
    public static Usuario buscarPorUsername(String username) {
        try {
            String ruta = RAIZ + username + "/insta.ins";
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta));
            Usuario u = (Usuario) ois.readObject();
            ois.close();
            return u;
        } catch (Exception e) {
            return null;  // si no existe el archivo, devuelve null
        }
    }

    /**
     * Verifica si un username ya está registrado en el sistema.
     * Revisa la lista global INSTA_RAIZ/users.ins
     *
     * @param  username el username a verificar
     * @return true si ya existe, false si está disponible
     */
    public static boolean usuarioExiste(String username) {
        return cargarListaUsuarios().contains(username);
    }

    /**
     * Intenta hacer login con username y password.
     * También verifica que la cuenta esté ACTIVA.
     *
     * @return el Usuario si las credenciales son correctas, null si no
     */
    public static Usuario login(String username, String password) {
        Usuario u = buscarPorUsername(username);
        if (u == null) return null;                          // no existe
        if (u.getEstado() == EstadoCuenta.INACTIVO) return null; // cuenta inactiva
        if (!u.getPassword().equals(password)) return null;  // contraseña incorrecta
        return u;  // login exitoso
    }

    /**
     * Carga la lista global de usernames desde INSTA_RAIZ/users.ins
     * Si el archivo no existe aún, devuelve una lista vacía.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<String> cargarListaUsuarios() {
        try {
            // Crear carpeta raíz si no existe
            new File(RAIZ).mkdirs();
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_USERS)
            );
            ArrayList<String> lista = (ArrayList<String>) ois.readObject();
            ois.close();
            return lista;
        } catch (Exception e) {
            return new ArrayList<>();  // primera vez que se ejecuta: lista vacía
        }
    }

    /**
     * Guarda la lista global de usernames en INSTA_RAIZ/users.ins
     */
    private static void guardarListaUsuarios(ArrayList<String> lista) throws Exception {
        new File(RAIZ).mkdirs();
        ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(ARCHIVO_USERS)
        );
        oos.writeObject(lista);
        oos.close();
    }

    /**
     * Carga una lista de Strings desde cualquier archivo .ins
     * Se usa para followers.ins y following.ins
     * Si no existe, devuelve lista vacía.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<String> cargarLista(String ruta) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta));
            ArrayList<String> lista = (ArrayList<String>) ois.readObject();
            ois.close();
            return lista;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Guarda una lista de Strings en cualquier archivo .ins
     * Se usa para followers.ins y following.ins
     */
    public static void guardarLista(String ruta, ArrayList<String> lista) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta));
        oos.writeObject(lista);
        oos.close();
    }

    /**
     * Elimina una carpeta y todo su contenido recursivamente.
     * Se usa en eliminar() para borrar toda la carpeta del usuario.
     */
    private void eliminarCarpeta(File carpeta) {
        if (carpeta.isDirectory()) {
            for (File archivo : carpeta.listFiles()) {
                eliminarCarpeta(archivo);  // llamada recursiva para subcarpetas
            }
        }
        carpeta.delete();
    }

    // ── Métodos para desactivar/activar cuenta ─────────────────────────────

    /**
     * Desactiva la cuenta del usuario.
     * No aparecerá en búsquedas ni podrá hacer login.
     * Guarda el cambio en el archivo insta.ins
     */
    public void desactivar() throws Exception {
        this.estado = EstadoCuenta.INACTIVO;
        guardar();  // persistir el cambio
    }

    /**
     * Reactiva la cuenta del usuario.
     * Vuelve a aparecer en búsquedas y puede hacer login.
     */
    public void activar() throws Exception {
        this.estado = EstadoCuenta.ACTIVO;
        guardar();  // persistir el cambio
    }

    // ── Getters y Setters ──────────────────────────────────────────────────

    public String       getNombreCompleto() { return nombreCompleto; }
    public Genero       getGenero()         { return genero; }
    public String       getUsername()       { return username; }
    public String       getPassword()       { return password; }
    public String       getFechaRegistro()  { return fechaRegistro; }
    public int          getEdad()           { return edad; }
    public EstadoCuenta getEstado()         { return estado; }
    public String       getFotoPerfil()     { return fotoPerfil; }
    public TipoCuenta   getTipoCuenta()     { return tipoCuenta; }

    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setPassword(String password)             { this.password = password; }
    public void setEdad(int edad)                        { this.edad = edad; }
    public void setFotoPerfil(String fotoPerfil)         { this.fotoPerfil = fotoPerfil; }
    public void setTipoCuenta(TipoCuenta tipoCuenta)     { this.tipoCuenta = tipoCuenta; }

    // ── toString ───────────────────────────────────────────────────────────

    /**
     * Representación en texto del usuario.
     * Útil para debug y para mostrar datos en pantalla.
     */
    @Override
    public String toString() {
        return "Usuario{" +
            "username='"       + username       + '\'' +
            ", nombre='"       + nombreCompleto + '\'' +
            ", genero="        + genero         +
            ", edad="          + edad           +
            ", estado="        + estado         +
            ", tipoCuenta="    + tipoCuenta     +
            ", fechaRegistro='" + fechaRegistro + '\'' +
            '}';
    }
}