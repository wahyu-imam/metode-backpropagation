/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import view.utama;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import rendering.*;
import koneksi.koneksi;
import main.index;

/**
 *
 * @author Wahyu Imam
 */
public class model_emas implements controller.controller_CRUD{
    index main = new index();
    koneksi koneksi = new koneksi();
    lebarKolomTabel tabel = new lebarKolomTabel();
    tengah tengah = new tengah();
    kanan kanan = new kanan();
    
    @Override
    public void simpan(utama utama) {
        try {
            String sql = "INSERT `harga_emas`(`TANGGAL`, `HARGA`) VALUES (?,?)";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, utama.getTxtTgl().getText());
            pst.setString(2, utama.getTxtHarga().getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            tampil(utama);
            bersih(utama);
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error tambah emas : "+e);
        }
    }

    @Override
    public void ubah(utama utama) {
        try {
            String sql = "UPDATE `harga_emas` SET `HARGA` = ? WHERE `KD_HARGA` = ?";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, utama.getTxtHarga().getText());
            pst.setString(2, getKodeEmas(utama));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
            tampil(utama);
            bersih(utama);
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error ubah emas : "+e);
        }
    }

    @Override
    public void hapus(utama utama) {
        try {
            int baris = utama.getTbEmas().getSelectedRow();
            String tgl = utama.getTbEmas().getValueAt(baris, 1).toString();
            utama.getTxtTgl().setText(tgl);
            String sql = "DELETE FROM `harga_emas` WHERE `KD_HARGA` = ?";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, getKodeEmas(utama));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
            tampil(utama);
            bersih(utama);
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error hapus emas : "+e);
        }
    }

    @Override
    public void tampil(utama utama) {
        int column = utama.getTbEmas().getColumnCount();
        utama.modelTbEmas.getDataVector().removeAllElements();
        utama.modelTbEmas.fireTableDataChanged();
        int no = 1;
        try {
            String sql = "SELECT `TANGGAL`, `HARGA` FROM `harga_emas`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                Object[] ob = new Object[column];
                ob[0] = no++;
                for(int i = 1; i < ob.length; i++){
                    ob[i] = rs.getString(i);
                }
                utama.modelTbEmas.addRow(ob);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error tampil emas : "+e);
        }
        tabel.setLebarKolom(utama.getTbEmas());
        main.headerTable(utama.getTbEmas());
        utama.getTbEmas().getColumnModel().getColumn(1).setCellRenderer(tengah);
        utama.getTbEmas().getColumnModel().getColumn(2).setCellRenderer(kanan);
    }

    @Override
    public void klikTabel(utama utama) {
        try {
            int pilih = utama.getTbEmas().getSelectedRow();
            utama.getTxtTgl().setText(utama.getTbEmas().getValueAt(pilih, 1).toString());
            utama.getTxtHarga().setText(utama.getTbEmas().getValueAt(pilih, 2).toString());
        } catch (Exception e) {
            System.out.println("error klikTabel emas :"+e);
        }
    }

    @Override
    public void bersih(utama utama) {
        utama.getTxtHarga().setText(null);
    }
    
    public String getKodeEmas(utama utama){
        String kode = null;
        try {
            String sql = "SELECT `KD_HARGA` FROM `harga_emas` WHERE `TANGGAL` = '"+utama.getTxtTgl().getText()+"'";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                kode = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getKodeEmas : "+e);
        }
        return kode;
    }
}
