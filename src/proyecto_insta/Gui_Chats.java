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
import java.util.List;
import javax.swing.*;
 
/**
 *
 * @author Rogelio
 */
public class Gui_Chats {
    private static final Color C_BORDE   = new Color(219, 219, 219);
    private static final Color C_FONDO   = new Color(250, 250, 250);
    private static final Color C_TEXTO   = new Color(38,  38,  38);
    private static final Color C_GRIS    = new Color(142, 142, 142);
    private static final Color C_AZUL    = new Color(0,   149, 246);
    private static final Color C_BLANCO  = Color.WHITE;
    private static final Color C_HOVER   = new Color(245, 245, 245);
    private static final Color C_BURBUJA_MIO  = new Color(0, 149, 246);
    private static final Color C_BURBUJA_OTRO = new Color(239, 239, 239);
 
    private static final int W         = 1366;
    private static final int H         = 768;
    private static final int TOPBAR_H  = 54;
    private static final int SIDEBAR_W = 244;
    private static final int LISTA_W   = 300;
 
    private final JFrame     ventana;
    private final CardLayout cardLayout;
    private final JPanel     pnlCards;
    private final String     usuarioActual;
 
    private JPanel      pnlMensajes;
    private JTextField  txtMensaje;
    private JLabel      lblChatActivo;
    private JPanel      pnlListaConvs;
    private String      chatAbierto = null;
 
    public Gui_Chats(JFrame ventana, CardLayout cardLayout, JPanel pnlCards, String usuarioActual) {
        this.ventana       = ventana;
        this.cardLayout    = cardLayout;
        this.pnlCards      = pnlCards;
        this.usuarioActual = usuarioActual;
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
 
        JPanel avatar = crearAvatar(56, new Color(180, 160, 220));
        avatar.setBounds(16, 20, 56, 56);
        side.add(avatar);
 
        JLabel lblNombre = new JLabel(usuarioActual);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setForeground(C_TEXTO);
        lblNombre.setBounds(84, 26, 144, 18);
        side.add(lblNombre);
 
        JLabel lblVer = new JLabel("Ver perfil");
        lblVer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblVer.setForeground(C_AZUL);
        lblVer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblVer.setBounds(84, 44, 80, 16);
        side.add(lblVer);
 
        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDE);
        sep.setBounds(16, 90, SIDEBAR_W - 32, 1);
        side.add(sep);
 
        String[] nombres = { "Inicio", "Buscar", "Crear", "Chats", "Perfil" };
        int[]    posY    = { 110, 162, 214, 266, 318 };
 
        for (int i = 0; i < nombres.length; i++) {
            final int idx    = i;
            final boolean activo = (idx == 3);
 
            JButton btn = new JButton() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (activo)                       { g2.setColor(new Color(240,240,255)); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); }
                    else if (getModel().isRollover()) { g2.setColor(C_HOVER);               g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); }
                    int cx = 28, cy = getHeight() / 2;
                    g2.setColor(activo ? new Color(88,81,219) : C_TEXTO);
                    g2.setStroke(new BasicStroke(activo ? 2.2f : 1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (idx) {
                        case 0: int[] hx={cx,cx+10,cx+10,cx-10,cx-10},hy={cy-10,cy,cy+10,cy+10,cy};
                                g2.drawPolygon(hx,hy,5); g2.drawRect(cx-4,cy+1,8,9); break;
                        case 1: g2.drawOval(cx-9,cy-10,18,18); g2.drawLine(cx+7,cy+6,cx+12,cy+11); break;
                        case 2: g2.drawRoundRect(cx-11,cy-11,22,22,6,6);
                                g2.drawLine(cx,cy-6,cx,cy+6); g2.drawLine(cx-6,cy,cx+6,cy); break;
                        case 3: g2.drawRoundRect(cx-11,cy-9,22,17,5,5); g2.drawLine(cx-5,cy+8,cx-8,cy+12); break;
                        case 4: g2.drawOval(cx-10,cy-10,20,20); g2.fillOval(cx-4,cy-6,8,8);
                                g2.drawArc(cx-8,cy+1,16,12,0,180); break;
                    }
                    g2.setFont(new Font("SansSerif", activo ? Font.BOLD : Font.PLAIN, 14));
                    g2.setColor(activo ? new Color(88,81,219) : C_TEXTO);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(nombres[idx], 52, cy + fm.getAscent()/2 - 1);
                    g2.dispose();
                }
            };
            btn.setBounds(8, posY[i], SIDEBAR_W - 16, 44);
            btn.setOpaque(false); btn.setContentAreaFilled(false);
            btn.setBorderPainted(false); btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (idx == 0) btn.addActionListener(e -> cardLayout.show(pnlCards, "home"));
            if (idx == 1) btn.addActionListener(e -> cardLayout.show(pnlCards, "buscar"));
            if (idx == 2) btn.addActionListener(e -> cardLayout.show(pnlCards, "crear"));
            side.add(btn);
        }
 
        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(C_GRIS);
        lblFooter.setBounds(16, H - TOPBAR_H - 28, 220, 14);
        side.add(lblFooter);
        return side;
    }
 
    private JScrollPane construirListaConversaciones() {
        pnlListaConvs = new JPanel();
        pnlListaConvs.setLayout(new BoxLayout(pnlListaConvs, BoxLayout.Y_AXIS));
        pnlListaConvs.setBackground(C_BLANCO);
 
        JLabel lblTitulo = new JLabel("  Mensajes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(C_TEXTO);
        lblTitulo.setMaximumSize(new Dimension(LISTA_W, 40));
        lblTitulo.setPreferredSize(new Dimension(LISTA_W, 40));
        pnlListaConvs.add(lblTitulo);
 
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
        // quitar items anteriores menos titulo y separador
        while (pnlListaConvs.getComponentCount() > 2) pnlListaConvs.remove(2);
 
        List<String> convs = Conversacion.getConversaciones(usuarioActual);
 
        if (convs.isEmpty()) {
            JLabel lblVacio = new JLabel("  Sin conversaciones");
            lblVacio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblVacio.setForeground(C_GRIS);
            lblVacio.setPreferredSize(new Dimension(LISTA_W, 40));
            lblVacio.setMaximumSize(new Dimension(LISTA_W, 40));
            pnlListaConvs.add(lblVacio);
        }
 
        for (String otro : convs) {
            Conversacion conv = new Conversacion(usuarioActual, otro);
            int noLeidos = conv.getMensajesNoLeidos();
 
            JPanel fila = new JPanel(null) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(C_BORDE);
                    g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                    g2.dispose();
                }
            };
            fila.setBackground(C_BLANCO);
            fila.setPreferredSize(new Dimension(LISTA_W, 60));
            fila.setMaximumSize(new Dimension(LISTA_W, 60));
            fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
 
            JPanel av = crearAvatar(38, colorDeUsuario(otro));
            av.setBounds(10, 11, 38, 38);
            fila.add(av);
 
            JLabel lblNombre = new JLabel("@" + otro);
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblNombre.setForeground(C_TEXTO);
            lblNombre.setBounds(58, 12, 180, 18);
            fila.add(lblNombre);
 
            if (noLeidos > 0) {
                JLabel badge = new JLabel(String.valueOf(noLeidos), SwingConstants.CENTER) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(C_AZUL);
                        g2.fillOval(0, 0, getWidth(), getHeight());
                        g2.setColor(C_BLANCO);
                        g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                        g2.dispose();
                    }
                };
                badge.setBounds(262, 20, 22, 22);
                fila.add(badge);
            }
 
            fila.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { fila.setBackground(C_HOVER); }
                @Override public void mouseExited(MouseEvent e)  { fila.setBackground(C_BLANCO); }
                @Override public void mouseClicked(MouseEvent e) { abrirChat(otro); }
            });
 
            // boton eliminar conversacion
            JButton btnX = new JButton("✕") {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(getModel().isRollover() ? new Color(237,73,86) : C_GRIS);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                    g2.dispose();
                }
            };
            btnX.setBounds(264, 4, 24, 24);
            btnX.setOpaque(false); btnX.setContentAreaFilled(false);
            btnX.setBorderPainted(false); btnX.setFocusPainted(false);
            btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnX.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(ventana,
                    "Eliminar conversacion con @" + otro + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    new Conversacion(usuarioActual, otro).eliminar();
                    if (otro.equals(chatAbierto)) { chatAbierto = null; limpiarAreaChat(); }
                    cargarListaConversaciones();
                    pnlListaConvs.revalidate();
                    pnlListaConvs.repaint();
                }
            });
            fila.add(btnX);
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
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
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
 
        JScrollPane scrollMsg = new JScrollPane(pnlMensajes);
        scrollMsg.setBorder(BorderFactory.createEmptyBorder());
        scrollMsg.getVerticalScrollBar().setUnitIncrement(14);
        scrollMsg.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scrollMsg.setBounds(0, 54, w, H - TOPBAR_H - 54 - 60);
        area.add(scrollMsg);
 
        // barra inferior para escribir
        JPanel barraEscribir = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(0, 0, getWidth(), 0);
            }
        };
        barraEscribir.setBackground(C_BLANCO);
        barraEscribir.setBounds(0, H - TOPBAR_H - 60, w, 60);
 
        txtMensaje = new JTextField("Escribe un mensaje...");
        txtMensaje.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtMensaje.setForeground(C_GRIS);
        txtMensaje.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDE, 1),
            BorderFactory.createEmptyBorder(0, 12, 0, 12)));
        txtMensaje.setBounds(14, 12, w - 160, 36);
        txtMensaje.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (txtMensaje.getText().equals("Escribe un mensaje...")) {
                    txtMensaje.setText(""); txtMensaje.setForeground(C_TEXTO);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (txtMensaje.getText().isEmpty()) {
                    txtMensaje.setText("Escribe un mensaje..."); txtMensaje.setForeground(C_GRIS);
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
        chatAbierto = otro;
        lblChatActivo.setText("@" + otro);
        lblChatActivo.setForeground(C_TEXTO);
 
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
        if (chatAbierto == null) return;
        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty() || texto.equals("Escribe un mensaje...")) return;
 
        Mensaje m = new MensajeTexto(usuarioActual, chatAbierto, texto);
        new Conversacion(usuarioActual, chatAbierto).enviar(m);
 
        txtMensaje.setText("");
        txtMensaje.setForeground(C_TEXTO);
        abrirChat(chatAbierto);
    }
 
    private void mostrarSelectorSticker() {
        if (chatAbierto == null) return;
        List<String> stickers = SistemaArchivos.getStickersUsuario(usuarioActual);
 
        JDialog dlg = new JDialog(ventana, "Stickers", true);
        dlg.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        dlg.setSize(320, 200);
        dlg.setLocationRelativeTo(ventana);
 
        for (String s : stickers) {
            JButton btn = new JButton(s.replace(".png",""));
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                Mensaje m = new MensajeSticker(usuarioActual, chatAbierto, s);
                new Conversacion(usuarioActual, chatAbierto).enviar(m);
                dlg.dispose();
                abrirChat(chatAbierto);
            });
            dlg.add(btn);
        }
 
        dlg.setVisible(true);
    }
 
    private JPanel crearBurbuja(Mensaje m, boolean esMio) {
        String texto = m.getTipo().equals("sticker")
            ? "[Sticker: " + m.getContenido().replace(".png","") + "]"
            : m.getContenido();
 
        JPanel contenedor = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        contenedor.setOpaque(false);
        contenedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
 
        JLabel burbuja = new JLabel("<html><div style='width:240px;padding:4px'>" + texto + "</div></html>") {
            @Override protected void paintComponent(Graphics g) {
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
 
    private void limpiarAreaChat() {
        lblChatActivo.setText("Selecciona una conversacion");
        lblChatActivo.setForeground(C_GRIS);
        pnlMensajes.removeAll();
        pnlMensajes.revalidate();
        pnlMensajes.repaint();
    }
 
    private Color colorDeUsuario(String username) {
        int hash = Math.abs(username.hashCode());
        Color[] colores = {
            new Color(70,130,180), new Color(180,80,80), new Color(80,160,80),
            new Color(180,140,50), new Color(130,80,180), new Color(80,160,160)
        };
        return colores[hash % colores.length];
    }
 
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
 
    private JButton crearBotonAzul(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_AZUL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BLANCO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
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
 
    private JButton crearBotonSimple(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? C_HOVER : C_BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.setColor(C_TEXTO);
                g2.setFont(getFont());
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
}
