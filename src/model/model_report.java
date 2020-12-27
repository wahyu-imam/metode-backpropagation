/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import koneksi.koneksi;
import main.index;
import rendering.*;
import view.utama;
import java.util.LinkedList;

/**
 *
 * @author Wahyu Imam
 */
public class model_report {
    DecimalFormat df = new DecimalFormat("#.#############");
    koneksi koneksi = new koneksi();
    index main = new index();
    kanan kanan = new kanan();
    lebarKolomTabel tabel = new lebarKolomTabel();
    LinkedList<Double> errorTerkecil = new LinkedList<>();
    
    public void tampil(utama utama){
        int column = utama.getTbReport().getColumnCount();
        utama.modelTbReport.getDataVector().removeAllElements();
        utama.modelTbReport.fireTableDataChanged();
        try {
            String sql = "SELECT `input_layer`, `node_hidden`, `max_iter`, `target_error`, `kb`, \n" +
                "`jarak_data_out`, `tgl_awal_data_train`, `tgl_akhir_data_train`, \n" +
                "`jml_data_Train`, `last_iterasi`, `rata_error`, `tgl_awal_data_test`, \n" +
                "`tgl_akhir_data_test`, `jml_data_test`, `error` FROM `pelatihan_jst`, `pengujian_jst` \n" +
                "WHERE `pelatihan_jst`.`kode_pelatihan`=`pengujian_jst`.`kode_pelatihan`";
            String sql2 = "SELECT `input_layer`, `node_hidden`, `max_iter`, `target_error`, `kb`, \n" +
                "`jarak_data_out`, `error` FROM `pelatihan_jst`, `pengujian_jst` \n" +
                "WHERE `pelatihan_jst`.`kode_pelatihan`=`pengujian_jst`.`kode_pelatihan`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql2);
            int index = 1;
            while(rs.next()){
                Object[] ob = new Object[column];
                ob[0] = index++;
                for(int i = 1; i < ob.length; i++){
                    ob[i] = rs.getString(i);
                }
                utama.modelTbReport.addRow(ob);
            }
            utama.getTbReport().setModel(utama.modelTbReport);
            main.headerTable(utama.getTbReport());
            tabel.setLebarKolom(utama.getTbReport());
            con.close(); stm.close(); rs.close();
            errorTerkecil(utama);
        } catch (Exception e) {
            System.out.println("error tampil report : "+e);
        }
    }
    
    private void errorTerkecil(utama utama){
        String sql = "SELECT MIN(`error`) FROM `pelatihan_jst`, `pengujian_jst` \n" +
            "WHERE `pelatihan_jst`.`kode_pelatihan`=`pengujian_jst`.`kode_pelatihan`";
        try {
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            double errorMin = 0;
            while(rs.next()){
                errorMin = Double.parseDouble(rs.getString(1));
            }
            con.close(); stm.close(); rs.close();
            double error = 0;
            int percobaanKe = 0;
            for(int i = 0; i < utama.getTbReport().getRowCount(); i++){
                int column = utama.getTbReport().getColumnCount() - 1;
                error = Double.parseDouble(utama.getTbReport().getValueAt(i, column).toString());
                if(error == errorMin){
                    percobaanKe = i+1;
                }
            }
            utama.getLbInfoErrorTerkecil().setText("Error terkecil yang dihasilkan selama pengujian adalah "+
                    df.format(errorMin)+" terdapat pada percobaan ke "+percobaanKe);
        } catch (Exception e) {
            System.out.println("error errorTerkecil report : "+e);
        }
    }
}
