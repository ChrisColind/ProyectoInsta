/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import Logica.*;
import Logica.GestorUsuarios;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Set;
import javax.swing.*;

/**
 *
 * @author Rogelio
 */
public class Gui_Home {

    private static final Color BORDE = new Color(219, 219, 219);
    private static final Color FONDO = new Color(250, 250, 250);
    private static final Color TEXTO = new Color(38,  38,  38);
    private static final Color GRIS = new Color(142, 142, 142);
    private static final Color AZUL = new Color(0,   149, 246);
    private static final Color BLANCO = Color.WHITE;
    private static final Color HOVER = new Color(245, 245, 245);
    private static final Color ROJO = new Color(237, 73,  86);

    private static final int W         = 1366;
    private static final int H         = 768;
    private static final int TOPBAR_H  = 54;
    private static final int SIDEBAR_W = 244;
    private static final int RIGHT_W   = 0;
    private static final int FEED_W    = W - SIDEBAR_W;

    private final JFrame        ventana;
    private final Gui_Navegador nav;
    private final String        usuarioActual;

    public Gui_Home(JFrame ventana, Gui_Navegador nav, String usuarioActual) {
        this.ventana       = ventana;
        this.nav           = nav;
        this.usuarioActual = usuarioActual;
    }

    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(FONDO);
        panel.setBounds(0, 0, W, H);
        panel.add(construirTopBar());
        panel.add(construirSidebar());
        panel.add(construirFeed());
        return panel;
    }

    private JPanel construirTopBar() {
        JPanel bar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BORDE);
                g.drawLine(0, TOPBAR_H - 1, W, TOPBAR_H - 1);
            }
        };
        bar.setBackground(BLANCO);
        bar.setBounds(0, 0, W, TOPBAR_H);

        JLabel lblLogo = new JLabel("Instagram") {
            @Override
            protected void paintComponent(Graphics g) {
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
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BORDE);
                g.drawLine(SIDEBAR_W - 1, 0, SIDEBAR_W - 1, H);
            }
        };
        side.setBackground(BLANCO);
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
        lblNombre.setForeground(TEXTO);
        lblNombre.setBounds(84, 26, 144, 18);
        side.add(lblNombre);

        JLabel lblVerPerfil = new JLabel("Ver perfil");
        lblVerPerfil.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblVerPerfil.setForeground(AZUL);
        lblVerPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblVerPerfil.setBounds(84, 44, 80, 16);
        lblVerPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nav.ir("perfil");
            }
        });
        side.add(lblVerPerfil);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        sep.setBounds(16, 90, SIDEBAR_W - 32, 1);
        side.add(sep);

        String[] opNombres = { "Inicio", "Buscar", "Crear", "Chats", "Perfil" };
        int[]    opY       = { 110, 162, 214, 266, 318 };

        for (int i = 0; i < opNombres.length; i++) {
            final int     idx    = i;
            final boolean activo = (idx == 0);

            JButton btnNav = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (activo) {
                        g2.setColor(new Color(240, 240, 255));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    } else if (getModel().isRollover()) {
                        g2.setColor(HOVER);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    }
                    int cx = 28, cy = getHeight() / 2;
                    g2.setColor(activo ? new Color(88, 81, 219) : TEXTO);
                    g2.setStroke(new BasicStroke(activo ? 2.2f : 1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (idx) {
                        case 0:
                            int[] hx = {cx, cx+10, cx+10, cx-10, cx-10};
                            int[] hy = {cy-10, cy, cy+10, cy+10, cy};
                            g2.drawPolygon(hx, hy, 5);
                            g2.drawRect(cx-4, cy+1, 8, 9);
                            break;
                        case 1:
                            g2.drawOval(cx-9, cy-10, 18, 18);
                            g2.drawLine(cx+7, cy+6, cx+12, cy+11);
                            break;
                        case 2:
                            g2.drawRoundRect(cx-11, cy-11, 22, 22, 6, 6);
                            g2.drawLine(cx, cy-6, cx, cy+6);
                            g2.drawLine(cx-6, cy, cx+6, cy);
                            break;
                        case 3:
                            g2.drawRoundRect(cx-11, cy-9, 22, 17, 5, 5);
                            g2.drawLine(cx-5, cy+8, cx-8, cy+12);
                            break;
                        case 4:
                            g2.drawOval(cx-10, cy-10, 20, 20);
                            g2.fillOval(cx-4, cy-6, 8, 8);
                            g2.drawArc(cx-8, cy+1, 16, 12, 0, 180);
                            break;
                    }
                    g2.setFont(new Font("SansSerif", activo ? Font.BOLD : Font.PLAIN, 14));
                    g2.setColor(activo ? new Color(88, 81, 219) : TEXTO);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(opNombres[idx], 52, cy + fm.getAscent() / 2 - 1);
                    g2.dispose();
                }
            };
            btnNav.setBounds(8, opY[i], SIDEBAR_W - 16, 44);
            btnNav.setOpaque(false);
            btnNav.setContentAreaFilled(false);
            btnNav.setBorderPainted(false);
            btnNav.setFocusPainted(false);
            btnNav.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (idx == 1) btnNav.addActionListener(e -> nav.ir("buscar"));
            if (idx == 2) btnNav.addActionListener(e -> nav.ir("crear"));
            if (idx == 3) btnNav.addActionListener(e -> nav.ir("chats"));
            if (idx == 4) btnNav.addActionListener(e -> nav.ir("perfil"));
            side.add(btnNav);
        }

        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(GRIS);
        lblFooter.setBounds(16, H - TOPBAR_H - 28, 220, 14);
        side.add(lblFooter);
        return side;
    }

    private JScrollPane construirFeed() {
        JPanel feed = new JPanel();
        feed.setLayout(new BoxLayout(feed, BoxLayout.Y_AXIS));
        feed.setBackground(FONDO);
        feed.setBorder(BorderFactory.createEmptyBorder(16, 60, 16, 60));

        int postAncho = 500;

        List<Publicacion> publicaciones = new Timeline(usuarioActual).getFeed();

        // siempre se muestran las cuentas predeterminadas sin importar si las sigue
        String[] cuentasPredeter = {"razer_hn", "espnfc_hn", "memes_hn", "titanfit_hn"};
        Set<String> idsEnFeed = new java.util.HashSet<>();
        for (Publicacion p : publicaciones) idsEnFeed.add(p.getId());

        for (String cuenta : cuentasPredeter) {
            if (cuenta.equalsIgnoreCase(usuarioActual)) continue;
            for (Publicacion p : GestorPublicaciones.getPublicacionesDeUsuario(cuenta)) {
                if (idsEnFeed.add(p.getId())) publicaciones.add(p);
            }
        }

        if (publicaciones.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay publicaciones aun.");
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblVacio.setForeground(GRIS);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            feed.add(lblVacio);
        }

        for (Publicacion p : publicaciones) {
            feed.add(crearPost(p, postAncho));
            feed.add(Box.createVerticalStrut(12));
        }

        JScrollPane scroll = new JScrollPane(feed);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scroll.setBounds(SIDEBAR_W, TOPBAR_H, FEED_W, H - TOPBAR_H);
        scroll.setBackground(FONDO);
        scroll.getViewport().setBackground(FONDO);
        return scroll;
    }

    private JPanel crearPost(Publicacion p, int postAncho) {
        String usuario     = p.getAutor();
        String fechaHora   = p.getFecha() + " " + p.getHora();
        String caption     = p.getContenido();
        String proporcion  = p.getProporcion();

        int imgH;
        if ("Vertical".equalsIgnoreCase(proporcion))        imgH = (int)(postAncho * 750.0 / 600);
        else if ("Horizontal".equalsIgnoreCase(proporcion)) imgH = (int)(postAncho * 400.0 / 600);
        else                                                imgH = postAncho;

        int headerH  = 60;
        int accionH  = 44;
        int captionH = 52;
        int totalH   = headerH + imgH + accionH + captionH;

        Color avatarColor = colorDeUsuario(usuario);

        JPanel post = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 4, 4);
                g2.dispose();
            }
        };
        post.setOpaque(false);
        post.setPreferredSize(new Dimension(postAncho, totalH));
        post.setMaximumSize(new Dimension(postAncho, totalH));
        post.setMinimumSize(new Dimension(postAncho, totalH));
        post.setAlignmentX(Component.CENTER_ALIGNMENT);

        Usuario uAutor = Usuario.cargarDesdeArchivo(usuario);
        String rutaAutor = uAutor != null ? uAutor.getRutaFoto() : null;
        JPanel av = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (rutaAutor != null && !rutaAutor.isEmpty()) {
                    try {
                        BufferedImage img = javax.imageio.ImageIO.read(new File(rutaAutor).getAbsoluteFile());
                        if (img != null) {
                            g2.setClip(new Ellipse2D.Float(0, 0, 38, 38));
                            g2.drawImage(img, 0, 0, 38, 38, this);
                            g2.dispose();
                            return;
                        }
                    } catch (Exception ex) { }
                }
                g2.setColor(avatarColor);
                g2.fillOval(0, 0, 38, 38);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setBounds(12, 11, 38, 38);
        post.add(av);

        JLabel lblNombre = new JLabel(usuario);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNombre.setForeground(TEXTO);
        lblNombre.setBounds(58, 12, 200, 17);
        post.add(lblNombre);

        JLabel lblHandle = new JLabel("@" +usuario);
        lblHandle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblHandle.setForeground(GRIS);
        lblHandle.setBounds(58, 30, 180, 14);
        post.add(lblHandle);

        JLabel lblFechaHora = new JLabel(fechaHora);
        lblFechaHora.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFechaHora.setForeground(GRIS);
        lblFechaHora.setBounds(postAncho - 140, 22, 128, 14);
        post.add(lblFechaHora);

        String rutaImg = p.getRutaImagen();
        BufferedImage imagenCargada = null;
        if (rutaImg != null && !rutaImg.isEmpty()) {
            try {
                imagenCargada = javax.imageio.ImageIO.read(new File(rutaImg).getAbsoluteFile());
            } catch (Exception ex) {
                imagenCargada = null;
            }
        }
        final BufferedImage imagenFinal = imagenCargada;

        JPanel imgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (imagenFinal != null) {
                    g.drawImage(imagenFinal, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(avatarColor.darker());
                    g.fillRect(0, 0, getWidth(), getHeight());
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int cx = getWidth() / 2, cy = getHeight() / 2;
                    g2.setColor(new Color(255, 255, 255, 100));
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawRoundRect(cx - 16, cy - 10, 32, 22, 6, 6);
                    g2.drawOval(cx - 7, cy - 7, 14, 14);
                    g2.dispose();
                }
            }
        };
        imgPanel.setBounds(0, headerH, postAncho, imgH);
        post.add(imgPanel);

        int ay = headerH + imgH + 8;
        boolean[] likedState = {p.yaLeGustaA(usuarioActual)};

        JButton btnLike = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                GeneralPath heart = new GeneralPath();
                heart.moveTo(cx, cy+9);
                heart.curveTo(cx-12, cy+2, cx-13, cy-7, cx-6, cy-9);
                heart.curveTo(cx-2, cy-11, cx, cy-8, cx, cy-8);
                heart.curveTo(cx, cy-8, cx+2, cy-11, cx+6, cy-9);
                heart.curveTo(cx+13, cy-7, cx+12, cy+2, cx, cy+9);
                heart.closePath();
                if (likedState[0]) {
                    g2.setColor(ROJO);
                    g2.fill(heart);
                } else {
                    g2.setColor(getModel().isRollover() ? ROJO : TEXTO);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.draw(heart);
                }
                g2.dispose();
            }
        };
        btnLike.setBounds(12, ay, 28, 28);
        btnLike.setOpaque(false);
        btnLike.setContentAreaFilled(false);
        btnLike.setBorderPainted(false);
        btnLike.setFocusPainted(false);
        btnLike.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        int[] likesCount = {p.getTotalLikes()};
        JLabel lblLikes = new JLabel(likesCount[0] + " likes");
        lblLikes.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblLikes.setForeground(GRIS);
        lblLikes.setBounds(42, ay + 7, 70, 14);
        post.add(lblLikes);

        btnLike.addActionListener(e -> {
            likedState[0] = !likedState[0];
            if (likedState[0]) {
                p.reaccionar(usuarioActual);
                likesCount[0]++;
            } else {
                p.quitarLike(usuarioActual);
                likesCount[0] = Math.max(0, likesCount[0] - 1);
            }
            lblLikes.setText(likesCount[0] + " likes");
            btnLike.repaint();
        });
        post.add(btnLike);

        JButton btnCom = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(getModel().isRollover() ? GRIS : TEXTO);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(cx-10, cy-9, 20, 16, 5, 5);
                g2.drawLine(cx-5, cy+7, cx-8, cy+11);
                g2.dispose();
            }
        };
        btnCom.setBounds(116, ay, 28, 28);
        btnCom.setOpaque(false);
        btnCom.setContentAreaFilled(false);
        btnCom.setBorderPainted(false);
        btnCom.setFocusPainted(false);
        btnCom.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        int[] comsCount = {p.getComentarios() != null ? p.getComentarios().size() : 0};
        JLabel lblComs = new JLabel(comsCount[0] + " comentarios");
        lblComs.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblComs.setForeground(GRIS);
        lblComs.setBounds(146, ay + 7, 100, 14);
        post.add(lblComs);

        btnCom.addActionListener(e -> {
            mostrarDialogoComentarios(p, lblComs, comsCount);
        });
        post.add(btnCom);

        int capY = ay + 30;
        String htmlCaption = "<html><div style='width:" + (postAncho - 28)
            + "px; font-family:SansSerif; font-size:10pt'>"
            + "<b>" + usuario+":"+ "</b> " + caption
            + "</div></html>";
        // Avatar del autor al lado del caption
        Usuario uCaption = Usuario.cargarDesdeArchivo(usuario);
        String rutaCaption = uCaption != null ? uCaption.getRutaFoto() : null;
        JPanel avCaption = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (rutaCaption != null && !rutaCaption.isEmpty()) {
                    try {
                        BufferedImage img = javax.imageio.ImageIO.read(new File(rutaCaption).getAbsoluteFile());
                        if (img != null) {
                            g2.setClip(new Ellipse2D.Float(0, 0, 28, 28));
                            g2.drawImage(img, 0, 0, 28, 28, this);
                            g2.dispose();
                            return;
                        }
                    } catch (Exception ex) { }
                }
                g2.setColor(avatarColor);
                g2.fillOval(0, 0, 28, 28);
                g2.dispose();
            }
        };
        avCaption.setOpaque(false);
        avCaption.setBounds(12, capY + 10, 28, 28);
        post.add(avCaption);

        JLabel lblCaption = new JLabel(htmlCaption);
        lblCaption.setBounds(46, capY, postAncho - 58, captionH);
        post.add(lblCaption);

        return post;
    }

    private void mostrarDialogoComentarios(Publicacion p, JLabel lblComs, int[] comsCount) {
        JDialog dlg = new JDialog(ventana, "Comentarios", true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(700, 500);
        dlg.setLocationRelativeTo(ventana);

        // Panel izquierdo — imagen del post
        BufferedImage[] imgPost = {null};
        String rutaImg = p.getRutaImagen();
        if (rutaImg != null && !rutaImg.isEmpty()) {
            try {
                imgPost[0] = javax.imageio.ImageIO.read(new File(rutaImg).getAbsoluteFile());
            } catch (Exception ex) { }
        }

        JPanel pnlImagen = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imgPost[0] != null) {
                    g.drawImage(imgPost[0], 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(BORDE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        pnlImagen.setPreferredSize(new Dimension(280, 500));
        dlg.add(pnlImagen, BorderLayout.WEST);

        // Panel derecho — comentarios
        JPanel pnlDerecho = new JPanel(new BorderLayout());
        dlg.add(pnlDerecho, BorderLayout.CENTER);

        JPanel pnlLista = new JPanel();
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));
        pnlLista.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Titulo del post al tope
        pnlLista.add(crearFilaComentario(p.getAutor() + ": " + p.getContenido()));
        pnlLista.add(Box.createVerticalStrut(8));

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        pnlLista.add(sep);
        pnlLista.add(Box.createVerticalStrut(8));

        // Comentarios
        for (String com : p.getComentarios()) {
            pnlLista.add(crearFilaComentario(com));
            pnlLista.add(Box.createVerticalStrut(6));
        }

        JScrollPane scroll = new JScrollPane(pnlLista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        pnlDerecho.add(scroll, BorderLayout.CENTER);

        // Barra escribir comentario
        JPanel barraEscribir = new JPanel(new BorderLayout(6, 0));
        barraEscribir.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JTextField txtCom = new JTextField();
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(e -> {
            String texto = txtCom.getText().trim();
            if (!texto.isEmpty()) {
                p.comentar(usuarioActual, texto);
                pnlLista.add(crearFilaComentario(usuarioActual + ": " + texto));
                pnlLista.add(Box.createVerticalStrut(6));
                pnlLista.revalidate();
                pnlLista.repaint();
                txtCom.setText("");
                comsCount[0]++;
                lblComs.setText(comsCount[0] + " comentarios");
            }
        });
        barraEscribir.add(txtCom, BorderLayout.CENTER);
        barraEscribir.add(btnEnviar, BorderLayout.EAST);
        pnlDerecho.add(barraEscribir, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private JPanel crearFilaComentario(String com) {
        String username = com.contains(":") ? com.split(":")[0].trim() : "";

        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));

        // Avatar
        Usuario uCom = username.isEmpty() ? null : Usuario.cargarDesdeArchivo(username);
        String rutaCom = uCom != null ? uCom.getRutaFoto() : null;
        JPanel avatar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (rutaCom != null && !rutaCom.isEmpty()) {
                    try {
                        BufferedImage img = javax.imageio.ImageIO.read(new File(rutaCom).getAbsoluteFile());
                        if (img != null) {
                            g2.setClip(new Ellipse2D.Float(0, 0, 32, 32));
                            g2.drawImage(img, 0, 0, 32, 32, this);
                            g2.dispose();
                            return;
                        }
                    } catch (Exception ex) { }
                }
                g2.setColor(colorDeUsuario(username));
                g2.fillOval(0, 0, 32, 32);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(32, 32));
        avatar.setMinimumSize(new Dimension(32, 32));
        avatar.setMaximumSize(new Dimension(32, 32));

        // Texto con wrap
        JTextArea txtArea = new JTextArea(com);
        txtArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtArea.setForeground(TEXTO);
        txtArea.setOpaque(false);
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setEditable(false);
        txtArea.setFocusable(false);
        txtArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 8));

        fila.add(avatar, BorderLayout.WEST);
        fila.add(txtArea, BorderLayout.CENTER);
        return fila;
    }

    private JPanel construirPanelDerecho() {
        JPanel right = new JPanel(null);
        right.setBackground(FONDO);
        right.setBounds(W - RIGHT_W, TOPBAR_H, RIGHT_W, H - TOPBAR_H);

        JLabel lblSug = new JLabel("Sugerencias para ti");
        lblSug.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSug.setForeground(TEXTO);
        lblSug.setBounds(12, 18, 180, 18);
        right.add(lblSug);

        JLabel lblVerTodo = new JLabel("Ver todo");
        lblVerTodo.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblVerTodo.setForeground(AZUL);
        lblVerTodo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblVerTodo.setBounds(RIGHT_W - 66, 18, 54, 18);
        right.add(lblVerTodo);

        List<String> sugerencias = GestorArchivos.getTodosLosUsuarios();
        sugerencias.remove(usuarioActual.toLowerCase());
        int sugY = 46;
        int max = Math.min(sugerencias.size(), 4);

        Usuario yo = Usuario.cargarDesdeArchivo(usuarioActual);

        for (int i = 0; i < max; i++) {
            final String otro = sugerencias.get(i);

            JPanel av = crearAvatar(36, colorDeUsuario(otro));
            av.setBounds(12, sugY, 36, 36);
            av.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            av.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirPerfilOtro(otro);
                }
            });
            right.add(av);

            // contar seguidores
            int seguidores = GestorUsuarios.getFollowers(otro).size();

            JLabel lUser = new JLabel(otro);
            lUser.setFont(new Font("SansSerif", Font.BOLD, 12));
            lUser.setForeground(TEXTO);
            lUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lUser.setBounds(56, sugY + 2, 140, 15);
            lUser.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirPerfilOtro(otro);
                }
            });
            right.add(lUser);

            JLabel lSub = new JLabel(seguidores + " seguidores");
            lSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lSub.setForeground(GRIS);
            lSub.setBounds(56, sugY + 19, 140, 13);
            right.add(lSub);

            boolean yaSigue = yo != null && yo.sigueA(otro);

            final JLabel btnSeg = new JLabel(yaSigue ? "Siguiendo" : "Seguir");
            btnSeg.setFont(new Font("SansSerif", yaSigue ? Font.PLAIN : Font.BOLD, 12));
            btnSeg.setForeground(yaSigue ? GRIS : AZUL);
            btnSeg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnSeg.setBounds(RIGHT_W - 70, sugY + 10, 58, 16);
            btnSeg.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean sigue = btnSeg.getText().equals("Siguiendo");
                    if (sigue) {
                        GestorUsuarios.dejarDeSeguir(usuarioActual, otro);
                        btnSeg.setText("Seguir");
                        btnSeg.setFont(new Font("SansSerif", Font.BOLD, 12));
                        btnSeg.setForeground(AZUL);
                    } else {
                        GestorUsuarios.seguir(usuarioActual, otro);
                        btnSeg.setText("Siguiendo");
                        btnSeg.setFont(new Font("SansSerif", Font.PLAIN, 12));
                        btnSeg.setForeground(GRIS);
                    }
                }
            });
            right.add(btnSeg);
            sugY += 46;
        }

        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(GRIS);
        lblFooter.setBounds(12, H - TOPBAR_H - 28, 220, 14);
        right.add(lblFooter);
        return right;
    }

    private void abrirPerfilOtro(String username) {
        String key = "perfil_" + username;
        Gui_Perfil guiPerfil = new Gui_Perfil(ventana, nav, usuarioActual, username);
        nav.getPnlCards().add(guiPerfil.construirPantalla(), key);
        nav.ir(key);
    }

    private Color colorDeUsuario(String username) { return new Color(180, 180, 180); }

    private JPanel crearAvatar(int size, Color color) {
        JPanel p = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, size, size);
                g2.dispose();
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(size, size);
            }
        };
        p.setOpaque(false);
        return p;
    }
}