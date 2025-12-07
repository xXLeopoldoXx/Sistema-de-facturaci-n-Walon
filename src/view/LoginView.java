package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

public class LoginView extends javax.swing.JFrame {

    public LoginView() {
        initComponents();
        aplicarEstilosModernos();
        controller.LoginController c = new controller.LoginController(this);
        this.setLocationRelativeTo(null); // centro de pantalla
    }
    
    private void aplicarEstilosModernos() {
        // Labels dentro del formulario blanco (color oscuro)
        lblUsuario.setForeground(new Color(60, 60, 60));
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setOpaque(false);
        
        lblPass.setForeground(new Color(60, 60, 60));
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPass.setOpaque(false);
        
        // Campos de texto estilo oscuro
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        txtUser.setBackground(new Color(60, 60, 60));
        txtUser.setForeground(Color.WHITE);
        txtUser.setCaretColor(Color.WHITE);
        txtUser.setPreferredSize(new java.awt.Dimension(390, 45));
        
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 15, 12, 15));
        txtPass.setBackground(new Color(60, 60, 60));
        txtPass.setForeground(Color.WHITE);
        txtPass.setCaretColor(Color.WHITE);
        txtPass.setPreferredSize(new java.awt.Dimension(390, 45));
        
        // Botón Ingresar azul profesional con bordes redondeados (ya tiene paintComponent personalizado)
        btnIngresar.setBackground(new Color(52, 58, 64)); // Gris oscuro profesional
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setOpaque(true);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new java.awt.Dimension(390, 48));
        btnIngresar.setText("Iniciar sesión");
        
        // Efecto hover para botón ingresar
        btnIngresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIngresar.setBackground(new Color(73, 80, 87));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIngresar.setBackground(new Color(52, 58, 64));
            }
        });
        
        // Ocultar botón salir (no se usa en este diseño)
        btnSalir.setVisible(false);
        
        // Ocultar título viejo (se usa el banner ahora)
        if (lblTitulo != null) {
            lblTitulo.setVisible(false);
        }
        
        // Forzar actualización
        panelLogin.revalidate();
        panelLogin.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        // Panel principal con fondo con formas orgánicas púrpura/lavanda
        panelLogin = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Color de fondo base (gris muy claro)
                g2d.setColor(new Color(248, 249, 250));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Formas orgánicas en tonos grises y azules profesionales
                // Forma grande superior izquierda
                Ellipse2D.Double forma1 = new Ellipse2D.Double(-100, -50, 400, 350);
                GradientPaint grad1 = new GradientPaint(0, 0, new Color(108, 117, 125, 100), 
                                                        getWidth(), 0, new Color(73, 80, 87, 120));
                g2d.setPaint(grad1);
                g2d.fill(forma1);
                
                // Forma mediana central derecha
                Ellipse2D.Double forma2 = new Ellipse2D.Double(getWidth() - 300, 100, 450, 400);
                GradientPaint grad2 = new GradientPaint(0, 0, new Color(52, 58, 64, 80), 
                                                        getWidth(), 0, new Color(33, 37, 41, 100));
                g2d.setPaint(grad2);
                g2d.fill(forma2);
                
                // Forma pequeña inferior
                Ellipse2D.Double forma3 = new Ellipse2D.Double(getWidth() - 200, getHeight() - 150, 350, 300);
                g2d.setColor(new Color(73, 80, 87, 80));
                g2d.fill(forma3);
            }
        };
        panelLogin.setOpaque(true);
        // Panel del formulario blanco con bordes redondeados
        JPanel panelFormulario = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo blanco con bordes redondeados
                RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.setColor(Color.WHITE);
                g2d.fill(rect);
                g2d.setColor(new Color(230, 230, 230));
                g2d.draw(rect);
            }
        };
        panelFormulario.setOpaque(false);
        panelFormulario.setLayout(new GridBagLayout());
        
        // Panel del banner con gradiente profesional
        JPanel panelBanner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(52, 58, 64),
                    getWidth(), 0, new Color(33, 37, 41)
                );
                g2d.setPaint(gradient);
                
                // Bordes redondeados solo en la parte superior
                RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.fill(rect);
            }
        };
        panelBanner.setOpaque(false);
        panelBanner.setPreferredSize(new java.awt.Dimension(450, 120));
        
        lblUsuario = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        lblPass = new javax.swing.JLabel();
        txtPass = new javax.swing.JPasswordField();
        // Botón personalizado con bordes redondeados
        btnIngresar = new javax.swing.JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        lblTitulo = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        
        // Nuevos componentes
        JCheckBox chkRecuerdame = new JCheckBox("Recuérdame");
        JLabel lblOlvidaste = new JLabel("¿Olvidaste la contraseña?");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(550, 550));

        lblUsuario.setText("Usuario:");

        txtUser.setColumns(20);
        txtUser.setToolTipText("Ingrese su usuario");

        lblPass.setText("Contraseña:");

        txtPass.setColumns(20);
        txtPass.setToolTipText("Ingrese contraseña");

        btnIngresar.setText("Iniciar sesión");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });

        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("INICIAR SESIÓN");
        

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        // Layout del panel principal - centrar formulario
        panelLogin.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        
        // ========== CONSTRUIR FORMULARIO ==========
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(10, 30, 10, 30);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        
        // Banner con gradiente
        JLabel lblBienvenido = new JLabel("¡Bienvenido!");
        lblBienvenido.setForeground(Color.WHITE);
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel lblSubtituloBanner = new JLabel("TIENDA WALON está para servirle");
        lblSubtituloBanner.setForeground(new Color(255, 255, 255, 230));
        lblSubtituloBanner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        javax.swing.GroupLayout bannerLayout = new javax.swing.GroupLayout(panelBanner);
        panelBanner.setLayout(bannerLayout);
        bannerLayout.setHorizontalGroup(
            bannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bannerLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(bannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBienvenido)
                    .addComponent(lblSubtituloBanner))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        bannerLayout.setVerticalGroup(
            bannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bannerLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblBienvenido)
                .addGap(5, 5, 5)
                .addComponent(lblSubtituloBanner)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        
        // Agregar banner al formulario
        gbcForm.gridx = 0;
        gbcForm.gridy = 0;
        gbcForm.gridwidth = 2;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.insets = new Insets(0, 0, 0, 0);
        panelFormulario.add(panelBanner, gbcForm);
        
        // Campos del formulario
        gbcForm.gridwidth = 1;
        gbcForm.insets = new Insets(15, 30, 5, 30);
        
        // Usuario
        gbcForm.gridx = 0;
        gbcForm.gridy = 1;
        gbcForm.anchor = GridBagConstraints.WEST;
        panelFormulario.add(lblUsuario, gbcForm);
        
        gbcForm.gridy = 2;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.weightx = 1.0;
        panelFormulario.add(txtUser, gbcForm);
        
        // Contraseña
        gbcForm.gridy = 3;
        gbcForm.fill = GridBagConstraints.NONE;
        gbcForm.weightx = 0;
        gbcForm.anchor = GridBagConstraints.WEST;
        gbcForm.insets = new Insets(15, 30, 5, 30);
        panelFormulario.add(lblPass, gbcForm);
        
        gbcForm.gridy = 4;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.weightx = 1.0;
        panelFormulario.add(txtPass, gbcForm);
        
        // Checkbox Recuérdame
        gbcForm.gridy = 5;
        gbcForm.fill = GridBagConstraints.NONE;
        gbcForm.weightx = 0;
        gbcForm.anchor = GridBagConstraints.WEST;
        gbcForm.insets = new Insets(10, 30, 5, 30);
        panelFormulario.add(chkRecuerdame, gbcForm);
        
        // Botón Ingresar
        gbcForm.gridy = 6;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.insets = new Insets(15, 30, 10, 30);
        panelFormulario.add(btnIngresar, gbcForm);
        
        // Link Olvidaste contraseña
        lblOlvidaste.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblOlvidaste.setForeground(new Color(52, 58, 64));
        lblOlvidaste.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Checkbox Recuérdame (aplicar estilos)
        chkRecuerdame.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkRecuerdame.setForeground(new Color(100, 100, 100));
        chkRecuerdame.setOpaque(false);
        
        gbcForm.gridy = 7;
        gbcForm.fill = GridBagConstraints.NONE;
        gbcForm.anchor = GridBagConstraints.CENTER;
        gbcForm.insets = new Insets(5, 30, 20, 30);
        panelFormulario.add(lblOlvidaste, gbcForm);
        
        // Agregar formulario al panel principal (centrado)
        panelFormulario.setPreferredSize(new java.awt.Dimension(450, 500));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 20, 20, 20);
        panelLogin.add(panelFormulario, gbc);
        
        // Footer (opcional)
        JLabel lblFooter = new JLabel("© 2024 TIENDA WALON - Sistema de Facturación");
        lblFooter.setForeground(new Color(100, 100, 100));
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 20, 20, 20);
        panelLogin.add(lblFooter, gbc);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnIngresar;
    public javax.swing.JButton btnSalir;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel panelLogin;
    public javax.swing.JPasswordField txtPass;
    public javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
