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
public class ListaPublicaciones {
 
    private Nodo<Publicacion> cabeza;
    private int tamaño;
 
    public ListaPublicaciones() {
        cabeza  = null;
        tamaño = 0;
    }
 
    public void agregar(Publicacion p) {
        Nodo<Publicacion> nuevo = new Nodo<>(p);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<Publicacion> actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }
 
    public void eliminar(String id) {
        if (cabeza == null) return;
        if (cabeza.dato.getId().equals(id)) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return;
        }
        Nodo<Publicacion> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.getId().equals(id)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return;
            }
            actual = actual.siguiente;
        }
    }
 
    public List<Publicacion> buscarHashtag(String tag, Nodo<Publicacion> nodo, List<String> idsVistos) {
        List<Publicacion> resultado = new ArrayList<>();
        if (nodo == null) return resultado;
        if (nodo.dato.contieneHashtag(tag) && !idsVistos.contains(nodo.dato.getId())) {
            idsVistos.add(nodo.dato.getId());
            resultado.add(nodo.dato);
        }
        resultado.addAll(buscarHashtag(tag, nodo.siguiente, idsVistos));
        return resultado;
    }
 
    public List<Publicacion> buscarPorHashtag(String tag) {
        return buscarHashtag(tag, cabeza, new ArrayList<>());
    }
 
    public List<Publicacion> aLista() {
        List<Publicacion> lista = new ArrayList<>();
        Nodo<Publicacion> actual = cabeza;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }
 
    public Nodo<Publicacion> getCabeza() { 
        return cabeza; 
    }
    public int getTamaño() { 
        return tamaño; 
    }
    public List<Publicacion> buscarPorMencion(String username) {
        return buscarMencionRecursivo(cabeza, username.toLowerCase(), new ArrayList<>(), new ArrayList<>());
    }

    private List<Publicacion> buscarMencionRecursivo(Nodo<Publicacion> nodo, String username,
            List<Publicacion> resultado, List<String> idsVistos) {
        if (nodo == null) return resultado;
        if (nodo.dato.mencionaA(username) && !idsVistos.contains(nodo.dato.getId())) {
            idsVistos.add(nodo.dato.getId());
            resultado.add(nodo.dato);
        }
        return buscarMencionRecursivo(nodo.siguiente, username, resultado, idsVistos);
    }
}