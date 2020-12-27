/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import view.tampilBobot;
import view.utama;
import koneksi.koneksi;

/**
 *
 * @author Wahyu Imam
 */
public class model_tampil_bobot {
    koneksi koneksi = new koneksi();
    
    private int getInput(){
        int input = 0;
        String sql = "SELECT `input_layer` FROM `pelatihan_jst` WHERE `kode_pelatihan` "
                + "ORDER BY `kode_pelatihan` DESC LIMIT 1";
        try {
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                input = Integer.parseInt(rs.getString(1));
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getInput pelatihan : "+e);
        }
        return input;
    }
    
    private int getNodeHidden(){
        int nodeHidden = 0;
        String sql = "SELECT `node_hidden` FROM `pelatihan_jst` WHERE `kode_pelatihan` "
                + "ORDER BY `kode_pelatihan` DESC LIMIT 1";
        try {
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                nodeHidden = Integer.parseInt(rs.getString(1));
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getNodeHidden pelatihan : "+e);
        }
        return nodeHidden;
    }
    
    public void tampilBobot(tampilBobot tampil, utama utama){
        DefaultTableModel modelBiasInp = new DefaultTableModel();
        DefaultTableModel modelInp = new DefaultTableModel();
        DefaultTableModel modelBiasHidden = new DefaultTableModel();
        DefaultTableModel modelHidden = new DefaultTableModel();
        
        int inputLayer = getInput();
        int nodeHidden = getNodeHidden();
        try {
            // TAMPIL BIAS INPUT
            modelBiasInp.addColumn("Bias 1");
            modelBiasInp.addColumn("1");
            String sql = "SELECT `bobot` FROM `last_bbt_bias_inp`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            Object[] ob = new Object[2];
            int index = 1;
            while(rs.next()){
                ob[0] = index++;
                ob[1] = rs.getString(1);
                modelBiasInp.addRow(ob);
            }
            con.close(); rs.close(); stm.close();
            tampil.getTbBbtBiasInp().setModel(modelBiasInp);
            
            // TAMPIL INPUT
            modelInp.addColumn("X");
            for(int i = 0; i < nodeHidden; i++){
                modelInp.addColumn(i+1);
            }
            sql = "SELECT `bobot` FROM `last_bbt_inp`";
            con = koneksi.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            ob = new Object[nodeHidden + 1];
            index = 0;
            double[] tampungBbt = new double[inputLayer * nodeHidden];
            while(rs.next()){
                tampungBbt[index] = Double.parseDouble(rs.getString(1));
                index++;
            }
            con.close(); rs.close(); stm.close();
            index = 1;
            int k = 0;
            for(int i = 0; i < inputLayer; i++){
                ob[0] = index+i;
                for(int j = 0; j < nodeHidden; j++){
                    ob[j+1] = tampungBbt[k];
                    k++;
                }
                modelInp.addRow(ob);
            }
            tampil.getTbBbtInp().setModel(modelInp);
            TableColumn column;
            tampil.getTbBbtInp().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            for(int l = 0; l < tampil.getTbBbtInp().getColumnCount(); l++){
                //utama.getTbPengujian().getColumnModel().getColumn(l).setCellRenderer(kanan);
                column = tampil.getTbBbtInp().getColumnModel().getColumn(l); 
                column.setPreferredWidth(100);
            }
            
            // TAMPIL BIAS HIDDEN
            modelBiasHidden.addColumn("Bias 2");
            modelBiasHidden.addColumn("1");
            sql = "SELECT `bobot` FROM `last_bbt_bias_hidden`";
            con = koneksi.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            ob = new Object[2];
            index = 1;
            while(rs.next()){
                ob[0] = index++;
                ob[1] = rs.getString(1);
                modelBiasHidden.addRow(ob);
            }
            con.close(); rs.close(); stm.close();
            tampil.getTbBbtBiasHidden().setModel(modelBiasHidden);
            
            // TAMPIL HIDDEN
            modelHidden.addColumn("Z");
            modelHidden.addColumn("Bobot");
            sql = "SELECT `bobot` FROM `last_bbt_hidden`";
            con = koneksi.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            ob = new Object[2];
            index = 1;
            while(rs.next()){
                ob[0] = index++;
                ob[1] = rs.getString(1);
                modelHidden.addRow(ob);
            }
            con.close(); stm.close(); rs.close();
            tampil.getTbBbtHidden().setModel(modelHidden);
        } catch (Exception e) {
            System.out.println("error tampilBoobot pelatihan : "+e);
        }
    }
}
