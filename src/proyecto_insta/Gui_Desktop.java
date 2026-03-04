/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_insta;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author croge
 */
public class Gui_Desktop {
    
    public static void main(String []args){
        JFrame Ventana = new JFrame("Instagram desktop");
        
        Ventana.setSize(1366,768);
        Ventana.setLocationRelativeTo(null);
        Ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        Ventana.setLayout(null);
        
          JPanel panelFondo = new JPanel(null) {   // null layout igual que antes
            private Image img = new ImageIcon("Fondos/FondoPrincipal.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null) {
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        
        
        //x, y, ancho, alto
        JLabel lblTitulo = new JLabel("Iniciar sesion en instagram:"); 
        lblTitulo.setBounds(1000, 50, 200, 30);  //mover
        panelFondo.add(lblTitulo);
        
        
        JTextField txtUsuario = new JTextField("  Usuario");
        txtUsuario.setBounds(1000, 50+40, 250, 60); //mover 1= +100, 3= +50
        panelFondo.add(txtUsuario);
        
        txtUsuario.addFocusListener(new FocusAdapter() { 
            @Override
            public void focusGained(FocusEvent e) { 
                if (txtUsuario.getText().equals("Usuario")) { 
                    txtUsuario.setText("");
                }
            }
            @Override
        public void focusLost(FocusEvent e) {
            if (txtUsuario.getText().isEmpty()) {
                txtUsuario.setText("Usuario");
            }
        }
        });
        
        JTextField txtContra = new JTextField("   Contrase;a");
        txtContra.setBounds(1000, 50+110, 250, 60); //mover 1= +100, 3= +50
        panelFondo.add(txtContra);
        
        JButton btnLogin = new JButton("Iniciar sesion"); 
        btnLogin.setBounds(1000, 300, 200, 40); 
        panelFondo.add(btnLogin);
        Ventana.setContentPane(panelFondo);
        Ventana.setVisible(true);
    }
}
