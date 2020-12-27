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
import main.index;
import koneksi.koneksi;
import view.utama;
import Data.Data;
import backpro.backpro;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import rendering.*;

/**
 *
 * @author Wahyu Imam
 */
public class model_pelatihan implements controller.controller_CRUD{
    model_data_train modelTrain = new model_data_train();
    koneksi koneksi = new koneksi();
    kanan kanan = new kanan();
    lebarKolomTabel tabel = new lebarKolomTabel();
    index main = new index();
    Data data = new Data();
    backpro bp = new backpro();
    
    private int lastIterasi;
    private double lastSSE;
    private double[] lastBbtBiasInp;
    private double[][] lastBbtInp;
    private double[] lastBbtHidden;
    private double lastBbtBiasHidden;
    public double[] mse;
    
    @Override
    public void simpan(utama utama) {
        try {
            String sql = "INSERT INTO `pelatihan_jst`(`input_layer`, `node_hidden`, `max_iter`, "
                    + "`target_error`, `kb`, `jarak_data_out`, \n" +
                    "`jml_data_Train`, `last_iterasi`, `rata_error`, `tgl_awal_data_train`, "
                    + "`tgl_akhir_data_train`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, utama.getTxtInputLayer().getText());
            pst.setString(2, utama.getTxtNodeHiddenLayer().getText());
            pst.setString(3, utama.getTxtMaxIterasi().getText());
            pst.setString(4, utama.getTxtTargerError().getText());
            pst.setString(5, utama.getTxtKonstantaBelajar().getText());
            pst.setString(6, utama.getTxtJarakDataOut().getText());
            pst.setString(7, String.valueOf(utama.getTbPelatihan().getRowCount()));
            pst.setString(8, String.valueOf(lastIterasi));
            pst.setString(9, String.valueOf(lastSSE));
            pst.setString(10, modelTrain.getTglAwal());
            pst.setString(11, modelTrain.getTglAkhir());
            pst.executeUpdate();
//            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            con.close(); pst.close();
        } catch (Exception e) {
            System.out.println("error simpan pelatihan : "+e);
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
        utama.getTxtHasilPelatihan().setText(null);
    }
    
    // PEMBUATAN DATA TRAIN
    public double[][] dataTrain(utama utama){
        int inputLayer = Integer.parseInt(utama.getTxtInputLayer().getText());
        int nodeHidden = Integer.parseInt(utama.getTxtNodeHiddenLayer().getText());
        int jarak = Integer.parseInt(utama.getTxtJarakDataOut().getText());
        String sql = "SELECT COUNT(`HARGA`) AS JML FROM `harga_emas` "
                + "WHERE `TANGGAL` BETWEEN '"+modelTrain.getTglAwal()+"' AND '"+modelTrain.getTglAkhir()+"'";
        int jmlData = data.panjangData(sql);
        sql = "SELECT HARGA FROM `harga_emas` WHERE `TANGGAL` BETWEEN '"+modelTrain.getTglAwal()+
                "' AND '"+modelTrain.getTglAkhir()+"'";
        double[] allDataTrain = data.semuaData(jmlData, sql);
        double[][] dataTrain = data.data(allDataTrain, inputLayer, jarak);
        bp.ambilBobot(inputLayer, nodeHidden);
        return dataTrain;
    }
    
    // PROSES PELATIAN    
    public void prosesTrain(utama utama, double[][] dataTrain, String[] parameter){
        bersih(utama);
        DecimalFormat df = new DecimalFormat("#.#############");
        boolean print = false, printAkhir = false;
        
        int iterasi = Integer.parseInt(parameter[0]);
        double targetError = Double.parseDouble(parameter[1]);
        double kb = Double.parseDouble(parameter[2]);
        double nilaiBiasInp = bp.nilaiBiasInput;
        double nilaiBiasHidden = bp.nilaiBiasHidden;
        
        double[][] norDataTrain = bp.normaslisasi(dataTrain);
        double[][] dataTrainInp = data.dataInp(norDataTrain);
        double[] dataTrainOut = data.dataOut(norDataTrain);
        
        double[][] bobotInp = bp.bobotInput;
        double[] bobotBiasInp = bp.bobotBiasInput;
        double[] bobotHidden = bp.bobotHidden;
        double bobotBiasHidden = bp.bobotBiasHidden;
        
        System.out.println("Bobot bias input");
        bp.tampil1D(bobotBiasInp);
        System.out.println("Bobot Input");
        bp.tampil2D(bobotInp);
        System.out.println("Bobot bias hidden");
        System.out.println(bobotBiasHidden);
        System.out.println("Bobot hidden");
        bp.tampil1D(bobotHidden);
        
        double[] actualTrain = new double[dataTrainOut.length];

        int epoch = 0;
        double error = 0.0;
        double kuadratError = 0;
        double jmlKuadratError = 0;
        double mse = 0.0;
        this.mse = new double[iterasi];

        //VARIABEL DATA KE k
        int i = 0;

        // VARIABEL DATA KE k
        double[] x;

        // VARIABEL HIDDEN LAYER
        double[] z_in, z;

        // VARIABEL OUTPUT LAYER
        double y_in, y;

        // VARIABEL DELTA Y
        double deltaY = 0;

        // VARIABEL DELTA Z
        double[] deltaZ = null;

        // VARIABEL BOBOT HIDDEN TO OUTPUT
        double[] deltaBbtHidden, updateBbtHidden = null;
        double deltaBbtBiasHidden, updateBbtBiasHidden = 0;

        // VARIABEL BOBOT INPUT TO HIDDEN
        double[][] deltaBbtInp, updateBbtInp = null;
        double[] deltaBbtBiasInp, updateBbtBiasInp = null;
        
        while(epoch < iterasi){
            epoch++;
            System.out.println("Iterasi ke : "+(epoch));
            //ystem.out.println("-----------------");
            for(int j = 0; j < dataTrainInp.length; j++){

                i = j % dataTrainInp.length;
                x = bp.x(dataTrainInp, i);

                z_in = bp.z_in(bobotBiasInp, bobotInp, x);

                z = bp.z(z_in);

                y_in = bp.y_in(z, bobotBiasHidden, bobotHidden);

                y = bp.y(y_in);
                actualTrain[i] = y;

                error = bp.error(dataTrainOut, actualTrain, i);
                kuadratError = bp.kuadratError(error);
                jmlKuadratError = jmlKuadratError + kuadratError;

                deltaY = bp.deltaY(dataTrainOut, actualTrain, i);
                deltaZ = bp.deltaZ(z, deltaY, bobotHidden);
                
                deltaBbtHidden = bp.deltaBbtHidden(z, deltaY, kb);
                deltaBbtBiasHidden = bp.deltaBbtBiasHidden(nilaiBiasHidden, deltaY, kb);
                updateBbtHidden = bp.updateBbtHidden(bobotHidden, deltaBbtHidden);
                updateBbtBiasHidden = bp.updateBbtBiasHidden(bobotBiasHidden, deltaBbtBiasHidden);
                
                deltaBbtInp = bp.deltaBbtInput(bobotInp, deltaZ, x, kb);
                deltaBbtBiasInp = bp.deltaBbtBiasInp(nilaiBiasInp, deltaZ, kb);
                updateBbtInp = bp.updateBbtInp(bobotInp, deltaBbtInp);
                updateBbtBiasInp = bp.updateBbtBiasInp(bobotBiasInp, deltaBbtBiasInp);
                
                bobotInp = updateBbtInp;
                bobotBiasInp = updateBbtBiasInp;
                bobotHidden = updateBbtHidden;
                bobotBiasHidden = updateBbtBiasHidden;

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
                    System.out.println("  ͦ Informasi error dari output layer : ");
                    System.out.println("\t* Delta ke-1 : "+deltaY);
                    System.out.println("\t  - Perubahan Bobot Bias : "+updateBbtBiasHidden);
                    for(int a = 0; a < updateBbtHidden.length; a++){
                        System.out.println("\t  - Perubahan Bobot Lapisan ("+(a+1)+"-1) : "+updateBbtHidden[a]);
                    }
                    System.out.println("  ͦ Informasi error dari hidden layer : ");
                    for(int a = 0; a < deltaZ.length; a++){
                        System.out.println("\t* Delta ke-"+(a+1)+" : "+deltaZ[a]);
                        System.out.println("\t  - Perubahan Bobot Bias ("+(a+1)+")    : "+updateBbtBiasInp[a]);
                        for(int b = 0; b < updateBbtInp.length; b++){
                            System.out.println("\t  - Perubahan Bobot Input ("+(b+1)+","+(a+1)+") : "+updateBbtInp[b][a]);
                        }
                    }
                }
            }
            mse = bp.mse(actualTrain, dataTrainOut);
            this.mse[epoch - 1] = mse;
            System.out.println("error : "+df.format(mse));
            //System.out.println("");
            if(epoch == iterasi || mse < targetError){
                if(printAkhir){
                    System.out.println("\nͦ Berhenti di Iterasi Ke-"+ (epoch));
                    System.out.println("ͦ MSE Terakhir : "+ df.format(mse));
                    System.out.println("ͦ Bobot Terakhir Input Ke Hidden");
                    System.out.println("ͦ Bobot Akhir input ke hidden : ");
                    for(int a = 0; a < bobotInp.length; a++){
                        for(int b = 0; b < bobotInp[0].length; b++){
                            System.out.print("\t"+bobotInp[a][b]);
                        }
                        System.out.println("");
                    }
                    System.out.println("ͦ Bobot Akhir bias ke hidden : ");
                    for(int a = 0; a < bobotBiasInp.length; a++){
                        System.out.print("\t"+bobotBiasInp[a]);
                    }
                    System.out.println("\nͦ Bobot Akhir hidden ke output : ");
                    for(int a = 0; a < bobotHidden.length; a++){
                        System.out.println("\t"+bobotHidden[a]);
                    }
                    System.out.println("ͦ Bobot Akhir bias ke output : ");
                    System.out.println("\t"+bobotBiasHidden);
                    System.out.println("\n----------------------------------------\n");
                }
//                    for(int a = 0; a < dataTrainOut.length; a++){
//                        System.out.println(backpro.denor(dataTrainOut[a], dataTrain)+" - "+backpro.denor(actualTrain[a], dataTrain));
//                    }
                lastBbtInp = bobotInp;
                lastBbtBiasInp = bobotBiasInp;
                lastBbtBiasHidden = bobotBiasHidden;
                lastBbtHidden = bobotHidden;
                lastSSE = mse;
                lastIterasi = epoch;
                
                utama.getTxtHasilPelatihan().append("         -- Hasil Pelatihan --\n\n");
                utama.getTxtHasilPelatihan().append("Berhenti di iterasi ke-"+epoch);
                utama.getTxtHasilPelatihan().append("\ndengan MSE : "+df.format(mse));
                break;
            }
        }
//        simpan(utama);
//        if(cekBbt() == null){
//            simpanBbt();
//        }else{
//            hapusBbt();
//            simpanBbt();
//        }
    }
    
    public void tampilDataTrain(utama utama, double[][] dataTrain){
        DefaultTableModel model = new DefaultTableModel(){
          @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }  
        };
        for(int i = 0; i < dataTrain[0].length; i++){
            if(i == dataTrain[0].length - 1){
                model.addColumn("Target");
            }else{
                model.addColumn("X"+(i+1));
            }
        }
        Object[] ob = new Object[dataTrain[0].length];
        for(int j = 0; j < dataTrain.length; j++){
            for(int k = 0; k < dataTrain[0].length; k++){
                ob[k] = dataTrain[j][k];
            }
            model.addRow(ob);
        }
        utama.getTbPelatihan().setModel(model);
        main.headerTable(utama.getTbPelatihan());
        TableColumn column;
        utama.getTbPelatihan().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        for(int l = 0; l < utama.getTbPelatihan().getColumnCount(); l++){
            utama.getTbPelatihan().getColumnModel().getColumn(l).setCellRenderer(kanan);
            column = utama.getTbPelatihan().getColumnModel().getColumn(l); 
            column.setPreferredWidth(100);
        }
        tabel.setLebarKolom(utama.getTbPelatihan());
    }
    
    private void simpanBbt(){
        try {
            // SIMPAN BOBOT BIAS INPUT
            for(int i = 0; i < lastBbtBiasInp.length; i++){
                String sql = "INSERT `last_bbt_bias_inp`(`bobot`) VALUES (?)";
                Connection con = koneksi.getKoneksi();
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, String.valueOf(lastBbtBiasInp[i]));
                pst.executeUpdate();
                con.close(); pst.close();
            }
            
            // SIMPAN BOBOT INPUT
            for(int i = 0; i < lastBbtInp.length; i++){
                for(int j = 0; j < lastBbtInp[0].length; j++){
                    String sql = "INSERT `last_bbt_inp`(`bobot`) VALUES (?)";
                    Connection con = koneksi.getKoneksi();
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, String.valueOf(lastBbtInp[i][j]));
                    pst.executeUpdate();
                    con.close(); pst.close();
                }
            }
            
            // SIMPAN BOBOT HIDDEN
            for(int i = 0; i < lastBbtHidden.length; i++){
                String sql = "INSERT `last_bbt_hidden`(`bobot`) VALUES (?)";
                Connection con = koneksi.getKoneksi();
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, String.valueOf(lastBbtHidden[i]));
                pst.executeUpdate();
                con.close(); pst.close();
            }
            
            // SIMPAN BOBOT BIAS HIDDEN
            String sql = "INSERT `last_bbt_bias_hidden`(`bobot`) VALUES (?)";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, String.valueOf(lastBbtBiasHidden));
            pst.executeUpdate();
            con.close(); pst.close();
        } catch (Exception e) {
            System.out.println("error simpanBbt pelatihan : "+e);
        }
    }
    
    private void hapusBbt(){
        try {
            // HAPUS BOBOT BIAS INPUT
            String sql = "DELETE FROM `last_bbt_bias_inp`";
            Connection con = koneksi.getKoneksi();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.executeUpdate();
            con.close(); pst.close();
            
            // HAPUS BOBOT INPUT
            sql = "DELETE FROM `last_bbt_inp`";
            con = koneksi.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.executeUpdate();
            con.close(); pst.close();
            
            // HAPUS BOBOT HIDDEN
            sql = "DELETE FROM `last_bbt_hidden`";
            con = koneksi.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.executeUpdate();
            con.close(); pst.close();
            
            // HAPUS BOBOT BIAS HIDDEN
            sql = "DELETE FROM `last_bbt_bias_hidden`";
            con = koneksi.getKoneksi();
            pst = con.prepareStatement(sql);
            pst.executeUpdate();
            con.close(); pst.close();
        } catch (Exception e) {
            System.out.println("error hapusBbt pelatihan : "+e);
        }
    }
    
    private String cekBbt(){
        String bbt = null;
        try {
            String sql = "SELECT `bobot` FROM `last_bbt_bias_hidden`";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                bbt = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error cekBbt pelatihan : "+e);
        }
        return bbt;
    }
    
    public String getKodePelatihan(){
        String kode = null;
        try {
            String sql = "SELECT `kode_pelatihan` FROM `pelatihan_jst` "
                    + "WHERE `kode_pelatihan` ORDER BY `kode_pelatihan` DESC LIMIT 1";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if(rs.next()){
                kode = rs.getString(1);
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error getKodePelatihan pelatihan : "+e);
        }
        return kode;
    }
}
