/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import view.utama;
import koneksi.koneksi;
import rendering.*;
import main.index;

/**
 *
 * @author Wahyu Imam
 */

public class model_data_test implements controller.controller_CRUD{
    koneksi koneksi = new koneksi();
    kanan kanan = new kanan();
    tengah tengah = new tengah();
    lebarKolomTabel tabel = new lebarKolomTabel();
    index main = new index();
    
    @Override
    public void simpan(utama utama) {
        try {
            String sql = "INSERT INTO `data_test`(`tgl_awal`, `tgl_akhir`) VALUES(?,?)";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, utama.getTglTestDari());
            pst.setString(2, utama.getTglTestSampai());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            tampil(utama);
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error simpan test : "+e);
        }
    }

    @Override
    public void ubah(utama utama) {
        try {
            String sql = "UPDATE data_test SET `tgl_awal` = ?, `tgl_akhir` = ? WHERE `kode_data_test` = ?";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, utama.getTglTestDari());
            pst.setString(2, utama.getTglTestSampai());
            pst.setString(3, getKodeDataTest());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
            tampil(utama);
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error ubah train : "+e);
        }
    }

    @Override
    public void hapus(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tampil(utama utama) {
        int column = utama.getTbEmas().getColumnCount();
        utama.modelTbDataTest.getDataVector().removeAllElements();
        utama.modelTbDataTest.fireTableDataChanged();
        int no = 1;
        try {
            String sql = "SELECT tanggal, harga FROM `harga_emas` WHERE `TANGGAL` BETWEEN '"+utama.getTglTestDari()+
                    "' AND '"+utama.getTglTestSampai()+"'";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[column];
                ob[0] = no++;
                for(int i = 1; i < ob.length; i++){
                    ob[i] = rs.getString(i);
                }
                utama.modelTbDataTest.addRow(ob);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error tampil emas : "+e);
        }
        tabel.setLebarKolom(utama.getTbdataUji());
        main.headerTable(utama.getTbdataUji());
        utama.getTbdataUji().getColumnModel().getColumn(1).setCellRenderer(tengah);
        utama.getTbdataUji().getColumnModel().getColumn(2).setCellRenderer(kanan);
    }

    @Override
    public void klikTabel(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bersih(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getKodeDataTest(){
        String kode = null;
        try {
            String sql = "SELECT `kode_data_test` FROM `data_test`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                kode = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getKodeDataTest : "+e);
        }
        return kode;
    }
    
    public void setTglTest(utama utama){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String sql = "SELECT `tgl_awal`, `tgl_akhir` FROM `data_test` WHERE `kode_data_test` = '"+getKodeDataTest()+"'";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                Date tglAwal = format.parse(rs.getString(1));
                utama.getDcUjiDari().setDate(tglAwal);
                Date tglAkhir = format.parse(rs.getString(2));
                utama.getDcUjiSampai().setDate(tglAkhir);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error setTglTest : "+e);
        }
    }
    
    public String getTglAwal(){
        String tgl = null;
        try {
            String sql = "SELECT `tgl_awal` FROM `data_test`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                tgl = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getTglAwal test : "+e);
        }
        return tgl;
    }
    
    public String getTglAkhir(){
        String tgl = null;
        try {
            String sql = "SELECT `tgl_akhir` FROM `data_test`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                tgl = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getTglAkhir test : "+e);
        }
        return tgl;
    }
}
