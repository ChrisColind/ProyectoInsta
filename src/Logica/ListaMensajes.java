/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Absrtact.Mensaje;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rogelio
 */
public class ListaMensajes {
 
    private Nodo<Mensaje> cabeza;
    private int tamaño;
 
    public ListaMensajes() {
        cabeza  = null;
        tamaño = 0;
    }
 
    public void agregar(Mensaje m) {
        Nodo<Mensaje> nuevo = new Nodo<>(m);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<Mensaje> actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }
 
    public int getNoLeidos(String para) {
        return contarNoLeidos(cabeza, para, 0);
    }
 
    private int contarNoLeidos(Nodo<Mensaje> nodo, String para, int acum) {
        if (nodo == null) return acum;
        if (!nodo.dato.isLeido() && nodo.dato.getPara().equals(para)) acum++;
        return contarNoLeidos(nodo.siguiente, para, acum);
    }
 
    public List<Mensaje> aLista() {
        List<Mensaje> lista = new ArrayList<>();
        Nodo<Mensaje> actual = cabeza;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }
 
    public int getTamaño() { 
        return tamaño; 
    }
}
