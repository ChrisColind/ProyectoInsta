/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import java.awt.CardLayout;
import javax.swing.*;

/**
 *
 * @author Rogelio
 */
public class Gui_Navegador {

    private final CardLayout cardLayout;
    private final JPanel     pnlCards;

    private JFrame ventana;
    private String usuarioActual;

    public Gui_Navegador(CardLayout cardLayout, JPanel pnlCards) {
        this.cardLayout = cardLayout;
        this.pnlCards   = pnlCards;
    }

    public void setContexto(JFrame ventana, String usuarioActual) {
        this.ventana       = ventana;
        this.usuarioActual = usuarioActual;
    }

    public void ir(String pantalla) {
        if (pantalla.equals("perfil") && ventana != null && usuarioActual != null) {
            Gui_Perfil guiPerfil = new Gui_Perfil(ventana, this, usuarioActual, usuarioActual);
            pnlCards.add(guiPerfil.construirPantalla(), "perfil");
        }
        cardLayout.show(pnlCards, pantalla);
    }

    public JPanel getPnlCards() {
        return pnlCards;
    }
}