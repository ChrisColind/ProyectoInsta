/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_insta;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
        
        //x, y, ancho, alto
        JLabel lblTitulo = new JLabel("Iniciar sesion en instagram:"); 
        lblTitulo.setBounds(1000, 50, 200, 30);  //mover
        Ventana.add(lblTitulo);
        
        
        JTextField txtUsuario = new JTextField("  Usuario");
        txtUsuario.setBounds(1000, 50+40, 250, 60); //mover 1= +100, 3= +50
        Ventana.add(txtUsuario);
        
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
        Ventana.add(txtContra);
        
        JButton btnLogin = new JButton("Iniciar sesion"); 
        btnLogin.setBounds(1000, 300, 200, 40); 
        Ventana.add(btnLogin);
        
        Ventana.setVisible(true);
    }
}
