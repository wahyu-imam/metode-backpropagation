/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import koneksi.koneksi;
import view.utama;
import Data.Data;
import backpro.backpro;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import rendering.*;
import main.index;
import view.tampilHasilActualOut;

/**
 *
 * @author Wahyu Imam
 */
public class model_pengujian implements controller.controller_CRUD{
    model_data_test modelTest = new model_data_test();
    model_pelatihan modelPelatihan = new model_pelatihan();
    kanan kanan = new kanan();
    lebarKolomTabel tabel = new lebarKolomTabel();
    koneksi koneksi = new koneksi();
    backpro bp = new backpro();
    Data data = new Data();
    index main = new index();
    
    double errorPengujian;
    
    @Override
    public void simpan(utama utama) {
        try {
            String sql = "INSERT INTO `pengujian_jst`(`kode_pelatihan`, `jml_data_test`, "
                    + "`tgl_awal_data_test`, `tgl_akhir_data_test`, error) VALUES(?,?,?,?,?)";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, modelPelatihan.getKodePelatihan());
            pst.setString(2, String.valueOf(utama.getTbPengujian().getRowCount()));
            pst.setString(3, modelTest.getTglAwal());
            pst.setString(4, modelTest.getTglAkhir());
            pst.setString(5, String.valueOf(errorPengujian));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            con.close(); pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error simpan pelatihan : "+e);
        }
    }

    @Override
    public void ubah(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hapus(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void tampil(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void klikTabel(utama utama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bersih(utama utama) {
        utama.getTxtHasilPengujian().setText(null);
    }
    
    public void tampilParameter(utama utama){
        String sql = "SELECT `input_layer`, `node_hidden`, `jarak_data_out` \n" +
            "FROM `pelatihan_jst` WHERE `kode_pelatihan` ORDER BY `kode_pelatihan` DESC LIMIT 1";
        try {
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                utama.getTxtInputLayerUji().setText(rs.getString(1));
                utama.getTxtNodeHiddenLayerUji().setText(rs.getString(2));
                utama.getTxtJarakDataOutUji().setText(rs.getString(3));
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error tampilParameter pengujian : "+e);
        }
    }
    
    private void getBobot(int inputLayer, int nodeHidden){
        try {
            // BOBOT BIAS INPUT
            String sql = "SELECT `bobot` FROM `last_bbt_bias_inp`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            int index = 0;
            bp.bobotBiasInput = new double[nodeHidden];
            while(rs.next()){
                bp.bobotBiasInput[index] = Double.parseDouble(rs.getString(1));
                index++;
            }
            con.close(); rs.close(); stm.close();
            
            // BOBOT INPUT
            sql = "SELECT `bobot` FROM `last_bbt_inp`";
            con = koneksi.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            index = 0;
            double[] tampungBbt = new double[inputLayer * nodeHidden];
            while(rs.next()){
                tampungBbt[index] = Double.parseDouble(rs.getString(1));
                index++;
            }
            con.close(); rs.close(); stm.close();
            bp.bobotInput = new double[inputLayer][nodeHidden];
            index = 0;
            for(int i = 0; i < inputLayer; i++){
                for(int j = 0; j < nodeHidden; j++){
                    bp.bobotInput[i][j] = tampungBbt[index];
                    index++;
                }
            }
            
            // BOBOT BIAS HIDDEN
            sql = "SELECT `bobot` FROM `last_bbt_bias_hidden`";
            con = koneksi.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            while(rs.next()){
                bp.bobotBiasHidden = Double.parseDouble(rs.getString(1));
            }
            con.close(); rs.close(); stm.close();
            
            // BOBOT HIDDEN
            sql = "SELECT `bobot` FROM `last_bbt_hidden`";
            con = koneksi.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            index = 0;
            bp.bobotHidden = new double[nodeHidden];
            while(rs.next()){
                bp.bobotHidden[index] = Double.parseDouble(rs.getString(1));
                index++;
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getBobot pengujian : "+e);
        }
    }
    
    // PEMBUATAN DATA TRAIN
    public double[][] dataTest(utama utama){
        int inputLayer = Integer.parseInt(utama.getTxtInputLayerUji().getText());
        int nodeHidden = Integer.parseInt(utama.getTxtNodeHiddenLayerUji().getText());
        int jarak = Integer.parseInt(utama.getTxtJarakDataOutUji().getText());
        String sql = "SELECT COUNT(`HARGA`) AS JML FROM `harga_emas` "
                + "WHERE `TANGGAL` BETWEEN '"+modelTest.getTglAwal()+"' AND '"+modelTest.getTglAkhir()+"'";
        int jmlData = data.panjangData(sql);
        sql = "SELECT HARGA FROM `harga_emas` WHERE `TANGGAL` BETWEEN '"+modelTest.getTglAwal()+
                "' AND '"+modelTest.getTglAkhir()+"'";
        double[] allDataTest = data.semuaData(jmlData, sql);
        double[][] dataTest = data.data(allDataTest, inputLayer, jarak);
        getBobot(inputLayer, nodeHidden);
        return dataTest;
    }
    
    public void tampilDataTest(utama utama, double[][] dataTest){
        DefaultTableModel model = new DefaultTableModel(){
          @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }  
        };
        for(int i = 0; i < dataTest[0].length; i++){
            if(i == dataTest[0].length - 1){
                model.addColumn("Target");
            }else{
                model.addColumn("X"+(i+1));
            }
        }
        Object[] ob = new Object[dataTest[0].length];
        for(int j = 0; j < dataTest.length; j++){
            for(int k = 0; k < dataTest[0].length; k++){
                ob[k] = dataTest[j][k];
            }
            model.addRow(ob);
        }
        utama.getTbPengujian().setModel(model);
        main.headerTable(utama.getTbPengujian());
        TableColumn column;
        utama.getTbPengujian().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        for(int l = 0; l < utama.getTbPengujian().getColumnCount(); l++){
            utama.getTbPengujian().getColumnModel().getColumn(l).setCellRenderer(kanan);
            column = utama.getTbPengujian().getColumnModel().getColumn(l); 
            column.setPreferredWidth(100);
        }
        tabel.setLebarKolom(utama.getTbPengujian());
    }
    
    public double[] prosesPengujian(utama utama, double[][] dataTest){
        bersih(utama);
        DecimalFormat df = new DecimalFormat("#.#############");
        boolean print = false;
        
        double[][] norDataTest = bp.normaslisasi(dataTest);
        double[][] dataTestInp = data.dataInp(norDataTest);
        double[] dataTestOut = data.dataOut(norDataTest);
        
        double[][] bobotInp = bp.bobotInput;
        double[] bobotBiasInp = bp.bobotBiasInput;
        double[] bobotHidden = bp.bobotHidden;
        double bobotBiasHidden = bp.bobotBiasHidden;
        
        double[] actualTest = new double[dataTestOut.length];
        
        double error = 0;
        double kuadratError = 0;
        double jmlKuadratError = 0;
        double mse = 0;
        
        //VARIABEL DATA KE k
        int i = 0;

        // VARIABEL DATA KE k
        double[] x;

        // VARIABEL HIDDEN LAYER
        double[] z_in, z;

        // VARIABEL OUTPUT LAYER
        double y_in, y;
        
        for(int j = 0; j < dataTestInp.length; j++){

                i = j % dataTestInp.length;
                x = bp.x(dataTestInp, i);

                z_in = bp.z_in(bobotBiasInp, bobotInp, x);

                z = bp.z(z_in);

                y_in = bp.y_in(z, bobotBiasHidden, bobotHidden);

                y = bp.y(y_in);
                actualTest[i] = y;

                error = bp.error(dataTestOut, actualTest, i);
//                kuadratError = bp.kuadratError(error);
//                jmlKuadratError = jmlKuadratError + kuadratError;
                
                if(print){
                    System.out.println("Data ke = "+(j+1));
                    System.out.println("\n  ͦ Operasi pada Hidden Layer --->");
                    System.out.print("\t* Perkalian z_in : \n\t\t");
                    for(int a = 0; a < z_in.length; a++){
                        System.out.print(z_in[a]+"\t");
                    }
                    System.out.print("\n\t* Pengaktifan z : \n\t\t");
                    for(int a = 0; a < z.length; a++){
                        System.out.print(z[a]+"\t");
                    }
                    System.out.println("\n  ͦ Operasi pada Output Layer --->");
                    System.out.print("\t* Perkalian y_in : \n\t\t");
                    System.out.println(y_in);
                    System.out.print("\t* Pengaktifan y : \n\t\t");
                    System.out.print(y);
                    System.out.println("\n  ͦ Error = "+error);
                    System.out.println("  ͦ Jumlah Kuadrat Error : "+jmlKuadratError);
                }
        }
        mse = bp.sse(actualTest, dataTestOut);
        errorPengujian = mse;
        double[] denorTarget = new double[dataTestOut.length];
        double[] denorActual = new double[actualTest.length];
        for(int j = 0; j < dataTestOut.length; j++){
            denorTarget[j] = bp.denor(dataTestOut[j], dataTest);
            denorActual[j] = bp.denor(actualTest[j], dataTest);
        }
        double mse2 = bp.sse(denorActual, denorTarget);
        utama.getTxtHasilPengujian().append("     -- Hasil Pengujian --\n\n");
        utama.getTxtHasilPengujian().append("Error untuk normalisasi data uji :\n "+df.format(mse));
        utama.getTxtHasilPengujian().append("\nError untuk denormalisasi data uji:\n "+df.format(mse2));
        return actualTest;
    }
    
    public void tampilActual(tampilHasilActualOut tampil, double[][] dataTest , 
            double[] dataTestOut, double[] actualOut){
        DecimalFormat df = new DecimalFormat("#.###");
        int column = tampil.getTbHasilActual().getColumnCount();
        tampil.model.getDataVector().removeAllElements();
        tampil.model.fireTableDataChanged();
        Object[] ob = new Object[column];
        for(int i = 0; i < dataTestOut.length; i++){
            ob[0] = i+1;
            ob[1] = bp.denor(dataTestOut[i], dataTest);
            double target = bp.denor(dataTestOut[i], dataTest);
            ob[2] = bp.denor(actualOut[i], dataTest);
            double actual = bp.denor(actualOut[i], dataTest);
            ob[3] = df.format(Math.abs(target - actual));
            tampil.model.addRow(ob);
        }
        tampil.getTbHasilActual().setModel(tampil.model);
    }
}
