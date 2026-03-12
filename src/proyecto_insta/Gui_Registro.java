package proyecto_insta;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.*;

public class Gui_Registro {

    private static final Color C_BORDE      = new Color(219, 219, 219);
    private static final Color C_FONDO_CAMP = new Color(250, 250, 250);
    private static final Color C_TEXTO      = new Color(38,  38,  38);
    private static final Color C_GRIS       = new Color(142, 142, 142);
    private static final Color C_AZUL       = new Color(0,   149, 246);
    private static final Color C_BTN_GRIS   = new Color(147, 204, 247);
    private static final Color C_ERROR      = new Color(237, 73,  86);
    private static final Color C_BLANCO     = Color.WHITE;
    private static final Color C_VERDE      = new Color(39,  174, 96);

    private static final int W = 1366;
    private static final int H = 768;

    private static final Set<String> usuariosRegistrados = new HashSet<>();

    private final JFrame     ventana;
    private final CardLayout cardLayout;
    private final JPanel     pnlCards;

    private JTextField     txtNombreCompleto;
    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JTextField     txtEdad;
    private JLabel         lblFecha;
    private JLabel         lblError;
    private JButton        btnOjo;
    private boolean[]      passVisible = {false};

    private String   generoSeleccionado   = null;
    private String   tipoCuentaSeleccionado = "Publica";
    private boolean  fotoSeleccionada     = false;

    public Gui_Registro(JFrame ventana, CardLayout cardLayout, JPanel pnlCards) {
        this.ventana    = ventana;
        this.cardLayout = cardLayout;
        this.pnlCards   = pnlCards;
    }

    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        int cartaAncho = 370;
        int cartaAlto  = 620;
        int cartaX     = (W - cartaAncho) / 2;
        int cartaY     = (H - cartaAlto)  / 2 - 10;

        JPanel carta = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 4, 4);
                g2.dispose();
            }
        };
        carta.setOpaque(false);
        carta.setBounds(cartaX, cartaY, cartaAncho, cartaAlto);
        panel.add(carta);

        JLabel lblLogo = new JLabel("Instagram") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                GradientPaint gp = new GradientPaint(0, 0, new Color(88, 81, 219), getWidth(), 0, new Color(247, 119, 55));
                g2.setPaint(gp);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, fm.getAscent());
                g2.dispose();
            }
        };
        lblLogo.setFont(new Font("Segoe Script", Font.PLAIN, 34));
        lblLogo.setBounds(20, 18, 330, 50);
        carta.add(lblLogo);

        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDE);
        sep.setBounds(30, 72, 310, 1);
        carta.add(sep);

        int cW = 310, cH = 36, cX = 30, gap = 10;
        int y = 82;

        txtNombreCompleto = crearCampo("Nombre completo", cX, y, cW, cH);
        carta.add(txtNombreCompleto);
        y += cH + gap;

        JLabel lblGenero = new JLabel("Genero:");
        lblGenero.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenero.setForeground(C_GRIS);
        lblGenero.setBounds(cX, y, 60, cH);
        carta.add(lblGenero);

        JButton btnM = crearBtnToggle("M");
        JButton btnF = crearBtnToggle("F");
        btnM.setBounds(cX + 65, y, 60, cH);
        btnF.setBounds(cX + 135, y, 60, cH);
        carta.add(btnM);
        carta.add(btnF);

        btnM.addActionListener(e -> {
            generoSeleccionado = "M";
            btnM.setBackground(C_AZUL); btnM.setForeground(C_BLANCO);
            btnF.setBackground(C_FONDO_CAMP); btnF.setForeground(C_TEXTO);
            actualizarBoton();
        });
        btnF.addActionListener(e -> {
            generoSeleccionado = "F";
            btnF.setBackground(C_AZUL); btnF.setForeground(C_BLANCO);
            btnM.setBackground(C_FONDO_CAMP); btnM.setForeground(C_TEXTO);
            actualizarBoton();
        });
        y += cH + gap;

        txtUsername = crearCampo("Username", cX, y, cW, cH);
        carta.add(txtUsername);
        y += cH + gap;

        txtPassword = crearCampoPass("Password", cX, y, cW, cH);
        carta.add(txtPassword);
        btnOjo = crearBtnOjo(passVisible, txtPassword, cX + cW - 36, y, 36, cH);
        carta.add(btnOjo);
        carta.setComponentZOrder(btnOjo, 0);
        y += cH + gap;

        txtEdad = crearCampo("Edad", cX, y, cW, cH);
        carta.add(txtEdad);
        y += cH + gap;

        JLabel lblFechaLabel = new JLabel("Fecha de registro:");
        lblFechaLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFechaLabel.setForeground(C_GRIS);
        lblFechaLabel.setBounds(cX, y, 130, cH);
        carta.add(lblFechaLabel);

        lblFecha = new JLabel(LocalDate.now().toString());
        lblFecha.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblFecha.setForeground(C_TEXTO);
        lblFecha.setBounds(cX + 135, y, 175, cH);
        carta.add(lblFecha);
        y += cH + gap;


        JLabel lblFotoLabel = new JLabel("Foto de perfil:");
        lblFotoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFotoLabel.setForeground(C_GRIS);
        lblFotoLabel.setBounds(cX, y, 110, cH);
        carta.add(lblFotoLabel);

        JLabel lblFotoNombre = new JLabel("Sin imagen");
        lblFotoNombre.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFotoNombre.setForeground(C_GRIS);
        lblFotoNombre.setBounds(cX + 120, y, 140, cH);
        carta.add(lblFotoNombre);

        JButton btnFoto = crearBotonSecundario("Elegir");
        btnFoto.setBounds(cX + 265, y, 45, cH);
        btnFoto.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Selecciona tu foto de perfil");
            int res = fc.showOpenDialog(ventana);
            if (res == JFileChooser.APPROVE_OPTION) {
                fotoSeleccionada = true;
                String nombre = fc.getSelectedFile().getName();
                lblFotoNombre.setText(nombre.length() > 14 ? nombre.substring(0, 14) + "..." : nombre);
                lblFotoNombre.setForeground(C_TEXTO);
            }
        });
        carta.add(btnFoto);
        y += cH + gap;

        JLabel lblTipo = new JLabel("Tipo de cuenta:");
        lblTipo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTipo.setForeground(C_GRIS);
        lblTipo.setBounds(cX, y, 120, cH);
        carta.add(lblTipo);

        JButton btnPublica  = crearBtnToggle("Publica");
        JButton btnPrivada  = crearBtnToggle("Privada");
        btnPublica.setBackground(C_AZUL); btnPublica.setForeground(C_BLANCO);
        btnPublica.setBounds(cX + 125, y, 80, cH);
        btnPrivada.setBounds(cX + 215, y, 80, cH);
        carta.add(btnPublica);
        carta.add(btnPrivada);

        btnPublica.addActionListener(e -> {
            tipoCuentaSeleccionado = "Publica";
            btnPublica.setBackground(C_AZUL); btnPublica.setForeground(C_BLANCO);
            btnPrivada.setBackground(C_FONDO_CAMP); btnPrivada.setForeground(C_TEXTO);
        });
        btnPrivada.addActionListener(e -> {
            tipoCuentaSeleccionado = "Privada";
            btnPrivada.setBackground(C_AZUL); btnPrivada.setForeground(C_BLANCO);
            btnPublica.setBackground(C_FONDO_CAMP); btnPublica.setForeground(C_TEXTO);
        });
        y += cH + gap;

        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblError.setForeground(C_ERROR);
        lblError.setBounds(20, y, 330, 16);
        carta.add(lblError);
        y += 20;

        JButton btnRegistrar = crearBoton("Registrarse");
        btnRegistrar.setBounds(cX, y, cW, 38);
        btnRegistrar.setBackground(C_BTN_GRIS);
        btnRegistrar.setEnabled(false);
        carta.add(btnRegistrar);

        DocumentListener dl = new DocumentListener() {
            void act() {
                boolean ok = camposLlenos();
                btnRegistrar.setBackground(ok ? C_AZUL : C_BTN_GRIS);
                btnRegistrar.setEnabled(ok);
            }
            @Override public void insertUpdate(DocumentEvent e)  { act(); }
            @Override public void removeUpdate(DocumentEvent e)  { act(); }
            @Override public void changedUpdate(DocumentEvent e) { act(); }
        };
        txtNombreCompleto.getDocument().addDocumentListener(dl);
        txtUsername.getDocument().addDocumentListener(dl);
        txtPassword.getDocument().addDocumentListener(dl);
        txtEdad.getDocument().addDocumentListener(dl);

        btnRegistrar.addActionListener(e -> accionRegistrar(btnRegistrar));

        int c2Y = cartaY + cartaAlto + 10;
        JPanel cardVolver = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_BLANCO); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_BORDE);  g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
                g2.dispose();
            }
        };
        cardVolver.setOpaque(false);
        cardVolver.setBounds(cartaX, c2Y, cartaAncho, 46);
        panel.add(cardVolver);

        JLabel lblYaTienes = new JLabel("Ya tienes una cuenta?");
        lblYaTienes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblYaTienes.setForeground(C_TEXTO);
        lblYaTienes.setBounds(28, 12, 175, 22);
        cardVolver.add(lblYaTienes);

        JLabel lblIniciar = new JLabel("Inicia sesion");
        lblIniciar.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblIniciar.setForeground(C_AZUL);
        lblIniciar.setBounds(210, 12, 120, 22);
        lblIniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblIniciar.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                limpiarFormulario(btnRegistrar, btnM, btnF, btnPublica, btnPrivada);
                cardLayout.show(pnlCards, "login");
            }
        });
        cardVolver.add(lblIniciar);

        return panel;
    }

    private void accionRegistrar(JButton btnRegistrar) {
        String nombre   = txtNombreCompleto.getText().trim();
        String username = txtUsername.getText().trim();
        String pass     = String.valueOf(txtPassword.getPassword());
        String edadStr  = txtEdad.getText().trim();

        if (nombre.equals("Nombre completo") || username.equals("Username")
                || pass.equals("Password") || edadStr.equals("Edad")) {
            lblError.setText("Completa todos los campos.");
            return;
        }

        if (generoSeleccionado == null) {
            lblError.setText("Selecciona un genero.");
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 1 || edad > 120) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            lblError.setText("Ingresa una edad valida.");
            return;
        }

        if (pass.length() < 6) {
            lblError.setText("La password debe tener al menos 6 caracteres.");
            return;
        }

        String userLower = username.toLowerCase();
        if (usuariosRegistrados.contains(userLower)) {
            lblError.setText("Ese username ya esta en uso.");
            return;
        }

        usuariosRegistrados.add(userLower);

        lblError.setForeground(C_VERDE);
        lblError.setText("Cuenta creada exitosamente!");

        Timer t = new Timer(1200, ev -> {
            lblError.setForeground(C_ERROR);
            cardLayout.show(pnlCards, "login");
        });
        t.setRepeats(false);
        t.start();
    }

    private void actualizarBoton() {}

    private boolean camposLlenos() {
        return !txtNombreCompleto.getText().equals("Nombre completo") && !txtNombreCompleto.getText().isEmpty()
            && !txtUsername.getText().equals("Username")              && !txtUsername.getText().isEmpty()
            && !String.valueOf(txtPassword.getPassword()).equals("Password") && txtPassword.getPassword().length > 0
            && !txtEdad.getText().equals("Edad")                      && !txtEdad.getText().isEmpty();
    }

    private void limpiarFormulario(JButton btnReg, JButton btnM, JButton btnF, JButton btnPub, JButton btnPriv) {
        resetCampo(txtNombreCompleto, "Nombre completo");
        resetCampo(txtUsername, "Username");
        resetPass(txtPassword, "Password");
        resetCampo(txtEdad, "Edad");
        generoSeleccionado    = null;
        tipoCuentaSeleccionado = "Publica";
        fotoSeleccionada      = false;
        btnM.setBackground(C_FONDO_CAMP); btnM.setForeground(C_TEXTO);
        btnF.setBackground(C_FONDO_CAMP); btnF.setForeground(C_TEXTO);
        btnPub.setBackground(C_AZUL);     btnPub.setForeground(C_BLANCO);
        btnPriv.setBackground(C_FONDO_CAMP); btnPriv.setForeground(C_TEXTO);
        btnReg.setBackground(C_BTN_GRIS);
        btnReg.setEnabled(false);
        lblError.setText(" ");
    }

    private void resetCampo(JTextField tf, String placeholder) {
        tf.setText(placeholder);
        tf.setForeground(C_GRIS);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
    }

    private void resetPass(JPasswordField pf, String placeholder) {
        pf.setText(placeholder);
        pf.setForeground(C_GRIS);
        pf.setEchoChar((char) 0);
        passVisible[0] = false;
        btnOjo.repaint();
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 36)));
    }

    private JTextField crearCampo(String placeholder, int x, int y, int w, int h) {
        JTextField tf = new JTextField(placeholder);
        tf.setBounds(x, y, w, h);
        tf.setForeground(C_GRIS);
        tf.setBackground(C_FONDO_CAMP);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(C_TEXTO); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(C_GRIS); }
            }
        });
        return tf;
    }

    private JPasswordField crearCampoPass(String placeholder, int x, int y, int w, int h) {
        JPasswordField pf = new JPasswordField(placeholder);
        pf.setBounds(x, y, w, h);
        pf.setForeground(C_GRIS);
        pf.setBackground(C_FONDO_CAMP);
        pf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pf.setEchoChar((char) 0);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 36)));
        pf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (String.valueOf(pf.getPassword()).equals(placeholder)) {
                    pf.setText(""); pf.setForeground(C_TEXTO); pf.setEchoChar('●');
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (pf.getPassword().length == 0) {
                    pf.setText(placeholder); pf.setForeground(C_GRIS); pf.setEchoChar((char) 0);
                }
            }
        });
        return pf;
    }

    private JButton crearBtnOjo(boolean[] visible, JPasswordField campo, int x, int y, int w, int h) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2;
                Color c = getModel().isRollover() ? C_TEXTO : C_GRIS;
                g2.setColor(c);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(cx-10, cy-6, 20, 14,  0,  180);
                g2.drawArc(cx-10, cy-8, 20, 14,  0, -180);
                g2.fillOval(cx-3, cy-3, 6, 6);
                if (!visible[0]) {
                    g2.setColor(C_BLANCO);
                    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx-8, cy-7, cx+8, cy+7);
                    g2.setColor(c);
                    g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx-9, cy-8, cx+9, cy+8);
                }
                g2.dispose();
            }
        };
        btn.setBounds(x, y, w, h);
        btn.setOpaque(false); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            visible[0] = !visible[0];
            campo.setEchoChar(visible[0] ? (char) 0 : '●');
            btn.repaint();
        });
        return btn;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(C_BLANCO);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBtnToggle(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(C_FONDO_CAMP);
        btn.setForeground(C_TEXTO);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_FONDO_CAMP);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                g2.setColor(C_TEXTO);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}