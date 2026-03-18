/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_insta;

import Logica.*;
import Absrtact.Mensaje;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Rogelio
 */
public class Gui_Chats {

    public static final Color C_BORDE = new Color(219, 219, 219);
    public static final Color C_FONDO = new Color(250, 250, 250);
    public static final Color C_TEXTO = new Color(38, 38, 38);
    public static final Color C_GRIS = new Color(142, 142, 142);
    public static final Color C_AZUL = new Color(0, 149, 246);
    public static final Color C_BLANCO = Color.WHITE;
    public static final Color C_HOVER = new Color(245, 245, 245);
    public static final Color C_BURBUJA_MIO = new Color(0, 149, 246);
    public static final Color C_BURBUJA_OTRO = new Color(239, 239, 239);

    private static final int W = 1366;
    public static final int H = 768;
    public static final int TOPBAR_H = 54;
    public static final int SIDEBAR_W = 244;
    public static final int LISTA_W = 300;

    
    private JTextField txtMensaje;
    private final Gui_Navegador nav;
    private JLabel lblChatActivo;
    private JPanel pnlListaConvs;
    private final JFrame ventana;
    private final String usuarioActual;
    private String chatAbierto = null;
    private boolean escribiendo = false;

    private JPanel pnlMensajes;

    private ClienteNotificaciones cliente;

    public Gui_Chats(JFrame ventana, Gui_Navegador nav, String usuarioActual) {
        this.ventana = ventana;
        this.nav = nav;
        this.usuarioActual = usuarioActual;
        this.cliente = new ClienteNotificaciones((tipo, dato) -> {
            if (tipo.equals("MENSAJE") && dato.equals(usuarioActual)) {
                if (chatAbierto != null) {
                    abrirChat(chatAbierto);
                }
                cargarListaConversaciones();
                repintarSidebar();
            }
        });
        cliente.conectar();
    }

    private void repintarSidebar() {
        for (Component c : construirSidebar().getComponents()) {
            c.repaint();
        }
    }

    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(C_FONDO);
        panel.add(construirTopBar());
        panel.add(construirSidebar());
        panel.add(construirListaConversaciones());
        panel.add(construirAreaChat());
        return panel;
    }

    private JPanel construirTopBar() {
        JPanel bar = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(0, TOPBAR_H - 1, W, TOPBAR_H - 1);
            }
        };
        bar.setBackground(C_BLANCO);
        bar.setBounds(0, 0, W, TOPBAR_H);

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
                g.setColor(C_BORDE);
                g.drawLine(SIDEBAR_W - 1, 0, SIDEBAR_W - 1, H);
            }
        };
        side.setBackground(C_BLANCO);
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
            public void mouseClicked(MouseEvent e) {
                nav.ir("perfil");
            }
        });
        side.add(lblVerPerfil);

        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDE);
        sep.setBounds(16, 90, SIDEBAR_W - 32, 1);
        side.add(sep);

        String[] nombres = {"Inicio", "Buscar", "Crear", "Chats", "Perfil"};
        int[] posY = {110, 162, 214, 266, 318};

        for (int i = 0; i < nombres.length; i++) {
            final int idx = i;
            final boolean activo = (idx == 3);

            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (activo) {
                        g2.setColor(new Color(240, 240, 255));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    } else if (getModel().isRollover()) {
                        g2.setColor(C_HOVER);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    }
                    int cx = 28, cy = getHeight() / 2;
                    g2.setColor(activo ? new Color(88, 81, 219) : C_TEXTO);
                    g2.setStroke(new BasicStroke(activo ? 2.2f : 1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (idx) {
                        case 0:
                            int[] hx = {cx, cx + 10, cx + 10, cx - 10, cx - 10},
                             hy = {cy - 10, cy, cy + 10, cy + 10, cy};
                            g2.drawPolygon(hx, hy, 5);
                            g2.drawRect(cx - 4, cy + 1, 8, 9);
                            break;
                        case 1:
                            g2.drawOval(cx - 9, cy - 10, 18, 18);
                            g2.drawLine(cx + 7, cy + 6, cx + 12, cy + 11);
                            break;
                        case 2:
                            g2.drawRoundRect(cx - 11, cy - 11, 22, 22, 6, 6);
                            g2.drawLine(cx, cy - 6, cx, cy + 6);
                            g2.drawLine(cx - 6, cy, cx + 6, cy);
                            break;
                        case 3:
                            g2.drawRoundRect(cx - 11, cy - 9, 22, 17, 5, 5);
                            g2.drawLine(cx - 5, cy + 8, cx - 8, cy + 12);
                            int noLeidos = contarMensajesNoLeidos();
                            if (noLeidos > 0) {
                                String badge = noLeidos > 4 ? "4+" : String.valueOf(noLeidos);
                                g2.setColor(new Color(237, 73, 86));
                                g2.fillOval(cx + 4, cy - 14, 16, 16);
                                g2.setColor(Color.WHITE);
                                g2.setFont(new Font("SansSerif", Font.BOLD, 8));
                                FontMetrics fmb = g2.getFontMetrics();
                                g2.drawString(badge, cx + 4 + (16 - fmb.stringWidth(badge)) / 2, cy - 14 + 11);
                            }
                            break;
                        case 4:
                            g2.drawOval(cx - 10, cy - 10, 20, 20);
                            g2.fillOval(cx - 4, cy - 6, 8, 8);
                            g2.drawArc(cx - 8, cy + 1, 16, 12, 0, 180);
                            break;
                    }
                    g2.setFont(new Font("SansSerif", activo ? Font.BOLD : Font.PLAIN, 14));
                    g2.setColor(activo ? new Color(88, 81, 219) : C_TEXTO);
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
            if (idx == 0) {
                btn.addActionListener(e -> nav.ir("home"));
            }
            if (idx == 1) {
                btn.addActionListener(e -> nav.ir("buscar"));
            }
            if (idx == 2) {
                btn.addActionListener(e -> nav.ir("crear"));
            }
            if (idx == 4) {
                btn.addActionListener(e -> nav.ir("perfil"));
            }
            side.add(btn);
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

    private JScrollPane construirListaConversaciones() {
        pnlListaConvs = new JPanel();
        pnlListaConvs.setLayout(new BoxLayout(pnlListaConvs, BoxLayout.Y_AXIS));
        pnlListaConvs.setBackground(C_BLANCO);

        JPanel headerLista = new JPanel(null);
        headerLista.setBackground(C_BLANCO);
        headerLista.setMaximumSize(new Dimension(LISTA_W, 44));
        headerLista.setPreferredSize(new Dimension(LISTA_W, 44));

        JLabel lblTitulo = new JLabel("  Mensajes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(C_TEXTO);
        lblTitulo.setBounds(0, 10, 180, 24);
        headerLista.add(lblTitulo);

        JLabel btnNuevo = new JLabel("+ Nuevo");
        btnNuevo.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnNuevo.setForeground(C_AZUL);
        btnNuevo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNuevo.setBounds(LISTA_W - 70, 14, 65, 16);
        btnNuevo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDialogoNuevoChat();
            }
        });
        headerLista.add(btnNuevo);
        pnlListaConvs.add(headerLista);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(LISTA_W, 1));
        sep.setForeground(C_BORDE);
        pnlListaConvs.add(sep);

        cargarListaConversaciones();

        JScrollPane scroll = new JScrollPane(pnlListaConvs);
        scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, C_BORDE));
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scroll.setBounds(SIDEBAR_W, TOPBAR_H, LISTA_W, H - TOPBAR_H);
        return scroll;
    }

    private void cargarListaConversaciones() {
        while (pnlListaConvs.getComponentCount() > 2) {
            pnlListaConvs.remove(2);
        }

        Usuario yo = Usuario.cargarDesdeArchivo(usuarioActual);
        List<String> siguiendo = yo != null ? yo.getFollowing() : new ArrayList<>();

        List<String> convs = Conversacion.getConversaciones(usuarioActual);
        for (String c : convs) {
            if (!siguiendo.contains(c)) {
                siguiendo.add(c);
            }
        }

        if (siguiendo.isEmpty()) {
            JLabel lblVacio = new JLabel("  No sigues a nadie aun");
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblVacio.setForeground(C_GRIS);
            lblVacio.setPreferredSize(new Dimension(LISTA_W, 70));
            lblVacio.setMaximumSize(new Dimension(LISTA_W, 70));
            pnlListaConvs.add(lblVacio);
        }

        for (String otro : siguiendo) {
            Usuario uOtro = Usuario.cargarDesdeArchivo(otro);
            Conversacion conv = new Conversacion(usuarioActual, otro);
            int noLeidos = conv.getMensajesNoLeidos();
            boolean desactivado = uOtro == null || !uOtro.esActivo();

            JPanel fila = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(C_BORDE);
                    g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                    g2.dispose();
                }
            };
            fila.setBackground(C_BLANCO);
            fila.setPreferredSize(new Dimension(LISTA_W, 70));
            fila.setMaximumSize(new Dimension(LISTA_W, 70));
            fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            String rutaOtro = uOtro != null ? uOtro.getRutaFoto() : null;
            JPanel av = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (rutaOtro != null && !rutaOtro.isEmpty()) {
                        try {
                            BufferedImage img = javax.imageio.ImageIO.read(new File(rutaOtro).getAbsoluteFile());
                            if (img != null) {
                                g2.setClip(new Ellipse2D.Float(0, 0, 38, 38));
                                g2.drawImage(img, 0, 0, 38, 38, this);
                                g2.dispose();
                                return;
                            }
                        } catch (Exception ex) {
                        }
                    }
                    g2.setColor(colorDeUsuario(otro));
                    g2.fillOval(0, 0, 38, 38);
                    g2.dispose();
                }
            };
            av.setOpaque(false);
            av.setBounds(10, 11, 38, 38);
            fila.add(av);

            String badgeTexto = noLeidos == 0 ? "" : noLeidos > 4 ? " (4+ mensajes nuevos)" : " (" + noLeidos + " mensaje" + (noLeidos > 1 ? "s nuevos)" : " nuevo)");
            JLabel lblNombre = new JLabel("@" + otro + (desactivado ? " (CUENTA DESACTIVADA)" : "") + badgeTexto);
            lblNombre.setFont(new Font("SansSerif", noLeidos > 0 ? Font.BOLD : Font.PLAIN, 13));
            lblNombre.setForeground(desactivado ? C_GRIS : noLeidos > 0 ? C_AZUL : C_TEXTO);
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblNombre.setForeground(desactivado ? C_GRIS : C_TEXTO);
            lblNombre.setBounds(58, 8, 220, 36);
            lblNombre.setVerticalAlignment(SwingConstants.TOP);
            fila.add(lblNombre);

            fila.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    fila.setBackground(C_HOVER);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    fila.setBackground(C_BLANCO);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirChat(otro);
                }
            });

            if (convs.contains(otro)) {
                JButton btnX = new JButton("✕") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(getModel().isRollover() ? new Color(237, 73, 86) : C_GRIS);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                        g2.dispose();
                    }
                };
                btnX.setBounds(264, 4, 24, 24);
                btnX.setOpaque(false);
                btnX.setContentAreaFilled(false);
                btnX.setBorderPainted(false);
                btnX.setFocusPainted(false);
                btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnX.addActionListener(e -> {
                    int conf = JOptionPane.showConfirmDialog(ventana,
                            "Eliminar conversacion con @" + otro + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        new Conversacion(usuarioActual, otro).eliminar();
                        if (otro.equals(chatAbierto)) {
                            chatAbierto = null;
                            limpiarAreaChat();
                        }
                        cargarListaConversaciones();
                        pnlListaConvs.revalidate();
                        pnlListaConvs.repaint();
                    }
                });
                fila.add(btnX);
            }

            pnlListaConvs.add(fila);
        }

        pnlListaConvs.revalidate();
        pnlListaConvs.repaint();
    }

    private JPanel construirAreaChat() {
        JPanel area = new JPanel(null);
        area.setBackground(C_FONDO);
        int x = SIDEBAR_W + LISTA_W;
        int w = W - x;
        area.setBounds(x, TOPBAR_H, w, H - TOPBAR_H);

        // cabecera del chat
        JPanel header = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        header.setBackground(C_BLANCO);
        header.setBounds(0, 0, w, 54);

        lblChatActivo = new JLabel("Selecciona una conversacion");
        lblChatActivo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblChatActivo.setForeground(C_GRIS);
        lblChatActivo.setBounds(16, 15, w - 32, 24);
        header.add(lblChatActivo);
        area.add(header);

        // area de mensajes
        pnlMensajes = new JPanel();
        pnlMensajes.setLayout(new BoxLayout(pnlMensajes, BoxLayout.Y_AXIS));
        pnlMensajes.setBackground(C_FONDO);
        pnlMensajes.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        int areaH = H - TOPBAR_H;

        JScrollPane scrollMsg = new JScrollPane(pnlMensajes);
        scrollMsg.setBorder(BorderFactory.createEmptyBorder());
        scrollMsg.getVerticalScrollBar().setUnitIncrement(14);
        scrollMsg.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scrollMsg.setBounds(0, 54, w, areaH - 54 - 80);
        area.add(scrollMsg);

        JPanel barraEscribir = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(0, 0, getWidth(), 0);
            }
        };
        barraEscribir.setBackground(C_BLANCO);
        barraEscribir.setBounds(0, areaH - 80, w, 70);

        txtMensaje = new JTextField();
        txtMensaje.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMensaje.setForeground(C_GRIS);
        txtMensaje.setText("Envia un mensaje...");
        txtMensaje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDE, 1),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)));
        txtMensaje.setBounds(14, 12, w - 160, 36);
        txtMensaje.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (txtMensaje.getText().equals("Envia un mensaje...")) {
                    txtMensaje.setText("");
                    txtMensaje.setForeground(C_TEXTO);
                }
            }
        });
        txtMensaje.addActionListener(e -> enviarTexto());
        barraEscribir.add(txtMensaje);

        JButton btnSticker = crearBotonSimple("🙂");
        btnSticker.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnSticker.setBounds(w - 140, 12, 44, 36);
        btnSticker.addActionListener(e -> mostrarSelectorSticker());
        barraEscribir.add(btnSticker);

        JButton btnEnviar = crearBotonAzul("Enviar");
        btnEnviar.setBounds(w - 90, 12, 76, 36);
        btnEnviar.addActionListener(e -> enviarTexto());
        barraEscribir.add(btnEnviar);

        area.add(barraEscribir);
        return area;
    }

    private void abrirChat(String otro) {

        escribiendo = false;
        chatAbierto = otro;
        txtMensaje.setText("Envia un mensaje...");
        txtMensaje.setForeground(C_GRIS);
        txtMensaje.setEnabled(true);
        Usuario uOtro = Usuario.cargarDesdeArchivo(otro);
        boolean desactivado = uOtro == null || !uOtro.esActivo();

        lblChatActivo.setText("@" + otro + (desactivado ? "  (cuenta desactivada)" : ""));
        lblChatActivo.setForeground(desactivado ? C_GRIS : C_TEXTO);

        txtMensaje.setEnabled(!desactivado);
        txtMensaje.setText(desactivado ? "No puedes enviar mensajes" : "Envia un mensaje...");
        txtMensaje.setForeground(C_GRIS);

        Conversacion conv = new Conversacion(usuarioActual, otro);
        conv.marcarTodosLeidos();

        pnlMensajes.removeAll();

        for (Mensaje m : conv.getMensajes()) {
            boolean esMio = m.getDe().equals(usuarioActual);
            pnlMensajes.add(crearBurbuja(m, esMio));
            pnlMensajes.add(Box.createVerticalStrut(6));
        }

        pnlMensajes.revalidate();
        pnlMensajes.repaint();
        cargarListaConversaciones();

        // scroll al fondo
        SwingUtilities.invokeLater(() -> {
            JScrollPane sc = (JScrollPane) pnlMensajes.getParent().getParent();
            sc.getVerticalScrollBar().setValue(sc.getVerticalScrollBar().getMaximum());
        });
    }

    private void enviarTexto() {
        if (chatAbierto == null) {
            return;
        }
        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty() || texto.equals("Envia un mensaje...")) {
            return;
        }

        Usuario uOtro = Usuario.cargarDesdeArchivo(chatAbierto);
        if (uOtro == null || !uOtro.esActivo()) {
            JOptionPane.showMessageDialog(ventana, "No puedes enviar mensajes a esta cuenta.");
            return;
        }
        if (!uOtro.esPublico() && !GestorUsuarios.sigueA(chatAbierto, usuarioActual)) {
            JOptionPane.showMessageDialog(ventana, "Esta cuenta es privada. Debes ser seguidor para enviar mensajes.");
            return;
        }

        Mensaje m = new MensajeTexto(usuarioActual, chatAbierto, texto);
        new Conversacion(usuarioActual, chatAbierto).enviar(m);
        txtMensaje.setText("");
        txtMensaje.setForeground(C_TEXTO);
        abrirChat(chatAbierto);
    }

    public void abrirChatDirecto(String username) {
        abrirChat(username);
    }

    private void mostrarSelectorSticker() {
        if (chatAbierto == null) {
            return;
        }
        Usuario uOtro = Usuario.cargarDesdeArchivo(chatAbierto);
        if (uOtro == null || !uOtro.esActivo()) {
            JOptionPane.showMessageDialog(ventana, "No puedes enviar mensajes a esta cuenta.");
            return;
        }
        if (!uOtro.esPublico() && !GestorUsuarios.sigueA(chatAbierto, usuarioActual)) {
            JOptionPane.showMessageDialog(ventana, "Esta cuenta es privada. Debes ser seguidor para enviar mensajes.");
            return;
        }
        JDialog dlg = new JDialog(ventana, "Stickers", true);
        dlg.setLayout(new BorderLayout());
        dlg.setSize(400, 300);
        dlg.setLocationRelativeTo(ventana);

        JPanel pnlStickers = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        pnlStickers.setBackground(C_BLANCO);

        // Cargar stickers del usuario
        cargarStickersEnPanel(pnlStickers, dlg);

        JScrollPane scroll = new JScrollPane(pnlStickers);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        dlg.add(scroll, BorderLayout.CENTER);

        // Boton importar sticker
        JButton btnImportar = new JButton("+ Importar sticker");
        btnImportar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imagenes", "jpg", "jpeg", "png"));
            fc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Downloads"));
            if (fc.showOpenDialog(dlg) == JFileChooser.APPROVE_OPTION) {
                try {
                    File origen = fc.getSelectedFile();
                    String nombre = origen.getName();
                    String destino = GestorArchivos.RAIZ + usuarioActual + "/stickers_personales/" + nombre;
                    java.nio.file.Files.copy(origen.toPath(),
                            new File(destino).toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    GestorArchivos.importarSticker(usuarioActual, nombre);
                    // Recargar stickers
                    pnlStickers.removeAll();
                    cargarStickersEnPanel(pnlStickers, dlg);
                    pnlStickers.revalidate();
                    pnlStickers.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dlg, "Error al importar sticker.");
                }
            }
        });
        dlg.add(btnImportar, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void cargarStickersEnPanel(JPanel pnlStickers, JDialog dlg) {
        List<String> stickers = GestorArchivos.getStickersUsuario(usuarioActual);

        if (stickers.isEmpty()) {
            JLabel lbl = new JLabel("No tienes stickers. Importa uno.");
            lbl.setForeground(C_GRIS);
            pnlStickers.add(lbl);
            return;
        }

        for (String nombre : stickers) {
            File f = new File(GestorArchivos.RAIZ + usuarioActual + "/stickers_personales/" + nombre);
            if (!f.exists()) {
                f = new File(GestorArchivos.STICKERS_GLOBALES + nombre);
            }

            final File archivoFinal = f;

            JButton btn = new JButton() {
                BufferedImage img = null;

                {
                    // Cargar imagen una sola vez
                    try {
                        if (archivoFinal.exists() && archivoFinal.length() > 0) {
                            img = javax.imageio.ImageIO.read(archivoFinal);
                        }
                    } catch (Exception ex) {
                    }
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover() ? new Color(240, 240, 255) : C_BLANCO);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(C_BORDE);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    if (img != null) {
                        g2.drawImage(img, 4, 4, getWidth() - 8, getHeight() - 8, this);
                    } else {
                        // Si no hay imagen mostrar nombre
                        g2.setColor(C_GRIS);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                        FontMetrics fm = g2.getFontMetrics();
                        String n = nombre.length() > 8 ? nombre.substring(0, 8) : nombre;
                        g2.drawString(n, (getWidth() - fm.stringWidth(n)) / 2,
                                (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    }
                    g2.dispose();
                }
            };
            btn.setPreferredSize(new Dimension(70, 70));
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                Mensaje m = new MensajeSticker(usuarioActual, chatAbierto, nombre);
                new Conversacion(usuarioActual, chatAbierto).enviar(m);
                dlg.dispose();
                abrirChat(chatAbierto);
            });
            pnlStickers.add(btn);
        }
    }

    private JPanel crearBurbuja(Mensaje m, boolean esMio) {
        JPanel contenedor = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        contenedor.setOpaque(false);
        contenedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        if (m.getTipo().equals("sticker")) {
            // Buscar imagen del sticker
            String nombre = m.getContenido();
            File f = new File(GestorArchivos.RAIZ + m.getDe() + "/stickers_personales/" + nombre);
            if (!f.exists()) {
                f = new File(GestorArchivos.STICKERS_GLOBALES + nombre);
            }

            final File archivoFinal = f;
            BufferedImage[] imgCache = {null};
            try {
                if (archivoFinal.exists() && archivoFinal.length() > 0) {
                    imgCache[0] = javax.imageio.ImageIO.read(archivoFinal);
                }
            } catch (Exception ex) {
            }

            JPanel stickerPanel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    if (imgCache[0] != null) {
                        g2.drawImage(imgCache[0], 0, 0, 100, 100, this);
                    } else {
                        g2.setColor(C_BORDE);
                        g2.fillRoundRect(0, 0, 100, 100, 8, 8);
                        g2.setColor(C_GRIS);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                        g2.drawString(nombre.replace(".png", ""), 8, 50);
                    }
                    g2.dispose();
                }
            };
            stickerPanel.setPreferredSize(new Dimension(100, 100));
            stickerPanel.setOpaque(false);

            JLabel lblHora = new JLabel(m.getHora());
            lblHora.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lblHora.setForeground(C_GRIS);
            lblHora.setHorizontalAlignment(esMio ? SwingConstants.RIGHT : SwingConstants.LEFT);

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.add(stickerPanel, BorderLayout.CENTER);
            wrapper.add(lblHora, BorderLayout.SOUTH);
            contenedor.add(wrapper);
            return contenedor;
        }

        // Mensaje de texto normal
        String texto = m.getContenido();
        JLabel burbuja = new JLabel("<html><div style='width:240px;padding:4px'>" + texto + "</div></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(esMio ? C_BURBUJA_MIO : C_BURBUJA_OTRO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        burbuja.setForeground(esMio ? C_BLANCO : C_TEXTO);
        burbuja.setFont(new Font("SansSerif", Font.PLAIN, 13));
        burbuja.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        burbuja.setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(burbuja, esMio ? BorderLayout.EAST : BorderLayout.WEST);

        JLabel lblHora = new JLabel(m.getHora());
        lblHora.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblHora.setForeground(C_GRIS);
        lblHora.setHorizontalAlignment(esMio ? SwingConstants.RIGHT : SwingConstants.LEFT);
        wrapper.add(lblHora, BorderLayout.SOUTH);

        contenedor.add(wrapper);
        return contenedor;
    }

    private void mostrarDialogoNuevoChat() {
        Usuario yo = Usuario.cargarDesdeArchivo(usuarioActual);
        if (yo == null) {
            return;
        }

        List<String> siguiendo = yo.getFollowing();
        List<String> activos = new ArrayList<>();
        for (String u : siguiendo) {
            Usuario uOtro = Usuario.cargarDesdeArchivo(u);
            if (uOtro != null && uOtro.esActivo()) {
                activos.add(u);
            }
        }

        if (activos.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "No sigues a ningun usuario activo.");
            return;
        }

        String[] opciones = activos.toArray(new String[0]);
        String seleccionado = (String) JOptionPane.showInputDialog(
                ventana, "Selecciona un usuario:", "Nueva conversacion",
                JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (seleccionado != null && !seleccionado.isEmpty()) {
            abrirChat(seleccionado);
        }
    }

    private void limpiarAreaChat() {
        lblChatActivo.setText("Selecciona una conversacion");
        lblChatActivo.setForeground(C_GRIS);
        pnlMensajes.removeAll();
        pnlMensajes.revalidate();
        pnlMensajes.repaint();
    }

    private Color colorDeUsuario(String username) {
        return new Color(180, 180, 180);
    }

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

    private JButton crearBotonAzul(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_AZUL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BLANCO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonSimple(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? C_HOVER : C_BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(C_TEXTO);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
