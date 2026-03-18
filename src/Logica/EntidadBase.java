/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import PEnums.Enums.EstadoCuenta;
import PEnums.Enums.Genero;
import PEnums.Enums.TipoCuenta;
import java.util.List;

/**
 *
 * @author Rogelio
 */
public abstract class EntidadBase {

    protected String username;
    protected String nombreCompleto;
    protected Genero genero;
    protected String password;
    protected int edad;
    protected String fechaCreacion;
    protected EstadoCuenta estado;
    protected TipoCuenta tipoCuenta;
    protected String rutaFoto;
    protected String carpeta;

    public String getUsername() {
        return username;
    }

    public String getNombre() {
        return nombreCompleto;
    }

    public Genero getGenero() {
        return genero;
    }

    public int getEdad() {
        return edad;
    }

    public String getFecha() {
        return fechaCreacion;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public boolean esPublico() {
        return tipoCuenta == TipoCuenta.PUBLICA;
    }

    public boolean esActivo() {
        return estado == EstadoCuenta.ACTIVO;
    }

    public void setEstado(EstadoCuenta nuevoEstado) {
        this.estado = nuevoEstado;
    }
    public void setTipoCuenta(String tipo) {
        this.tipoCuenta = tipo.equalsIgnoreCase("Publica") ? TipoCuenta.PUBLICA : TipoCuenta.PRIVADA;
    }

    public boolean verificarPassword(String pass) {
        return this.password.equals(pass);
    }

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
