/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Rogelio
 */

public class ServidorNotificaciones {

    private static final int PUERTO = 9876;
    private static ServidorNotificaciones instancia;
    private ServerSocket serverSocket;
    private List<PrintWriter> clientes = Collections.synchronizedList(new ArrayList<>());
    private boolean corriendo = false;

    private ServidorNotificaciones() {}

    public static ServidorNotificaciones getInstance() {
        if (instancia == null) instancia = new ServidorNotificaciones();
        return instancia;
    }

    public void iniciar() {
        if (corriendo) return;
        corriendo = true;
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PUERTO);
                while (corriendo) {
                    try {
                        Socket cliente = serverSocket.accept();
                        PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                        clientes.add(out);
                        new Thread(() -> {
                            try {
                                new BufferedReader(new InputStreamReader(cliente.getInputStream())).readLine();
                            } catch (Exception e) { }
                            clientes.remove(out);
                        }).start();
                    } catch (Exception e) { }
                }
            } catch (Exception e) {
                System.out.println("Servidor: " + e.getMessage());
            }
        }).start();
    }

    public void notificar(String tipo, String dato) {
        String mensaje = tipo + ":" + dato;
        synchronized (clientes) {
            for (PrintWriter pw : clientes) {
                try { pw.println(mensaje); } catch (Exception e) { }
            }
        }
    }

    public void detener() {
        corriendo = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception e) { }
    }
}