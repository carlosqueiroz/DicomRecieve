/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connect.dicom.query;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author cwilliams
 */
public class Main extends javax.swing.JFrame {

    SendDICOM sd = null;
    AdvancedPreferences ap = new AdvancedPreferences();
    Thread backgroundWorker = new Thread();
    String ipaddr = "N/A";
    Process p = null;
    Properties prop = new Properties();

    protected static Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public void initTray() {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("icon.gif");
            // create a action listener to listen for default action executed on the tray icon
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // execute default action of the application
                    rootPane.setVisible(true);
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // create menu item for the default action
            MenuItem defaultItem = new MenuItem("Show Window");
            defaultItem.addActionListener(listener);
            popup.add(defaultItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Tray Demo", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(listener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        }

    }

    /**
     * Creates new form MainWindow
     */
    public Main() {



        initComponents();
        //initTray();

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println("Unable to load Windows look and feel");
        }


        addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        Image image = Toolkit.getDefaultToolkit().getImage("icon.gif");
                        final TrayIcon trayIcon = new TrayIcon(image);
                        trayIcon.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                setVisible(true);
                                setExtendedState(JFrame.NORMAL);
                                SystemTray.getSystemTray().remove(trayIcon);
                            }
                        });
                        SystemTray.getSystemTray().add(trayIcon);
                        setVisible(false);
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    Runtime.getRuntime().exec("taskkill /IM java.exe /F");
                } catch (Exception ie) {
                }
                System.exit(0);
            }
        });


        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipaddr = ip.getHostAddress();
        } catch (Exception e) {
            System.out.println("Erro ao obter o endereço IP - " + e.toString());
        }

        File fCheck = new File("config.properties");
        if (!fCheck.exists()) {
            prop.setProperty("SD_IP", "");
            prop.setProperty("AETitle", "RTCONNECT");
            prop.setProperty("SD_AETitle", "RTCONNECT");
            prop.setProperty("port", "104");
            prop.setProperty("SD_port", "104");
            prop.setProperty("autostart", "0");
            prop.setProperty("destination", System.getProperty("user.dir") + "\\incoming");
            prop.setProperty("SD_destination", System.getProperty("user.dir") + "\\incoming");
            jTextField1.setText(System.getProperty("user.dir") + "\\incoming");
            try {
                prop.store(new FileOutputStream("config.properties"), null);
            } catch (Exception e) {
                System.out.println("Erro ao guardar propriedades de configuração - " + e.toString());
            }
        } else {
            try {

                prop.load(new FileInputStream("config.properties"));
                ap.AETitle = prop.getProperty("AETitle");
                ap.port = prop.getProperty("port");
                jTextField1.setText(prop.getProperty("destination"));
                if (prop.getProperty("autostart").equals("0")) {
                    jCheckBoxMenuItem1.setSelected(false);
                } else {
                    jCheckBoxMenuItem1.setSelected(true);

                }
            } catch (Exception e) {
                System.out.println("Error Opening Configuration Properties - " + e.toString());
            }
        }
        ap.prop = prop;
        jLabel1.setText("Status: Aguardando - (IP: " + ipaddr + ", AE Title: " + ap.AETitle + ", Port: " + ap.port + ")");

        if (jCheckBoxMenuItem1.isSelected() == true) {
            jButton1ActionPerformed(null);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mobius DICOM Receiver");

        jButton1.setText("Iniciar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Parar");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Status: Aguardando...");

        jLabel2.setText("Pasta:");

        jButton3.setText("Procurar...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel3.setText("Mensagem:");

        jMenu1.setText("Arquivo");

        jMenuItem1.setText("Configurações...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jCheckBoxMenuItem1.setText("Iniciar Automáticamente...");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem1);

        jMenuItem3.setText("Enviar Arquivos...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem2.setText("Sobre ...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addGap(0, 156, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)))))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTextArea1.setText("");
        jScrollPane1.getHorizontalScrollBar().setValue(0);

        prop.setProperty("destination", jTextField1.getText());

        try {
            prop.store(new FileOutputStream("config.properties"), null);
        } catch (Exception e) {
            System.out.println("Erro ao guardar propriedades de configuração - " + e.toString());
        }

        backgroundWorker = new Thread() {
            public void run() {

                try {

                    System.out.println("cmd /c dcm4che-2.0.27\\bin\\dcmrcv.bat " + ap.AETitle + "@" + ipaddr + ":" + ap.port + " -dest \"" + jTextField1.getText() + "\"");
                    ProcessBuilder pb = new ProcessBuilder();
                    pb.redirectErrorStream(true);
                    String[] cmd = new String[6];
                    cmd[0] = "cmd";
                    cmd[1] = "/c";
                    cmd[2] = "dcm4che-2.0.27\\bin\\dcmrcv.bat";
                    cmd[3] = ap.AETitle + "@" + ipaddr + ":" + ap.port;
                    cmd[4] = "-dest";
                    cmd[5] = jTextField1.getText();
                    pb.command(cmd);

                    //pb.command("cmd /c dcm4che-2.0.27\\bin\\dcmrcv.bat " + ap.AETitle + "@"+ ipaddr +":"+ ap.port + " -dest \""+ jTextField1.getText() +"\"");
                    p = pb.start();



                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        final String lineF = line;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jTextArea1.append(lineF.trim() + "\r\n");
                                //jTextArea1.append(fLine.trim() + "\r\n");
                                jScrollPane1.getHorizontalScrollBar().setValue(0);

                            }
                        });

                    }
                } catch (final Exception e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            jTextArea1.append(e.toString() + "\r\n");
                            jScrollPane1.getHorizontalScrollBar().setValue(0);
                        }
                    });
                }


                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        jButton1.setEnabled(true);
                        jButton2.setEnabled(false);
                        jButton3.setEnabled(true);
                        jMenuItem1.setEnabled(true);
                        jTextArea1.append("Parado pelo Usuário.\r\n");
                        jScrollPane1.getHorizontalScrollBar().setValue(0);
                        jLabel1.setText("Status: Parado - (IP: " + ipaddr + ", AE Title: " + ap.AETitle + ", Porta: " + ap.port + ")");
                    }
                });


            }
        };

        backgroundWorker.start();
        jButton1.setEnabled(false);
        jButton2.setEnabled(true);
        jButton3.setEnabled(false);
        jMenuItem1.setEnabled(false);
        jLabel1.setText("Status: Em Execução - (IP: " + ipaddr + ", AE Title: " + ap.AETitle + ", Porta: " + ap.port + ")");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        ap.pack();
        ap.setVisible(true);
        ap.setText1(prop.getProperty("AETitle"));
        ap.setText2(prop.getProperty("port"));
        ap.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                jLabel1.setText("Status: Aguardando - (IP: " + ipaddr + ", AE Title: " + ap.AETitle + ", Porta: " + ap.port + ")");
            }
        });

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "RT Connect DICOM Query\r\nCopyright (c) 2018 RT Medical Systems\r\nAcesse nosso site http://rtmedical.com.br", "About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        JFileChooser j = new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = j.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            jTextField1.setText(j.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        // TODO add your handling code here:
        if (jCheckBoxMenuItem1.isSelected() == true) {
            prop.setProperty("autostart", "1");
        } else {
            prop.setProperty("autostart", "0");
        }

        try {
            prop.store(new FileOutputStream("config.properties"), null);
        } catch (Exception e) {
            System.out.println("Erro ao salvar propriedades de configuração - " + e.toString());
        }
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            Runtime.getRuntime().exec("taskkill /IM java.exe /F");
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        if (sd == null) {
            sd = new SendDICOM(prop.getProperty("SD_IP"), prop.getProperty("SD_AETitle"), prop.getProperty("SD_port"), prop.getProperty("SD_destination"), prop);
        }

        sd.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
