/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import Logica.*;
import PEnums.Enums.TipoMultimedia;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author Rogelio
 */
public class Gui_Crear {

    private static final Color C_BORDE      = new Color(219, 219, 219);
    private static final Color C_FONDO      = new Color(250, 250, 250);
    private static final Color C_TEXTO      = new Color(38,  38,  38);
    private static final Color C_GRIS       = new Color(142, 142, 142);
    private static final Color C_AZUL       = new Color(0,   149, 246);
    private static final Color C_BLANCO     = Color.WHITE;
    private static final Color C_HOVER      = new Color(245, 245, 245);
    private static final Color C_FONDO_CAMP = new Color(239, 239, 239);

    private static final int W         = 1366;
    private static final int H         = 768;
    private static final int TOPBAR_H  = 54;
    private static final int SIDEBAR_W = 244;

    private final JFrame ventana;
    private final Gui_Navegador nav;
    private final String usuarioActual;

    private String   propSeleccionada = "Cuadrada";
    private String   rutaImagen       = null;
    private JLabel   lblVistaImagen;
    private JLabel   lblNombreArchivo;
    private JTextArea txtContenido;
    private JTextField txtHashtags;
    private JTextField txtMenciones;
    private JLabel   lblContador;

    private JButton btnCuadrada;
    private JButton btnVertical;
    private JButton btnHorizontal;

    public Gui_Crear(JFrame ventana, Gui_Navegador nav, String usuarioActual) {
        this.ventana       = ventana;
        this.nav           = nav;
        this.usuarioActual = usuarioActual;
    }

    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(C_FONDO);
        panel.setBounds(0, 0, W, H);
        panel.add(construirTopBar());
        panel.add(construirSidebar());
        panel.add(construirAreaCrear());
        return panel;
    }

    private JPanel construirTopBar() {
        JPanel bar = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(0, TOPBAR_H - 1, W, TOPBAR_H - 1);
            }
        };
        bar.setBackground(C_BLANCO);
        bar.setBounds(0, 0, W, TOPBAR_H);

        JLabel lblLogo = new JLabel("Instagram") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                GradientPaint gp = new GradientPaint(0, 0, new Color(88, 81, 219), getWidth(), 0, new Color(247, 119, 55));
                g2.setPaint(gp);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 0, fm.getAscent());
                g2.dispose();
            }
        };
        lblLogo.setFont(new Font("Segoe Script", Font.PLAIN, 26));
        lblLogo.setBounds(16, 10, 160, 36);
        bar.add(lblLogo);
        return bar;
    }

    private JPanel construirSidebar() {
        JPanel side = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(SIDEBAR_W - 1, 0, SIDEBAR_W - 1, H);
            }
        };
        side.setBackground(C_BLANCO);
        side.setBounds(0, TOPBAR_H, SIDEBAR_W, H - TOPBAR_H);

        Usuario _uSide = Usuario.cargarDesdeArchivo(usuarioActual);
        String _rutaSide = _uSide != null ? _uSide.getRutaFoto() : null;
        JPanel fotoPerfil = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (_rutaSide != null && !_rutaSide.isEmpty()) {
                    try {
                        BufferedImage img = javax.imageio.ImageIO.read(new File(_rutaSide).getAbsoluteFile());
                        if (img != null) {
                            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 56, 56));
                            g2.drawImage(img, 0, 0, 56, 56, this);
                        }
                    } catch (Exception ex) {
                        g2.setColor(colorDeUsuario(usuarioActual));
                        g2.fillOval(0, 0, 56, 56);
                    }
                } else {
                    g2.setColor(colorDeUsuario(usuarioActual));
                    g2.fillOval(0, 0, 56, 56);
                }
                g2.dispose();
            }
        };
        fotoPerfil.setOpaque(false);
        fotoPerfil.setBounds(16, 20, 56, 56);
        side.add(fotoPerfil);

        JLabel lblNombre = new JLabel(usuarioActual);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(C_TEXTO);
        lblNombre.setBounds(84, 26, 144, 18);
        side.add(lblNombre);

        JLabel lblVerPerfil = new JLabel("Ver perfil");
        lblVerPerfil.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblVerPerfil.setForeground(new Color(0, 149, 246));
        lblVerPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblVerPerfil.setBounds(84, 44, 80, 16);
        lblVerPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { nav.ir("perfil"); }
        });
        side.add(lblVerPerfil);


        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDE);
        sep.setBounds(16, 90, SIDEBAR_W - 32, 1);
        side.add(sep);

        String[] opNombres = { "Inicio", "Buscar", "Crear", "Chats", "Perfil" };
        int[]    opY       = { 110, 162, 214, 266, 318 };

        for (int i = 0; i < opNombres.length; i++) {
            final int     idx    = i;
            final boolean activo = (idx == 2);

            JButton btnNav = new JButton() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (activo)                       { g2.setColor(new Color(240,240,255)); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); }
                    else if (getModel().isRollover()) { g2.setColor(C_HOVER);               g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); }
                    int cx = 28, cy = getHeight() / 2;
                    g2.setColor(activo ? new Color(88,81,219) : C_TEXTO);
                    g2.setStroke(new BasicStroke(activo ? 2.2f : 1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (idx) {
                        case 0:
                            int[] hx={cx,cx+10,cx+10,cx-10,cx-10}, hy={cy-10,cy,cy+10,cy+10,cy};
                            g2.drawPolygon(hx,hy,5); g2.drawRect(cx-4,cy+1,8,9); break;
                        case 1:
                            g2.drawOval(cx-9,cy-10,18,18); g2.drawLine(cx+7,cy+6,cx+12,cy+11); break;
                        case 2:
                            g2.drawRoundRect(cx-11,cy-11,22,22,6,6);
                            g2.drawLine(cx,cy-6,cx,cy+6); g2.drawLine(cx-6,cy,cx+6,cy); break;
                        case 3:
                            g2.drawRoundRect(cx-11, cy-9, 22, 17, 5, 5);
                            g2.drawLine(cx-5, cy+8, cx-8, cy+12);
                            // Badge de mensajes no leidos
                            int noLeidos = contarMensajesNoLeidos();
                            if (noLeidos > 0) {
                                String badge = noLeidos > 4 ? "4+" : String.valueOf(noLeidos);
                                g2.setColor(new Color(237, 73, 86));
                                g2.fillOval(cx + 4, cy - 14, 16, 16);
                                g2.setColor(Color.WHITE);
                                g2.setFont(new Font("SansSerif", Font.BOLD, 8));
                                FontMetrics fmb = g2.getFontMetrics();
                                g2.drawString(badge, cx + 4 + (16 - fmb.stringWidth(badge))/2, cy - 14 + 11);
                            }
                            break;
                        case 4:
                            g2.drawOval(cx-10,cy-10,20,20); g2.fillOval(cx-4,cy-6,8,8);
                            g2.drawArc(cx-8,cy+1,16,12,0,180); break;
                    }
                    g2.setFont(new Font("SansSerif", activo ? Font.BOLD : Font.PLAIN, 14));
                    g2.setColor(activo ? new Color(88,81,219) : C_TEXTO);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(opNombres[idx], 52, cy + fm.getAscent()/2 - 1);
                    g2.dispose();
                }
            };
            btnNav.setBounds(8, opY[i], SIDEBAR_W - 16, 44);
            btnNav.setOpaque(false); btnNav.setContentAreaFilled(false);
            btnNav.setBorderPainted(false); btnNav.setFocusPainted(false);
            btnNav.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (idx == 0) btnNav.addActionListener(e -> nav.ir("home"));
            if (idx == 1) btnNav.addActionListener(e -> nav.ir("buscar"));
            if (idx == 3) btnNav.addActionListener(e -> nav.ir("chats"));
            if (idx == 4) btnNav.addActionListener(e -> nav.ir("perfil"));
            side.add(btnNav);
        }

        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(C_GRIS);
        lblFooter.setBounds(16, H - TOPBAR_H - 28, 220, 14);
        side.add(lblFooter);

        return side;
    }
    private int contarMensajesNoLeidos() {
        int total = 0;
        for (String otro : Conversacion.getConversaciones(usuarioActual)) {
            total += new Conversacion(usuarioActual, otro).getMensajesNoLeidos();
        }
        return total;
    }

    private JPanel construirAreaCrear() {
        int areaX = SIDEBAR_W;
        int areaW = W - SIDEBAR_W;
        int areaH = H - TOPBAR_H;

        JPanel area = new JPanel(null);
        area.setBackground(C_FONDO);
        area.setBounds(areaX, TOPBAR_H, areaW, areaH);

        JLabel lblTitulo = new JLabel("Crear nueva publicacion");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(C_TEXTO);
        lblTitulo.setBounds(30, 20, 320, 26);
        area.add(lblTitulo);

        JSeparator sepTop = new JSeparator();
        sepTop.setForeground(C_BORDE);
        sepTop.setBounds(0, 54, areaW, 1);
        area.add(sepTop);

        int col1X = 30;
        int col2X = 430;
        int y = 70;

        JLabel lblImagen = new JLabel("Imagen");
        lblImagen.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblImagen.setForeground(C_TEXTO);
        lblImagen.setBounds(col1X, y, 100, 18);
        area.add(lblImagen);
        y += 26;

        lblVistaImagen = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_FONDO_CAMP);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                if (getIcon() == null) {
                    int cx = getWidth()/2, cy = getHeight()/2;
                    g2.setColor(C_GRIS);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawRoundRect(cx-24, cy-18, 48, 34, 8, 8);
                    g2.drawOval(cx-10, cy-10, 20, 20);
                    g2.fillRoundRect(cx-5, cy-22, 10, 6, 3, 3);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    FontMetrics fm = g2.getFontMetrics();
                    String txt = "Sin imagen seleccionada";
                    g2.drawString(txt, cx - fm.stringWidth(txt)/2, cy + 30);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblVistaImagen.setBounds(col1X, y, 360, 220);
        lblVistaImagen.setHorizontalAlignment(SwingConstants.CENTER);
        area.add(lblVistaImagen);
        y += 230;

        lblNombreArchivo = new JLabel("Ningun archivo seleccionado");
        lblNombreArchivo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblNombreArchivo.setForeground(C_GRIS);
        lblNombreArchivo.setBounds(col1X, y, 260, 18);
        area.add(lblNombreArchivo);

        JButton btnElegirImg = crearBotonSecundario("Elegir imagen");
        btnElegirImg.setBounds(col1X + 265, y - 2, 95, 24);
        btnElegirImg.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(System.getProperty("user.home") + "/Downloads");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagenes", "jpg", "jpeg", "png", "gif", "bmp", "webp"));
            fc.setDialogTitle("Selecciona una imagen");
            if (fc.showOpenDialog(ventana) == JFileChooser.APPROVE_OPTION) {
                rutaImagen = fc.getSelectedFile().getAbsolutePath();
                String nombre = fc.getSelectedFile().getName();
                lblNombreArchivo.setText(nombre.length() > 28 ? nombre.substring(0,28)+"..." : nombre);
                lblNombreArchivo.setForeground(C_TEXTO);
                try {
                    ImageIcon icon = new ImageIcon(rutaImagen);
                    Image scaled = icon.getImage().getScaledInstance(360, 220, Image.SCALE_SMOOTH);
                    lblVistaImagen.setIcon(new ImageIcon(scaled));
                } catch (Exception ex) { }
                lblVistaImagen.repaint();
            }
        });
        area.add(btnElegirImg);
        y += 28;

        JLabel lblProp = new JLabel("Proporcion");
        lblProp.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblProp.setForeground(C_TEXTO);
        lblProp.setBounds(col1X, y, 100, 18);
        area.add(lblProp);
        y += 24;

        btnCuadrada   = crearBtnProp("Cuadrada\n600x600");
        btnVertical   = crearBtnProp("Vertical\n600x750");
        btnHorizontal = crearBtnProp("Horizontal\n600x400");

        btnCuadrada.setBounds(col1X,       y, 110, 44);
        btnVertical.setBounds(col1X + 120, y, 110, 44);
        btnHorizontal.setBounds(col1X + 240, y, 120, 44);

        seleccionarProp("Cuadrada");

        btnCuadrada.addActionListener(e   -> seleccionarProp("Cuadrada"));
        btnVertical.addActionListener(e   -> seleccionarProp("Vertical"));
        btnHorizontal.addActionListener(e -> seleccionarProp("Horizontal"));

        area.add(btnCuadrada);
        area.add(btnVertical);
        area.add(btnHorizontal);

        int y2 = 70;

        JLabel lblContenido = new JLabel("Contenido (max. 220 caracteres)");
        lblContenido.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblContenido.setForeground(C_TEXTO);
        lblContenido.setBounds(col2X, y2, 300, 18);
        area.add(lblContenido);
        y2 += 26;

        txtContenido = new JTextArea();
        txtContenido.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtContenido.setForeground(C_TEXTO);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        txtContenido.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtContenido.setBackground(C_BLANCO);

        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        scrollContenido.setBorder(BorderFactory.createLineBorder(C_BORDE, 1));
        scrollContenido.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scrollContenido.setBounds(col2X, y2, 460, 110);
        area.add(scrollContenido);
        y2 += 118;

        lblContador = new JLabel("0 / 220");
        lblContador.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblContador.setForeground(C_GRIS);
        lblContador.setHorizontalAlignment(SwingConstants.RIGHT);
        lblContador.setBounds(col2X, y2, 460, 16);
        area.add(lblContador);

        txtContenido.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            void act() {
                try {
                    int len = txtContenido.getText().length();
                    if (len > 220) {
                        String recortado = txtContenido.getText().substring(0, 220);
                        SwingUtilities.invokeLater(() -> {
                            try {
                                txtContenido.setText(recortado);
                                txtContenido.setCaretPosition(220);
                            } catch (Exception ex) { }
                        });
                        len = 220;
                    }
                    final int largo = len;
                    SwingUtilities.invokeLater(() -> {
                        lblContador.setText(largo + " / 220");
                        lblContador.setForeground(largo >= 200 ? new Color(237, 73, 86) : C_GRIS);
                    });
                } catch (Exception ex) { }
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { act(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { act(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { act(); }
        });
        y2 += 22;

        JLabel lblHash = new JLabel("Hashtags");
        lblHash.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblHash.setForeground(C_TEXTO);
        lblHash.setBounds(col2X, y2, 200, 18);
        area.add(lblHash);
        y2 += 24;

        txtHashtags = crearCampo("#foto #nature #travel", col2X, y2, 460, 36);
        area.add(txtHashtags);
        y2 += 46;

        JLabel lblMenc = new JLabel("Menciones");
        lblMenc.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblMenc.setForeground(C_TEXTO);
        lblMenc.setBounds(col2X, y2, 200, 18);
        area.add(lblMenc);
        y2 += 24;

        txtMenciones = crearCampo("@usuario1 @usuario2", col2X, y2, 460, 36);
        area.add(txtMenciones);
        y2 += 50;

        JLabel lblInfoFecha = new JLabel("Fecha y hora de publicacion: automatica al publicar");
        lblInfoFecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblInfoFecha.setForeground(C_GRIS);
        lblInfoFecha.setBounds(col2X, y2, 460, 16);
        area.add(lblInfoFecha);
        y2 += 30;

        JLabel lblAutor = new JLabel("Autor: @" + usuarioActual);
        lblAutor.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblAutor.setForeground(C_GRIS);
        lblAutor.setBounds(col2X, y2, 300, 16);
        area.add(lblAutor);
        y2 += 36;

        JButton btnCancelar = crearBotonSecundario("Cancelar");
        btnCancelar.setBounds(col2X, y2, 110, 38);
        btnCancelar.addActionListener(e -> nav.ir("home"));
        area.add(btnCancelar);

        JButton btnPublicar = crearBotonPrincipal("Publicar");
        btnPublicar.setBounds(col2X + 350, y2, 110, 38);
        btnPublicar.addActionListener(e -> publicar());
        area.add(btnPublicar);

        return area;
    }

    private void publicar() {
        String contenido = txtContenido.getText().trim();

        if (contenido.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Escribe algo antes de publicar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hashtags  = txtHashtags.getText().trim();
        String menciones = txtMenciones.getText().trim();

        if (hashtags.equals("#foto #nature #travel"))  hashtags  = "";
        if (menciones.equals("@usuario1 @usuario2"))   menciones = "";

        TipoMultimedia tipoMultimedia = rutaImagen != null ? TipoMultimedia.IMAGEN : TipoMultimedia.NINGUNO;

        Publicacion p = new Publicacion(usuarioActual, contenido, hashtags, menciones, rutaImagen, tipoMultimedia, propSeleccionada);
        p.guardar();

        JOptionPane.showMessageDialog(ventana, "Publicacion creada!", "Listo", JOptionPane.INFORMATION_MESSAGE);
        limpiarFormulario();
        nav.ir("home");
    }

    private void limpiarFormulario() {
        txtContenido.setText("");
        txtHashtags.setText("#foto #nature #travel");
        txtHashtags.setForeground(C_GRIS);
        txtMenciones.setText("@usuario1 @usuario2");
        txtMenciones.setForeground(C_GRIS);
        lblNombreArchivo.setText("Ningun archivo seleccionado");
        lblNombreArchivo.setForeground(C_GRIS);
        lblVistaImagen.setIcon(null);
        lblVistaImagen.repaint();
        rutaImagen = null;
        lblContador.setText("0 / 220");
        lblContador.setForeground(C_GRIS);
        seleccionarProp("Cuadrada");
    }

    private void seleccionarProp(String prop) {
        propSeleccionada = prop;
        Color fondoActivo = new Color(88, 81, 219);
        Color fondoInact  = C_FONDO_CAMP;
        btnCuadrada.setBackground(prop.equals("Cuadrada")    ? fondoActivo : fondoInact);
        btnCuadrada.setForeground(prop.equals("Cuadrada")    ? C_BLANCO    : C_TEXTO);
        btnVertical.setBackground(prop.equals("Vertical")    ? fondoActivo : fondoInact);
        btnVertical.setForeground(prop.equals("Vertical")    ? C_BLANCO    : C_TEXTO);
        btnHorizontal.setBackground(prop.equals("Horizontal") ? fondoActivo : fondoInact);
        btnHorizontal.setForeground(prop.equals("Horizontal") ? C_BLANCO    : C_TEXTO);
        btnCuadrada.repaint(); btnVertical.repaint(); btnHorizontal.repaint();
    }

    private JTextField crearCampo(String placeholder, int x, int y, int w, int h) {
        JTextField tf = new JTextField(placeholder);
        tf.setBounds(x, y, w, h);
        tf.setForeground(C_GRIS);
        tf.setBackground(C_BLANCO);
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

    private JButton crearBtnProp(String texto) {
        String[] partes = texto.split("\n");
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(getForeground());
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(partes[0], (getWidth()-fm.stringWidth(partes[0]))/2, getHeight()/2 - 2);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                fm = g2.getFontMetrics();
                g2.drawString(partes[1], (getWidth()-fm.stringWidth(partes[1]))/2, getHeight()/2 + 12);
                g2.dispose();
            }
        };
        btn.setBackground(C_FONDO_CAMP);
        btn.setForeground(C_TEXTO);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonPrincipal(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(88, 81, 219));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BLANCO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(C_TEXTO);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private Color colorDeUsuario(String username) { return new Color(180, 180, 180); }

    private JPanel crearAvatar(int size, Color color) {
        JPanel p = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, size, size);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(size, size); }
        };
        p.setOpaque(false);
        return p;
    }
}