/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Absrtact;
import Interfaces.GestionUser;
import java.io.Serializable;
/**
 *
 * @author croge
 */

//instrucciones para las otras clases: 
public abstract class ABSinstru implements Serializable, GestionUser {
        protected String rutaArchivo;
        
    public ABSinstru(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}
    