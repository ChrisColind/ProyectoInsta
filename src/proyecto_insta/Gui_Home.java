package proyecto_insta;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import Logica.*;
import Absrtact.*;
import javax.swing.*;

public class Gui_Home {
    
    private static final Color C_BORDE  = new Color(219, 219, 219);
    private static final Color C_FONDO  = new Color(250, 250, 250);
    private static final Color C_TEXTO  = new Color(38,  38,  38);
    private static final Color C_GRIS   = new Color(142, 142, 142);
    private static final Color C_AZUL   = new Color(0,   149, 246);
    private static final Color C_BLANCO = Color.WHITE;
    private static final Color C_HOVER  = new Color(245, 245, 245);
    private static final Color C_ROJO   = new Color(237, 73,  86);
    private static final int W         = 1366;
    private static final int H         = 768;
    private static final int TOPBAR_H  = 54;
    private static final int SIDEBAR_W = 244;
    private static final int RIGHT_W   = 300;
    private static final int FEED_W    = W - SIDEBAR_W - RIGHT_W; 
    private final JFrame     ventana;
    private final CardLayout cardLayout;
    private final JPanel     pnlCards;
    private final String     usuarioActual;
    public Gui_Home(JFrame ventana, CardLayout cardLayout, JPanel pnlCards, String usuarioActual) {
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
        panel.add(construirFeed());
        panel.add(construirPanelDerecho());
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
                g.drawLine(SIDEBAR_W-1, 0, SIDEBAR_W-1, H);
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
        lblVerPerfil.setForeground(C_AZUL);
        lblVerPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblVerPerfil.setBounds(84, 44, 80, 16);
        side.add(lblVerPerfil);
        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDE);
        sep.setBounds(16, 90, SIDEBAR_W - 32, 1);
        side.add(sep);
        String[] opNombres = { "Inicio", "Buscar", "Crear", "Chats", "Perfil" };
        int[] opY = { 110, 162, 214, 266, 318 };
        for (int i = 0; i < opNombres.length; i++) {
            final int idx = i;
            final boolean activo = (idx == 0); 
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
            if (idx == 1) btnNav.addActionListener(e -> cardLayout.show(pnlCards, "buscar"));
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
    private JScrollPane construirFeed() {
        JPanel feed = new JPanel();
        feed.setLayout(new BoxLayout(feed, BoxLayout.Y_AXIS));
        feed.setBackground(C_FONDO);
        feed.setBorder(BorderFactory.createEmptyBorder(16, 60, 16, 60));
        int postAncho = 500;
        Object[][] posts = {
            {"pc_components_hn", "PC Components HN",  new Color(70,  130, 180), 1, "15/01/2025 09:32", "Nueva llegada: RTX 4070 Super disponible ahora. #hardware #gaming"},
            {"zara_honduras",    "ZARA Honduras",      new Color(210, 180, 140), 0, "15/01/2025 11:15", "Nueva coleccion primavera 2025 ya disponible en tiendas. #moda #zara"},
            {"unitec_honduras",  "UNITEC Honduras",    new Color(0,   102,  51), 2, "15/01/2025 08:00", "Inscripciones abiertas! Periodo academico 2025. #UNITEC #Honduras"},
            {"proceso_digital",  "Proceso Digital HN", new Color(180,  30,  30), 0, "15/01/2025 12:45", "Ultimas noticias de Honduras. Mantente informado. #Honduras #noticias"},
        };
        Color[] avatarColors = {
            new Color(70,130,180), new Color(210,180,140),
            new Color(0,102,51),   new Color(180,30,30)
        };
        for (int i = 0; i < posts.length; i++) {
            feed.add(crearPost(posts[i], avatarColors[i], postAncho));
            feed.add(Box.createVerticalStrut(12));
        }
        JScrollPane scroll = new JScrollPane(feed);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scroll.setBounds(SIDEBAR_W, TOPBAR_H, FEED_W, H - TOPBAR_H);
        scroll.setBackground(C_FONDO);
        scroll.getViewport().setBackground(C_FONDO);
        return scroll;
    }
    private JPanel crearPost(Object[] data, Color avatarColor, int postAncho) {
        String usuario     = (String) data[0];
        String displayName = (String) data[1];
        Color  imgColor    = (Color)  data[2];
        int    prop        = (int)    data[3];
        String fechaHora   = (String) data[4];
        String caption     = (String) data[5];
        int imgH;
        switch (prop) {
            case 1:  imgH = (int)(postAncho * 400.0 / 600); break;
            case 2:  imgH = (int)(postAncho * 750.0 / 600); break;
            default: imgH = postAncho;
        }
        int headerH  = 60;  
        int accionH  = 44;  
        int captionH = 52;  
        int totalH   = headerH + imgH + accionH + captionH;
        JPanel post = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_BLANCO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(C_BORDE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 4, 4);
                g2.dispose();
            }
        };
        post.setOpaque(false);
        post.setPreferredSize(new Dimension(postAncho, totalH));
        post.setMaximumSize(new Dimension(postAncho, totalH));
        post.setMinimumSize(new Dimension(postAncho, totalH));
        post.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel av = crearAvatar(38, avatarColor);
        av.setBounds(12, 11, 38, 38);
        post.add(av);
        JLabel lblNombre = new JLabel(displayName);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNombre.setForeground(C_TEXTO);
        lblNombre.setBounds(58, 12, 200, 17);
        post.add(lblNombre);
        JLabel lblHandle = new JLabel("@" + usuario);
        lblHandle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblHandle.setForeground(C_GRIS);
        lblHandle.setBounds(58, 30, 180, 14);
        post.add(lblHandle);
        JLabel lblFechaHora = new JLabel(fechaHora);
        lblFechaHora.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblFechaHora.setForeground(C_GRIS);
        lblFechaHora.setBounds(postAncho - 140, 22, 128, 14);
        post.add(lblFechaHora);
        JPanel imgPanel = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(imgColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2;
                g2.setColor(new Color(255,255,255,100));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(cx-16, cy-10, 32, 22, 6, 6);
                g2.drawOval(cx-7, cy-7, 14, 14);
                g2.dispose();
            }
        };
        imgPanel.setBounds(0, headerH, postAncho, imgH);
        post.add(imgPanel);
        int ay = headerH + imgH + 8;
        boolean[] likedState = {false};
        JButton btnLike = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2;
                GeneralPath heart = new GeneralPath();
                heart.moveTo(cx,cy+9); heart.curveTo(cx-12,cy+2,cx-13,cy-7,cx-6,cy-9);
                heart.curveTo(cx-2,cy-11,cx,cy-8,cx,cy-8);
                heart.curveTo(cx,cy-8,cx+2,cy-11,cx+6,cy-9);
                heart.curveTo(cx+13,cy-7,cx+12,cy+2,cx,cy+9);
                heart.closePath();
                if (likedState[0]) {
                    g2.setColor(C_ROJO); g2.fill(heart);
                } else {
                    g2.setColor(getModel().isRollover() ? C_ROJO : C_TEXTO);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.draw(heart);
                }
                g2.dispose();
            }
        };
        btnLike.setBounds(12, ay, 28, 28);
        btnLike.setOpaque(false); btnLike.setContentAreaFilled(false);
        btnLike.setBorderPainted(false); btnLike.setFocusPainted(false);
        btnLike.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLike.addActionListener(e -> { likedState[0] = !likedState[0]; btnLike.repaint(); });
        post.add(btnLike);
        JButton btnCom = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2;
                g2.setColor(getModel().isRollover() ? C_GRIS : C_TEXTO);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(cx-10, cy-9, 20, 16, 5, 5);
                g2.drawLine(cx-5, cy+7, cx-8, cy+11);
                g2.dispose();
            }
        };
        btnCom.setBounds(46, ay, 28, 28);
        btnCom.setOpaque(false); btnCom.setContentAreaFilled(false);
        btnCom.setBorderPainted(false); btnCom.setFocusPainted(false);
        btnCom.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        post.add(btnCom);
        int capY = ay + 30;
        String htmlCaption = "<html><div style='width:" + (postAncho - 28)
            + "px; font-family:SansSerif; font-size:10pt'>"
            + "<b>" + displayName + "</b> " + caption
            + "</div></html>";
        JLabel lblCaption = new JLabel(htmlCaption);
        lblCaption.setBounds(12, capY, postAncho - 24, captionH);
        post.add(lblCaption);
        return post;
    }
    private JPanel construirPanelDerecho() {
        JPanel right = new JPanel(null);
        right.setBackground(C_FONDO);
        right.setBounds(W - RIGHT_W, TOPBAR_H, RIGHT_W, H - TOPBAR_H);
        JLabel lblSug = new JLabel("Sugerencias para ti");
        lblSug.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSug.setForeground(C_TEXTO);
        lblSug.setBounds(12, 18, 180, 18);
        right.add(lblSug);
        JLabel lblVerTodo = new JLabel("Ver todo");
        lblVerTodo.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblVerTodo.setForeground(C_AZUL);
        lblVerTodo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblVerTodo.setBounds(RIGHT_W - 66, 18, 54, 18);
        right.add(lblVerTodo);
        Color[] sugColors = {
            new Color(70,130,180), new Color(210,180,140),
            new Color(0,102,51),   new Color(180,30,30)
        };
        String[] sugNames = {
            "PC Components HN",
            "ZARA Honduras",
            "UNITEC Honduras",
            "Proceso Digital HN"
        };
        String[] sugSub = {
            "pc_components_hn",
            "zara_honduras",
            "unitec_honduras",
            "proceso_digital"
        };
        int sugY = 46;
        for (int i = 0; i < 4; i++) {
            JPanel avSug = crearAvatar(36, sugColors[i]);
            avSug.setBounds(12, sugY, 36, 36);
            right.add(avSug);
            JLabel lUser = new JLabel(sugNames[i]);
            lUser.setFont(new Font("SansSerif", Font.BOLD, 12));
            lUser.setForeground(C_TEXTO);
            lUser.setBounds(56, sugY + 4, 140, 15);
            right.add(lUser);
            JLabel lSub = new JLabel("@" + sugSub[i]);
            lSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lSub.setForeground(C_GRIS);
            lSub.setBounds(56, sugY + 20, 140, 13);
            right.add(lSub);
            final JLabel btnSeg = new JLabel("Seguir");
            btnSeg.setFont(new Font("SansSerif", Font.BOLD, 12));
            btnSeg.setForeground(C_AZUL);
            btnSeg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnSeg.setBounds(RIGHT_W - 66, sugY + 10, 54, 16);
            btnSeg.addMouseListener(new MouseAdapter() {
                boolean sig = false;
                @Override public void mouseClicked(MouseEvent e) {
                    sig = !sig;
                    btnSeg.setText(sig ? "Siguiendo" : "Seguir");
                    btnSeg.setFont(new Font("SansSerif", sig ? Font.PLAIN : Font.BOLD, 12));
                    btnSeg.setForeground(sig ? C_GRIS : C_AZUL);
                }
            });
            right.add(btnSeg);
            sugY += 46;
        }
        JLabel lblFooter = new JLabel("© 2025 Instagram from Meta");
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFooter.setForeground(C_GRIS);
        lblFooter.setBounds(12, H - TOPBAR_H - 28, 220, 14);
        right.add(lblFooter);
        return right;
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
}