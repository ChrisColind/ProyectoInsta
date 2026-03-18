/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.*;
import java.net.*;

/**
 *
 * @author Rogelio
 */
public class ClienteNotificaciones {

    private static final int PUERTO = 9876;
    private Socket socket;
    private boolean conectado = false;
    private NotificacionListener listener;

    public interface NotificacionListener {
        void onNotificacion(String tipo, String dato);
    }

    public ClienteNotificaciones(NotificacionListener listener) {
        this.listener = listener;
    }

    public void conectar() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", PUERTO);
                conectado = true;
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String linea;
                while ((linea = in.readLine()) != null) {
                    String[] partes = linea.split(":", 2);
                    if (partes.length == 2 && listener != null) {
                        final String tipo = partes[0];
                        final String dato = partes[1];
                        javax.swing.SwingUtilities.invokeLater(() -> listener.onNotificacion(tipo, dato));
                    }
                }
            } catch (Exception e) {
                System.out.println("Cliente desconectado: " + e.getMessage());
            }
            conectado = false;
        }).start();
    }

    public void desconectar() {
        try { if (socket != null) socket.close(); } catch (Exception e) { }
        conectado = false;
    }
}