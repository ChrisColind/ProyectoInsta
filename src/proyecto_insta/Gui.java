package proyecto_insta;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.*;

public class Gui {

    //Colores
    static final Color C_BORDE      = new Color(219, 219, 219);
    static final Color C_FONDO_CAMP = new Color(250, 250, 250);
    static final Color C_TEXTO      = new Color(38,  38,  38);
    static final Color C_GRIS       = new Color(142, 142, 142);
    static final Color C_AZUL       = new Color(0,   149, 246);
    static final Color C_BTN_GRIS   = new Color(147, 204, 247);
    static final Color C_ERROR      = new Color(237, 73,  86);
    static final Color C_BLANCO     = Color.WHITE;
    static final Color C_FONDO      = new Color(250, 250, 250);

    //Dimensiones
    static final int W          = 1366;
    static final int H          = 768;
    static final int TOPBAR_H   = 54;
    static final int SIDEBAR_W  = 244;
    static final int RIGHT_W    = 300;

    //Ventana y paneles
    JFrame       ventana;
    JPanel       panelRaiz;      
    CardLayout   cardLayout;
    JPanel       pnlCards;      

    //Datos de sesion 
    String usuarioActual = "";

    //Referencias utiles del login 
    JLabel       lblExcepcion;
    JTextField   txtUsuario;
    JPasswordField txtContra;
    JButton      btnLogin;
    JButton      btnOjo;
    boolean[]    passVisible = {false};

    public Gui() {

        ventana = new JFrame("Instagram");
        ventana.setSize(W, H);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);

        //Panel raiz con imagen de fondo
        panelRaiz = new JPanel(null) {
            private Image img;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null && img.getWidth(null) > 0)
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                else {
                    Graphics2D g2 = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(240, 230, 255),
                        W, H, new Color(255, 230, 210));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelRaiz.setBackground(C_FONDO);
        panelRaiz.setBounds(0, 0, W, H);

        cardLayout = new CardLayout();
        pnlCards   = new JPanel(cardLayout);
        pnlCards.setOpaque(false);
        pnlCards.setBounds(0, 0, W, H);

        pnlCards.add(construirPantallaLogin(),    "login");
        pnlCards.add(construirPantallaHome(),     "home");
        pnlCards.add(construirVistaExplorar(),    "explorar");
        pnlCards.add(construirVistaInbox(),       "inbox");
        pnlCards.add(construirVistaNotif(),       "notif");
        pnlCards.add(construirVistaPerfil(),      "perfil");

        panelRaiz.add(pnlCards);

        cardLayout.show(pnlCards, "login");

        ventana.setContentPane(panelRaiz);
        ventana.setVisible(true);
    }
    private void ir(String pantalla) {
        cardLayout.show(pnlCards, pantalla);
    }

    private JPanel construirPantallaLogin() {

        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        int CartaAncho = 350;
        int CartaAlto  = 390;
        int CartaX     = (W - CartaAncho) / 2;
        int CartaY     = (H - CartaAlto)  / 2 - 50;

        JPanel Carta = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
                g2.dispose();
            }
        };
        Carta.setOpaque(false);
        Carta.setBounds(CartaX, CartaY, CartaAncho, CartaAlto);
        panel.add(Carta);

        // Logo degradado
        JLabel lblLogo = new JLabel("Instagram") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
        lblLogo.setBounds(20, 28, 310, 55);
        Carta.add(lblLogo);

        JLabel lblTitulo = new JLabel("Iniciar sesión en Instagram:", SwingConstants.CENTER);
        lblTitulo.setForeground(C_GRIS);
        lblTitulo.setBounds(20, 88, 310, 20);
        Carta.add(lblTitulo);

        // Campo usuario
        txtUsuario = new JTextField("Usuario");
        txtUsuario.setBounds(30, 120, 290, 38);
        txtUsuario.setForeground(C_GRIS);
        txtUsuario.setBackground(C_FONDO_CAMP);
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        Carta.add(txtUsuario);

        txtUsuario.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (txtUsuario.getText().equals("Usuario")) {
                    txtUsuario.setText(""); txtUsuario.setForeground(C_TEXTO);
                    txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(168, 168, 168), 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (txtUsuario.getText().isEmpty()) {
                    txtUsuario.setText("Usuario"); txtUsuario.setForeground(C_GRIS);
                    txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDE, 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                }
            }
        });

        // Campo contrasena
        txtContra = new JPasswordField("Contraseña");
        txtContra.setBounds(30, 168, 290, 38);
        txtContra.setForeground(C_GRIS);
        txtContra.setBackground(C_FONDO_CAMP);
        txtContra.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtContra.setEchoChar((char) 0);
        txtContra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 36)));
        Carta.add(txtContra);

        // Boton ojo
        btnOjo = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int x = getWidth() / 2, y = getHeight() / 2;
                Color c = getModel().isRollover() ? C_TEXTO : C_GRIS;
                g2.setColor(c);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (passVisible[0]) {
                    g2.drawArc(x - 10, y - 6, 20, 14,  0,  180);
                    g2.drawArc(x - 10, y - 8, 20, 14,  0, -180);
                    g2.fillOval(x - 3, y - 3, 6, 6);
                } else {
                    g2.drawArc(x - 10, y - 6, 20, 14,  0,  180);
                    g2.drawArc(x - 10, y - 8, 20, 14,  0, -180);
                    g2.fillOval(x - 3, y - 3, 6, 6);
                    g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setColor(C_BLANCO);
                    g2.drawLine(x - 8, y - 7, x + 8, y + 7);
                    g2.setColor(c);
                    g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(x - 9, y - 8, x + 9, y + 8);
                }
                g2.dispose();
            }
        };
        btnOjo.setBounds(30 + 290 - 36, 168, 36, 38);
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
            @Override public void focusGained(FocusEvent e) {
                if (String.valueOf(txtContra.getPassword()).equals("Contraseña")) {
                    txtContra.setText(""); txtContra.setForeground(C_TEXTO);
                    txtContra.setEchoChar('●'); passVisible[0] = false; btnOjo.repaint();
                    txtContra.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(168, 168, 168), 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 36)));
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (txtContra.getPassword().length == 0) {
                    txtContra.setText("Contraseña"); txtContra.setForeground(C_GRIS);
                    txtContra.setEchoChar((char) 0); passVisible[0] = false; btnOjo.repaint();
                    txtContra.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDE, 1),
                        BorderFactory.createEmptyBorder(0, 10, 0, 36)));
                }
            }
        });

        // Label error
        lblExcepcion = new JLabel(" ", SwingConstants.CENTER);
        lblExcepcion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblExcepcion.setForeground(C_ERROR);
        lblExcepcion.setBounds(20, 212, 310, 18);
        Carta.add(lblExcepcion);

        // Boton login
        btnLogin = new JButton("Iniciar sesión") {
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
        btnLogin.setBounds(30, 237, 290, 38);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setForeground(C_BLANCO);
        btnLogin.setBackground(C_BTN_GRIS);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setFocusPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setEnabled(false);
        Carta.add(btnLogin);

        // Hover del boton
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (camposOk()) btnLogin.setBackground(new Color(24, 119, 242));
            }
            @Override public void mouseExited(MouseEvent e) {
                if (camposOk()) btnLogin.setBackground(C_AZUL);
                else            btnLogin.setBackground(C_BTN_GRIS);
            }
        });

        // Accion login
        btnLogin.addActionListener(e -> accionLogin());
        txtUsuario.addActionListener(e -> accionLogin());
        txtContra .addActionListener(e -> accionLogin());

        // Label error debajo de campos
        JLabel lblOlvide = new JLabel("¿Olvidaste tu contraseña?", SwingConstants.CENTER);
        lblOlvide.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblOlvide.setForeground(C_AZUL);
        lblOlvide.setBounds(20, 284, 310, 20);
        lblOlvide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Carta.add(lblOlvide);

        // Carta "No tienes cuenta"
        int C2Y = CartaY + CartaAlto + 10;
        JPanel cardRegistro = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_BLANCO); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_BORDE);  g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        cardRegistro.setOpaque(false);
        cardRegistro.setBounds(CartaX, C2Y, CartaAncho, 52);
        panel.add(cardRegistro);

        JLabel lblNoTienes = new JLabel("¿No tienes una cuenta?");
        lblNoTienes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNoTienes.setForeground(C_TEXTO);
        lblNoTienes.setBounds(38, 14, 185, 24);
        cardRegistro.add(lblNoTienes);

        JLabel lblRegistrate = new JLabel("Regístrate");
        lblRegistrate.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRegistrate.setForeground(C_AZUL);
        lblRegistrate.setBounds(228, 14, 90, 24);
        lblRegistrate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegistrate.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(ventana,
                    "Aquí irá la pantalla de registro.",
                    "Registro", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        cardRegistro.add(lblRegistrate);

        javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
            void actualizar() {
                boolean ok = camposOk();
                btnLogin.setBackground(ok ? C_AZUL : C_BTN_GRIS);
                btnLogin.setEnabled(ok);
            }
            @Override public void insertUpdate (javax.swing.event.DocumentEvent e) { actualizar(); }
            @Override public void removeUpdate (javax.swing.event.DocumentEvent e) { actualizar(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
        };
        txtUsuario.getDocument().addDocumentListener(dl);
        txtContra .getDocument().addDocumentListener(dl);

        return panel;
    }

    private boolean camposOk() {
        String u = txtUsuario.getText();
        String p = String.valueOf(txtContra.getPassword());
        return !u.isEmpty() && !u.equals("Usuario")
            && !p.isEmpty() && !p.equals("Contraseña");
    }

    private void accionLogin() {
        String usuario = txtUsuario.getText();
        String contra  = String.valueOf(txtContra.getPassword());

        if (usuario.isEmpty() || usuario.equals("Usuario") ||
            contra.isEmpty()  || contra.equals("Contraseña")) {
            lblExcepcion.setText("Completa todos los campos.");
            return;
        }

        //    Por ahora: usuario "chri", contrasena "123"
        if (usuario.equals("chri") && contra.equals("123")) {
            usuarioActual = usuario;
            lblExcepcion.setText(" ");
            
            txtUsuario.setText("Usuario"); txtUsuario.setForeground(C_GRIS);
            txtContra.setText("Contraseña"); txtContra.setForeground(C_GRIS);
            txtContra.setEchoChar((char) 0); passVisible[0] = false;
            ir("home");
        } else {
            lblExcepcion.setText("Usuario o contraseña incorrectos.");
            txtContra.setText("Contraseña"); txtContra.setForeground(C_GRIS);
            txtContra.setEchoChar((char) 0); passVisible[0] = false;
            btnOjo.repaint();
        }
    }
    
    //FIN DE INICIO===============================================================
    
    private JPanel construirPantallaHome() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        construirTopBar(panel);
        construirSidebar(panel);

        //El feed ocupa el centro
        JPanel feed = construirVista_Feed();
        feed.setBounds(SIDEBAR_W, TOPBAR_H, W - SIDEBAR_W - RIGHT_W, H - TOPBAR_H);
        panel.add(feed);

        construirPanelDerecho(panel);
        return panel;
    }

    private void construirTopBar(JPanel padre) {
        JPanel bar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_BLANCO); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_BORDE);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setBounds(0, 0, W, TOPBAR_H);
        padre.add(bar);

        // Logo
        JLabel logo = new JLabel("Instagram") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(88, 81, 219),
                    getWidth(), 0, new Color(247, 119, 55));
                g2.setPaint(gp); g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 0, fm.getAscent());
                g2.dispose();
            }
        };
        logo.setFont(new Font("Segoe Script", Font.PLAIN, 24));
        logo.setBounds(20, 10, 200, 36);
        bar.add(logo);

        // Buscador
        JTextField txtBuscar = campoBusqueda("  Buscar...", W / 2 - 130, 12, 260, 30);
        bar.add(txtBuscar);

        // Botones de navegacion derecha
        int bx = W - 195, by = 10;
        bar.add(btnTopBar("Inicio",  bx,       by, () -> ir("home")));
        bar.add(btnTopBar("DMs",     bx + 45,  by, () -> ir("inbox")));
        bar.add(btnTopBar("Notif.",  bx + 90,  by, () -> ir("notif")));
        bar.add(btnTopBar("Perfil",  bx + 135, by, () -> ir("perfil")));
    }

    private JButton btnTopBar(String txt, int x, int y, Runnable accion) {
        JButton b = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(0, 0, 0, 12));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                g2.setColor(C_TEXTO); g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        b.setBounds(x, y, 42, 34);
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> accion.run());
        return b;
    }

    // ── SIDEBAR ───────────────────────────────────────────────
    private void construirSidebar(JPanel padre) {
        JPanel sb = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_BLANCO); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_BORDE);
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
                g2.dispose();
            }
        };
        sb.setOpaque(false);
        sb.setBounds(0, TOPBAR_H, SIDEBAR_W, H - TOPBAR_H);
        padre.add(sb);

        String[]   items   = {"Inicio","Explorar","Nueva pub.","Mensajes","Notificaciones","Perfil","Configuración"};
        Runnable[] acciones = {
            () -> ir("home"), () -> ir("explorar"),
            this::dialogoNuevaPublicacion,
            () -> ir("inbox"), () -> ir("notif"), () -> ir("perfil"),
            this::dialogoConfiguracion
        };
        int y = 18;
        for (int i = 0; i < items.length; i++) {
            final Runnable ac = acciones[i];
            JButton btn = itemSidebar(items[i]);
            btn.setBounds(0, y, SIDEBAR_W, 46);
            btn.addActionListener(e -> ac.run());
            sb.add(btn); y += 48;
        }

        // Mini card usuario
        JPanel mini = miniCardUsuario();
        mini.setBounds(0, H - TOPBAR_H - 64, SIDEBAR_W, 58);
        sb.add(mini);
    }

    private JButton itemSidebar(String txt) {
        JButton b = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (getModel().isRollover()) {
                    g2.setColor(new Color(0, 0, 0, 8));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.setColor(C_TEXTO); g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 22, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        b.setFont(new Font("SansSerif", Font.PLAIN, 15));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel miniCardUsuario() {
        JPanel p = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(C_BLANCO); g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(C_BORDE);  g.drawLine(0, 0, getWidth(), 0);
            }
        };
        p.setOpaque(false);
        avatar(38, new Color(200, 210, 230)).setBounds(14, 10, 38, 38);
        p.add(avatar(38, new Color(200, 210, 230)));

                JLabel u = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                setText(usuarioActual.isEmpty() ? "usuario" : usuarioActual);
                super.paintComponent(g);
            }
        };
        u.setFont(new Font("SansSerif", Font.BOLD, 13));
        u.setForeground(C_TEXTO); u.setBounds(60, 10, 140, 18); p.add(u);

        JLabel sub = new JLabel("Tu cuenta");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(C_GRIS); sub.setBounds(60, 29, 140, 16); p.add(sub);

        JButton salir = new JButton("Salir") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_GRIS); g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        salir.setBounds(196, 3, 40, 24);
        salir.setBorderPainted(false); salir.setContentAreaFilled(false); salir.setFocusPainted(false);
        salir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        salir.addActionListener(e -> ir("login"));
        p.add(salir);
        return p;
    }

    private void construirPanelDerecho(JPanel padre) {
        JPanel p = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(C_BLANCO); g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(C_BORDE);  g.drawLine(0, 0, 0, getHeight());
            }
        };
        p.setOpaque(false);
        p.setBounds(W - RIGHT_W, TOPBAR_H, RIGHT_W, H - TOPBAR_H);
        padre.add(p);

        // Usuario actual
        p.add(avatar(44, new Color(200, 210, 230))).setBounds(16, 18, 44, 44);
        JLabel me = new JLabel(usuarioActual.isEmpty() ? "usuario" : usuarioActual);
        me.setFont(new Font("SansSerif", Font.BOLD, 14)); me.setForeground(C_TEXTO);
        me.setBounds(70, 18, 150, 20); p.add(me);
        JLabel meN = new JLabel("Nombre Completo");
        meN.setFont(new Font("SansSerif", Font.PLAIN, 12)); meN.setForeground(C_GRIS);
        meN.setBounds(70, 39, 150, 16); p.add(meN);

        JSeparator sep = new JSeparator(); sep.setBounds(16, 74, RIGHT_W - 32, 1); p.add(sep);

        JLabel lSug = new JLabel("Sugerencias para ti");
        lSug.setFont(new Font("SansSerif", Font.BOLD, 13)); lSug.setForeground(C_TEXTO);
        lSug.setBounds(16, 82, 200, 20); p.add(lSug);

        String[] sugs = {"Cuenta1","Cuenta2","Cuenta3","Cuenta4","Cuenta5"};
        int sy = 110;
        for (String s : sugs) { p.add(filaSugerencia(s, sy)); sy += 48; }
    }

    private JPanel filaSugerencia(String username, int y) {
        JPanel row = new JPanel(null); row.setOpaque(false);
        row.setBounds(0, y, RIGHT_W, 44);
        row.add(avatar(32, new Color(210, 215, 230))).setBounds(16, 6, 32, 32);

        JLabel n = new JLabel("@" + username);
        n.setFont(new Font("SansSerif", Font.BOLD, 12)); n.setForeground(C_TEXTO);
        n.setBounds(56, 6, 140, 16); row.add(n);
        JLabel s = new JLabel("Sugerido para ti");
        s.setFont(new Font("SansSerif", Font.PLAIN, 11)); s.setForeground(C_GRIS);
        s.setBounds(56, 23, 140, 14); row.add(s);

        JButton f = new JButton("Seguir") {
            @Override protected void paintComponent(Graphics g) {
                ((Graphics2D)g).setColor(getText().equals("Seguir") ? C_AZUL : C_GRIS);
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        f.setBounds(RIGHT_W - 72, 12, 60, 22);
        f.setBorderPainted(false); f.setContentAreaFilled(false); f.setFocusPainted(false);
        f.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        f.addActionListener(e -> f.setText(f.getText().equals("Seguir") ? "Siguiendo" : "Seguir"));
        row.add(f);
        return row;
    }
    
    // VISTA: FEED
    private JPanel construirVista_Feed() {
        JPanel outer = new JPanel(null);
        outer.setOpaque(false);

        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(C_FONDO);

        wrap.add(Box.createVerticalStrut(14));
        wrap.add(post("cuenta4",  "",  "Hace 0h",  600, 400));
        wrap.add(Box.createVerticalStrut(10));
        wrap.add(post("cuenta3",   "",           "Hace 0h",  600, 600));
        wrap.add(Box.createVerticalStrut(10));
        wrap.add(post("cuenta2",   "",               "Hace 0h",  600, 400));
        wrap.add(Box.createVerticalStrut(10));
        wrap.add(post("cuenta1",  "",            "Ayer",     600, 750));
        wrap.add(Box.createVerticalStrut(24));

        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.setBounds(0, 0, W - SIDEBAR_W - RIGHT_W, H - TOPBAR_H);
        outer.add(scroll);
        return outer;
    }

    private JPanel post(String user, String contenido, String fecha, int imgW, int imgH) {
        int aw  = W - SIDEBAR_W - RIGHT_W - 20;
        int ah  = Math.min((int)(aw * ((double) imgH / imgW)), 500);
        int tot = 52 + ah + 44 + 22 + 20 + 20 + 44 + 8;

        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(C_BLANCO); g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(C_BORDE);
                g.drawLine(0, 0, getWidth(), 0);
                g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(aw, tot));
        card.setMaximumSize(new Dimension(aw, tot));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(avatar(34, new Color(200,210,230))).setBounds(14, 9, 34, 34);
        JLabel lu = new JLabel("@" + user);
        lu.setFont(new Font("SansSerif", Font.BOLD, 14)); lu.setForeground(C_TEXTO);
        lu.setBounds(56, 9, 200, 18); card.add(lu);
        JLabel lf = new JLabel(fecha);
        lf.setFont(new Font("SansSerif", Font.PLAIN, 11)); lf.setForeground(C_GRIS);
        lf.setBounds(56, 28, 200, 14); card.add(lf);

        JPanel img = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0,0,new Color(195,205,230),
                    getWidth(),getHeight(),new Color(175,175,210));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(new Color(130,130,160));
                g2.setFont(new Font("SansSerif",Font.PLAIN,13));
                String t = imgW + " × " + imgH + " px";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(t,(getWidth()-fm.stringWidth(t))/2, getHeight()/2+fm.getAscent()/2);
                g2.dispose();
            }
        };
        img.setBounds(0, 52, aw, ah); card.add(img);

        // Botones accion
        int ay = 52 + ah + 6;
        boolean[] liked = {false};
        JButton bLike = btnAccion("♡ Me gusta");
        bLike.setBounds(14, ay, 108, 32);
        bLike.addActionListener(e -> {
            liked[0] = !liked[0];
            bLike.setForeground(liked[0] ? C_ERROR : C_TEXTO);
            bLike.setText(liked[0] ? "♥ Me gusta" : "♡ Me gusta");
        });
        card.add(bLike);
        JButton bComm = btnAccion("💬 Comentar");
        bComm.setBounds(128, ay, 112, 32);
        bComm.addActionListener(e -> dialogoComentarios(user));
        card.add(bComm);
        JButton bComp = btnAccion("➤ Compartir");
        bComp.setBounds(248, ay, 112, 32); card.add(bComp);

        int iy = ay + 36;
        JLabel lLikes = new JLabel("0 me gusta");
        lLikes.setFont(new Font("SansSerif",Font.BOLD,13)); lLikes.setForeground(C_TEXTO);
        lLikes.setBounds(14, iy, 200, 18); card.add(lLikes);

        JLabel lCap = new JLabel("<html><b>@"+user+"</b>  "+contenido+"</html>");
        lCap.setFont(new Font("SansSerif",Font.PLAIN,13)); lCap.setForeground(C_TEXTO);
        lCap.setBounds(14, iy+20, aw-28, 18); card.add(lCap);

        JLabel lVer = new JLabel("Ver los 0 comentarios");
        lVer.setFont(new Font("SansSerif",Font.PLAIN,12)); lVer.setForeground(C_GRIS);
        lVer.setBounds(14, iy+40, 180, 16);
        lVer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lVer.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e) { dialogoComentarios(user); }
        });
        card.add(lVer);

        int cy = iy + 62;
        card.add(avatar(26, new Color(200,210,230))).setBounds(14, cy+9, 26, 26);
        
        JButton bp = new JButton(""){
            @Override protected void paintComponent(Graphics g){
                g.setColor(C_AZUL); g.setFont(new Font("SansSerif",Font.BOLD,12));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        bp.setBounds(aw-46, cy+9, 42, 24);
        bp.setBorderPainted(false); bp.setContentAreaFilled(false); bp.setFocusPainted(false);
        bp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(bp);

        return card;
    }

    private JPanel construirVistaExplorar() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        JPanel wrap = new JPanel(null);
        wrap.setOpaque(false);
        wrap.setBounds(0, 0, W, H);

        JPanel contenido = new JPanel(null);
        contenido.setBackground(C_FONDO);
        contenido.setBounds(SIDEBAR_W, TOPBAR_H, W - SIDEBAR_W - RIGHT_W, H - TOPBAR_H);

        JLabel titulo = new JLabel("Explorar");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(C_TEXTO); titulo.setBounds(20, 18, 200, 30); contenido.add(titulo);

        JTextField srch = campoBusqueda("  Buscar #hashtag o @usuario", 20, 56, contenido.getPreferredSize().width - 40, 34);
        srch.setBounds(20, 56, W - SIDEBAR_W - RIGHT_W - 40, 34);
        contenido.add(srch);

        int cols = 3, gap = 3;
        int gw = (W - SIDEBAR_W - RIGHT_W - 40 - gap * (cols - 1)) / cols;
        Color[] cs = {new Color(255,200,200),new Color(200,220,255),new Color(200,255,210),
                      new Color(255,240,200),new Color(225,200,255)};
        for (int i = 0; i < 9; i++) {
            final Color c = cs[i % cs.length]; final int idx = i;
            JPanel cell = new JPanel(){
                @Override protected void paintComponent(Graphics g){
                    g.setColor(c); g.fillRect(0,0,getWidth(),getHeight());
                    g.setColor(new Color(0,0,0,60)); g.setFont(new Font("SansSerif",Font.PLAIN,12));
                    g.drawString("Post "+(idx+1), getWidth()/2-20, getHeight()/2+5);
                }
            };
            cell.setBounds(20+(i%cols)*(gw+gap), 104+(i/cols)*(gw+gap), gw, gw);
            cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            contenido.add(cell);
        }
        wrap.add(contenido);
        construirTopBar(wrap); construirSidebar(wrap); construirPanelDerecho(wrap);
        panel.add(wrap);
        return panel;
    }

    private JPanel construirVistaInbox() {
        JPanel panel = new JPanel(null); panel.setOpaque(false);
        JPanel wrap  = new JPanel(null); wrap.setOpaque(false); wrap.setBounds(0,0,W,H);

        JPanel contenido = new JPanel(null);
        contenido.setBackground(C_BLANCO);
        contenido.setBounds(SIDEBAR_W, TOPBAR_H, W - SIDEBAR_W - RIGHT_W, H - TOPBAR_H);

        // Lista conversaciones
        JPanel lista = new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                g.setColor(C_BLANCO); g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(C_BORDE); g.drawLine(getWidth()-1,0,getWidth()-1,getHeight());
            }
        };
        lista.setBounds(0, 0, 300, H - TOPBAR_H);

        JLabel lblM = new JLabel("  Mensajes");
        lblM.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblM.setBounds(0, 0, 300, 50);
        lblM.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDE));
        lista.add(lblM);

        String[][] convs = {
            /*
            {"ana_garcia",  "Hola! Como estas?",         "10:30","2"},
            {"carlos_mx",   "Viste mi ultima foto?",      "09:15","0"},
            {"sofia_dev",   "Revisa este link!",          "Ayer", "1"},
            {"mario_foto",  "Gracias por el like!",       "Lun",  "0"},
            {"luna_arte",   "Me encanto tu publicacion!", "Dom",  "0"}
            */
        };
        int ry = 52;
        for (String[] c : convs) { lista.add(filaConv(c[0],c[1],c[2],Integer.parseInt(c[3]),ry)); ry+=70; }
        contenido.add(lista);

        // Chat
        int chatX = 300, chatW = W - SIDEBAR_W - RIGHT_W - 300;
        contenido.add(panelChat(chatX, chatW, H - TOPBAR_H));

        wrap.add(contenido);
        construirTopBar(wrap); construirSidebar(wrap); construirPanelDerecho(wrap);
        panel.add(wrap);
        return panel;
    }

    private JPanel filaConv(String user, String msg, String time, int unread, int y) {
        JPanel r = new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                g.setColor(unread>0?new Color(245,245,255):C_BLANCO); g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(new Color(240,240,240)); g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };
        r.setOpaque(false); r.setBounds(0,y,299,68);
        r.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        r.add(avatar(42, new Color(200,210,230))).setBounds(12,13,42,42);
        JLabel n = new JLabel("@"+user); n.setFont(new Font("SansSerif",unread>0?Font.BOLD:Font.PLAIN,13));
        n.setForeground(C_TEXTO); n.setBounds(64,14,170,18); r.add(n);
        String sm = msg.length()>26 ? msg.substring(0,26)+"…" : msg;
        JLabel m = new JLabel(sm); m.setFont(new Font("SansSerif",Font.PLAIN,12)); m.setForeground(C_GRIS);
        m.setBounds(64,34,170,16); r.add(m);
        JLabel t = new JLabel(time); t.setFont(new Font("SansSerif",Font.PLAIN,11)); t.setForeground(C_GRIS);
        t.setBounds(236,14,50,16); r.add(t);
        if (unread>0){
            JLabel b = new JLabel(String.valueOf(unread),SwingConstants.CENTER){
                @Override protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(C_AZUL); g2.fillOval(0,0,getWidth(),getHeight());
                    g2.setColor(C_BLANCO); g2.setFont(new Font("SansSerif",Font.BOLD,10));
                    FontMetrics fm=g2.getFontMetrics();
                    g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
                    g2.dispose();
                }
            };
            b.setBounds(246,36,20,20); r.add(b);
        }
        return r;
    }

    private JPanel panelChat(int x, int w, int h) {
        JPanel chat = new JPanel(null); chat.setBackground(C_BLANCO);
        chat.setBounds(x, 0, w, h);

        JPanel hdr = new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                g.setColor(C_BLANCO); g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(C_BORDE); g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };
        hdr.setBounds(0,0,w,52);
        hdr.add(avatar(34, new Color(200,210,230))).setBounds(14,9,34,34);
        JLabel hu = new JLabel("@Cuenta"); hu.setFont(new Font("SansSerif",Font.BOLD,14));
        hu.setForeground(C_TEXTO); hu.setBounds(56,9,200,34); hdr.add(hu);
        chat.add(hdr);

        JPanel msgs = new JPanel(null); msgs.setBackground(C_BLANCO);
        String[][] ms = {
            /*
            {"Hola! Como estas?",                           "otro","10:28"},
            {"Todo bien! Y tu?",                             "yo",  "10:29"},
            {"Muy bien! Vi tu ultima foto, quedo increible!","otro","10:30"},
            {"Gracias! Era en la playa",                    "yo",  "10:31"}
            */
        };
        int my = 10;
        for (String[] m : ms) {
            boolean mine = m[1].equals("yo");
            JPanel bbl = burbuja(m[0], mine, w);
            bbl.setBounds(0, my, w, bbl.getPreferredSize().height+10);
            msgs.add(bbl); my += bbl.getPreferredSize().height + 14;
        }
        msgs.setPreferredSize(new Dimension(w, my+10));
        JScrollPane sc = new JScrollPane(msgs);
        sc.setBorder(BorderFactory.createEmptyBorder()); sc.getVerticalScrollBar().setUnitIncrement(14);
        sc.setBounds(0, 52, w, h - 52 - 56); chat.add(sc);

        JPanel inp = new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                g.setColor(C_BLANCO); g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(C_BORDE); g.drawLine(0,0,getWidth(),0);
            }
        };
        inp.setBounds(0, h-56, w, 56);
        JTextField ti = campoBusqueda("Escribe un mensaje...", 14, 12, w-80, 32);
        inp.add(ti);
        JButton bs = new JButton("Enviar"){
            @Override protected void paintComponent(Graphics g){
                g.setColor(C_AZUL); g.setFont(new Font("SansSerif",Font.BOLD,13));
                FontMetrics fm=g.getFontMetrics();
                g.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        bs.setBounds(w-60,14,52,28); bs.setBorderPainted(false); bs.setContentAreaFilled(false);
        bs.setFocusPainted(false); bs.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        inp.add(bs); chat.add(inp);
        return chat;
    }

    private JPanel burbuja(String texto, boolean mine, int panelW) {
        int maxW = (int)(panelW * 0.58);
        JLabel lbl = new JLabel("<html><div style='width:"+maxW+"px'>"+texto+"</div></html>");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(mine ? C_BLANCO : C_TEXTO);
        Dimension ps = lbl.getPreferredSize();
        int bw = Math.min(ps.width+26, maxW+26);
        int bh = ps.height+20;

        JPanel bubble = new JPanel(null){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(mine ? C_AZUL : new Color(239,239,239));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),18,18);
                g2.dispose();
            }
        };
        lbl.setBounds(12,6,bw-24,ps.height+4); bubble.add(lbl);
        bubble.setOpaque(false); bubble.setPreferredSize(new Dimension(bw,bh));

        JPanel row = new JPanel(null); row.setOpaque(false);
        bubble.setBounds(mine ? panelW-bw-16 : 16, 0, bw, bh);
        row.add(bubble); row.setPreferredSize(new Dimension(panelW, bh));
        return row;
    }

    private JPanel construirVistaNotif() {
        JPanel panel = new JPanel(null); panel.setOpaque(false);
        JPanel wrap  = new JPanel(null); wrap.setOpaque(false); wrap.setBounds(0,0,W,H);

        JPanel contenido = new JPanel(null);
        contenido.setBackground(C_FONDO);
        contenido.setBounds(SIDEBAR_W, TOPBAR_H, W - SIDEBAR_W - RIGHT_W, H - TOPBAR_H);

        JLabel titulo = new JLabel("Notificaciones");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20)); titulo.setForeground(C_TEXTO);
        titulo.setBounds(20, 18, 260, 30);
        titulo.setBorder(BorderFactory.createMatteBorder(0,0,1,0,C_BORDE));
        contenido.add(titulo);

        String[][] ns = {
            /*
            {"❤",  "ana_garcia",   "le dio me gusta a tu foto.",            "10:30"},
            {"👤", "carlos_mx",    "empezó a seguirte.",                    "09:00"},
            {"💬", "sofia_dev",    "comentó: \"Genial!\"",                  "Ayer"},
            {"📌", "mario_foto",   "te mencionó en una publicación.",       "Lun"},
            {"👤", "pedro_v",      "quiere seguirte. (Perfil privado)",     "Dom"}
            */
        };
        int ny = 60;
        for (String[] n : ns) {
            JPanel row = new JPanel(null); row.setOpaque(false);
            row.setBounds(0, ny, W-SIDEBAR_W-RIGHT_W, 52);
            row.add(avatar(36, new Color(200,210,230))).setBounds(14,8,36,36);
            JLabel ic = new JLabel(n[0]); ic.setFont(new Font("SansSerif",Font.PLAIN,18));
            ic.setBounds(58,14,28,24); row.add(ic);
            JLabel lb = new JLabel("<html><b>@"+n[1]+"</b>  "+n[2]+"</html>");
            lb.setFont(new Font("SansSerif",Font.PLAIN,13)); lb.setForeground(C_TEXTO);
            lb.setBounds(90,8,400,36); row.add(lb);
            JLabel ti = new JLabel(n[3]); ti.setFont(new Font("SansSerif",Font.PLAIN,11));
            ti.setForeground(C_GRIS); ti.setBounds(W-SIDEBAR_W-RIGHT_W-70,18,56,16); row.add(ti);
            JSeparator sep = new JSeparator(); sep.setBounds(14,50,W-SIDEBAR_W-RIGHT_W-28,1); row.add(sep);
            contenido.add(row); ny += 52;
        }
        wrap.add(contenido);
        construirTopBar(wrap); construirSidebar(wrap); construirPanelDerecho(wrap);
        panel.add(wrap);
        return panel;
    }

    private JPanel construirVistaPerfil() {
        JPanel panel = new JPanel(null); panel.setOpaque(false);
        JPanel wrap  = new JPanel(null); wrap.setOpaque(false); wrap.setBounds(0,0,W,H);

        JPanel outer = new JPanel(null);
        outer.setBackground(C_FONDO);
        outer.setBounds(SIDEBAR_W, TOPBAR_H, W - SIDEBAR_W - RIGHT_W, H - TOPBAR_H);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(C_FONDO);

        JPanel head = new JPanel(null); head.setBackground(C_FONDO);
        head.setPreferredSize(new Dimension(W-SIDEBAR_W-RIGHT_W, 130));
        head.setMaximumSize(new Dimension(W-SIDEBAR_W-RIGHT_W, 130));
        head.add(avatar(86, new Color(180,195,220))).setBounds(40,22,86,86);

        JLabel lu = new JLabel(usuarioActual.isEmpty() ? "usuario" : usuarioActual);
        lu.setFont(new Font("SansSerif",Font.PLAIN,22)); lu.setForeground(C_TEXTO);
        lu.setBounds(160,22,200,30); head.add(lu);

        JButton bedit = btnSolido("Editar perfil"); bedit.setBounds(160,60,120,32); head.add(bedit);

        head.add(statLabel("0","publicaciones",320,22));
        head.add(statLabel("0","seguidores",430,22));
        head.add(statLabel("0","seguidos",540,22));

        JLabel lnom = new JLabel("Nombre Completo");
        lnom.setFont(new Font("SansSerif",Font.BOLD,13)); lnom.setForeground(C_TEXTO);
        lnom.setBounds(160,100,260,18); head.add(lnom);
        content.add(head);

        JSeparator sep = new JSeparator(); sep.setMaximumSize(new Dimension(W,1)); sep.setForeground(C_BORDE);
        content.add(sep);

        JPanel tabRow = new JPanel(null); tabRow.setBackground(C_FONDO);
        tabRow.setPreferredSize(new Dimension(W-SIDEBAR_W-RIGHT_W,36));
        tabRow.setMaximumSize(new Dimension(W-SIDEBAR_W-RIGHT_W,36));
        JLabel tab = new JLabel("PUBLICACIONES", SwingConstants.CENTER);
        tab.setFont(new Font("SansSerif",Font.BOLD,12)); tab.setForeground(C_TEXTO);
        tab.setBorder(BorderFactory.createMatteBorder(2,0,0,0,C_TEXTO));
        tab.setBounds(0,0,160,36); tabRow.add(tab); content.add(tabRow);

        int gw = W-SIDEBAR_W-RIGHT_W, cols = 4, gap = 3;
        int cw = (gw - gap*(cols-1)) / cols;
        JPanel grid = new JPanel(null); grid.setBackground(C_FONDO);
        int rows = 4;
        grid.setPreferredSize(new Dimension(gw, rows*(cw+gap)));
        grid.setMaximumSize(new Dimension(gw, rows*(cw+gap)));
        Color[] pal = {new Color(200,215,235),new Color(215,200,235),
                       new Color(200,235,210),new Color(235,225,200)};
        for (int i = 0; i < cols*rows; i++) {
            final Color c = pal[i%pal.length]; final int idx = i;
            JPanel cell = new JPanel(){
                @Override protected void paintComponent(Graphics g){
                    g.setColor(c); g.fillRect(0,0,getWidth(),getHeight());
                    g.setColor(new Color(80,80,100,140)); g.setFont(new Font("SansSerif",Font.PLAIN,11));
                    g.drawString("Foto "+(idx+1), getWidth()/2-18, getHeight()/2+4);
                }
            };
            cell.setBounds((i%cols)*(cw+gap),(i/cols)*(cw+gap),cw,cw);
            cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cell.addMouseListener(new MouseAdapter(){
                @Override public void mouseClicked(MouseEvent e){ dialogoComentarios("Foto"+(idx+1)); }
            });
            grid.add(cell);
        }
        content.add(grid);
        content.add(Box.createVerticalStrut(20));

        JScrollPane sc = new JScrollPane(content);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sc.getVerticalScrollBar().setUnitIncrement(16);
        sc.setBounds(0, 0, W-SIDEBAR_W-RIGHT_W, H-TOPBAR_H);
        outer.add(sc);

        wrap.add(outer);
        construirTopBar(wrap); construirSidebar(wrap); construirPanelDerecho(wrap);
        panel.add(wrap);
        return panel;
    }

    private void dialogoNuevaPublicacion() {
        JDialog dlg = new JDialog(ventana, "Nueva publicación", true);
        dlg.setSize(520, 460); dlg.setLocationRelativeTo(ventana); dlg.setLayout(null);

        JLabel t = new JLabel("Crear publicación", SwingConstants.CENTER);
        t.setFont(new Font("SansSerif",Font.BOLD,16)); t.setBounds(0,14,520,28); dlg.add(t);
        new JSeparator().setBounds(0,46,520,1);

        JPanel drop = new JPanel(){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(new Color(248,248,248)); g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(C_BORDE);
                g2.setStroke(new BasicStroke(1.8f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10,new float[]{6},0));
                g2.drawRect(1,1,getWidth()-2,getHeight()-2);
                g2.setColor(C_GRIS); g2.setFont(new Font("SansSerif",Font.PLAIN,13));
                String tx = "Haz clic para seleccionar imagen";
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(tx,(getWidth()-fm.stringWidth(tx))/2,getHeight()/2+5);
                g2.dispose();
            }
        };
        drop.setBounds(20,58,478,150);
        drop.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        drop.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){ new JFileChooser().showOpenDialog(dlg); }
        });
        dlg.add(drop);

        JLabel lTipo = new JLabel("Tipo:");
        lTipo.setFont(new Font("SansSerif",Font.PLAIN,13)); lTipo.setBounds(20,220,60,26); dlg.add(lTipo);
        JComboBox<String> cmb = new JComboBox<>(
            new String[]{"Cuadrada (600×600)","Vertical (600×750)","Horizontal (600×400)"});
        cmb.setBounds(70,220,200,26); dlg.add(cmb);

        JLabel lD = new JLabel("Descripción (máx. 220 caracteres):");
        lD.setFont(new Font("SansSerif",Font.PLAIN,13)); lD.setBounds(20,256,300,20); dlg.add(lD);
        JTextArea ta = new JTextArea(); ta.setLineWrap(true); ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createLineBorder(C_BORDE));
        JScrollPane sc = new JScrollPane(ta); sc.setBounds(20,280,478,80); dlg.add(sc);

        JLabel lc = new JLabel("0 / 220"); lc.setFont(new Font("SansSerif",Font.PLAIN,11));
        lc.setForeground(C_GRIS); lc.setBounds(440,364,60,16); dlg.add(lc);
        ta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            void u(){ int n=ta.getText().length(); lc.setText(n+" / 220"); lc.setForeground(n>220?C_ERROR:C_GRIS); }
            public void insertUpdate(javax.swing.event.DocumentEvent e){u();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){u();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){u();}
        });

        JButton bC = btnSolido("Cancelar"); bC.setBounds(310,388,90,34);
        bC.addActionListener(e -> dlg.dispose()); dlg.add(bC);
        JButton bP = btnAzul("Publicar"); bP.setBounds(410,388,90,34);
        bP.addActionListener(e -> { JOptionPane.showMessageDialog(dlg,"Publicacion creada!","Listo",1); dlg.dispose(); });
        dlg.add(bP);
        dlg.setVisible(true);
    }

    private void dialogoComentarios(String autor) {
        JDialog dlg = new JDialog(ventana, "Comentarios – @" + autor, true);
        dlg.setSize(480, 400); dlg.setLocationRelativeTo(ventana); dlg.setLayout(null);

        JPanel lista = new JPanel(); lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(C_BLANCO);
        String[][] cs = {{/*"carlos_mx","Increible foto!"},{"sofia_dev","Me encanta"},{"mario_foto","Que camara?"},{"luna_arte","Hermoso"*/}};
        for (String[] c : cs) {
            JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
            r.setBackground(C_BLANCO); r.setMaximumSize(new Dimension(9999,40));
            r.add(avatar(28,new Color(200,210,230)));
            JLabel lb = new JLabel("<html><b>@"+c[0]+"</b>  "+c[1]+"</html>");
            lb.setFont(new Font("SansSerif",Font.PLAIN,13)); r.add(lb); lista.add(r);
        }
        JScrollPane sc = new JScrollPane(lista); sc.setBorder(null); sc.setBounds(0,0,480,310); dlg.add(sc);

        JTextField tf = new JTextField("...");
        tf.setBounds(10,320,380,34); tf.setFont(new Font("SansSerif",Font.PLAIN,13)); tf.setForeground(C_GRIS);
        tf.setBorder(BorderFactory.createLineBorder(C_BORDE));
        tf.addFocusListener(new FocusAdapter(){
            @Override public void focusGained(FocusEvent e){
                if(tf.getText().equals("...")){ tf.setText(""); tf.setForeground(C_TEXTO); }
            }
        });
        dlg.add(tf);

        JButton bEnv = btnSolido("Publicar"); bEnv.setBounds(398,322,72,32);
        bEnv.addActionListener(e -> {
            if(!tf.getText().isEmpty() && !tf.getText().equals("...")){
                JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
                r.setBackground(C_BLANCO); r.setMaximumSize(new Dimension(9999,40));
                r.add(avatar(28,new Color(200,210,230)));
                JLabel lb = new JLabel("<html><b>@"+usuarioActual+"</b>  "+tf.getText()+"</html>");
                lb.setFont(new Font("SansSerif",Font.PLAIN,13)); r.add(lb); lista.add(r);
                lista.revalidate(); lista.repaint(); tf.setText("");
            }
        });
        dlg.add(bEnv);
        dlg.setVisible(true);
    }

    private void dialogoConfiguracion() {
        JDialog dlg = new JDialog(ventana, "Configuración", true);
        dlg.setSize(360, 300); dlg.setLocationRelativeTo(ventana); dlg.setLayout(null);
        JLabel t = new JLabel("Configuración de cuenta", SwingConstants.CENTER);
        t.setFont(new Font("SansSerif",Font.BOLD,15)); t.setBounds(0,14,360,26); dlg.add(t);
        String[] opts = {"Editar perfil","Cambiar contraseña","Cambiar a Privado / Público","Desactivar cuenta","Cerrar sesión"};
        int oy = 50;
        for (String o : opts) {
            JButton b = new JButton(o); b.setFont(new Font("SansSerif",Font.PLAIN,14));
            b.setForeground(o.contains("Desactivar") ? C_ERROR : C_TEXTO);
            b.setBounds(0,oy,360,40); b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,C_BORDE),
                BorderFactory.createEmptyBorder(0,20,0,0)));
            b.setBackground(C_BLANCO); b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (o.equals("Cerrar sesión")) b.addActionListener(e -> { dlg.dispose(); ir("login"); });
            dlg.add(b); oy += 40;
        }
        dlg.setVisible(true);
    }
    
    private JLabel avatar(int size, Color color) {
        return new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color); g2.fillOval(0, 0, size, size);
                g2.setColor(C_BORDE); g2.setStroke(new BasicStroke(1.2f));
                g2.drawOval(0, 0, size-1, size-1);
                g2.setColor(new Color(255,255,255,160));
                g2.fillOval(size/2-size/6, size/4, size/3, size/3);
                g2.dispose();
            }
            public Dimension getPreferredSize(){ return new Dimension(size,size); }
            public Dimension getMinimumSize()  { return new Dimension(size,size); }
        };
    }

    private JTextField campoBusqueda(String ph, int x, int y, int w, int h) {
        JTextField tf = new JTextField(ph) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(239,239,239)); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        tf.setBounds(x,y,w,h);
        tf.setFont(new Font("SansSerif",Font.PLAIN,13)); tf.setForeground(C_GRIS);
        tf.setBorder(BorderFactory.createEmptyBorder(0,10,0,10)); tf.setOpaque(false);
        final String p = ph;
        tf.addFocusListener(new FocusAdapter(){
            @Override public void focusGained(FocusEvent e){ if(tf.getText().equals(p)){ tf.setText(""); tf.setForeground(C_TEXTO); } }
            @Override public void focusLost(FocusEvent e){ if(tf.getText().isEmpty()){ tf.setText(p); tf.setForeground(C_GRIS); } }
        });
        return tf;
    }

    private JButton btnAccion(String txt) {
        JButton b = new JButton(txt){
            @Override protected void paintComponent(Graphics g){
                if(getModel().isRollover()){ g.setColor(new Color(0,0,0,8)); g.fillRect(0,0,getWidth(),getHeight()); }
                g.setColor(getForeground()); g.setFont(getFont());
                FontMetrics fm=g.getFontMetrics();
                g.drawString(getText(),4,(getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        b.setFont(new Font("SansSerif",Font.PLAIN,14)); b.setForeground(C_TEXTO);
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton btnSolido(String txt) {
        JButton b = new JButton(txt){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_BLANCO); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(C_BORDE);  g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                g2.setColor(C_TEXTO); g2.setFont(getFont());
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setFont(new Font("SansSerif",Font.BOLD,13));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton btnAzul(String txt) {
        JButton b = new JButton(txt){
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_AZUL); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.setColor(C_BLANCO); g2.setFont(getFont());
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setFont(new Font("SansSerif",Font.BOLD,13));
        b.setBorderPainted(false); b.setContentAreaFilled(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel statLabel(String val, String lbl, int x, int y) {
        JPanel p = new JPanel(null); p.setOpaque(false); p.setBounds(x,y,110,40);
        JLabel v = new JLabel(val, SwingConstants.CENTER);
        v.setFont(new Font("SansSerif",Font.BOLD,16)); v.setForeground(C_TEXTO); v.setBounds(0,0,110,22); p.add(v);
        JLabel l = new JLabel(lbl, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif",Font.PLAIN,12)); l.setForeground(C_GRIS); l.setBounds(0,22,110,16); p.add(l);
        return p;
    }

}