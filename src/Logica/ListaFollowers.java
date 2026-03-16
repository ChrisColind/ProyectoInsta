/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rogelio
 */
public class ListaFollowers {
 
    private Nodo<String> cabeza;
    private int tamaño;
 
    public ListaFollowers() {
        cabeza  = null;
        tamaño = 0;
    }
 
    public void agregar(String username) {
        if (contiene(username)) return;
        Nodo<String> nuevo = new Nodo<>(username.toLowerCase());
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<String> actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }
 
    public void eliminar(String username) {
        if (cabeza == null) return;
        String u = username.toLowerCase();
        if (cabeza.dato.equals(u)) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return;
        }
        Nodo<String> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(u)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return;
            }
            actual = actual.siguiente;
        }
    }
 
    public boolean contiene(String username) {
        Nodo<String> actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(username.toLowerCase())) return true;
            actual = actual.siguiente;
        }
        return false;
    }
 
    public List<String> aLista() {
        List<String> lista = new ArrayList<>();
        Nodo<String> actual = cabeza;
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
