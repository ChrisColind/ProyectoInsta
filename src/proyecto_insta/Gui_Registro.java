/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Rogelio
 */
public class Gui_Registro {

    // ── Referencia al frame principal y al CardLayout ──────────────────────────
    private final JFrame      ventana;
    private final CardLayout  cardLayout;
    private final JPanel      pnlCards;

    // ── Campos de formulario ───────────────────────────────────────────────────
    private JTextField     txtNombre;
    private JTextField     txtApellido;
    private JTextField     txtCorreo;
    private JTextField     txtUsuario;
    private JPasswordField txtContra;
    private JPasswordField txtContraConf;
    private JButton        btnOjo1;
    private JButton        btnOjo2;
    private boolean[]      pass1Visible = {false};
    private boolean[]      pass2Visible = {false};

    // ── Feedback ───────────────────────────────────────────────────────────────
    private JLabel lblError;

    // ── Colores y dimensiones (mismos que Gui_Inicio) ─────────────────────────
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

    // ══════════════════════════════════════════════════════════════════════════
    //  Constructor – recibe ventana + CardLayout para poder navegar de vuelta
    // ══════════════════════════════════════════════════════════════════════════
    public Gui_Registro(JFrame ventana, CardLayout cardLayout, JPanel pnlCards) {
        this.ventana    = ventana;
        this.cardLayout = cardLayout;
        this.pnlCards   = pnlCards;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Construye y devuelve el JPanel de registro (se añade al CardLayout)
    // ══════════════════════════════════════════════════════════════════════════
    public JPanel construirPantalla() {

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        // ── Carta principal ────────────────────────────────────────────────
        int cartaAncho = 350;
        int cartaAlto  = 560;
        int cartaX     = (W - cartaAncho) / 2;
        int cartaY     = (H - cartaAlto)  / 2 - 20;

        JPanel carta = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
                g2.dispose();
            }
        };
        carta.setOpaque(false);
        carta.setBounds(cartaX, cartaY, cartaAncho, cartaAlto);
        panel.add(carta);

        // ── Logo con degradado (igual que login) ───────────────────────────
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
                    getWidth(), 0, new Color(247, 119, 55));
                g2.setPaint(gp);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                g2.drawString(getText(), x, fm.getAscent());
                g2.dispose();
            }
        };
        lblLogo.setFont(new Font("Segoe Script", Font.PLAIN, 36));
        lblLogo.setBounds(20, 20, 310, 55);
        carta.add(lblLogo);

        // ── Subtítulo ──────────────────────────────────────────────────────
        JLabel lblSub = new JLabel(
            "<html><center>Registrate para compartir con <br> tus amigos.</center></html>",
            SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblSub.setForeground(C_GRIS);
        lblSub.setBounds(20, 78, 310, 36);
        carta.add(lblSub);

        // ── Separador ─────────────────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDE);
        sep.setBounds(30, 118, 290, 1);
        carta.add(sep);

        // ── Campos del formulario ──────────────────────────────────────────
        int campoAncho = 290;
        int campoAlto  = 36;
        int campoX     = 30;
        int gap        = 10;
        int yInicio    = 128;

        // Nombre
        txtNombre = crearCampo("Nombre", campoX, yInicio, campoAncho, campoAlto);
        carta.add(txtNombre);

        // Apellido
        txtApellido = crearCampo("Apellido", campoX, yInicio + (campoAlto + gap), campoAncho, campoAlto);
        carta.add(txtApellido);

        // Correo
        txtCorreo = crearCampo("Correo electronico", campoX, yInicio + 2 * (campoAlto + gap), campoAncho, campoAlto);
        carta.add(txtCorreo);

        // Usuario
        txtUsuario = crearCampo("Nombre de usuario", campoX, yInicio + 3 * (campoAlto + gap), campoAncho, campoAlto);
        carta.add(txtUsuario);

        // Contraseña 1
        int y5 = yInicio + 4 * (campoAlto + gap);
        txtContra = crearCampoPass("Contraseña", campoX, y5, campoAncho, campoAlto);
        carta.add(txtContra);
        btnOjo1 = crearBtnOjo(pass1Visible, txtContra, campoX + campoAncho - 36, y5, 36, campoAlto);
        carta.add(btnOjo1);
        carta.setComponentZOrder(btnOjo1, 0);

        // Confirmar contraseña
        int y6 = yInicio + 5 * (campoAlto + gap);
        txtContraConf = crearCampoPass("Confirmar contraseña", campoX, y6, campoAncho, campoAlto);
        carta.add(txtContraConf);
        btnOjo2 = crearBtnOjo(pass2Visible, txtContraConf, campoX + campoAncho - 36, y6, 36, campoAlto);
        carta.add(btnOjo2);
        carta.setComponentZOrder(btnOjo2, 0);

        // ── Label error ────────────────────────────────────────────────────
        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblError.setForeground(C_ERROR);
        int yErr = y6 + campoAlto + 4;
        lblError.setBounds(20, yErr, 310, 16);
        carta.add(lblError);

        // ── Botón Registrarse ──────────────────────────────────────────────
        int yBtn = yErr + 20;
        JButton btnRegistrar = crearBoton("Registrarse");
        btnRegistrar.setBounds(campoX, yBtn, campoAncho, 38);
        btnRegistrar.setBackground(C_BTN_GRIS);
        btnRegistrar.setEnabled(false);
        carta.add(btnRegistrar);

        // ── DocumentListener para activar/desactivar botón ─────────────────
        DocumentListener dl = new DocumentListener() {
            void actualizar() {
                boolean ok = todosLlenos();
                btnRegistrar.setBackground(ok ? C_AZUL : C_BTN_GRIS);
                btnRegistrar.setEnabled(ok);
            }
            @Override public void insertUpdate (DocumentEvent e) { actualizar(); }
            @Override public void removeUpdate (DocumentEvent e) { actualizar(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizar(); }
        };
        txtNombre    .getDocument().addDocumentListener(dl);
        txtApellido  .getDocument().addDocumentListener(dl);
        txtCorreo    .getDocument().addDocumentListener(dl);
        txtUsuario   .getDocument().addDocumentListener(dl);
        txtContra    .getDocument().addDocumentListener(dl);
        txtContraConf.getDocument().addDocumentListener(dl);

        // ── Hover botón ────────────────────────────────────────────────────
        btnRegistrar.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (todosLlenos()) btnRegistrar.setBackground(new Color(24, 119, 242));
            }
            @Override public void mouseExited(MouseEvent e) {
                if (todosLlenos()) btnRegistrar.setBackground(C_AZUL);
                else               btnRegistrar.setBackground(C_BTN_GRIS);
            }
        });

        // ── Acción registrar ───────────────────────────────────────────────
        btnRegistrar.addActionListener(e -> accionRegistrar());


        // ── Carta inferior "¿Ya tienes cuenta?" ───────────────────────────
        int c2Y = cartaY + cartaAlto + 10;
        JPanel cardVolver = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_BLANCO); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_BORDE);  g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        cardVolver.setOpaque(false);
        cardVolver.setBounds(cartaX, c2Y, cartaAncho, 52);
        panel.add(cardVolver);

        JLabel lblYaTienes = new JLabel("¿Ya tienes una cuenta?");
        lblYaTienes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblYaTienes.setForeground(C_TEXTO);
        lblYaTienes.setBounds(28, 14, 185, 24);
        cardVolver.add(lblYaTienes);

        JLabel lblIniciar = new JLabel("Inicia sesión");
        lblIniciar.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblIniciar.setForeground(C_AZUL);
        lblIniciar.setBounds(218, 14, 110, 24);
        lblIniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblIniciar.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                limpiarFormulario();
                cardLayout.show(pnlCards, "login");
            }
        });
        cardVolver.add(lblIniciar);

        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Acción del botón Registrarse
    // ══════════════════════════════════════════════════════════════════════════
    private void accionRegistrar() {

        String nombre  = txtNombre.getText().trim();
        String apellid = txtApellido.getText().trim();
        String correo  = txtCorreo.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String pass1   = String.valueOf(txtContra.getPassword());
        String pass2   = String.valueOf(txtContraConf.getPassword());

        // ── Validaciones ───────────────────────────────────────────────────
        if (nombre.equals("Nombre") || apellid.equals("Apellido")
                || correo.equals("Correo electronico")
                || usuario.equals("Nombre de usuario")
                || pass1.equals("Contraseña") || pass2.equals("Confirmar contraseña")) {
            lblError.setText("Completa todos los campos.");
            return;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            lblError.setText("Ingresa un correo electronico valido.");
            return;
        }

        if (pass1.length() < 6) {
            lblError.setText("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        if (!pass1.equals(pass2)) {
            lblError.setText("Las contraseñas no coinciden.");
            txtContraConf.setText("Confirmar contraseña");
            txtContraConf.setForeground(C_GRIS);
            txtContraConf.setEchoChar((char) 0);
            pass2Visible[0] = false;
            btnOjo2.repaint();
            return;
        }

        // ── Registro exitoso ───────────────────────────────────────────────
        lblError.setForeground(C_VERDE);
        lblError.setText("¡Cuenta creada exitosamente!");

        // Aqui se conectaria con la logica de negocio / base de datos
        // Por ahora mostramos dialogo y volvemos al login
        Timer t = new Timer(1200, ev -> {
            limpiarFormulario();
            lblError.setForeground(C_ERROR);   // restaurar color para proxima vez
            cardLayout.show(pnlCards, "login");
        });
        t.setRepeats(false);
        t.start();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Helpers UI
    // ══════════════════════════════════════════════════════════════════════════

    /** Crea un JTextField con placeholder y estilo estándar. */
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
                if (tf.getText().equals(placeholder)) {
                    tf.setText(""); tf.setForeground(C_TEXTO);
                    tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(168, 168, 168), 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder); tf.setForeground(C_GRIS);
                    tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDE, 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                }
            }
        });
        return tf;
    }

    /** Crea un JPasswordField con placeholder y estilo estándar. */
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
                    pf.setText(""); pf.setForeground(C_TEXTO);
                    pf.setEchoChar('●');
                    pf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(168, 168, 168), 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 36)));
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (pf.getPassword().length == 0) {
                    pf.setText(placeholder); pf.setForeground(C_GRIS);
                    pf.setEchoChar((char) 0);
                    pf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDE, 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 36)));
                }
            }
        });
        return pf;
    }

    /** Crea el botón de ojo para mostrar/ocultar contraseña. */
    private JButton crearBtnOjo(boolean[] visible, JPasswordField campo, int x, int y, int w, int h) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                Color c = getModel().isRollover() ? C_TEXTO : C_GRIS;
                g2.setColor(c);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(cx - 10, cy - 6, 20, 14,  0,  180);
                g2.drawArc(cx - 10, cy - 8, 20, 14,  0, -180);
                g2.fillOval(cx - 3, cy - 3, 6, 6);
                if (!visible[0]) {
                    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setColor(C_BLANCO);
                    g2.drawLine(cx - 8, cy - 7, cx + 8, cy + 7);
                    g2.setColor(c);
                    g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx - 9, cy - 8, cx + 9, cy + 8);
                }
                g2.dispose();
            }
        };
        btn.setBounds(x, y, w, h);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            visible[0] = !visible[0];
            campo.setEchoChar(visible[0] ? (char) 0 : '●');
            btn.repaint();
        });
        return btn;
    }

    /** Crea el botón principal con bordes redondeados. */
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(C_BLANCO);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Verifica que todos los campos tengan contenido real (no placeholder). */
    private boolean todosLlenos() {
        return !txtNombre.getText().equals("Nombre")         && !txtNombre.getText().isEmpty()
            && !txtApellido.getText().equals("Apellido")     && !txtApellido.getText().isEmpty()
            && !txtCorreo.getText().equals("Correo electronico") && !txtCorreo.getText().isEmpty()
            && !txtUsuario.getText().equals("Nombre de usuario") && !txtUsuario.getText().isEmpty()
            && !String.valueOf(txtContra.getPassword()).equals("Contraseña")
                && txtContra.getPassword().length > 0
            && !String.valueOf(txtContraConf.getPassword()).equals("Confirmar contraseña")
                && txtContraConf.getPassword().length > 0;
    }

    /** Resetea todos los campos a su estado inicial (placeholders). */
    private void limpiarFormulario() {
        resetCampo(txtNombre,    "Nombre");
        resetCampo(txtApellido,  "Apellido");
        resetCampo(txtCorreo,    "Correo electronico");
        resetCampo(txtUsuario,   "Nombre de usuario");
        resetPass(txtContra,    "Contraseña",          pass1Visible, btnOjo1);
        resetPass(txtContraConf,"Confirmar contraseña", pass2Visible, btnOjo2);
        lblError.setText(" ");
    }

    private void resetCampo(JTextField tf, String placeholder) {
        tf.setText(placeholder);
        tf.setForeground(C_GRIS);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
    }

    private void resetPass(JPasswordField pf, String placeholder,
                           boolean[] visible, JButton ojo) {
        pf.setText(placeholder);
        pf.setForeground(C_GRIS);
        pf.setEchoChar((char) 0);
        visible[0] = false;
        ojo.repaint();
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 36)));
    }
}

