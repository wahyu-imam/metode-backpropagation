/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.model_pengujian;
import backpro.backpro;
import Data.Data;
import java.text.DecimalFormat;

/**
 *
 * @author Wahyu Imam
 */
public class tampilHasilActualOut extends javax.swing.JDialog {
    backpro bp = new backpro();
    Data data = new Data();
    model_pengujian modelPengujian = new model_pengujian();
    
    public DefaultTableModel model;
    String[] headerTbActual = {"No","Target","Actual","Selisih"};
    double[][] dataTest;
    double[] norActual;
    /**
     * Creates new form tampilHasilActualOut
     * @param parent
     * @param modal
     */
    public tampilHasilActualOut(java.awt.Frame parent, boolean modal, double[][] dataTest, double[] norActual) {
        super(parent, modal);
        initComponents();
        awal();
        this.dataTest = dataTest;
        this.norActual = norActual;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pHeader = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        lbTitleProject = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbHasilActual = new javax.swing.JTable();
        lbNorDenor = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(241, 244, 247));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pHeader.setBackground(new java.awt.Color(72, 78, 84));
        pHeader.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(153, 153, 153)));
        pHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnExit.setBackground(new java.awt.Color(173, 9, 0));
        btnExit.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("X");
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        pHeader.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 50, 30));

        lbTitleProject.setFont(new java.awt.Font("Comic Sans MS", 2, 14)); // NOI18N
        lbTitleProject.setForeground(new java.awt.Color(255, 255, 255));
        lbTitleProject.setText("Aplikasi Prediksi Harga Emas");
        pHeader.add(lbTitleProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 200, 30));

        jPanel1.add(pHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 30));

        jPanel2.setBackground(new java.awt.Color(241, 244, 247));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Hasil Actual Output", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Ebrima", 0, 12))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbHasilActual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbHasilActual);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 430, 460));

        lbNorDenor.setFont(new java.awt.Font("Ebrima", 0, 14)); // NOI18N
        lbNorDenor.setText("Normalisasi");
        lbNorDenor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbNorDenor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbNorDenorMouseClicked(evt);
            }
        });
        jPanel2.add(lbNorDenor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 450, 510));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void awal(){
        model = new DefaultTableModel(null, headerTbActual);
        tbHasilActual.setModel(model);
    }
    
    public JTable getTbHasilActual() {
        return tbHasilActual;
    }
    
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_btnExitActionPerformed

    private void lbNorDenorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbNorDenorMouseClicked
        // TODO add your handling code here:
        DecimalFormat df = new DecimalFormat("#.###");
        if(lbNorDenor.getText().equals("Normalisasi")){
            double[][] norDataTest = bp.normaslisasi(dataTest);
            double[] norTarget = data.dataOut(norDataTest);
            for(int i = 0; i < norTarget.length; i++){
                tbHasilActual.setValueAt(norTarget[i], i, 1);
                tbHasilActual.setValueAt(norActual[i], i, 2);
                double selisih = Math.abs(norTarget[i] - norActual[i]);
                tbHasilActual.setValueAt(selisih, i, 3);
            }
            lbNorDenor.setText("Denormalisasi");
        }else{
            double[] target = data.dataOut(dataTest);
            for(int i = 0; i < target.length; i++){
                tbHasilActual.setValueAt(target[i], i, 1);
                tbHasilActual.setValueAt(bp.denor(norActual[i], dataTest), i, 2);
                double selisih = Math.abs(target[i] - bp.denor(norActual[i], dataTest));
                tbHasilActual.setValueAt(df.format(selisih), i, 3);
            }
            lbNorDenor.setText("Normalisasi");
        }
    }//GEN-LAST:event_lbNorDenorMouseClicked

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(tampilHasilActualOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(tampilHasilActualOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(tampilHasilActualOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(tampilHasilActualOut.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                tampilHasilActualOut dialog = new tampilHasilActualOut(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbNorDenor;
    private javax.swing.JLabel lbTitleProject;
    private javax.swing.JPanel pHeader;
    private javax.swing.JTable tbHasilActual;
    // End of variables declaration//GEN-END:variables
}
