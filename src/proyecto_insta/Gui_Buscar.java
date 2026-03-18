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
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import Logica.Nodo;
/**
 *
 * @author Rogelio
 */
public class Gui_Buscar {

    private static final Color BORDE = new Color(219, 219, 219);
    private static final Color FONDO = new Color(250, 250, 250);
    private static final Color TEXTO = new Color(38,  38,  38);
    private static final Color GRIS = new Color(142, 142, 142);
    private static final Color AZUL = new Color(0,   149, 246);
    private static final Color BLANCO = Color.WHITE;
    private static final Color FONDO_CAMP = new Color(239, 239, 239);
    private static final Color HOVER = new Color(245, 245, 245);

    private static final int W         = 1366;
    private static final int H         = 768;
    private static final int TOPBAR_H  = 54;
    private static final int SIDEBAR_W = 244;

    private final JFrame        ventana;
    private final Gui_Navegador nav;
    private final String        usuarioActual;

    private JTextField txtBuscar;
    private JPanel     pnlResultados;
    private JLabel     lblRecientes;
    private JLabel     lblSinResultados;

    public Gui_Buscar(JFrame ventana, Gui_Navegador nav, String usuarioActual) {
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
        panel.add(construirAreaBusqueda());
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

        String[] opNombres = { "Inicio", "Buscar", "Crear", "Chats", "Perfil" };
        int[]    opY       = { 110, 162, 214, 266, 318 };

        for (int i = 0; i < opNombres.length; i++) {
            final int     idx    = i;
            final boolean activo = (idx == 1);

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
                            int[] hx = {cx,cx+10,cx+10,cx-10,cx-10};
                            int[] hy = {cy-10,cy,cy+10,cy+10,cy};
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
            if (idx == 0) btnNav.addActionListener(e -> nav.ir("home"));
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
    private int contarMensajesNoLeidos() {
        int total = 0;
        for (String otro : Conversacion.getConversaciones(usuarioActual)) {
            total += new Conversacion(usuarioActual, otro).getMensajesNoLeidos();
        }
        return total;
    }
    private JPanel construirAreaBusqueda() {
        int areaX = SIDEBAR_W;
        int areaW = W - SIDEBAR_W;

        JPanel area = new JPanel(null);
        area.setBackground(FONDO);
        area.setBounds(areaX, TOPBAR_H, areaW, H - TOPBAR_H);

        int campW = 420, campH = 40;
        int campX = (areaW - campW) / 2;

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBuscar.setForeground(TEXTO);
        txtBuscar.setBackground(FONDO_CAMP);
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12),
            BorderFactory.createEmptyBorder(0, 42, 0, 10)
        ));
        txtBuscar.setBounds(campX, 28, campW, campH);
        area.add(txtBuscar);

        JLabel lblLupa = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(6, 9, 16, 16);
                g2.drawLine(20, 23, 26, 29);
                g2.dispose();
            }
        };
        lblLupa.setBounds(campX + 6, 28, 34, campH);
        area.add(lblLupa);
        area.setComponentZOrder(lblLupa, 0);

        JLabel lblPlaceholder = new JLabel("Buscar usuarios o #hashtags");
        lblPlaceholder.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblPlaceholder.setForeground(GRIS);
        lblPlaceholder.setBounds(campX + 42, 28, campW - 54, campH);
        area.add(lblPlaceholder);
        area.setComponentZOrder(lblPlaceholder, 0);

        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { lblPlaceholder.setVisible(false); }
            @Override public void focusLost(FocusEvent e)   { lblPlaceholder.setVisible(txtBuscar.getText().isEmpty()); }
        });

        lblSinResultados = new JLabel("");
        lblSinResultados.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSinResultados.setForeground(GRIS);
        lblSinResultados.setVisible(false);
        lblSinResultados.setBounds(campX, 86, campW, 26);
        area.add(lblSinResultados);

        pnlResultados = new JPanel();
        pnlResultados.setLayout(new BoxLayout(pnlResultados, BoxLayout.Y_AXIS));
        pnlResultados.setBackground(FONDO);

        JScrollPane scrollResultados = new JScrollPane(pnlResultados);
        scrollResultados.setBorder(BorderFactory.createEmptyBorder());
        scrollResultados.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scrollResultados.setBounds(campX - 10, 118, campW + 20, H - TOPBAR_H - 130);
        area.add(scrollResultados);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            void actualizar() {
                String texto = txtBuscar.getText().trim();
                if (texto.isEmpty()) {
                    lblSinResultados.setVisible(false);
                    pnlResultados.removeAll();
                    pnlResultados.revalidate();
                    pnlResultados.repaint();
                } else if (texto.startsWith("#")) {
                    lblSinResultados.setVisible(true);
                    lblSinResultados.setText("Resultados para " + texto);
                    buscarHashtag(texto, campW);
                }else if (texto.startsWith("@")) {
                    lblSinResultados.setVisible(true);
                    lblSinResultados.setText("Publicaciones donde mencionan " + texto);
                    buscarMenciones(texto, campW);
                }else {
                    lblSinResultados.setVisible(true);
                    lblSinResultados.setText("Resultados para \"" + texto + "\"");
                    buscarUsuarios(texto, campW);
                }
            }
            @Override public void insertUpdate(DocumentEvent e)  { actualizar(); }
            @Override public void removeUpdate(DocumentEvent e)  { actualizar(); }
            @Override public void changedUpdate(DocumentEvent e) { actualizar(); }
        });

        return area;
    }
    private void buscarMenciones(String tag, int campW) {
        pnlResultados.removeAll();
        pnlResultados.setLayout(new GridBuscar(FlowLayout.LEFT, 4, 4));

        ListaPublicaciones lista = new ListaPublicaciones();
        for (String username : GestorArchivos.getTodosLosUsuarios()) {
            Usuario u = Usuario.cargarDesdeArchivo(username);
            if (u == null || !u.esActivo()) continue;
            for (Publicacion p : GestorPublicaciones.getPublicacionesDeUsuario(username)) {
                lista.agregar(p);
            }
        }

        List<Publicacion> pubs = lista.buscarPorMencion(tag.replace("@", ""));

        if (pubs.isEmpty()) {
            pnlResultados.setLayout(new BoxLayout(pnlResultados, BoxLayout.Y_AXIS));
            JLabel lbl = new JLabel("Sin publicaciones que mencionen " + tag);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setForeground(GRIS);
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 0));
            pnlResultados.add(lbl);
        } else {
            for (Publicacion p : pubs) {
                pnlResultados.add(crearCeldaHashtag(p));
            }
        }
        pnlResultados.revalidate();
        pnlResultados.repaint();
        for (Publicacion p : pubs) {
    System.out.println("MENCION encontrada: " + p.getId() + " | menciones: " + p.getMenciones());
}
    }

    
    private void buscarUsuarios(String texto, int campW) {
        pnlResultados.removeAll();
        List<String> resultados = GestorUsuarios.buscarPorCoincidencia(texto);
        resultados.remove(usuarioActual.toLowerCase());
        if (resultados.isEmpty()) {
            JLabel lbl = new JLabel("Sin resultados para \"" + texto + "\"");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setForeground(GRIS);
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 0));
            pnlResultados.add(lbl);
        }
        for (String u : resultados) {
            Usuario usr = Usuario.cargarDesdeArchivo(u);
            if (usr != null) pnlResultados.add(crearFila(usr, campW));
        }
        pnlResultados.revalidate();
        pnlResultados.repaint();
    }

    private void buscarHashtag(String tag, int campW) {
        pnlResultados.removeAll();
        pnlResultados.setLayout(new GridBuscar(FlowLayout.LEFT, 4, 4));

        ListaPublicaciones lista = new ListaPublicaciones();
        for (String username : GestorArchivos.getTodosLosUsuarios()) {
            Usuario u = Usuario.cargarDesdeArchivo(username);
            if (u == null || !u.esActivo()) continue;
            for (Publicacion p : GestorPublicaciones.getPublicacionesDeUsuario(username)) {
                lista.agregar(p);
            }
        }

        List<Publicacion> pubs = lista.buscarPorHashtag(tag);

        if (pubs.isEmpty()) {
            pnlResultados.setLayout(new BoxLayout(pnlResultados, BoxLayout.Y_AXIS));
            JLabel lbl = new JLabel("Sin publicaciones con " + tag);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setForeground(GRIS);
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 4, 0, 0));
            pnlResultados.add(lbl);
        } else {
            for (Publicacion p : pubs) {
                pnlResultados.add(crearCeldaHashtag(p));
            }
        }
        pnlResultados.revalidate();
        pnlResultados.repaint();
    }
    
    private JPanel crearCeldaHashtag(Publicacion p) {
        int size = 180;

        BufferedImage[] imgCache = {null};
        String ruta = p.getRutaImagen();
        if (ruta != null && !ruta.isEmpty()) {
            try {
                imgCache[0] = javax.imageio.ImageIO.read(new File(ruta).getAbsoluteFile());
            } catch (Exception ex) { }
        }

        JPanel celda = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (imgCache[0] != null) {
                    g.drawImage(imgCache[0], 0, 0, size, size, this);
                } else {
                    g.setColor(colorDeUsuario(p.getAutor()).darker());
                    g.fillRect(0, 0, size, size);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int cx = size / 2, cy = size / 2;
                    g2.setColor(new Color(255, 255, 255, 80));
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawRoundRect(cx - 16, cy - 10, 32, 22, 6, 6);
                    g2.drawOval(cx - 7, cy - 7, 14, 14);
                    g2.dispose();
                }
            }
        };
        celda.setPreferredSize(new Dimension(size, size));
        celda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        celda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirPerfilConPublicacion(p);
            }
        });
        return celda;
    }
    
    private JPanel crearFila(Object item, int campW) {
        if (item instanceof Usuario) return crearFilaUsuario((Usuario) item, campW);
        if (item instanceof Publicacion) return crearFilaPublicacion((Publicacion) item, campW);
        return new JPanel();
    }
    
    private JPanel crearFilaPublicacion(Publicacion p, int campW) {
        JPanel fila = new JPanel(null);
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(campW + 20, 60));
        fila.setPreferredSize(new Dimension(campW + 20, 60));

        Usuario uAutor = Usuario.cargarDesdeArchivo(p.getAutor());
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
                g2.setColor(colorDeUsuario(p.getAutor()));
                g2.fillOval(0, 0, 38, 38);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setBounds(4, 11, 38, 38);
        fila.add(av);

        JLabel lAutor = new JLabel("@" + p.getAutor());
        lAutor.setFont(new Font("SansSerif", Font.BOLD, 12));
        lAutor.setForeground(TEXTO);
        lAutor.setBounds(52, 10, 200, 15);
        fila.add(lAutor);

        String preview = p.getContenido().length() > 60
            ? p.getContenido().substring(0, 60) + "..." : p.getContenido();
        JLabel lContenido = new JLabel(preview);
        lContenido.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lContenido.setForeground(GRIS);
        lContenido.setBounds(52, 27, campW - 60, 14);
        fila.add(lContenido);

        JLabel lFecha = new JLabel(p.getFecha() + " " + p.getHora());
        lFecha.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lFecha.setForeground(GRIS);
        lFecha.setBounds(52, 43, 200, 12);
        fila.add(lFecha);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        sep.setBounds(0, 58, campW + 20, 1);
        fila.add(sep);

        return fila;
    }
    
    private void abrirPerfilConPublicacion(Publicacion p) {
        String key = "perfil_" + p.getAutor();
        Gui_Perfil guiPerfil = new Gui_Perfil(ventana, nav, usuarioActual, p.getAutor());
        nav.getPnlCards().add(guiPerfil.construirPantalla(), key);
        nav.ir(key);
        SwingUtilities.invokeLater(() -> guiPerfil.abrirPublicacion(p));
    }

    private JPanel crearFilaUsuario(Usuario u, int campW) {
        JPanel fila = new JPanel(null);
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(campW + 20, 56));
        fila.setPreferredSize(new Dimension(campW + 20, 56));
        fila.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Avatar con foto de perfil
        String rutaU = u.getRutaFoto();
        JPanel av = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (rutaU != null && !rutaU.isEmpty()) {
                    try {
                        BufferedImage img = javax.imageio.ImageIO.read(new File(rutaU).getAbsoluteFile());
                        if (img != null) {
                            g2.setClip(new Ellipse2D.Float(0, 0, 38, 38));
                            g2.drawImage(img, 0, 0, 38, 38, this);
                            g2.dispose();
                            return;
                        }
                    } catch (Exception ex) { }
                }
                g2.setColor(colorDeUsuario(u.getUsername()));
                g2.fillOval(0, 0, 38, 38);
                g2.dispose();
            }
        };
        av.setOpaque(false);
        av.setBounds(4, 9, 38, 38);
        fila.add(av);

        JLabel lNombre = new JLabel(u.getNombre() != null ? u.getNombre() : u.getUsername());
        lNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lNombre.setForeground(TEXTO);
        lNombre.setBounds(52, 10, 200, 16);
        fila.add(lNombre);

        JLabel lHandle = new JLabel("@" + u.getUsername() + "  ·  " + u.getTipoCuenta());
        lHandle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lHandle.setForeground(GRIS);
        lHandle.setBounds(52, 28, 250, 14);
        fila.add(lHandle);

        Usuario yo = Usuario.cargarDesdeArchivo(usuarioActual);
        boolean sigue = yo != null && yo.sigueA(u.getUsername());
        JLabel btnSeg = new JLabel(sigue ? "Siguiendo" : "Seguir");
        btnSeg.setFont(new Font("SansSerif", sigue ? Font.PLAIN : Font.BOLD, 12));
        btnSeg.setForeground(sigue ? GRIS : AZUL);
        btnSeg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeg.setBounds(campW - 120, 20, 70, 16);
        btnSeg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume();
                boolean siguiendo = btnSeg.getText().equals("Siguiendo");
                if (siguiendo) {
                    GestorUsuarios.dejarDeSeguir(usuarioActual, u.getUsername());
                    btnSeg.setText("Seguir");
                    btnSeg.setFont(new Font("SansSerif", Font.BOLD, 12));
                    btnSeg.setForeground(AZUL);
                } else {
                    GestorUsuarios.seguir(usuarioActual, u.getUsername());
                    btnSeg.setText("Siguiendo");
                    btnSeg.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    btnSeg.setForeground(GRIS);
                }
            }
        });
        fila.add(btnSeg);

        JLabel btnVerPerfil = new JLabel("Ver perfil");
        btnVerPerfil.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVerPerfil.setForeground(TEXTO);
        btnVerPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVerPerfil.setBounds(campW - 40, 20, 60, 16);
        btnVerPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume();
                abrirPerfil(u.getUsername());
            }
        });
        fila.add(btnVerPerfil);

        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { abrirPerfil(u.getUsername()); }
        });

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDE);
        sep.setBounds(0, 54, campW + 20, 1);
        fila.add(sep);

        return fila;
    }

    private void abrirPerfil(String username) {
        String key = "perfil_" + username;
        Gui_Perfil guiPerfil = new Gui_Perfil(ventana, nav, usuarioActual, username);
        nav.getPnlCards().add(guiPerfil.construirPantalla(), key);
        nav.ir(key);
    }

    private Color colorDeUsuario(String username) { return new Color(180, 180, 180); }


    private static class RoundedBorder implements Border {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BORDE);
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
            g2.dispose();
        }
    }
    
    private static class GridBuscar extends FlowLayout {
    GridBuscar(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }

    private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0, rowHeight = 0;
                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component m = target.getComponent(i);
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            dim.width = Math.max(dim.width, rowWidth);
                            dim.height += rowHeight + vgap;
                            rowWidth = 0;
                            rowHeight = 0;
                        }
                        rowWidth += d.width + hgap;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                dim.width = Math.max(dim.width, rowWidth);
                dim.height += rowHeight + insets.top + insets.bottom + vgap * 2;
                return dim;
            }
        }
    }
}