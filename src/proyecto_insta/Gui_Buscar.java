package proyecto_insta;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import Logica.*;
import Absrtact.*;

public class Gui_Buscar {

    private static final Color C_BORDE      = new Color(219, 219, 219);
    private static final Color C_FONDO      = new Color(250, 250, 250);
    private static final Color C_TEXTO      = new Color(38,  38,  38);
    private static final Color C_GRIS       = new Color(142, 142, 142);
    private static final Color C_BLANCO     = Color.WHITE;
    private static final Color C_FONDO_CAMP = new Color(239, 239, 239);
    private static final Color C_HOVER      = new Color(245, 245, 245);

    private static final int W         = 1366;
    private static final int H         = 768;
    private static final int TOPBAR_H  = 54;
    private static final int SIDEBAR_W = 244;

    private final JFrame     ventana;
    private final CardLayout cardLayout;
    private final JPanel     pnlCards;
    private final String     usuarioActual;

    private JTextField txtBuscar;
    private JLabel     lblSinResultados;

    public Gui_Buscar(JFrame ventana, CardLayout cardLayout, JPanel pnlCards, String usuarioActual) {
        this.ventana       = ventana;
        this.cardLayout    = cardLayout;
        this.pnlCards      = pnlCards;
        this.usuarioActual = usuarioActual;
    }

    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(C_FONDO);
        panel.setBounds(0, 0, W, H);
        panel.add(construirTopBar());
        panel.add(construirSidebar());
        panel.add(construirAreaBusqueda());
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

        JPanel fotoPerfil = crearAvatar(56, new Color(180, 160, 220));
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
        side.add(lblVerPerfil);

        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDE);
        sep.setBounds(16, 90, SIDEBAR_W - 32, 1);
        side.add(sep);

        String[] opNombres = { "Inicio", "Buscar", "Crear", "Chats", "Perfil" };
        int[]    opY       = { 110, 162, 214, 266, 318 };

        for (int i = 0; i < opNombres.length; i++) {
            final int     idx    = i;
            final boolean activo = (idx == 1);

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
                            g2.drawRoundRect(cx-11,cy-9,22,17,5,5); g2.drawLine(cx-5,cy+8,cx-8,cy+12); break;
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
            if (idx == 0) btnNav.addActionListener(e -> cardLayout.show(pnlCards, "home"));
            if (idx == 2) btnNav.addActionListener(e -> cardLayout.show(pnlCards, "crear"));
            if (idx == 3) btnNav.addActionListener(e -> cardLayout.show(pnlCards, "chats"));
            side.add(btnNav);
        }

        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(C_GRIS);
        lblFooter.setBounds(16, H - TOPBAR_H - 28, 220, 14);
        side.add(lblFooter);

        return side;
    }

    private JPanel construirAreaBusqueda() {
        int areaX = SIDEBAR_W;
        int areaW = W - SIDEBAR_W;
        int areaH = H - TOPBAR_H;

        JPanel area = new JPanel(null);
        area.setBackground(C_FONDO);
        area.setBounds(areaX, TOPBAR_H, areaW, areaH);

        int campW = 400, campH = 40;
        int campX = (areaW - campW) / 2;

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBuscar.setForeground(C_TEXTO);
        txtBuscar.setBackground(C_FONDO_CAMP);
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12),
            BorderFactory.createEmptyBorder(0, 42, 0, 10)
        ));
        txtBuscar.setBounds(campX, 28, campW, campH);
        area.add(txtBuscar);

        JLabel lblLupa = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_GRIS);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(6, 9, 16, 16);
                g2.drawLine(20, 23, 26, 29);
                g2.dispose();
            }
        };
        lblLupa.setBounds(campX + 6, 28, 34, campH);
        area.add(lblLupa);
        area.setComponentZOrder(lblLupa, 0);

        JLabel lblPlaceholder = new JLabel("Buscar");
        lblPlaceholder.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblPlaceholder.setForeground(C_GRIS);
        lblPlaceholder.setBounds(campX + 42, 28, campW - 54, campH);
        area.add(lblPlaceholder);
        area.setComponentZOrder(lblPlaceholder, 0);

        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { lblPlaceholder.setVisible(false); }
            @Override public void focusLost(FocusEvent e)   { lblPlaceholder.setVisible(txtBuscar.getText().isEmpty()); }
        });

        JLabel lblRecientes = new JLabel("Recientes");
        lblRecientes.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblRecientes.setForeground(C_TEXTO);
        lblRecientes.setBounds(campX - 20, 86, 200, 26);
        area.add(lblRecientes);

        lblSinResultados = new JLabel("", SwingConstants.LEFT);
        lblSinResultados.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSinResultados.setForeground(C_GRIS);
        lblSinResultados.setVisible(false);
        lblSinResultados.setBounds(campX - 20, 86, campW + 40, 26);
        area.add(lblSinResultados);

        Color[]  recColors  = { new Color(70,130,180), new Color(210,180,140), new Color(0,102,51), new Color(180,30,30), new Color(180,160,220) };
        String[] recNombres = { "PC Components HN", "ZARA Honduras", "UNITEC Honduras", "Proceso Digital HN", usuarioActual };
        String[] recHandles = { "pc_components_hn", "zara_honduras", "unitec_honduras", "proceso_digital", "mi_cuenta" };

        int ry = 120;
        for (int i = 0; i < recNombres.length; i++) {
            JPanel fila = new JPanel(null);
            fila.setOpaque(false);
            fila.setBounds(campX - 20, ry, campW + 40, 48);
            area.add(fila);

            JPanel av = crearAvatar(38, recColors[i]);
            av.setBounds(0, 5, 38, 38);
            fila.add(av);

            JLabel lU = new JLabel(recNombres[i]);
            lU.setFont(new Font("SansSerif", Font.BOLD, 13));
            lU.setForeground(C_TEXTO);
            lU.setBounds(50, 8, 250, 16);
            fila.add(lU);

            JLabel lS = new JLabel("@" + recHandles[i] + " · Instagram");
            lS.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lS.setForeground(C_GRIS);
            lS.setBounds(50, 26, 280, 14);
            fila.add(lS);

            JLabel lblX = new JLabel("✕");
            lblX.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblX.setForeground(C_GRIS);
            lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lblX.setBounds(campW + 8, 14, 20, 20);
            lblX.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { fila.setVisible(false); }
            });
            fila.add(lblX);

            ry += 50;
        }

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            void actualizar() {
                String texto = txtBuscar.getText().trim();
                boolean vacio = texto.isEmpty();
                lblRecientes.setVisible(vacio);
                lblSinResultados.setVisible(!vacio);
                if (!vacio) lblSinResultados.setText("Buscar \"" + texto + "\"...");
            }
            @Override public void insertUpdate(DocumentEvent e)  { actualizar(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizar(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizar(); }
        });

        return area;
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

    private static class RoundedBorder implements Border {
        private final int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        @Override public Insets getBorderInsets(Component c) { return new Insets(2,2,2,2); }
        @Override public boolean isBorderOpaque() { return false; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(C_BORDE);
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
            g2.dispose();
        }
    }
}