/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;
import Absrtact.Mensaje;
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
 
    @Override public String getTipo()       { return "texto"; }
    @Override public String getContenido()  { return texto; }
    @Override protected void setContenidoInterno(String v) { this.texto = v; }
 
    @Override
    public String serializar() {
        return de + "|" + para + "|texto|" + texto + "|" + fecha + "|" + hora + "|" + leido;
    }
}
 
