package proyecto_insta;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class Gui_Buscar {

    // ── Colores ───────────────────────────────────────────────────────────────
    private static final Color C_BORDE      = new Color(219, 219, 219);
    private static final Color C_FONDO      = new Color(250, 250, 250);
    private static final Color C_TEXTO      = new Color(38,  38,  38);
    private static final Color C_GRIS       = new Color(142, 142, 142);
    private static final Color C_BLANCO     = Color.WHITE;
    private static final Color C_FONDO_CAMP = new Color(239, 239, 239);
    private static final Color C_HOVER      = new Color(245, 245, 245);

    // ── Dimensiones ───────────────────────────────────────────────────────────
    private static final int W        = 1366;
    private static final int H        = 768;
    private static final int TOPBAR_H = 54;
    private static final int SIDEBAR_W = 244;

    // ── Referencias ───────────────────────────────────────────────────────────
    private final JFrame     ventana;
    private final CardLayout cardLayout;
    private final JPanel     pnlCards;
    private final String     usuarioActual;

    // ── Componentes internos ──────────────────────────────────────────────────
    private JTextField  txtBuscar;
    private JPanel      pnlResultados;
    private JLabel      lblSinResultados;

    public Gui_Buscar(JFrame ventana, CardLayout cardLayout, JPanel pnlCards, String usuarioActual) {
        this.ventana       = ventana;
        this.cardLayout    = cardLayout;
        this.pnlCards      = pnlCards;
        this.usuarioActual = usuarioActual;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Panel principal
    // ══════════════════════════════════════════════════════════════════════════
    public JPanel construirPantalla() {
        JPanel panel = new JPanel(null);
        panel.setBackground(C_FONDO);
        panel.setBounds(0, 0, W, H);

        panel.add(construirTopBar());
        panel.add(construirSidebarBuscar());
        panel.add(construirAreaBusqueda());

        return panel;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  TOP BAR (igual que Home pero sin buscador central)
    // ══════════════════════════════════════════════════════════════════════════
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

    // ══════════════════════════════════════════════════════════════════════════
    //  SIDEBAR con navegacion (igual que Home)
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel construirSidebarBuscar() {
        JPanel side = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(C_BORDE);
                g.drawLine(SIDEBAR_W - 1, 0, SIDEBAR_W - 1, H);
            }
        };
        side.setBackground(C_BLANCO);
        side.setBounds(0, TOPBAR_H, SIDEBAR_W, H - TOPBAR_H);

        // Perfil del usuario
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

        // Botones de navegacion
        String[] opNombres = { "Inicio", "Buscar", "Crear", "Chats", "Perfil" };
        int[] opY = { 110, 162, 214, 266, 318 };

        for (int i = 0; i < opNombres.length; i++) {
            final int idx = i;
            // "Buscar" resaltado como activo
            final boolean activo = (idx == 1);

            JButton btnNav = new JButton() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (activo) { g2.setColor(new Color(240, 240, 255)); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); }
                    else if (getModel().isRollover()) { g2.setColor(C_HOVER); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); }
                    int cx = 28, cy = getHeight()/2;
                    g2.setColor(activo ? new Color(88, 81, 219) : C_TEXTO);
                    g2.setStroke(new BasicStroke(activo ? 2.2f : 1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (idx) {
                        case 0: // Inicio
                            int[] hx={cx,cx+10,cx+10,cx-10,cx-10}, hy={cy-10,cy,cy+10,cy+10,cy};
                            g2.drawPolygon(hx,hy,5); g2.drawRect(cx-4,cy+1,8,9); break;
                        case 1: // Buscar
                            g2.drawOval(cx-9,cy-10,18,18); g2.drawLine(cx+7,cy+6,cx+12,cy+11); break;
                        case 2: // Crear
                            g2.drawRoundRect(cx-11,cy-11,22,22,6,6);
                            g2.drawLine(cx,cy-6,cx,cy+6); g2.drawLine(cx-6,cy,cx+6,cy); break;
                        case 3: // Chats
                            g2.drawRoundRect(cx-11,cy-9,22,17,5,5); g2.drawLine(cx-5,cy+8,cx-8,cy+12); break;
                        case 4: // Perfil
                            g2.drawOval(cx-10,cy-10,20,20); g2.fillOval(cx-4,cy-6,8,8);
                            g2.drawArc(cx-8,cy+1,16,12,0,180); break;
                    }
                    g2.setFont(new Font("SansSerif", activo ? Font.BOLD : Font.PLAIN, 14));
                    g2.setColor(activo ? new Color(88, 81, 219) : C_TEXTO);
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
            side.add(btnNav);
        }

        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(C_GRIS);
        lblFooter.setBounds(16, H - TOPBAR_H - 28, 220, 14);
        side.add(lblFooter);

        return side;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  AREA PRINCIPAL DE BUSQUEDA
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel construirAreaBusqueda() {
        int areaX = SIDEBAR_W;
        int areaW = W - SIDEBAR_W;
        int areaH = H - TOPBAR_H;

        JPanel area = new JPanel(null);
        area.setBackground(C_FONDO);
        area.setBounds(areaX, TOPBAR_H, areaW, areaH);

        // ── Campo de búsqueda ──────────────────────────────────────────────
        int campW = 400, campH = 40;
        int campX = (areaW - campW) / 2;

        JPanel campoCont = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_FONDO_CAMP);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                // Icono lupa
                g2.setColor(C_GRIS);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(14, 11, 16, 16);
                g2.drawLine(28, 25, 34, 31);
                g2.dispose();
            }
        };
        campoCont.setBounds(campX, 28, campW, campH);
        area.add(campoCont);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBuscar.setForeground(C_TEXTO);
        txtBuscar.setBackground(new Color(0,0,0,0));
        txtBuscar.setOpaque(false);
        txtBuscar.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 12));
        txtBuscar.setBounds(campX + 42, 28, campW - 54, campH);
        area.add(txtBuscar);

        // Placeholder manual
        JLabel lblPlaceholder = new JLabel("Buscar");
        lblPlaceholder.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblPlaceholder.setForeground(C_GRIS);
        lblPlaceholder.setBounds(campX + 44, 28, campW - 54, campH);
        area.add(lblPlaceholder);

        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { lblPlaceholder.setVisible(false); }
            @Override public void focusLost(FocusEvent e)   { lblPlaceholder.setVisible(txtBuscar.getText().isEmpty()); }
        });

        // ── Panel de resultados (scroll) ───────────────────────────────────
        pnlResultados = new JPanel();
        pnlResultados.setLayout(new BoxLayout(pnlResultados, BoxLayout.Y_AXIS));
        pnlResultados.setBackground(C_FONDO);
        pnlResultados.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JScrollPane scroll = new JScrollPane(pnlResultados);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scroll.setBounds(campX - 20, 82, campW + 40, areaH - 100);
        scroll.setBackground(C_FONDO);
        scroll.getViewport().setBackground(C_FONDO);
        area.add(scroll);

        // Mensaje sin resultados
        lblSinResultados = new JLabel("No se encontraron resultados.", SwingConstants.CENTER);
        lblSinResultados.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSinResultados.setForeground(C_GRIS);
        lblSinResultados.setVisible(false);
        lblSinResultados.setBounds(campX - 20, 82, campW + 40, 40);
        area.add(lblSinResultados);

        // Titulo "Recientes" (visible cuando no hay texto en buscador)
        JLabel lblRecientes = new JLabel("Recientes");
        lblRecientes.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblRecientes.setForeground(C_TEXTO);
        lblRecientes.setBounds(campX - 20, 82, 200, 26);
        area.add(lblRecientes);

        // Cuentas recientes de ejemplo (placeholder)
        Color[] recColors = {
            new Color(255,180,150), new Color(150,200,255), new Color(180,255,180),
            new Color(255,220,150), new Color(220,170,255)
        };
        int ry = 116;
        for (int i = 0; i < 5; i++) {
            final int fi = i;
            JPanel fila = new JPanel(null);
            fila.setOpaque(false);
            fila.setBounds(campX - 20, ry, campW + 40, 48);
            area.add(fila);

            JPanel av = crearAvatar(38, recColors[i]);
            av.setBounds(0, 5, 38, 38);
            fila.add(av);

            JLabel lU = new JLabel("cuenta" + (i + 1));
            lU.setFont(new Font("SansSerif", Font.BOLD, 13));
            lU.setForeground(C_TEXTO);
            lU.setBounds(50, 8, 200, 16);
            fila.add(lU);

            JLabel lS = new JLabel("cuenta" + (i + 1) + " · Instagram");
            lS.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lS.setForeground(C_GRIS);
            lS.setBounds(50, 26, 250, 14);
            fila.add(lS);

            // X para eliminar reciente
            JLabel lblX = new JLabel("✕");
            lblX.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblX.setForeground(C_GRIS);
            lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lblX.setBounds(campW + 10, 14, 20, 20);
            lblX.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    fila.setVisible(false);
                }
            });
            fila.add(lblX);

            ry += 50;
        }

        // ── Listener del campo de busqueda ─────────────────────────────────
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            void actualizar() {
                String texto = txtBuscar.getText().trim();
                lblRecientes.setVisible(texto.isEmpty());
                // Aqui se conectara con la logica de busqueda real
                // Por ahora muestra/oculta el mensaje de sin resultados
                boolean hayTexto = !texto.isEmpty();
                lblSinResultados.setVisible(hayTexto);
                lblSinResultados.setText("Buscar \"" + texto + "\"...");
            }
            @Override public void insertUpdate(DocumentEvent e)  { actualizar(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizar(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizar(); }
        });

        return area;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Helper – avatar circular
    // ══════════════════════════════════════════════════════════════════════════
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