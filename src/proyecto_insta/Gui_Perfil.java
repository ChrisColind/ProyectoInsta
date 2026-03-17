/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import Logica.*;
import Logica.GestorUsuarios;
import Logica.GestorArchivos;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.*;
import static proyecto_insta.Gui_Inicio.C_AZUL;
import static proyecto_insta.Gui_Inicio.C_BLANCO;
import static proyecto_insta.Gui_Inicio.C_GRIS;
import static proyecto_insta.Gui_Inicio.C_TEXTO;

/**
 *
 * @author Rogelio
 */
public class Gui_Perfil {

    private static final Color BORDE    = new Color(219, 219, 219);
    private static final Color FONDO    = new Color(250, 250, 250);
    private static final Color TEXTO    = new Color(38, 38, 38);
    private static final Color GRIS     = new Color(142, 142, 142);
    private static final Color AZUL     = new Color(0, 149, 246);
    private static final Color BLANCO   = Color.WHITE;
    private static final Color HOVER    = new Color(245, 245, 245);
    private static final Color ROJO     = new Color(237, 73, 86);
    private static final Color VERDE    = new Color(39, 174, 96);
    private static final Color MORADO   = new Color(88, 81, 219);

    private static final int W         = 1366;
    private static final int H         = 768;
    private static final int TOPBAR_H  = 54;
    private static final int SIDEBAR_W = 244;

    private final JFrame ventana;
    private final Gui_Navegador nav;
    private final String usuarioActual;
    private final String usernamePerfil;

    private JPanel pnlGrid;
    private JLabel lblEstadoBtn;

    public Gui_Perfil(JFrame ventana, Gui_Navegador nav, String usuarioActual, String usernamePerfil) {
        this.ventana        = ventana;
        this.nav            = nav;
        this.usuarioActual  = usuarioActual;
        this.usernamePerfil = usernamePerfil;
    }

    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(FONDO);
        panel.setBounds(0, 0, W, H);
        panel.add(construirTopBar());
        panel.add(construirSidebar());
        panel.add(construirAreaPerfil());
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
                g2.drawString(getText(), 0, fm.getAscent());
                g2.dispose();
            }
        };
        logo.setFont(new Font("Segoe Script", Font.PLAIN, 26));
        logo.setBounds(16, 10, 160, 36);
        bar.add(logo);
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
        JPanel avatar = new JPanel(null) {
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
        avatar.setOpaque(false);
        avatar.setBounds(16, 20, 56, 56);
        side.add(avatar);

        JLabel lblNombre = new JLabel(usuarioActual);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(TEXTO);
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
        sep.setForeground(BORDE);
        sep.setBounds(16, 90, SIDEBAR_W - 32, 1);
        side.add(sep);

        String[] nombres = {"Inicio", "Buscar", "Crear", "Chats", "Perfil"};
        int[] posY = {110, 162, 214, 266, 318};

        for (int i = 0; i < nombres.length; i++) {
            final int idx = i;
            final boolean activo = (idx == 4);

            JButton btn = new JButton() {
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
                    g2.setColor(activo ? MORADO : TEXTO);
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
                    g2.setColor(activo ? MORADO : TEXTO);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(nombres[idx], 52, cy + fm.getAscent() / 2 - 1);
                    g2.dispose();
                }
            };
            btn.setBounds(8, posY[i], SIDEBAR_W - 16, 44);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (idx == 0) btn.addActionListener(e -> nav.ir("home"));
            if (idx == 1) btn.addActionListener(e -> nav.ir("buscar"));
            if (idx == 2) btn.addActionListener(e -> nav.ir("crear"));
            if (idx == 3) btn.addActionListener(e -> nav.ir("chats"));
            side.add(btn);
        }

        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(GRIS);
        lblFooter.setBounds(16, H - TOPBAR_H - 28, 220, 14);
        side.add(lblFooter);
        return side;
    }

    private JPanel construirAreaPerfil() {  
        int areaX = SIDEBAR_W;
        int areaW = W - SIDEBAR_W;

        JPanel area = new JPanel(null);
        area.setBackground(FONDO);
        area.setBounds(areaX, TOPBAR_H, areaW, H - TOPBAR_H);

        Usuario u = Usuario.cargarDesdeArchivo(usernamePerfil);
        if (u == null) {
            JLabel lblError = new JLabel("Usuario no encontrado.");
            lblError.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblError.setForeground(GRIS);
            lblError.setBounds(40, 40, 300, 24);
            area.add(lblError);
            return area;
        }

        boolean esMiPerfil = usuarioActual.equalsIgnoreCase(usernamePerfil);

        // avatar grande — foto si tiene, color si no
        String rutaFotoPerfil = u.getRutaFoto();
        JPanel avatarGrande = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (rutaFotoPerfil != null && !rutaFotoPerfil.isEmpty()) {
                    try {
                        File f = new File(rutaFotoPerfil).getAbsoluteFile();
                        BufferedImage img = javax.imageio.ImageIO.read(f);
                        if (img != null) {
                            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 90, 90));
                            g2.drawImage(img, 0, 0, 90, 90, this);
                        }
                    } catch (Exception ex) {
                        g2.setColor(colorDeUsuario(u.getUsername()));
                        g2.fillOval(0, 0, 90, 90);
                    }
                } else {
                    g2.setColor(colorDeUsuario(u.getUsername()));
                    g2.fillOval(0, 0, 90, 90);
                }
                g2.dispose();
            }
        };
        avatarGrande.setOpaque(false);
        avatarGrande.setBounds(40, 30, 90, 90);
        area.add(avatarGrande);

        // nombre y username
        JLabel lblNombreCompleto = new JLabel(u.getNombre() != null ? u.getNombre() : u.getUsername());
        lblNombreCompleto.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblNombreCompleto.setForeground(TEXTO);
        lblNombreCompleto.setBounds(150, 30, 400, 28);
        area.add(lblNombreCompleto);

        JLabel lblUsername = new JLabel("@" + u.getUsername());
        lblUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblUsername.setForeground(GRIS);
        lblUsername.setBounds(150, 60, 300, 20);
        area.add(lblUsername);

        // info: genero, edad, fecha, tipo cuenta
        String infoTexto = u.getGenero() + "  ·  " + u.getEdad() + " anos  ·  " + u.getFechaCreacion() + "  ·  " + u.getTipoCuenta();
        JLabel lblInfo = new JLabel(infoTexto);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInfo.setForeground(GRIS);
        lblInfo.setBounds(150, 84, 600, 16);
        area.add(lblInfo);

        // contadores followers / following / publicaciones
        List<String> followers = u.getFollowers();
        List<String> following = u.getFollowing();
        List<Publicacion> pubs = GestorPublicaciones.getPublicacionesDeUsuario(usernamePerfil);

        int contX = 150;
        area.add(crearContador(String.valueOf(pubs.size()), "publicaciones", contX, 108));
        area.add(crearContador(String.valueOf(followers.size()), "seguidores", contX + 110, 108));
        area.add(crearContador(String.valueOf(following.size()), "siguiendo", contX + 220, 108));

        // botones accion
        if (esMiPerfil) {
            boolean activo = u.esActivo();

            JButton btnFoto = crearBoton("Cambiar foto", new Color(239, 239, 239));
            btnFoto.setForeground(TEXTO);
            btnFoto.setBounds(150, 148, 130, 34);
            btnFoto.addActionListener(e -> {
                JFileChooser selector = new JFileChooser();
                selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagenes", "jpg", "jpeg", "png", "gif", "bmp", "webp"));
                selector.setDialogTitle("Selecciona tu nueva foto");
                if (selector.showOpenDialog(ventana) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String ruta = selector.getSelectedFile().getAbsolutePath();
                        String ext = ruta.substring(ruta.lastIndexOf('.'));
                        String destino = GestorArchivos.RAIZ + usernamePerfil + "/imagenes/perfil" + ext;
                        java.nio.file.Files.copy(java.nio.file.Paths.get(ruta),
                            java.nio.file.Paths.get(destino),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        Usuario uAct = Usuario.cargarDesdeArchivo(usernamePerfil);
                        if (uAct != null) {
                            uAct.setRutaFoto(destino);
                            uAct.guardar();
                        }
                        JOptionPane.showMessageDialog(ventana, "Foto actualizada!");
                        nav.ir("perfil");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ventana, "Error al cambiar la foto.");
                    }
                }
            });
            area.add(btnFoto);

            JButton btnEstado = crearBoton(activo ? "Desactivar cuenta" : "Activar cuenta", activo ? ROJO : VERDE);
            btnEstado.setBounds(290, 148, 160, 34);
            btnEstado.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(ventana,
                    activo ? "Desactivar tu cuenta?" : "Activar tu cuenta?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    if (activo) GestorUsuarios.desactivar(usernamePerfil);
                    else        GestorUsuarios.activar(usernamePerfil);
                    nav.ir("home");
                }
            });
            area.add(btnEstado);
        } else {
            Usuario yo = Usuario.cargarDesdeArchivo(usuarioActual);
            boolean sigue = yo != null && yo.sigueA(usernamePerfil);

            JButton btnSeguir = crearBoton(sigue ? "Dejar de seguir" : "Seguir", sigue ? new Color(239, 239, 239) : AZUL);
            btnSeguir.setForeground(sigue ? TEXTO : BLANCO);
            btnSeguir.setBounds(150, 148, 140, 34);
            btnSeguir.addActionListener(e -> {
                boolean siguiendo = btnSeguir.getText().equals("Dejar de seguir");
                if (siguiendo) {
                    GestorUsuarios.dejarDeSeguir(usuarioActual, usernamePerfil);
                    btnSeguir.setText("Seguir");
                    btnSeguir.setBackground(AZUL);
                    btnSeguir.setForeground(BLANCO);
                } else {
                    GestorUsuarios.seguir(usuarioActual, usernamePerfil);
                    btnSeguir.setText("Dejar de seguir");
                    btnSeguir.setBackground(new Color(239, 239, 239));
                    btnSeguir.setForeground(TEXTO);
                }
            });
            area.add(btnSeguir);

            JButton btnMensaje = crearBoton("Mensaje", new Color(239, 239, 239));
            btnMensaje.setForeground(TEXTO);
            btnMensaje.setBounds(300, 148, 100, 34);
            btnMensaje.addActionListener(e -> nav.ir("chats"));
            area.add(btnMensaje);
        }

        // separador
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        sep.setBounds(40, 196, areaW - 80, 1);
        area.add(sep);

        // grid de publicaciones
        JLabel lblPubs = new JLabel("Publicaciones");
        lblPubs.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPubs.setForeground(TEXTO);
        lblPubs.setBounds(40, 206, 200, 20);
        area.add(lblPubs);

        pnlGrid = new JPanel(null);
        pnlGrid.setBackground(FONDO);

        int gridW = areaW - 80;
        int celdaW = (gridW - 8) / 3;
        int celdaH = celdaW;
        int totalFilas = (int) Math.ceil(pubs.size() / 3.0);
        int gridH = Math.max(totalFilas * (celdaH + 4), celdaH);
        pnlGrid.setPreferredSize(new Dimension(gridW, gridH));

        for (int i = 0; i < pubs.size(); i++) {
            Publicacion p = pubs.get(i);
            int col = i % 3;
            int fila = i / 3;
            int px = col * (celdaW + 4);
            int py = fila * (celdaH + 4);

            JPanel celda = crearCeldaPublicacion(p, celdaW, celdaH);
            celda.setBounds(px, py, celdaW, celdaH);
            pnlGrid.add(celda);
        }

        JScrollPane scrollGrid = new JScrollPane(pnlGrid);
        scrollGrid.setBorder(BorderFactory.createEmptyBorder());
        scrollGrid.getVerticalScrollBar().setUnitIncrement(14);
        scrollGrid.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scrollGrid.setBounds(40, 232, gridW, H - TOPBAR_H - 245);
        area.add(scrollGrid);

        return area;
    }

    private JPanel crearCeldaPublicacion(Publicacion p, int w, int h) {
        Color color = colorDeUsuario(p.getAutor());

        // Cargar la imagen UNA sola vez, fuera del paintComponent
        BufferedImage[] imgCache = new BufferedImage[1];
        String ruta = p.getRutaImagen();
        if (ruta != null && !ruta.isEmpty()) {
            try {
                imgCache[0] = javax.imageio.ImageIO.read(new File(ruta).getAbsoluteFile());
            } catch (Exception ex) {
                imgCache[0] = null;
            }
        }

        JPanel celda = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (imgCache[0] != null) {
                    g.drawImage(imgCache[0], 0, 0, w, h, this);
                    return;
                }
                g.setColor(color.darker());
                g.fillRect(0, 0, w, h);
            }
        };
        celda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        celda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDialogoPublicacion(p);
            }
        });
        return celda;
    }

    private void mostrarDialogoPublicacion(Publicacion p) {
        JDialog dlg = new JDialog(ventana, "@" + p.getAutor(), true);
        dlg.setSize(480, 520);
        dlg.setLayout(new BorderLayout(0, 0));
        dlg.setLocationRelativeTo(ventana);

        JPanel contenido = new JPanel(null);
        contenido.setBackground(C_BLANCO);

        JLabel lblFecha = new JLabel(p.getFecha() + " " + p.getHora());
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFecha.setForeground(C_GRIS);
        lblFecha.setBounds(16, 12, 440, 16);
        contenido.add(lblFecha);

        // Cargar imagen UNA sola vez antes de crear el panel
        BufferedImage[] imgCache = new BufferedImage[1];
        String rutaImg = p.getRutaImagen();
        if (rutaImg != null && !rutaImg.isEmpty()) {
            try {
                imgCache[0] = javax.imageio.ImageIO.read(new File(rutaImg).getAbsoluteFile());
            } catch (Exception ex) {
                imgCache[0] = null;
            }
        }

        JPanel imgPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (imgCache[0] != null) {
                    g.drawImage(imgCache[0], 0, 0, 448, 280, this);
                    return;
                }
                g.setColor(colorDeUsuario(p.getAutor()).darker());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        imgPanel.setBounds(16, 34, 448, 280);
        contenido.add(imgPanel);

        JLabel lblContenido = new JLabel("<html><div style='width:440px'>" + p.getContenido() + "</div></html>");
        lblContenido.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblContenido.setForeground(C_TEXTO);
        lblContenido.setBounds(16, 322, 448, 50);
        contenido.add(lblContenido);

        if (p.getHashtags() != null && !p.getHashtags().isEmpty()) {
            JLabel lblHash = new JLabel(p.getHashtags());
            lblHash.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblHash.setForeground(C_AZUL);
            lblHash.setBounds(16, 374, 448, 16);
            contenido.add(lblHash);
        }

        dlg.add(contenido, BorderLayout.CENTER);
        dlg.setVisible(true);
    }

    private JPanel crearContador(String numero, String etiqueta, int x, int y) {
        JPanel p = new JPanel(null);
        p.setOpaque(false);
        p.setBounds(x, y, 100, 36);

        JLabel lblNum = new JLabel(numero, SwingConstants.LEFT);
        lblNum.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNum.setForeground(TEXTO);
        lblNum.setBounds(0, 0, 100, 20);
        p.add(lblNum);

        JLabel lblEtiq = new JLabel(etiqueta, SwingConstants.LEFT);
        lblEtiq.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEtiq.setForeground(GRIS);
        lblEtiq.setBounds(0, 18, 100, 16);
        p.add(lblEtiq);

        return p;
    }

    private JButton crearBoton(String texto, Color fondo) {
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
        btn.setBackground(fondo);
        btn.setForeground(BLANCO);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
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