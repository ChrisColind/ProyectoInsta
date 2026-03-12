/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;
import Absrtact.Mensaje;
import java.util.*;
/**
 *
 * @author Rogelio
 */
public class Conversacion {
 
    private String usuarioA;
    private String usuarioB;
    private String rutaInbox;
 
    public Conversacion(String usuarioA, String usuarioB) {
        this.usuarioA  = usuarioA.toLowerCase();
        this.usuarioB  = usuarioB.toLowerCase();
        this.rutaInbox = SistemaArchivos.RAIZ + this.usuarioA + "/inbox.ins";
    }
 
    public void enviar(Mensaje m) {
        SistemaArchivos.escribirLinea(rutaInbox, m.serializar());
        String inboxB = SistemaArchivos.RAIZ + usuarioB + "/inbox.ins";
        SistemaArchivos.escribirLinea(inboxB, m.serializar());
    }
 
    public List<Mensaje> getMensajes() {
        return cargarMensajesFiltrados(SistemaArchivos.leerLineas(rutaInbox), usuarioA, usuarioB, 0);
    }
 
    // recursividad: procesa la lista de lineas una por una
    private List<Mensaje> cargarMensajesFiltrados(List<String> lineas, String a, String b, int indice) {
        List<Mensaje> resultado = new ArrayList<>();
        if (indice >= lineas.size()) return resultado;
        Mensaje m = Mensaje.deserializar(lineas.get(indice));
        if (m != null) {
            boolean involucra = (m.getDe().equals(a) && m.getPara().equals(b))
                             || (m.getDe().equals(b) && m.getPara().equals(a));
            if (involucra) resultado.add(m);
        }
        resultado.addAll(cargarMensajesFiltrados(lineas, a, b, indice + 1));
        return resultado;
    }
 
    public int getMensajesNoLeidos() {
        int count = 0;
        for (Mensaje m : getMensajes())
            if (!m.isLeido() && m.getPara().equals(usuarioA)) count++;
        return count;
    }
 
    public void marcarTodosLeidos() {
        List<Mensaje> todos = getMensajes();
        for (Mensaje m : todos)
            if (m.getPara().equals(usuarioA)) m.marcarLeido();
        reescribir(todos);
    }
 
    public void eliminar() {
        List<String> lineas = SistemaArchivos.leerLineas(rutaInbox);
        List<String> restantes = new ArrayList<>();
        for (String l : lineas) {
            Mensaje m = Mensaje.deserializar(l);
            if (m == null) continue;
            boolean involucra = (m.getDe().equals(usuarioA) && m.getPara().equals(usuarioB))
                             || (m.getDe().equals(usuarioB) && m.getPara().equals(usuarioA));
            if (!involucra) restantes.add(l);
        }
        SistemaArchivos.sobreescribir(rutaInbox, restantes);
        String inboxB = SistemaArchivos.RAIZ + usuarioB + "/inbox.ins";
        List<String> lineasB = SistemaArchivos.leerLineas(inboxB);
        List<String> restantesB = new ArrayList<>();
        for (String l : lineasB) {
            Mensaje m = Mensaje.deserializar(l);
            if (m == null) continue;
            boolean involucra = (m.getDe().equals(usuarioA) && m.getPara().equals(usuarioB))
                             || (m.getDe().equals(usuarioB) && m.getPara().equals(usuarioA));
            if (!involucra) restantesB.add(l);
        }
        SistemaArchivos.sobreescribir(inboxB, restantesB);
    }
 
    private void reescribir(List<Mensaje> mensajes) {
        List<String> lineas = SistemaArchivos.leerLineas(rutaInbox);
        List<String> nuevas = new ArrayList<>();
        for (String l : lineas) {
            Mensaje m = Mensaje.deserializar(l);
            if (m == null) { nuevas.add(l); continue; }
            boolean involucra = (m.getDe().equals(usuarioA) && m.getPara().equals(usuarioB))
                             || (m.getDe().equals(usuarioB) && m.getPara().equals(usuarioA));
            if (involucra) {
                for (Mensaje actualizado : mensajes) {
                    if (actualizado.getFecha().equals(m.getFecha())
                            && actualizado.getHora().equals(m.getHora())
                            && actualizado.getDe().equals(m.getDe())) {
                        nuevas.add(actualizado.serializar());
                        break;
                    }
                }
            } else nuevas.add(l);
        }
        SistemaArchivos.sobreescribir(rutaInbox, nuevas);
    }
 
    // devuelve todos los usuarios con quienes hay conversacion en el inbox
    public static List<String> getConversaciones(String username) {
        return extraerInterlocutores(
            SistemaArchivos.leerLineas(SistemaArchivos.RAIZ + username + "/inbox.ins"),
            username, new ArrayList<>(), 0
        );
    }
 
    private static List<String> extraerInterlocutores(List<String> lineas, String yo, List<String> acum, int i) {
        if (i >= lineas.size()) return acum;
        Mensaje m = Mensaje.deserializar(lineas.get(i));
        if (m != null) {
            String otro = m.getDe().equals(yo) ? m.getPara() : m.getDe();
            if ((m.getDe().equals(yo) || m.getPara().equals(yo)) && !acum.contains(otro))
                acum.add(otro);
        }
        return extraerInterlocutores(lineas, yo, acum, i + 1);
    }
 
    public String getUsuarioB() { return usuarioB; }
}