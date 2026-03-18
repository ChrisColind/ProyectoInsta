/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import Logica.*;
import Logica.GestorUsuarios;
import Logica.GestorArchivos;
import Logica.Usuario;
import PEnums.Enums.TipoCuenta;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.*;

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
    private ClienteNotificaciones cliente;
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
        final JPanel panel = new JPanel(null);
        panel.setBackground(FONDO);
        panel.setBounds(0, 0, W, H);
        panel.add(construirTopBar());
        panel.add(construirSidebar());
        panel.add(construirAreaPerfil());

        cliente = new ClienteNotificaciones((tipo, dato) -> {
            if (tipo.equals("SEGUIDOR") && dato.equals(usernamePerfil)) {
                SwingUtilities.invokeLater(() -> {
                    JPanel nuevaArea = construirAreaPerfil();
                    for (Component c : panel.getComponents()) {
                        if (c.getBounds().x == SIDEBAR_W) {
                            panel.remove(c);
                            break;
                        }
                    }
                    panel.add(nuevaArea);
                    panel.revalidate();
                    panel.repaint();
                });
            }
        });
        cliente.conectar();

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
    private int contarMensajesNoLeidos() {
        int total = 0;
        for (String otro : Conversacion.getConversaciones(usuarioActual)) {
            total += new Conversacion(usuarioActual, otro).getMensajesNoLeidos();
        }
        return total;
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
        if (!u.esActivo() && !usuarioActual.equalsIgnoreCase(usernamePerfil)) {
            JLabel lblError = new JLabel("Esta cuenta esta desactivada.");
            lblError.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblError.setForeground(GRIS);
            lblError.setBounds(40, 40, 300, 24);
            area.add(lblError);
            return area;
        }

        boolean esMiPerfil = usuarioActual.equalsIgnoreCase(usernamePerfil);
        List<String> solicitudes = GestorSolicitudes.getSolicitudes(usernamePerfil);
        if (!solicitudes.isEmpty()) {
            JButton btnSolicitudes = crearBoton("Solicitudes (" + solicitudes.size() + ")", MORADO);
            btnSolicitudes.setBounds(460, 148, 180, 34);
            btnSolicitudes.addActionListener(e -> mostrarSolicitudes(solicitudes));
            area.add(btnSolicitudes);
        }
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

        String infoTexto = u.getGenero() + "  ·  " + u.getEdad() + " años  ·  " + u.getFechaCreacion() + "  ·  " + u.getTipoCuenta();
        JLabel lblInfo = new JLabel(infoTexto);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInfo.setForeground(GRIS);
        lblInfo.setBounds(150, 84, 600, 16);
        area.add(lblInfo);

        List<String> followers = u.getFollowers();
        List<String> following = u.getFollowing();
        List<Publicacion> pubs = GestorPublicaciones.getPublicacionesDeUsuario(usernamePerfil);

        int contX = 150;
        area.add(crearContador(String.valueOf(pubs.size()), "publicaciones", contX, 108));
        area.add(crearContador(String.valueOf(followers.size()), "seguidores", contX + 110, 108));
        area.add(crearContador(String.valueOf(following.size()), "siguiendo", contX + 220, 108));

        if (esMiPerfil) {
            boolean activo = u.esActivo();

            JButton btnFoto = crearBoton("Cambiar foto", new Color(239, 239, 239));
            btnFoto.setForeground(TEXTO);
            btnFoto.setBounds(150, 148, 130, 34);
            btnFoto.addActionListener(e -> {
                JFileChooser selector = new JFileChooser(System.getProperty("user.home") + "/Downloads");
                selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagenes", "jpg", "jpeg", "png"));
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
            JButton btnTipo = crearBoton(u.esPublico() ? "Hacer privada" : "Hacer publica", MORADO);
            btnTipo.setBounds(460, 148, 160, 34);
            btnTipo.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(ventana,
                    u.esPublico() ? "Cambiar cuenta a privada?" : "Cambiar cuenta a publica?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    u.setTipoCuenta(u.esPublico() ? "Privada" : "Publica");
                    u.guardar();
                    nav.ir("perfil");
                }
            });
            area.add(btnTipo);
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

            Usuario uPerfil = Usuario.cargarDesdeArchivo(usernamePerfil);
            boolean esPrivado = uPerfil != null && !uPerfil.esPublico();
            boolean solicitudPendiente = GestorSolicitudes.tieneSolicitudPendiente(usuarioActual, usernamePerfil);

            String textoBtn = sigue ? "Dejar de seguir" : (esPrivado && !sigue ? (solicitudPendiente ? "Solicitado" : "Solicitar") : "Seguir");
            Color colorBtn = sigue ? new Color(239, 239, 239) : (solicitudPendiente ? new Color(239, 239, 239) : AZUL);

            JButton btnSeguir = crearBoton(textoBtn, colorBtn);
            btnSeguir.setForeground(sigue || solicitudPendiente ? TEXTO : BLANCO);
            btnSeguir.setBounds(150, 148, 140, 34);
            btnSeguir.addActionListener(e -> {
                if (btnSeguir.getText().equals("Dejar de seguir")) {
                    GestorUsuarios.dejarDeSeguir(usuarioActual, usernamePerfil);
                    btnSeguir.setText(esPrivado ? "Solicitar" : "Seguir");
                    btnSeguir.setBackground(AZUL);
                    btnSeguir.setForeground(BLANCO);
                } else if (btnSeguir.getText().equals("Solicitar")) {
                    GestorSolicitudes.enviarSolicitud(usuarioActual, usernamePerfil);
                    btnSeguir.setText("Solicitado");
                    btnSeguir.setBackground(new Color(239, 239, 239));
                    btnSeguir.setForeground(TEXTO);
                } else if (btnSeguir.getText().equals("Seguir")) {
                    GestorUsuarios.seguir(usuarioActual, usernamePerfil);
                    btnSeguir.setText("Dejar de seguir");
                    btnSeguir.setBackground(new Color(239, 239, 239));
                    btnSeguir.setForeground(TEXTO);
                }
            });
            area.add(btnSeguir);

            Usuario uPerfilMsg = Usuario.cargarDesdeArchivo(usernamePerfil);
            boolean puedeEnviarMsg = uPerfilMsg != null && uPerfilMsg.esActivo() && (uPerfilMsg.esPublico() || GestorUsuarios.sigueA(usuarioActual, usernamePerfil));

            JButton btnMensaje = crearBoton("Mensaje", puedeEnviarMsg ? new Color(239, 239, 239) : GRIS);
            btnMensaje.setForeground(TEXTO);
            btnMensaje.setBounds(300, 148, 100, 34);
            btnMensaje.addActionListener(e -> {
                if (!puedeEnviarMsg) {
                    JOptionPane.showMessageDialog(ventana, "No puedes enviar mensajes a esta cuenta.");
                    return;
                }
                Gui_Chats guiChats = new Gui_Chats(ventana, nav, usuarioActual);
                JPanel chatsPanel = guiChats.construirPantalla();
                nav.getPnlCards().add(chatsPanel, "chats");
                nav.ir("chats");
                guiChats.abrirChatDirecto(usernamePerfil);
            });
            area.add(btnMensaje);
        }

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
    
    
    private void mostrarSolicitudes(List<String> solicitudes) {
        JDialog dlg = new JDialog(ventana, "Solicitudes de seguimiento", true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(400, 400);
        dlg.setLocationRelativeTo(ventana);

        JPanel pnlLista = new JPanel();
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));
        pnlLista.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        for (String solicitante : solicitudes) {
            JPanel fila = new JPanel(new BorderLayout(8, 0));
            fila.setOpaque(false);
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            JLabel lblUser = new JLabel("@" + solicitante);
            lblUser.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblUser.setForeground(TEXTO);
            fila.add(lblUser, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
            btnPanel.setOpaque(false);

            JButton btnAceptar = crearBoton("Aceptar", AZUL);
            btnAceptar.setPreferredSize(new Dimension(90, 30));
            btnAceptar.addActionListener(e -> {
                GestorSolicitudes.aceptarSolicitud(usernamePerfil, solicitante);
                dlg.dispose();
                nav.ir("perfil");
            });

            JButton btnRechazar = crearBoton("Rechazar", ROJO);
            btnRechazar.setPreferredSize(new Dimension(90, 30));
            btnRechazar.addActionListener(e -> {
                GestorSolicitudes.rechazarSolicitud(usernamePerfil, solicitante);
                dlg.dispose();
                nav.ir("perfil");
            });

            btnPanel.add(btnAceptar);
            btnPanel.add(btnRechazar);
            fila.add(btnPanel, BorderLayout.EAST);

            pnlLista.add(fila);
            pnlLista.add(Box.createVerticalStrut(6));
            pnlLista.add(new JSeparator());
        }

        JScrollPane scroll = new JScrollPane(pnlLista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        dlg.add(scroll, BorderLayout.CENTER);
        dlg.setVisible(true);
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
        String hashtagsTitulo = p.getHashtags() != null && !p.getHashtags().isEmpty()
            ? " " + p.getHashtags() : "";
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setOpaque(false);
        panelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));

        JPanel filaTitulo = crearFilaComentario(p.getAutor() + ": " + p.getContenido() + hashtagsTitulo);
        filaTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));
        panelTitulo.add(filaTitulo);

        if (p.getMenciones() != null && !p.getMenciones().isEmpty()) {
            JLabel lblMenciones = new JLabel("<html><div style='width:270px'>" + p.getMenciones() + "</div></html>");
            lblMenciones.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblMenciones.setForeground(AZUL);
            lblMenciones.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
            lblMenciones.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelTitulo.add(lblMenciones);
        }

        pnlLista.add(panelTitulo);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        pnlLista.add(sep);
        pnlLista.add(Box.createVerticalStrut(4));

        for (String com : p.getComentarios()) {
            pnlLista.add(crearFilaComentario(com));
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
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE - 1));

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
    
    public void abrirPublicacion(Publicacion p) {
        mostrarDialogoPublicacion(p);
    }

}