/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import Logica.*;
import PEnums.Enums.EstadoCuenta;
import PEnums.Enums.TipoMultimedia;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 *
 * @author Rogelio
 */
public class Gui_Inicio {

    static final Color C_BORDE      = new Color(219, 219, 219);
    static final Color C_FONDO_CAMP = new Color(250, 250, 250);
    static final Color C_TEXTO      = new Color(38, 38, 38);
    static final Color C_GRIS       = new Color(142, 142, 142);
    static final Color C_AZUL       = new Color(0, 149, 246);
    static final Color C_BTN_GRIS   = new Color(147, 204, 247);
    static final Color C_ERROR      = new Color(237, 73, 86);
    static final Color C_BLANCO     = Color.WHITE;
    static final Color C_FONDO      = new Color(250, 250, 250);

    static final int W         = 1366;
    static final int H         = 768;
    static final int TOPBAR_H  = 54;
    static final int SIDEBAR_W = 244;
    static final int RIGHT_W   = 300;

    JFrame        ventana;
    JPanel        panelRaiz;
    CardLayout    cardLayout;
    JPanel        pnlCards;
    String        usuarioActual = "";

    JLabel        lblExcepcion;
    JTextField    txtUsuario;
    JPasswordField txtContra;
    JButton       btnLogin;
    JButton       btnOjo;
    boolean[]     passVisible = {false};

    public Gui_Inicio() {
        ventana = new JFrame("Instagram");
        ventana.setSize(W, H);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);

        panelRaiz = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 230, 255), W, H, new Color(255, 230, 210));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelRaiz.setBackground(C_FONDO);
        panelRaiz.setBounds(0, 0, W, H);

        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        pnlCards.setOpaque(false);
        pnlCards.setBounds(0, 0, W, H);

        GestorArchivos.inicializar();
        IniciarCuentasPredeter();

        pnlCards.add(construirPantallaLogin(), "login");
        Gui_Registro guiReg = new Gui_Registro(ventana, cardLayout, pnlCards);
        pnlCards.add(guiReg.construirPantalla(), "registro");

        panelRaiz.add(pnlCards);
        cardLayout.show(pnlCards, "login");

        ventana.setContentPane(panelRaiz);
        ventana.setVisible(true);
    }

    private void IniciarCuentasPredeter() {
        crearCuentaConPublicaciones(
            "razer_hn", "Razer Honduras", "M", "razer123", 5, "Publica",
            new String[]{
                "Nueva llegada: Razer BlackWidow V4 Pro. El teclado mecanico definitivo para gaming. #razer #gaming #teclado",
                "Headset Razer BlackShark V2 ahora disponible. Sonido surround 7.1 para dominar cada partida. #razer #headset #gaming",
                "Mouse Razer DeathAdder V3 en stock. Sensor Focus Pro 30K para precision maxima. #razer #mouse #perifericos",
                "Silla Razer Iskur disponible en tienda. Ergonomia y estilo gamer en un solo producto. #razer #silla #setup"
            }
        );

        crearCuentaConPublicaciones(
            "espnfc_hn", "ESPN FC Honduras", "M", "espnfc123", 8, "Publica",
            new String[]{
                "Real Madrid golea 4-0 al Barcelona en el Clasico. Vinicius Jr anota doblete en el Bernabeu. #futbol #clasico #realmadrid",
                "Manchester City clasificado a semifinales de Champions League tras vencer al Bayern Munich. #champions #mancity",
                "Lionel Messi convierte desde el punto penal y Argentina avanza en las eliminatorias. #messi #argentina #eliminatorias",
                "Cristiano Ronaldo rompe otro record: 900 goles en su carrera profesional. #cristiano #record #futbol"
            }
        );

        crearCuentaConPublicaciones(
            "memes_hn", "Memes Honduras", "M", "memes123", 3, "Publica",
            new String[]{
                "Cuando el profesor dice que el examen sera facil y llega con 50 preguntas de desarrollo. #memes #universidad #examen",
                "Yo a las 11pm: voy a dormir temprano. Yo a las 3am: #memes #vida #noche",
                "El cafe de las 6am vs el cafe de las 6pm. Uno te despierta, el otro te despierta a las 3am. #memes #cafe #humor",
                "Cuando llegas tarde al trabajo y el jefe ya te estaba esperando en la puerta. #memes #trabajo #lunes"
            }
        );

        crearCuentaConPublicaciones(
            "titanfit_hn", "Titan Fitness HN", "M", "titanfit123", 6, "Publica",
            new String[]{
                "Nueva zona de pesas libres disponible. 500 metros cuadrados de equipos de ultima generacion. #gym #fitness #titanfit",
                "Clases de spinning todos los dias a las 6am y 7pm. Cupo limitado, reserva ya tu lugar. #spinning #cardio #fitness",
                "Proteina ON Gold Standard al mejor precio del mercado. Disponible en recepcion. #proteina #suplementos #gym",
                "Rutina de pecho y triceps para principiantes. Consulta con nuestros entrenadores certificados. #rutina #gym #entrenamiento"
            }
        );
    }

    private void crearCuentaConPublicaciones(String username, String nombre, String genero,
            String password, int edad, String tipoCuenta, String[] contenidos) {
        if (GestorArchivos.usuarioExiste(username)) return;

        GestorUsuarios.registrar(username, nombre, genero, password, edad, tipoCuenta, null);

        for (String contenido : contenidos) {
            Publicacion p = new Publicacion(username, contenido, extraerHashtags(contenido),
                "", null, TipoMultimedia.NINGUNO, "Cuadrada");
            p.guardar();
        }
    }

    private String extraerHashtags(String texto) {
        StringBuilder tags = new StringBuilder();
        for (String palabra : texto.split(" ")) {
            if (palabra.startsWith("#")) {
                if (tags.length() > 0) tags.append(" ");
                tags.append(palabra);
            }
        }
        return tags.toString();
    }

    private void ir(String pantalla) {
        cardLayout.show(pnlCards, pantalla);
    }

    private JPanel construirPantallaLogin() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        int cartaAncho = 350;
        int cartaAlto  = 390;
        int cartaX     = (W - cartaAncho) / 2;
        int cartaY     = (H - cartaAlto) / 2 - 50;

        JPanel carta = new JPanel(null) {
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
        carta.setOpaque(false);
        carta.setBounds(cartaX, cartaY, cartaAncho, cartaAlto);
        panel.add(carta);

        JLabel lblLogo = new JLabel("Instagram") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                GradientPaint gp = new GradientPaint(0, 0, new Color(88, 81, 219), getWidth(), 0, new Color(247, 119, 55));
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
        carta.add(lblLogo);

        JLabel lblTitulo = new JLabel("Iniciar sesion en Instagram:", SwingConstants.CENTER);
        lblTitulo.setForeground(C_GRIS);
        lblTitulo.setBounds(20, 88, 310, 20);
        carta.add(lblTitulo);

        txtUsuario = new JTextField("Usuario");
        txtUsuario.setBounds(30, 120, 290, 38);
        txtUsuario.setForeground(C_GRIS);
        txtUsuario.setBackground(C_FONDO_CAMP);
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        txtUsuario.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtUsuario.getText().equals("Usuario")) {
                    txtUsuario.setText("");
                    txtUsuario.setForeground(C_TEXTO);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtUsuario.getText().isEmpty()) {
                    txtUsuario.setText("Usuario");
                    txtUsuario.setForeground(C_GRIS);
                }
            }
        });
        carta.add(txtUsuario);

        txtContra = new JPasswordField("Contraseña");
        txtContra.setBounds(30, 168, 290, 38);
        txtContra.setForeground(C_GRIS);
        txtContra.setBackground(C_FONDO_CAMP);
        txtContra.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtContra.setEchoChar((char) 0);
        txtContra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 36)));
        txtContra.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(txtContra.getPassword()).equals("Contraseña")) {
                    txtContra.setText("");
                    txtContra.setForeground(C_TEXTO);
                    txtContra.setEchoChar('●');
                    passVisible[0] = false;
                    btnOjo.repaint();
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtContra.getPassword().length == 0) {
                    txtContra.setText("Contraseña");
                    txtContra.setForeground(C_GRIS);
                    txtContra.setEchoChar((char) 0);
                    passVisible[0] = false;
                    btnOjo.repaint();
                }
            }
        });
        carta.add(txtContra);

        btnOjo = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                Color c = getModel().isRollover() ? C_TEXTO : C_GRIS;
                g2.setColor(c);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(cx - 10, cy - 6, 20, 14, 0, 180);
                g2.drawArc(cx - 10, cy - 8, 20, 14, 0, -180);
                g2.fillOval(cx - 3, cy - 3, 6, 6);
                if (!passVisible[0]) {
                    g2.setColor(C_BLANCO);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx - 8, cy - 7, cx + 8, cy + 7);
                    g2.setColor(c);
                    g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx - 9, cy - 8, cx + 9, cy + 8);
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
        btnOjo.addActionListener(e -> {
            passVisible[0] = !passVisible[0];
            txtContra.setEchoChar(passVisible[0] ? (char) 0 : '●');
            btnOjo.repaint();
        });
        carta.add(btnOjo);
        carta.setComponentZOrder(btnOjo, 0);

        lblExcepcion = new JLabel(" ", SwingConstants.CENTER);
        lblExcepcion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblExcepcion.setForeground(C_ERROR);
        lblExcepcion.setBounds(20, 212, 310, 18);
        carta.add(lblExcepcion);

        btnLogin = new JButton("Iniciar sesion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
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
        btnLogin.setEnabled(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (camposOk()) btnLogin.setBackground(new Color(24, 119, 242));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(camposOk() ? C_AZUL : C_BTN_GRIS);
            }
        });
        btnLogin.addActionListener(e -> accionLogin());
        txtUsuario.addActionListener(e -> accionLogin());
        txtContra.addActionListener(e -> accionLogin());
        carta.add(btnLogin);

        int c2Y = cartaY + cartaAlto + 10;
        JPanel cardRegistro = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_BLANCO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(C_BORDE);
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        cardRegistro.setOpaque(false);
        cardRegistro.setBounds(cartaX, c2Y, cartaAncho, 52);
        panel.add(cardRegistro);

        JLabel lblNoTienes = new JLabel("No tienes una cuenta?");
        lblNoTienes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblNoTienes.setForeground(C_TEXTO);
        lblNoTienes.setBounds(38, 14, 185, 24);
        cardRegistro.add(lblNoTienes);

        JLabel lblRegistrate = new JLabel("Registrate");
        lblRegistrate.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRegistrate.setForeground(C_AZUL);
        lblRegistrate.setBounds(228, 14, 90, 24);
        lblRegistrate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegistrate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ir("registro");
            }
        });
        cardRegistro.add(lblRegistrate);

        javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
            void actualizar() {
                boolean ok = camposOk();
                btnLogin.setBackground(ok ? C_AZUL : C_BTN_GRIS);
                btnLogin.setEnabled(ok);
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { actualizar(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { actualizar(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
        };
        txtUsuario.getDocument().addDocumentListener(dl);
        txtContra.getDocument().addDocumentListener(dl);

        return panel;
    }

    private boolean camposOk() {
        String u = txtUsuario.getText();
        String p = String.valueOf(txtContra.getPassword());
        return !u.isEmpty() && !u.equals("Usuario") && !p.isEmpty() && !p.equals("Contraseña");
    }

    public void accionLogin() {
        String usuario = txtUsuario.getText().trim();
        String contra  = String.valueOf(txtContra.getPassword());

        if (usuario.isEmpty() || usuario.equals("Usuario")
                || contra.isEmpty() || contra.equals("Contraseña")) {
            lblExcepcion.setText("Completa todos los campos.");
            return;
        }

        Usuario u = GestorUsuarios.login(usuario, contra);

        if (u == null) {
            lblExcepcion.setText("Usuario o contrasena incorrectos / cuenta inactiva.");
            txtContra.setText("Contraseña");
            txtContra.setForeground(C_GRIS);
            txtContra.setEchoChar((char) 0);
            passVisible[0] = false;
            btnOjo.repaint();
            return;
        }

        usuarioActual = usuario;
        lblExcepcion.setText(" ");
        txtUsuario.setText("Usuario");
        txtUsuario.setForeground(C_GRIS);
        txtContra.setText("Contraseña");
        txtContra.setForeground(C_GRIS);
        txtContra.setEchoChar((char) 0);
        passVisible[0] = false;
        btnOjo.repaint();

        Gui_Navegador nav = new Gui_Navegador(cardLayout, pnlCards);
        nav.setContexto(ventana, usuarioActual);
        Gui_Home   guiHome   = new Gui_Home(ventana, nav, usuarioActual);
        Gui_Buscar guiBuscar = new Gui_Buscar(ventana, nav, usuarioActual);
        Gui_Chats  guiChats  = new Gui_Chats(ventana, nav, usuarioActual);
        Gui_Crear  guiCrear  = new Gui_Crear(ventana, nav, usuarioActual);
        Gui_Perfil guiPerfil = new Gui_Perfil(ventana, nav, usuarioActual, usuarioActual);

        pnlCards.add(guiHome.construirPantalla(),   "home");
        pnlCards.add(guiBuscar.construirPantalla(), "buscar");
        pnlCards.add(guiChats.construirPantalla(),  "chats");
        pnlCards.add(guiCrear.construirPantalla(),  "crear");
        pnlCards.add(guiPerfil.construirPantalla(), "perfil");

        ir("home");
    }
}