/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.event.*;
import Logica.*;

/**
 *
 * @author Rogelio
 */
public class Gui_Registro {

    private static final Color BORDE     = new Color(219, 219, 219);
    private static final Color FONDO     = new Color(250, 250, 250);
    private static final Color TEXTO     = new Color(38, 38, 38);
    private static final Color GRIS      = new Color(142, 142, 142);
    private static final Color AZUL      = new Color(0, 149, 246);
    private static final Color AZUL_GRIS = new Color(147, 204, 247);
    private static final Color ROJO      = new Color(237, 73, 86);
    private static final Color BLANCO    = Color.WHITE;
    private static final Color VERDE     = new Color(39, 174, 96);

    private final JFrame ventana;
    private final CardLayout cardLayout;
    private final JPanel pnlCards;

    private JTextField txtNombre;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtEdad;
    private JLabel lblError;
    private JButton btnOjo;
    private boolean[] mostrarPass = {false};

    private String genero = null;
    private String tipoCuenta = "Publica";
    private String rutaFoto = null;

    public Gui_Registro(JFrame ventana, CardLayout cardLayout, JPanel pnlCards) {
        this.ventana = ventana;
        this.cardLayout = cardLayout;
        this.pnlCards = pnlCards;
    }

    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);

        int ancho = 370;
        int alto = 600;
        int x = (1366 - ancho) / 2;
        int y = (768 - alto) / 2 - 10;

        JPanel carta = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
                g2.dispose();
            }
        };
        carta.setOpaque(false);
        carta.setBounds(x, y, ancho, alto);
        panel.add(carta);

        JLabel logo = new JLabel("Instagram") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                GradientPaint degradado = new GradientPaint(0, 0, new Color(88, 81, 219), getWidth(), 0, new Color(247, 119, 55));
                g2.setPaint(degradado);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, fm.getAscent());
                g2.dispose();
            }
        };
        logo.setFont(new Font("Segoe Script", Font.PLAIN, 34));
        logo.setBounds(20, 18, 330, 50);
        carta.add(logo);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        sep.setBounds(30, 72, 310, 1);
        carta.add(sep);

        int campoAncho = 310;
        int campoAlto = 36;
        int campoX = 30;
        int espacio = 10;
        int posY = 82;

        txtNombre = crearCampo("Nombre completo", campoX, posY, campoAncho, campoAlto);
        carta.add(txtNombre);
        posY += campoAlto + espacio;

        JLabel lblGenero = new JLabel("Genero:");
        lblGenero.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenero.setForeground(GRIS);
        lblGenero.setBounds(campoX, posY, 60, campoAlto);
        carta.add(lblGenero);

        JButton btnM = crearBtnToggle("M");
        JButton btnF = crearBtnToggle("F");
        btnM.setBounds(campoX + 65, posY, 60, campoAlto);
        btnF.setBounds(campoX + 135, posY, 60, campoAlto);
        carta.add(btnM);
        carta.add(btnF);

        btnM.addActionListener(e -> {
            genero = "M";
            btnM.setBackground(AZUL);
            btnM.setForeground(BLANCO);
            btnF.setBackground(FONDO);
            btnF.setForeground(TEXTO);
        });

        btnF.addActionListener(e -> {
            genero = "F";
            btnF.setBackground(AZUL);
            btnF.setForeground(BLANCO);
            btnM.setBackground(FONDO);
            btnM.setForeground(TEXTO);
        });
        posY += campoAlto + espacio;

        txtUsername = crearCampo("Username", campoX, posY, campoAncho, campoAlto);
        carta.add(txtUsername);
        posY += campoAlto + espacio;

        txtPassword = crearCampoPassword("Password", campoX, posY, campoAncho, campoAlto);
        carta.add(txtPassword);
        btnOjo = crearBtnOjo(campoX + campoAncho - 36, posY, 36, campoAlto);
        carta.add(btnOjo);
        carta.setComponentZOrder(btnOjo, 0);
        posY += campoAlto + espacio;

        txtEdad = crearCampo("Edad", campoX, posY, campoAncho, campoAlto);
        carta.add(txtEdad);
        posY += campoAlto + espacio;

        JLabel lblFechaTexto = new JLabel("Fecha de registro:");
        lblFechaTexto.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFechaTexto.setForeground(GRIS);
        lblFechaTexto.setBounds(campoX, posY, 130, campoAlto);
        carta.add(lblFechaTexto);

        JLabel lblFechaValor = new JLabel(LocalDate.now().toString());
        lblFechaValor.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblFechaValor.setForeground(TEXTO);
        lblFechaValor.setBounds(campoX + 135, posY, 175, campoAlto);
        carta.add(lblFechaValor);
        posY += campoAlto + espacio;

        JLabel lblFotoTexto = new JLabel("Foto de perfil:");
        lblFotoTexto.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFotoTexto.setForeground(GRIS);
        lblFotoTexto.setBounds(campoX, posY, 110, campoAlto);
        carta.add(lblFotoTexto);

        JLabel lblNombreFoto = new JLabel("Sin imagen");
        lblNombreFoto.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblNombreFoto.setForeground(GRIS);
        lblNombreFoto.setBounds(campoX + 120, posY, 140, campoAlto);
        carta.add(lblNombreFoto);

        JButton btnElegirFoto = crearBotonSecundario("Elegir");
        btnElegirFoto.setBounds(campoX + 265, posY, 45, campoAlto);
        btnElegirFoto.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
                selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagenes", "jpg", "jpeg", "png", "gif", "bmp", "webp"));
            selector.setDialogTitle("Selecciona tu foto de perfil");
            if (selector.showOpenDialog(ventana) == JFileChooser.APPROVE_OPTION) {
                rutaFoto = selector.getSelectedFile().getAbsolutePath();
                String nombre = selector.getSelectedFile().getName();
                lblNombreFoto.setText(nombre.length() > 14 ? nombre.substring(0, 14) + "..." : nombre);
                lblNombreFoto.setForeground(TEXTO);
            }
        });
        carta.add(btnElegirFoto);
        posY += campoAlto + espacio;

        JLabel lblTipoCuenta = new JLabel("Tipo de cuenta:");
        lblTipoCuenta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTipoCuenta.setForeground(GRIS);
        lblTipoCuenta.setBounds(campoX, posY, 120, campoAlto);
        carta.add(lblTipoCuenta);

        JButton btnPublica = crearBtnToggle("Publica");
        JButton btnPrivada = crearBtnToggle("Privada");
        btnPublica.setBackground(AZUL);
        btnPublica.setForeground(BLANCO);
        btnPublica.setBounds(campoX + 125, posY, 80, campoAlto);
        btnPrivada.setBounds(campoX + 215, posY, 80, campoAlto);
        carta.add(btnPublica);
        carta.add(btnPrivada);

        btnPublica.addActionListener(e -> {
            tipoCuenta = "Publica";
            btnPublica.setBackground(AZUL);
            btnPublica.setForeground(BLANCO);
            btnPrivada.setBackground(FONDO);
            btnPrivada.setForeground(TEXTO);
        });

        btnPrivada.addActionListener(e -> {
            tipoCuenta = "Privada";
            btnPrivada.setBackground(AZUL);
            btnPrivada.setForeground(BLANCO);
            btnPublica.setBackground(FONDO);
            btnPublica.setForeground(TEXTO);
        });
        posY += campoAlto + espacio;

        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblError.setForeground(ROJO);
        lblError.setBounds(20, posY, 330, 16);
        carta.add(lblError);
        posY += 20;

        JButton btnRegistrar = crearBoton("Registrarse");
        btnRegistrar.setBounds(campoX, posY, campoAncho, 38);
        btnRegistrar.setBackground(AZUL_GRIS);
        btnRegistrar.setEnabled(false);
        carta.add(btnRegistrar);

        DocumentListener escucharCambios = new DocumentListener() {
            void revisar() {
                boolean lleno = camposLlenos();
                btnRegistrar.setBackground(lleno ? AZUL : AZUL_GRIS);
                btnRegistrar.setEnabled(lleno);
            }
            @Override public void insertUpdate(DocumentEvent e) { revisar(); }
            @Override public void removeUpdate(DocumentEvent e) { revisar(); }
            @Override public void changedUpdate(DocumentEvent e) { revisar(); }
        };

        txtNombre.getDocument().addDocumentListener(escucharCambios);
        txtUsername.getDocument().addDocumentListener(escucharCambios);
        txtPassword.getDocument().addDocumentListener(escucharCambios);
        txtEdad.getDocument().addDocumentListener(escucharCambios);

        btnRegistrar.addActionListener(e -> registrar(btnRegistrar, btnM, btnF, btnPublica, btnPrivada));

        int yCardVolver = y + alto + 10;
        JPanel cardVolver = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BLANCO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BORDE);
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        cardVolver.setOpaque(false);
        cardVolver.setBounds(x, yCardVolver, ancho, 46);
        panel.add(cardVolver);

        JLabel lblPregunta = new JLabel("Ya tienes una cuenta?");
        lblPregunta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblPregunta.setForeground(TEXTO);
        lblPregunta.setBounds(28, 12, 175, 22);
        cardVolver.add(lblPregunta);

        JLabel lblIniciarSesion = new JLabel("Inicia sesion");
        lblIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblIniciarSesion.setForeground(AZUL);
        lblIniciarSesion.setBounds(210, 12, 120, 22);
        lblIniciarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblIniciarSesion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                limpiar(btnRegistrar, btnM, btnF, btnPublica, btnPrivada);
                cardLayout.show(pnlCards, "login");
            }
        });
        cardVolver.add(lblIniciarSesion);

        return panel;
    }

    private void registrar(JButton btnReg, JButton btnM, JButton btnF, JButton btnPub, JButton btnPriv) {
        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String pass = String.valueOf(txtPassword.getPassword());
        String edadTexto = txtEdad.getText().trim();

        if (nombre.equals("Nombre completo") || username.equals("Username")
                || pass.equals("Password") || edadTexto.equals("Edad")) {
            error("Completa todos los campos.");
            return;
        }

        if (genero == null) {
            error("Selecciona un genero.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadTexto);
            if (edad < 1 || edad > 120) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            error("Ingresa una edad valida.");
            return;
        }

        if (pass.length() < 6) {
            error("La password debe tener al menos 6 caracteres.");
            return;
        }

        GestorArchivos.inicializar();

        if (GestorArchivos.usuarioExiste(username)) {
            error("Ese username ya esta en uso.");
            return;
        }

        String fotoFinal = copiarFoto(username);

        Usuario nuevo = new Usuario(username, nombre, genero, pass, edad, tipoCuenta, fotoFinal);
        nuevo.guardar();
        GestorArchivos.registrarUsername(username);

        lblError.setForeground(VERDE);
        lblError.setText("Cuenta creada exitosamente!");

        Timer esperar = new Timer(1200, ev -> {
            lblError.setForeground(ROJO);
            limpiar(btnReg, btnM, btnF, btnPub, btnPriv);
            cardLayout.show(pnlCards, "login");
        });
        esperar.setRepeats(false);
        esperar.start();
    }

    private String copiarFoto(String username) {
        if (rutaFoto == null) return null;
        try {
            String extension = rutaFoto.substring(rutaFoto.lastIndexOf('.'));
            String destino = GestorArchivos.RAIZ + username.toLowerCase() + "/imagenes/perfil" + extension;
            GestorArchivos.crearEstructuraUsuario(username);
            Files.copy(Paths.get(rutaFoto), Paths.get(destino), StandardCopyOption.REPLACE_EXISTING);
            return destino;
        } catch (IOException ex) {
            return null;
        }
    }

    private void error(String mensaje) {
        lblError.setForeground(ROJO);
        lblError.setText(mensaje);
    }

    private boolean camposLlenos() {
        return !txtNombre.getText().equals("Nombre completo") && !txtNombre.getText().isEmpty()
            && !txtUsername.getText().equals("Username") && !txtUsername.getText().isEmpty()
            && !String.valueOf(txtPassword.getPassword()).equals("Password") && txtPassword.getPassword().length > 0
            && !txtEdad.getText().equals("Edad") && !txtEdad.getText().isEmpty();
    }

    private void limpiar(JButton btnReg, JButton btnM, JButton btnF, JButton btnPub, JButton btnPriv) {
        resetCampo(txtNombre, "Nombre completo");
        resetCampo(txtUsername, "Username");
        resetPassword(txtPassword, "Password");
        resetCampo(txtEdad, "Edad");
        genero = null;
        tipoCuenta = "Publica";
        rutaFoto = null;
        btnM.setBackground(FONDO);
        btnM.setForeground(TEXTO);
        btnF.setBackground(FONDO);
        btnF.setForeground(TEXTO);
        btnPub.setBackground(AZUL);
        btnPub.setForeground(BLANCO);
        btnPriv.setBackground(FONDO);
        btnPriv.setForeground(TEXTO);
        btnReg.setBackground(AZUL_GRIS);
        btnReg.setEnabled(false);
        lblError.setText(" ");
    }

    private void resetCampo(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(GRIS);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
    }

    private void resetPassword(JPasswordField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(GRIS);
        campo.setEchoChar((char) 0);
        mostrarPass[0] = false;
        btnOjo.repaint();
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 36)));
    }

    private JTextField crearCampo(String placeholder, int x, int y, int w, int h) {
        JTextField campo = new JTextField(placeholder);
        campo.setBounds(x, y, w, h);
        campo.setForeground(GRIS);
        campo.setBackground(FONDO);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(TEXTO);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(GRIS);
                }
            }
        });
        return campo;
    }

    private JPasswordField crearCampoPassword(String placeholder, int x, int y, int w, int h) {
        JPasswordField campo = new JPasswordField(placeholder);
        campo.setBounds(x, y, w, h);
        campo.setForeground(GRIS);
        campo.setBackground(FONDO);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setEchoChar((char) 0);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 36)));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(campo.getPassword()).equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(TEXTO);
                    campo.setEchoChar('●');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getPassword().length == 0) {
                    campo.setText(placeholder);
                    campo.setForeground(GRIS);
                    campo.setEchoChar((char) 0);
                }
            }
        });
        return campo;
    }

    private JButton crearBtnOjo(int x, int y, int w, int h) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                Color color = getModel().isRollover() ? TEXTO : GRIS;
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(cx - 10, cy - 6, 20, 14, 0, 180);
                g2.drawArc(cx - 10, cy - 8, 20, 14, 0, -180);
                g2.fillOval(cx - 3, cy - 3, 6, 6);
                if (!mostrarPass[0]) {
                    g2.setColor(BLANCO);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(cx - 8, cy - 7, cx + 8, cy + 7);
                    g2.setColor(color);
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
            mostrarPass[0] = !mostrarPass[0];
            txtPassword.setEchoChar(mostrarPass[0] ? (char) 0 : '●');
            btn.repaint();
        });
        return btn;
    }

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
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(BLANCO);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBtnToggle(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(FONDO);
        btn.setForeground(TEXTO);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FONDO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                g2.setColor(TEXTO);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}