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
public class model_data_train implements controller.controller_CRUD{
    koneksi koneksi = new koneksi();
    kanan kanan = new kanan();
    tengah tengah = new tengah();
    lebarKolomTabel tabel = new lebarKolomTabel();
    index main = new index();
    
    @Override
    public void simpan(utama utama) {
        try {
            String sql = "INSERT INTO `data_train`(`tgl_awal`, `tgl_akhir`) VALUES(?,?)";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, utama.getTglTrainDari());
            pst.setString(2, utama.getTglTrainSampai());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            tampil(utama);
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error simpan data train : "+e);
        }
    }

    @Override
    public void ubah(utama utama) {
        try {
            String sql = "UPDATE data_train SET `tgl_awal` = ?, `tgl_akhir` = ? WHERE `kode_data_train` = ?";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, utama.getTglTrainDari());
            pst.setString(2, utama.getTglTrainSampai());
            pst.setString(3, getKodeDataTrain());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
            tampil(utama);
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error ubah data train : "+e);
        }
    }

    @Override
    public void hapus(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tampil(utama utama) {
        int column = utama.getTbEmas().getColumnCount();
        utama.modelTbDataLatih.getDataVector().removeAllElements();
        utama.modelTbDataLatih.fireTableDataChanged();
        int no = 1;
        try {
            String sql = "SELECT tanggal, harga FROM `harga_emas` WHERE `TANGGAL` BETWEEN '"+utama.getTglTrainDari()+
                    "' AND '"+utama.getTglTrainSampai()+"'";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[column];
                ob[0] = no++;
                for(int i = 1; i < ob.length; i++){
                    ob[i] = rs.getString(i);
                }
                utama.modelTbDataLatih.addRow(ob);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error tampil emas : "+e);
        }
        tabel.setLebarKolom(utama.getTbDataLatih());
        main.headerTable(utama.getTbDataLatih());
        utama.getTbDataLatih().getColumnModel().getColumn(1).setCellRenderer(tengah);
        utama.getTbDataLatih().getColumnModel().getColumn(2).setCellRenderer(kanan);
    }

    @Override
    public void klikTabel(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bersih(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getKodeDataTrain(){
        String kode = null;
        try {
            String sql = "SELECT `kode_data_train` FROM `data_train`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                kode = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error kodeDataTrain : "+e);
        }
        return kode;
    }
    
    public void setTglTrain(utama utama){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String sql = "SELECT `tgl_awal`, `tgl_akhir` FROM `data_train` WHERE `kode_data_train` = '"+getKodeDataTrain()+"'";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                Date tglAwal = format.parse(rs.getString(1));
                utama.getDcLatihDari().setDate(tglAwal);
                Date tglAkhir = format.parse(rs.getString(2));
                utama.getDcLatihSampai().setDate(tglAkhir);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error setTglTrain : "+e);
        }
    }
    
    public String getTglAwal(){
        String tgl = null;
        try {
            String sql = "SELECT `tgl_awal` FROM `data_train`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                tgl = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getTglAwal train : "+e);
        }
        return tgl;
    }
    
    public String getTglAkhir(){
        String tgl = null;
        try {
            String sql = "SELECT `tgl_akhir` FROM `data_train`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                tgl = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getTglAkhir train : "+e);
        }
        return tgl;
    }
}
