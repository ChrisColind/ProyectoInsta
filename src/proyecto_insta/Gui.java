/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_insta;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Gui {
    JFrame VentanaP;
    
    //Colores
    static final Color COLOR_BORDE      = new Color(219, 219, 219);
    static final Color COLOR_FONDO_CAMP = new Color(250, 250, 250);
    static final Color COLOR_TEXTO      = new Color(38,  38,  38);
    static final Color COLOR_GRIS       = new Color(142, 142, 142);
    static final Color COLOR_AZUL       = new Color(0,   149, 246);
    static final Color COLOR_BTN_GRIS   = new Color(147, 204, 247);
    static final Color COLOR_ERROR      = new Color(237, 73,  86);

    public Gui(){

        VentanaP = new JFrame("Instagram");
        VentanaP.setSize(1366, 768);
        VentanaP.setLocationRelativeTo(null);
        VentanaP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        VentanaP.setLayout(null);

        // Fondo
        JPanel panelFondo = new JPanel(null) {
            private Image img = new ImageIcon("Fondos/FondoPrincipal.jpeg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null) {
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panelFondo.setBackground(new Color(250, 250, 250));

        // Posiciones de la carta 1
        int VentanaAncho = 1366;
        int VentanaAlto = 768;
        int CartaAncho = 350;
        int CartaAlto = 380;
        int CartaX = (VentanaAncho - CartaAncho) / 2;   
        int CartaY = ((VentanaAlto - CartaAlto) / 2) -50;   

        JPanel Carta = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D G2D = (Graphics2D) g.create();
                G2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                G2D.setColor(Color.WHITE);
                G2D.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                G2D.setColor(COLOR_BORDE);
                G2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
                G2D.dispose();
            }
        };
        Carta.setOpaque(false);
        Carta.setBounds(CartaX, CartaY, CartaAncho, CartaAlto);
        panelFondo.add(Carta);
        
        //Titulo de carta
        
        JLabel lblLogo = new JLabel("Instagram") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(88, 81, 219),
                    getWidth(), 0, new Color(247, 119,  55)
                );
                
                g2.setPaint(gp);
                g2.setFont(getFont());
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                g2.drawString(getText(), x, fm.getAscent());
                g2.dispose();
            }
        };
        lblLogo.setFont(new Font("Segoe Script", Font.PLAIN, 36));
        lblLogo.setBounds(20, 28, 310, 55);
        Carta.add(lblLogo);

        JLabel lblTitulo = new JLabel("Iniciar sesión en Instagram:", SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_GRIS);
        lblTitulo.setBounds(20, 86, 310, 20);
        Carta.add(lblTitulo);

        // Usuario -> Opcion Usuario
        JTextField txtUsuario = new JTextField("Usuario");
        txtUsuario.setBounds(30, 118, 290, 38);
        txtUsuario.setForeground(COLOR_GRIS);
        txtUsuario.setBackground(COLOR_FONDO_CAMP);
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        Carta.add(txtUsuario);

        txtUsuario.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtUsuario.getText().equals("Usuario")) {
                    txtUsuario.setText("");
                    txtUsuario.setForeground(COLOR_TEXTO);
                    txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(168, 168, 168), 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)
                    ));
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtUsuario.getText().isEmpty()) {
                    txtUsuario.setText("Usuario");
                    txtUsuario.setForeground(COLOR_GRIS);
                    txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)
                    ));
                }
            }
        });

        // Usuario -> Opcion Contraseña
        JPasswordField txtContra = new JPasswordField("Contraseña");
        txtContra.setBounds(30, 166, 290, 38);
        txtContra.setForeground(COLOR_GRIS);
        txtContra.setBackground(COLOR_FONDO_CAMP);
        txtContra.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtContra.setEchoChar((char) 0);   
        
        //espacio para colocar el ojo:
        txtContra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1), BorderFactory.createEmptyBorder(0, 10, 0, 36)   
        ));
        Carta.add(txtContra);

        // Boton ojo
        final boolean[] passVisible = {false};

        JButton btnOjo = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                int x = getWidth()  / 2;
                int y = getHeight() / 2;

                // hover para el ojo
                Color c = getModel().isRollover() ? COLOR_TEXTO : COLOR_GRIS;
                g2.setColor(c);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if(passVisible[0]){ // ojo ABIERTO
                    
                    g2.drawArc(x - 10, y - 6, 20, 14, 0,  180);
                    g2.drawArc(x - 10, y - 8, 20, 14, 0, -180);
                    g2.fillOval(x - 3, y - 3, 6, 6);
                    
                }else{ // ojo CERRADO 
                    g2.drawArc(x - 10, y - 6, 20, 14, 0,  180);
                    g2.drawArc(x - 10, y - 8, 20, 14, 0, -180);
                    g2.fillOval(x - 3, y - 3, 6, 6);
                    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setColor(Color.WHITE);
                    g2.drawLine(x - 8, y - 7, x + 8, y + 7);
                    g2.setColor(c);
                    g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x - 9, y - 8, x + 9, y + 8);
                }

                g2.dispose();
            }
        };
        
        // para el ojo
        btnOjo.setBounds(30 + 290 - 36, 166, 36, 38);
        btnOjo.setOpaque(false);
        btnOjo.setContentAreaFilled(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setFocusPainted(false);
        btnOjo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Carta.add(btnOjo);
        
        Carta.setComponentZOrder(btnOjo, 0);

        btnOjo.addActionListener(e -> {
            passVisible[0] = !passVisible[0];
            txtContra.setEchoChar(passVisible[0] ? (char) 0 : '●');
            btnOjo.repaint();
        });

        txtContra.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(txtContra.getPassword()).equals("Contraseña")) {
                    txtContra.setText("");
                    txtContra.setForeground(COLOR_TEXTO);
                    txtContra.setEchoChar('●');
                    passVisible[0] = false;
                    btnOjo.repaint();
                    txtContra.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(168, 168, 168), 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 36)
                    ));
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtContra.getPassword().length == 0) {
                    txtContra.setText("Contraseña");
                    txtContra.setForeground(COLOR_GRIS);
                    txtContra.setEchoChar((char) 0);
                    passVisible[0] = false;
                    btnOjo.repaint();
                    txtContra.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 36)
                    ));
                }
            }
        });

        // Label excepcion
        JLabel lblExcepcion = new JLabel(" ", SwingConstants.CENTER);
        lblExcepcion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblExcepcion.setForeground(COLOR_ERROR);
        lblExcepcion.setBounds(20, 210, 310, 18);
        Carta.add(lblExcepcion);

        // Boton Iniciar sesion 
        JButton btnLogin = new JButton("Iniciar sesión") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btnLogin.setBounds(30, 235, 290, 38);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(COLOR_BTN_GRIS);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Carta.add(btnLogin);
        btnLogin.setEnabled(false);

    btnLogin.addMouseListener(new MouseAdapter() {
        @Override 
        public void mouseEntered(MouseEvent e) {
            String u = txtUsuario.getText();
            String p = String.valueOf(txtContra.getPassword());
            boolean ok = !u.isEmpty() && !u.equals("Usuario")
                      && !p.isEmpty() && !p.equals("Contraseña");
            if (ok) btnLogin.setBackground(new Color(24, 119, 242));
        }
        
        @Override 
        public void mouseExited(MouseEvent e) {
            String u = txtUsuario.getText();
            String p = String.valueOf(txtContra.getPassword());
            boolean ok = !u.isEmpty() && !u.equals("Usuario")
                      && !p.isEmpty() && !p.equals("Contraseña");
            if (ok) btnLogin.setBackground(COLOR_AZUL);
            else    btnLogin.setBackground(COLOR_BTN_GRIS);
        }
    });

        // Carta "No tienes una cuenta"
        int Carta2Alto = 52;
        int Carta2Y = CartaY + CartaAlto + 10;

        JPanel cardRegistro = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(COLOR_BORDE);
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        cardRegistro.setOpaque(false);
        cardRegistro.setBounds(CartaX, Carta2Y, CartaAncho, Carta2Alto);
        panelFondo.add(cardRegistro);

        JLabel lblNoTienes = new JLabel("¿No tienes una cuenta?");
        lblNoTienes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNoTienes.setForeground(COLOR_TEXTO);
        lblNoTienes.setBounds(38, 14, 185, 24);
        cardRegistro.add(lblNoTienes);

        JLabel lblRegistrate = new JLabel("Regístrate");
        lblRegistrate.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRegistrate.setForeground(COLOR_AZUL);
        lblRegistrate.setBounds(228, 14, 90, 24);
        lblRegistrate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        lblRegistrate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(VentanaP,
                    "Redirigiendo a la página de registro...",
                    "Registro", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        cardRegistro.add(lblRegistrate);

        // Lógica del boton Iniciar sesion 
        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String contra  = String.valueOf(txtContra.getPassword());

            boolean noUser = usuario.isEmpty() || usuario.equals("Usuario");
            boolean noContra  = contra.isEmpty()  || contra.equals("Contraseña");

            if (noUser || noContra) {
                lblExcepcion.setText("Completa todos los campos.");
                return;
            }

            // testeo
            if (usuario.equals("chri") && contra.equals("123")) {
                lblExcepcion.setText(" ");
                JOptionPane.showMessageDialog(VentanaP,
                    "¡Bienvenido, " + usuario + "!",
                    "Inicio de sesión exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                lblExcepcion.setText("Datos incorrectos.");
                txtContra.setText("Contraseña");
                txtContra.setForeground(COLOR_GRIS);
                txtContra.setEchoChar((char) 0);
                passVisible[0] = false;
                btnOjo.repaint();
            }
        });

        // Boton azul
        javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
            void actualizar(){
                String Usuario = txtUsuario.getText();
                String Contra = String.valueOf(txtContra.getPassword());
                boolean ok;
                ok = !Usuario.isEmpty() && !Usuario.equals("Usuario")
                          && !Contra.isEmpty() && !Contra.equals("Contraseña");
                
                btnLogin.setBackground(ok ? COLOR_AZUL : COLOR_BTN_GRIS);
                btnLogin.setEnabled(ok);
            }
            @Override public void insertUpdate (javax.swing.event.DocumentEvent e) { actualizar(); }
            @Override public void removeUpdate (javax.swing.event.DocumentEvent e) { actualizar(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
        };
        txtUsuario.getDocument().addDocumentListener(dl);
        txtContra .getDocument().addDocumentListener(dl);

        VentanaP.setContentPane(panelFondo);
    }
}