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
public class MensajeSticker extends Mensaje {
 
    private String nombreSticker;
 
    public MensajeSticker(String de, String para, String nombreSticker) {
        super(de, para);
        this.nombreSticker = nombreSticker;
    }
 
    public MensajeSticker() {}
 
    @Override public String getTipo()      { return "sticker"; }
    @Override public String getContenido() { return nombreSticker; }
    @Override protected void setContenidoInterno(String v) { this.nombreSticker = v; }
 
    @Override
    public String serializar() {
        return de + "|" + para + "|sticker|" + nombreSticker + "|" + fecha + "|" + hora + "|" + leido;
    }
}
 