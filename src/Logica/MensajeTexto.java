/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;
import Absrtact.Mensaje;
import PEnums.Enums.TipoMensaje;
/**
 *
 * @author Rogelio
 */
public class MensajeTexto extends Mensaje {
 
    private String texto;
 
    public MensajeTexto(String de, String para, String texto) {
        super(de, para);
        this.texto = texto;
    }
 
    public MensajeTexto() {}
 
    public String mostrarMensaje() {
        return "[" + hora + "] " + de + ": " + texto;
    }
 
    @Override
    public String getTipo() {
        return TipoMensaje.TEXTO.name().toLowerCase();
    }
 
    @Override
    public String getContenido() {
        return texto;
    }
 
    @Override
    protected void setContenidoInterno(String v) {
        this.texto = v;
    }
 
    @Override
    public String serializar() {
        return de + "|" + para + "|" + getTipo() + "|" + texto + "|" + fecha + "|" + hora + "|" + leido;
    }
}