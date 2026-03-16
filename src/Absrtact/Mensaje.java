/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Absrtact;

import PEnums.Enums.TipoMensaje;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author croge
 */

//instrucciones para las otras clases: 
public abstract class Mensaje {

    protected String de;
    protected String para;
    protected String fecha;
    protected String hora;
    protected boolean leido;
    protected TipoMensaje tipoMensaje;

    public Mensaje(String de, String para) {
        this.de = de.toLowerCase();
        this.para = para.toLowerCase();
        this.fecha = LocalDate.now().toString();
        this.hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.leido = false;
    }

    public Mensaje() {
    }

    public String getDe() {
        return de;
    }

    public String getPara() {
        return para;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public boolean isLeido() {
        return leido;
    }

    public void marcarLeido() {
        leido = true;
    }

    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    // polimorfismo — cada subclase lo muestra diferente
    public abstract String mostrarMensaje();

    public abstract String getTipo();

    public abstract String getContenido();

    public abstract String serializar();

    protected abstract void setContenidoInterno(String valor);

    public static Mensaje deserializar(String linea) {
        String[] p = linea.split("\\|", 7);
        if (p.length < 7) {
            return null;
        }
        Mensaje m;
        if (p[2].equals(TipoMensaje.STICKER.name().toLowerCase())) {
            m = new Logica.MensajeSticker();
        } else {
            m = new Logica.MensajeTexto();
        }
        m.de = p[0];
        m.para = p[1];
        m.fecha = p[4];
        m.hora = p[5];
        m.leido = Boolean.parseBoolean(p[6]);
        m.setContenidoInterno(p[3]);
        return m;
    }
}
