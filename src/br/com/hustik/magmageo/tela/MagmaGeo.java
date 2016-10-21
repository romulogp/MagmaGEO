package br.com.hustik.magmageo.tela;

import br.com.hustik.magmageo.database.BancoDeDados;
import br.com.hustik.magmageo.tela.cadastro.CadastroBairro;
import br.com.hustik.magmageo.tela.cadastro.CadastroOcorrencia;
import br.com.hustik.magmageo.tela.relatorio.RelatorioOcorrencias;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel;

/**
 *
 * @author Rômulo Goelzer Portolann
 * @university UNIVALI - Universidade do Vale do Itajaí
 */
public class MagmaGeo extends javax.swing.JFrame {

    private final BancoDeDados bd = null;
    /**
     * Creates new form MainWindow
     */
    public MagmaGeo() {
        initComponents();
//        this.bd = new BancoDeDados();
//        updateOccourrencesTable();
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelLogo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelLogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanelMain = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanelTable2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableOccorrencias = new javax.swing.JTable();
        jPanelButtons = new javax.swing.JPanel();
        jButtonVisualizar = new javax.swing.JButton();
        jButtonOcorrencias = new javax.swing.JButton();
        jButtonBairros = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MagmaGeo - Geographic Solutions");
        setName("MainWindow"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("MagmaGeo");

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hustik/magmageo/icons/Global_Search-64.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Geographic Solutions");

        javax.swing.GroupLayout jPanelLogoLayout = new javax.swing.GroupLayout(jPanelLogo);
        jPanelLogo.setLayout(jPanelLogoLayout);
        jPanelLogoLayout.setHorizontalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabelLogo)
                .addGap(18, 18, 18)
                .addGroup(jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelLogoLayout.setVerticalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelLogo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(jLabel2)
                .addGap(12, 12, 12))
        );

        jTableOccorrencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Tipo", "Descrição", "Bairro"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableOccorrencias);

        javax.swing.GroupLayout jPanelTable2Layout = new javax.swing.GroupLayout(jPanelTable2);
        jPanelTable2.setLayout(jPanelTable2Layout);
        jPanelTable2Layout.setHorizontalGroup(
            jPanelTable2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTable2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTable2Layout.setVerticalGroup(
            jPanelTable2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jButtonVisualizar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonVisualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hustik/magmageo/icons/Bar-Chart-32x32.png"))); // NOI18N
        jButtonVisualizar.setText("    Visualizar");
        jButtonVisualizar.setPreferredSize(new java.awt.Dimension(125, 45));
        jButtonVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVisualizarActionPerformed(evt);
            }
        });

        jButtonOcorrencias.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonOcorrencias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hustik/magmageo/icons/Add-Point_32x32.png"))); // NOI18N
        jButtonOcorrencias.setText("Ocorrências");
        jButtonOcorrencias.setPreferredSize(new java.awt.Dimension(145, 45));
        jButtonOcorrencias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOcorrenciasActionPerformed(evt);
            }
        });

        jButtonBairros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonBairros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/hustik/magmageo/icons/Address-Icon_32x32.png"))); // NOI18N
        jButtonBairros.setText("Bairros");
        jButtonBairros.setPreferredSize(new java.awt.Dimension(125, 45));
        jButtonBairros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBairrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelButtonsLayout = new javax.swing.GroupLayout(jPanelButtons);
        jPanelButtons.setLayout(jPanelButtonsLayout);
        jPanelButtonsLayout.setHorizontalGroup(
            jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonBairros, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOcorrencias, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonVisualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelButtonsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonBairros, jButtonOcorrencias, jButtonVisualizar});

        jPanelButtonsLayout.setVerticalGroup(
            jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVisualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBairros, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOcorrencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTable2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addComponent(jPanelButtons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMainLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelTable2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBairrosActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonBairrosActionPerformed
    {//GEN-HEADEREND:event_jButtonBairrosActionPerformed
        CadastroBairro cadBairro = new CadastroBairro(this, true, this, bd);
        cadBairro.setLocationRelativeTo(this);
        cadBairro.setVisible(true);
        
    }//GEN-LAST:event_jButtonBairrosActionPerformed

    private void jButtonOcorrenciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOcorrenciasActionPerformed
        CadastroOcorrencia cadOcorr = new CadastroOcorrencia(this, true, this, bd);
        cadOcorr.setLocationRelativeTo(this);
        cadOcorr.setVisible(true);
    }//GEN-LAST:event_jButtonOcorrenciasActionPerformed

    private void jButtonVisualizarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonVisualizarActionPerformed
    {//GEN-HEADEREND:event_jButtonVisualizarActionPerformed
        RelatorioOcorrencias relOcorr = new RelatorioOcorrencias(this, true, this, bd);
        relOcorr.setLocationRelativeTo(this);
        relOcorr.setVisible(true);
    }//GEN-LAST:event_jButtonVisualizarActionPerformed

    
    public final void updateOccourrencesTable() {
        System.out.println("Atualizando tabela de ocorrências...");
        DefaultTableModel model = new DefaultTableModel();
        model = bd.atualizaListaOcorrencias(model);
        model = verificaBairrosDesconhecidos(model);
        jTableOccorrencias.setModel(model);
    }
    
    private DefaultTableModel verificaBairrosDesconhecidos(DefaultTableModel model) {
        int linhas = model.getRowCount();
        for (int i = 0; i < linhas; i++) {
            if (model.getValueAt(i, 3) == null) {
                model.setValueAt("Bairro não cadastrado", i, 3);
            }
        }
        return model;
    }
    
    /**
     * Loads the main window
     */
    public static void loadWindow() {
        try {
            UIManager.setLookAndFeel(new SubstanceNebulaBrickWallLookAndFeel());
            UIManager.setLookAndFeel(SubstanceNebulaBrickWallLookAndFeel.class.getName());
        } catch (ClassNotFoundException cnf) {
            System.out.println("Class Not Found");
        } catch (IllegalAccessException iae) {
            System.out.println("Illegal Acess Exception");
        } catch (InstantiationException ie) {
            System.out.println("Instantiation Exception");
        } catch (UnsupportedLookAndFeelException ulaf) {
            System.out.println("Unsupported Look and Feel");
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MagmaGeo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBairros;
    private javax.swing.JButton jButtonOcorrencias;
    private javax.swing.JButton jButtonVisualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelTable2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTableOccorrencias;
    // End of variables declaration//GEN-END:variables
}
