/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import koneksi.koneksi;
import backpro.backpro;
/**
 *
 * @author Wahyu Imam
 */
public class Data {
    
    koneksi db = new koneksi();
    Connection con;
    Statement stm;
    ResultSet rs;
    
    //--------------------------------------- PEMBUATAN DATA ---------------------------------------
    
    // JUMLAH DATA
    public int panjangData(String query){
        int panjangData = 0;
        //String query = "SELECT COUNT(`HARGA`) AS JML FROM `harga_emas` WHERE `TANGGAL` BETWEEN '1979-01-01' AND '2018-12-31'";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(query);
            if(rs.next()){
                panjangData = Integer.parseInt(rs.getString(1));
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
        return panjangData;
    }
    
    // MEMASUKKAN DATA KE ARRAY
    public double[] semuaData(int panjangData, String query){
        double[] semuaData = new double[panjangData];
        int n = 0;
        //String query = "SELECT * FROM `harga_emas` WHERE `TANGGAL` BETWEEN '1979-01-01' AND '2018-12-31'";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(query);
            while(rs.next()){
                semuaData[n] = Double.parseDouble(rs.getString(1));
                n++;
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
        return semuaData;
    }
    
    // MENGUBAH DATA KE 2D
    public double[][] data(double[] semuaData, int inputLayer, int jarakDataOut){
        double[][] data = new double[semuaData.length - (inputLayer - 1 + jarakDataOut)][inputLayer + 1];
        int k;
        for(int i = 0; i < data.length; i++){
            k = i;
            for(int j = 0; j < data[i].length - 1; j++){
                data[i][j] = semuaData[k];
                k++;
            }
            data[i][inputLayer] = semuaData[k + jarakDataOut - 1];
        }
        return data;
    }
    
    // DATA INPUT
    public double[][] dataInp(double[][] data){
        double[][] dataInp  = new double[data.length][data[0].length - 1];
        for(int i = 0; i < dataInp.length; i++){
            for(int j = 0; j < dataInp[0].length; j++){
                dataInp[i][j] = data[i][j];
            }
        }
        return dataInp;
    }
    
    // DATA OUPUT
    public double[] dataOut(double[][] data){
        double[] dataTrainOut = new double[data.length];
        for(int i = 0; i < dataTrainOut.length; i++){
            dataTrainOut[i] = data[i][data[0].length - 1];
        }
        return dataTrainOut;
    }
    
    public static void main(String[] args) {
        Data data = new Data();
        backpro bp = new backpro();
        int inputLayer = bp.inputLayer;
        int jarakDataOut = bp.jarakDataOut;
        int nodeHidden = bp.nodeHidden;
        
        int countDataTrain = data.panjangData("SELECT COUNT(`HARGA`) AS JML FROM `harga_emas` "
                + "WHERE `TANGGAL` BETWEEN '1979-01-01' AND '2018-12-31'");
        
        double[] semuaDataTrain = data.semuaData(countDataTrain, "SELECT HARGA FROM `harga_emas` "
                + "WHERE `TANGGAL` BETWEEN '1979-01-01' AND '2018-12-31'");
        
        double[][] dataTrain = data.data(semuaDataTrain, inputLayer, jarakDataOut);
        bp.tampil2D(dataTrain);
    }
}
