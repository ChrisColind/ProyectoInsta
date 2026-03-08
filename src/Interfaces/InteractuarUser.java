/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import Enums.Enums;

/**
 *
 * @author croge
 */
//interacciones del user
public interface InteractuarUser {
    void Seguir(String username) throws Exception;
    void NoSeguir(String username) throws Exception;
    void EnviarMensaje(String receptor, String contenido, Enums.TipoMensaje tipo) throws Exception;
    public boolean SigueA(String username) throws Exception;
    //revisar si x persona sigue a n persona
    
}
