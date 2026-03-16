/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

/**
 *
 * @author Rogelio
 */
public class Nodo<Ndato> {
    Ndato dato;
    Nodo<Ndato> siguiente;
 
    public Nodo(Ndato dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
