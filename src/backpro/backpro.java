/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backpro;

import Data.Data;
import java.text.DecimalFormat;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import koneksi.koneksi;
import diagram.garis;
/**
 *
 * @author Wahyu Imam
 */
public class backpro {
    DecimalFormat df = new DecimalFormat("#.###");
    koneksi db = new koneksi();
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pst;
    
    public int inputLayer, jarakDataOut, nodeHidden, iterasi;
    public double kb;
    public double sse;
    public double[][] dataTrain, dataTest;
    public double[][] dataTrainInp, dataTestInp;
    public double[] dataTrainOut, dataTestOut;
    public double[][] normalisasiDataTrain, normalisasiDataTest;
    public double[][] bobotInput;
    public double[] bobotHidden;
    public double[] bobotBiasInput;
    public double bobotBiasHidden;
    public double nilaiBiasInput = 1, nilaiBiasHidden = 1;
    
    // TAMPIL ARRAY 2D
    public void tampil2D(double[][] data){
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[0].length; j++){
                System.out.print(data[i][j]+"\t");
            }
            System.out.println("");
        }
    }
    
    // TAMPIL ARRAY 1D
    public void tampil1D(double[] data){
        for(int i = 0; i < data.length; i++){
            System.out.println(data[i]);
        }
    }
    
    // NORMALISASI DATA
    public double[][] normaslisasi(double[][] dataAsli){
        double[][] normalisasi = new double[dataAsli.length][dataAsli[0].length];
        
        // MENCARI NILAI MAX
        double max = 0;
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                if(max < dataAsli[i][j]){
                    max = dataAsli[i][j];
                }
            }
        }
        
        // MENCARI NILAI MIN
        double min = max;
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                if(min > dataAsli[i][j]){
                    min = dataAsli[i][j];
                }
            }
        }
        
        // NORMALISASI
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                //normalisasi[i][j] = 0.9 * (dataAsli[i][j] - min) / (max - min) + 0.1;
                normalisasi[i][j] = ((((dataAsli[i][j] -  min)) / (max - min)) * (0.9 - 0.1)) + 0.1 ;
            }
        }
        return normalisasi;
    }
    
    // DENORMALISASI
    public double denor(double dataNor, double[][] dataAsli){
        
        // MENCARI NILAI MAX
        double max = 0;
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                if(max < dataAsli[i][j]){
                    max = dataAsli[i][j];
                }
            }
        }
        
        // MENCARI NILAI MIN
        double min = max;
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                if(min > dataAsli[i][j]){
                    min = dataAsli[i][j];
                }
            }
        }
        
        // DENORMALISASI
        double denor = Double.parseDouble(df.format((((dataNor - 0.1) / (0.9 - 0.1)) * (max - min)) + min));
        return denor;
    }
    
    // DENOR MASAL
    public double[][] denorMasal(double[][] dataNor, double[][] dataAsli){
        // MENCARI NILAI MAX
        double max = 0;
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                if(max < dataAsli[i][j]){
                    max = dataAsli[i][j];
                }
            }
        }
        
        // MENCARI NILAI MIN
        double min = max;
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                if(min > dataAsli[i][j]){
                    min = dataAsli[i][j];
                }
            }
        }
        
        // DENORMALISASI
        double[][] denor = new double[dataAsli.length][dataAsli[0].length];
        for(int i = 0; i < dataAsli.length; i++){
            for(int j = 0; j < dataAsli[0].length; j++){
                denor[i][j] = Double.parseDouble(df.format((((dataNor[i][j] - 0.1) / (0.9 - 0.1)) * (max - min)) + min));
            }
        }
        return denor;
    }
    
    // CEK JUMLAH DATA BOBOT
    private int cekCountBobotDB(String query){
        int jmlNet = 0;
        //String query = "select count(bobot) as bobot from bobot_input";
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(query);
            if(rs.next()){
                jmlNet = Integer.parseInt(rs.getString(1));
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
        return jmlNet;
    }
    
    // TAMBAH BOBOT JIKA KURANG
    private void tambahBobot(int jmlNet, int jmlNetDB, String query){
        int i = jmlNet - jmlNetDB;
        String nilai;
        for(int j = 0; j < i; j++){
            double rNumb = ((Math.random() * 5 + 1) - 3)/ 10;
            double r = Math.pow(10, 2);
            double bobot = Math.round(rNumb * r) / r;
            if(bobot == 0){
                bobot = 0.1;
            }
            //query = "insert into bobot_bias_inp(bobot) values ('"+bobot+"')";
            nilai = query + " ('"+bobot+"')";
            try {
                con = db.getKoneksi();
                pst = con.prepareStatement(nilai);
                pst.execute();
                con.close(); pst.close();
            } catch (Exception e) {
                System.out.println("error : "+e);
            }
        }
    }
    
    // TAMPUNG BOBOT KE ARRAY
    private double[] tampungBobot(String query, int net){
        //String query = "select bobot from bobot_bias_inp LIMIT '"+jmlNetBiasInp+"'";
        double[] bobot = new double[net];
        int index = 0;
        try {
            con = db.getKoneksi();
            stm = con.createStatement();
            rs = stm.executeQuery(query);
            while(rs.next()){
                bobot[index] = Double.valueOf(rs.getString(1));
                index++;
            }
        } catch (Exception e) {
            System.out.println("error : "+e);
        }
        return bobot;
    }
    
    // BOBOT AWAL AMBIL DARI DB
    public void ambilBobot(int nodeInput, int nodeHidden){
        int jmlNetInp = nodeInput * nodeHidden;
        int jmlNetBiasInp = nodeHidden, jmlNetHidden = nodeHidden, jmlNetBiasHidden = 1;
        int jmlNetInpDB = 0, jmlNetHiddenDB = 0, jmlNetBiasInpDB = 0, jmlNetBiasHiddenDB = 0;
        
        // CEK PANJANG BOBOT INPUT
        jmlNetInpDB = cekCountBobotDB("select count(bobot) as bobot from bobot_input");
        
        // CEK PANJANG BOBOT BIAS INPUT
        jmlNetBiasInpDB = cekCountBobotDB("select count(bobot) as bobot from bobot_bias_inp");
        
        // CEK PANJANG BOBOT HIDDEN
        jmlNetHiddenDB = cekCountBobotDB("select count(bobot) as bobot from bobot_hidden");
        
        // CEK PANJANG BOBOT BIAS HIDDEN
        jmlNetBiasHiddenDB = cekCountBobotDB("select count(bobot) as bobot from bobot_bias_hidden");
        
        // TAMBAH BOBOT DI DB JIKA KURANG (BIAS INPUT)
        if(jmlNetBiasInpDB < jmlNetBiasInp){
            tambahBobot(jmlNetBiasInp, jmlNetBiasInpDB, "insert into bobot_bias_inp(bobot) values");
        }
        
        // TAMBAH BOBOT DI DB JIKA KURANG (INPUT)
        if(jmlNetInpDB < jmlNetInp){
            tambahBobot(jmlNetInp, jmlNetInpDB, "insert into bobot_input(bobot) values");
        }
        
        // TAMBAH BOBOT DI DB JIKA KURANG (BIAS HIDDEN)
        if(jmlNetBiasHiddenDB < jmlNetBiasHidden){
            tambahBobot(jmlNetHidden, jmlNetHiddenDB, "insert into bobot_bias_hidden(bobot) values");
        }
        
        // TAMBAH BOBOT DI DB JIKA KURANG (HIDDEN)
        if(jmlNetHiddenDB < jmlNetHidden){
            tambahBobot(jmlNetHidden, jmlNetHiddenDB, "insert into bobot_hidden(bobot) values");
        }
        
        double[] bbtInp, bbtBiasInp, bbtHidden, bbtBiasHidden;
        
        // TAMPUNG BOBOT BIAS INPUT
        bbtBiasInp = tampungBobot("select bobot from bobot_bias_inp LIMIT "+jmlNetBiasInp, jmlNetBiasInp);
        
        // TAMPUNG BOBOT INPUT
        bbtInp = tampungBobot("SELECT `bobot` FROM `bobot_input` LIMIT "+jmlNetInp, jmlNetInp);
        
        // TAMPUNG BOBOT BIAS HIDDEN
        bbtBiasHidden = tampungBobot("SELECT `bobot` FROM `bobot_bias_hidden` LIMIT "+jmlNetBiasHidden, jmlNetBiasHidden);
        
        // TAMPUNG BOBOT HIDDEN
        bbtHidden = tampungBobot("SELECT `bobot` FROM `bobot_hidden` LIMIT "+jmlNetHidden, jmlNetHidden);
        
        bobotInput = new double[nodeInput][nodeHidden];
        bobotBiasInput = new double[nodeHidden];
        bobotHidden = new double[nodeHidden];
        bobotBiasHidden = bbtBiasHidden[0];
        
        System.arraycopy(bbtBiasInp, 0, bobotBiasInput, 0, bobotBiasInput.length);
        int k = 0;
        for(int i = 0; i < bobotInput.length; i++){
            for(int j = 0; j < bobotInput[0].length; j++){
                bobotInput[i][j] = bbtInp[k];
                k++;
            }
        }
        System.arraycopy(bbtHidden, 0, bobotHidden, 0, bobotHidden.length);
    }
    
    // DATA INPUT LAYER
    public double[] x(double[][] normalisasi, int i){
        double[] x = new double[normalisasi[0].length];
        for(int j = 0; j < x.length; j++){
            x[j] = normalisasi[i][j];
        }
        return x;
    }
    
    // HITUNG Z_IN
    public double[] z_in(double[] bobotBiasInput, double[][] bobotInput, double[] x){
        double[] z_in = new double[bobotInput[0].length];
        for(int j = 0; j < z_in.length; j++){
            for(int k = 0; k < bobotInput.length; k++){
                z_in[j] = z_in[j] + (bobotInput[k][j] * x[k]);
            }
            z_in[j] = z_in[j] + bobotBiasInput[j];
        }
        return z_in;
    }
    
    // HITUNG Z
    public double[] z(double[] z_in){
        double[] z = new double[z_in.length];
        for(int j = 0; j < z.length; j++){
            z[j]=1/(1+Math.exp(-(z_in[j])));
        }
        return z;
    }
    
    // HITUNG Y_IN
    public double y_in(double[] z, double bobotBiasHidden,double[] bobotHidden){
        double y_in = 0;
        for(int j = 0; j < bobotHidden.length; j++){
            y_in = y_in + (bobotHidden[j] * z[j]);
        }
        y_in = y_in + bobotBiasHidden;
        return y_in;
    }
    
    // HITUNG Y
    public double y(double y_in){
        double y=1/(1+Math.exp(-(y_in)));
        return y;
    }
    
    // HITUNG ERROR
    public double error(double[] dataTrainOut, double[] actualOutput, int i){
        //double e = 0.5 * Math.pow(dataTrainOut[i] - actualOutput[i], 2);
        //double e = Math.abs(dataTrainOut[i] - actualOutput[i]);
        //double e = Math.pow(dataOut[k] - actualTrainOutput[k], 2);
        double e = dataTrainOut[i] - actualOutput[i];
        return e;
    }
    
    // HITUNG KUADRAT ERROR
    public double kuadratError(double error){
        //double e = 0.5 * Math.pow(dataTrainOut[i] - actualOutput[i], 2);
        //double e = Math.abs(dataTrainOut[i] - actualOutput[i]);
        //double e = Math.pow(dataOut[k] - actualTrainOutput[k], 2);
        double e = Math.pow(error, 2);
        return e;
    }
    
    // HITUNG DELTA Y
    public double deltaY(double[] dataTrainOut, double[] actualOutput, int i){
        double deltaY = (dataTrainOut[i] - actualOutput[i]) * actualOutput[i] * (1 - actualOutput[i]);
        return deltaY;
    }
    
    // HITUNG DELTA BOBOT HIDDEN TO OUTPUT
    public double[] deltaBbtHidden(double[] z, double deltaY, double kb){
        double[] deltaBbtHidden = new double[z.length];
        for(int j = 0; j < deltaBbtHidden.length; j++){
            deltaBbtHidden[j] = z[j] * deltaY * kb;
        }
        return deltaBbtHidden;
    }
    
    // HITUNG DELTA BOBOT BIAS HIDDEN TO OUTPUT
    public double deltaBbtBiasHidden(double nilaiBiasHidden, double deltaY, double kb){
        double deltaBbtBiasHidden = nilaiBiasHidden * deltaY * kb;
        return  deltaBbtBiasHidden;
    }
    
    // BOBOT BARU HIDDEN TO OUTPUT
    public double[] updateBbtHidden(double[] bobotHidden, double[] deltaBbtHidden){
        double[] updateBbtHidden = new double[deltaBbtHidden.length];
        for(int j = 0; j < updateBbtHidden.length; j++){
            updateBbtHidden[j] = bobotHidden[j] + deltaBbtHidden[j];
        }
        return updateBbtHidden;
    }
    
    // BOBOT BIAS BARU HIDDEN TO OUTPUT
    public double updateBbtBiasHidden(double bbtBiasHidden, double deltaBbtBiasHidden){
        double updateBbtBiasHidden = bbtBiasHidden + deltaBbtBiasHidden;
        return updateBbtBiasHidden;
    }
    
    // HITUNG DELTA Z
    public double[] deltaZ(double[] z, double deltaY, double[] bobotHidden){
        double[] deltaZ = new double[z.length];
        for(int j = 0; j < deltaZ.length; j++){
            deltaZ[j] = (deltaY * bobotHidden[j]) * z[j] * (1 - z[j]);
        }
        return deltaZ;
    }
    
    // HITUNG DELTA BOBOT INPUT TO HIDDEN
    public double[][] deltaBbtInput(double[][] bobotInput, double[] deltaZ, double[] x, double kb){
        double[][] deltaBbtInput = new double[bobotInput.length][bobotInput[0].length];
        for(int j = 0; j < deltaBbtInput.length; j++){
            for(int k = 0; k < deltaBbtInput[0].length; k++){
                deltaBbtInput[j][k] = x[j] * deltaZ[k] * kb;
            }
        }
        return deltaBbtInput;
    }
    
    // HITUNG DELTA BOBOT BIAS INPUT TO HIDDEN
    public double[] deltaBbtBiasInp(double nilaiBiasInput, double[] deltaZ, double kb){
        double[] deltaBbtBiasInp = new double[deltaZ.length];
        for(int i = 0; i < deltaBbtBiasInp.length; i++){
            deltaBbtBiasInp[i] = nilaiBiasInput * deltaZ[i] * kb;
        }
        return deltaBbtBiasInp;
    }
    
    // BOBOT BARU INPUT TO HIDDEN
    public double[][] updateBbtInp(double[][] bobotInput, double[][] deltaBbtInp){
        double[][] updateBbtInp = new double[deltaBbtInp.length][deltaBbtInp[0].length];
        for(int j = 0; j < updateBbtInp.length; j++){
            for(int k = 0; k < updateBbtInp[0].length; k++){
                updateBbtInp[j][k] = bobotInput[j][k] + deltaBbtInp[j][k];
            }
        }
        return updateBbtInp;
    }
    
    // BOBOT BIAS BARU INPUT TO HIDDEN
    public double[] updateBbtBiasInp(double[] bbtBiasInp, double[] deltaBbtBiasInp){
        double[] updateBbtBiasInp = new double[deltaBbtBiasInp.length];
        for(int i = 0; i < updateBbtBiasInp.length; i++){
            updateBbtBiasInp[i] = bbtBiasInp[i] + deltaBbtBiasInp[i];
        }
        return updateBbtBiasInp;
    }
    
    //HITUNG MSE
    public double mse(double[] actual, double[] target){
        double error = 0;
        double mse = 0;
        for(int i=0;i<actual.length;i++){
            error = error + (Math.pow((target[i]-actual[i]),2));
        }
        mse = error/actual.length;
        return mse;
    }
    
    public double sse(double[] actual, double[] target){
        double error = 0;
        double sse = 0;
        for(int i=0;i<actual.length;i++){
            error = error + Math.abs(((target[i]-actual[i])));
        }
        sse = error/actual.length;
        return sse;
    }
    
    // HITUNG MAPE
    public double mape(double[] actual, double[] target){
        double err = 0;
        for(int i = 0; i < target.length; i++){
            err = err + Math.abs(actual[i] - target[i]) / actual[i] * 100;
        }
        double mape = err / target.length;
        return mape;
    }
    
    //HITUNG NILAI AKURASI DENGAN MSE
    public double akurat(double mse){
        double akurat = 1/(1+mse) * 100;
        return akurat;
    }
    
    // HITUNG AKURASI DENGAN MAPE
    public double akurasiMAPE(double mape){
        double akurasi = 100 - mape;
        return akurasi;
    }
    
    // FEEDFORWARD
    public double[] feedForward(double[][] data, double[] bbtBiasInp, double[][] bobotInput, 
            double bbtBiasHidden, double[] bobotHidden){
        int i = 0;
        double[] x, z_in, z;
        double y_in, y;
        double[] actual = new double[data.length];
        for(int a = 0; a < data.length; a++){
            i = a % data.length;
            x = x(data, i);
            z_in = z_in(bbtBiasInp, bobotInput, x);
            z = z(z_in);
            y_in = y_in(z, bobotBiasHidden, bobotHidden);
            y = y(y_in);
            actual[a] = y;
        }
        return actual;
    }
    
    public static void main(String[] args) {
        DecimalFormat df2 = new DecimalFormat("#.#############");
        DecimalFormat df3 = new DecimalFormat("#.####");
        backpro backpro = new backpro();
        Data data = new Data();
        garis g = new garis();
        boolean print = false, printAkhir = false, data1 = true, data2 = false, test = true;
        
        int inputLayer = 14;
        int nodeHidden = 15;
        int jarakDataOut = 1;
        int iterasi = 200;
        double sse = 0.00001;
        double kb = 0.1;
        double nilaiBiasInp = backpro.nilaiBiasInput;
        double nilaiBiasHidden = backpro.nilaiBiasHidden;
        
        if(data1){
            int countDataTrain = data.panjangData("SELECT COUNT(`HARGA`) AS JML FROM `harga_emas` "
                    + "WHERE `TANGGAL` BETWEEN '1979-01-01' AND '2015-12-31'");
            double[] semuaDataTrain = data.semuaData(countDataTrain, "SELECT * FROM `harga_emas` "
                    + "WHERE `TANGGAL` BETWEEN '1979-01-01' AND '2015-12-31'");
            double[][] dataTrain = data.data(semuaDataTrain, inputLayer, jarakDataOut);
            double[][] norDataTrain = backpro.normaslisasi(dataTrain);
            double[][] dataTrainInp = data.dataInp(norDataTrain);
            double[] dataTrainOut = data.dataOut(norDataTrain);

            int countDataTest = data.panjangData("SELECT COUNT(`HARGA`) AS JML FROM `harga_emas` "
                    + "WHERE `TANGGAL` BETWEEN '2016-01-01' AND '2019-12-31'");
            double[] semuaDataTest = data.semuaData(countDataTest, "SELECT * FROM `harga_emas` "
                    + "WHERE `TANGGAL` BETWEEN '2016-01-01' AND '2019-12-31'");
            double[][] dataTest = data.data(semuaDataTest, inputLayer, jarakDataOut);
            double[][] norDataTest = backpro.normaslisasi(dataTest);
            double[][] dataTestInp = data.dataInp(norDataTest);
            double[] dataTestOut = data.dataOut(norDataTest);

            backpro.ambilBobot(inputLayer, nodeHidden);
            double[][] bobotInp = backpro.bobotInput;
            double[] bobotBiasInp = backpro.bobotBiasInput;
            double[] bobotHidden = backpro.bobotHidden;
            double bobotBiasHidden = backpro.bobotBiasHidden;

            double[] actualTrain = new double[dataTrainOut.length];
            double[] actualTest = new double[dataTestOut.length];

            int epoch = 0;
            double error = 0.0;
            double kuadratError = 0;
            double jmlKuadratError = 0;
            double mse = 0.0;
            double[] mse2 = new double[iterasi];

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
                //System.out.println("Iterasi ke : "+(epoch));
                //ystem.out.println("-----------------");
                for(int j = 0; j < dataTrainInp.length; j++){
                    
                    i = j % dataTrainInp.length;
                    x = backpro.x(dataTrainInp, i);
                    
                    z_in = backpro.z_in(bobotBiasInp, bobotInp, x);
                    
                    z = backpro.z(z_in);
                    
                    y_in = backpro.y_in(z, bobotBiasHidden, bobotHidden);
                    
                    y = backpro.y(y_in);
                    actualTrain[i] = y;
                    
                    error = backpro.error(dataTrainOut, actualTrain, i);
                    
                    deltaY = backpro.deltaY(dataTrainOut, actualTrain, i);
    //                System.out.println("Delta Y = "+deltaY);
    //
    //                System.out.println("\nDelta Z\n------------------------");
                    deltaZ = backpro.deltaZ(z, deltaY, bobotHidden);
    //                for(int j = 0; j < deltaZ.length; j++){
    //                    System.out.println("Z["+(j+1)+"] = "+deltaZ[j]);
    //                }
    //                
    //                System.out.println("\nBobot Baru Hidden To Output\n------------------------");
                    deltaBbtHidden = backpro.deltaBbtHidden(z, deltaY, kb);
                    deltaBbtBiasHidden = backpro.deltaBbtBiasHidden(nilaiBiasHidden, deltaY, kb);
                    updateBbtHidden = backpro.updateBbtHidden(bobotHidden, deltaBbtHidden);
                    updateBbtBiasHidden = backpro.updateBbtBiasHidden(bobotBiasHidden, deltaBbtBiasHidden);
    //                for(int j = 0; j < updateBbtHidden.length; j++){
    //                    System.out.println("W["+(j+1)+"] = "+updateBbtHidden[j]);
    //                }
    //                
    //                System.out.println("\nBobot Baru Input To Hidden\n------------------------");
                    deltaBbtInp = backpro.deltaBbtInput(bobotInp, deltaZ, x, kb);
                    deltaBbtBiasInp = backpro.deltaBbtBiasInp(nilaiBiasInp, deltaZ, kb);
                    updateBbtInp = backpro.updateBbtInp(bobotInp, deltaBbtInp);
                    updateBbtBiasInp = backpro.updateBbtBiasInp(bobotBiasInp, deltaBbtBiasInp);
    //                for(int j = 0; j < updateBbtInp.length; j++){
    //                    for(int k = 0; k < updateBbtInp[0].length; k++){
    //                        System.out.println("W["+(j+1)+"]["+(k+1)+"] = "+updateBbtInp[j][k]);
    //                    }
    //                }
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
                mse = backpro.sse(actualTrain, dataTrainOut);
                mse2[epoch -1] = mse;
                //System.out.println("MSE : "+df2.format(mse));
                //System.out.println("");
                if(epoch == iterasi){
                    if(printAkhir){
                        System.out.println("\nͦ Berhenti di Iterasi Ke-"+ (epoch));
                        System.out.println("ͦ SSE Terakhir : "+ df2.format(mse));
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
                        System.out.println("ͦ Bobot Akhir hidden ke output : ");
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
                    break;
                }
            }
            
            g.garisError(mse2);
            if(test){
                System.out.println("-------------------------------- DATA TEST -------------------------------- \n");
    //            for(int k = 0; k < 10; k++){
    //                System.out.print((k+1)+".\t");
    //                for(int j = 0; j < dataTestInp[0].length; j++){
    //                    System.out.print(dataTestInp[k][j]+"\t");
    //                }
    //                System.out.println("| "+dataTestOut[k]);
    //            }
    //            System.out.println(".\n.\n.");
    //            for(int k = (dataTestInp.length - 1); k < dataTestInp.length; k++){
    //                System.out.print((k+1)+".\t");
    //                for(int j = 0; j < dataTestInp[0].length; j++){
    //                    System.out.print(dataTestInp[k][j]+"\t");
    //                }
    //                System.out.println("| "+dataTestOut[k]);
    //            }
    //            System.out.println("\n\n");
    //            for(int a = 0; a < dataTestOut.length; a++){
    //                actualTest[a] = backpro.feedForward(iterasi, dataTestInp, bobotBiasInp, bobotInp, bobotBiasHidden, bobotHidden);
    //            }
                actualTest = backpro.feedForward(dataTestInp, bobotBiasInp, bobotInp, bobotBiasHidden, bobotHidden);
                double[] denorActual = new double[actualTest.length];
                double[] denorTarget = new double[dataTestOut.length];
                for(int a = 0; a < denorTarget.length; a++){
                    denorTarget[a] = backpro.denor(dataTestOut[a], dataTest);
                    denorActual[a] = backpro.denor(actualTest[a], dataTest);
                    //System.out.println(denorTarget[a]+" - "+denorActual[a]);
                }
//                System.out.println("\n\n");
                //double sse = backpro.sse(denorActual, denorTarget);
                double akurasi = backpro.akurat(mse);
                double e = backpro.mse(actualTest, dataTestOut);
                double mape = backpro.mape(denorActual, denorTarget);
                double akurasiMAPE = backpro.akurasiMAPE(mape);
                //System.out.println("\n"+df2.format(e));
//                System.out.println("MSE : "+ df2.format(sse));
//                System.out.println("Akurasi : "+ df2.format(akurasi));
//                System.out.println("MAPE : "+ df2.format(mape));
//                System.out.println("Akurasi MAPE : "+ df2.format(akurasiMAPE));
                g.garisTargetActual(dataTestOut, actualTest);
            }
        }
        
        if(data2){
            double mse = 0.0;
            double[][] dataTrainInp = {{0,0},
                                    {0,1},
                                    {1,0},
                                    {1,1}};
            double[] dataTrainOut = {0,1,1,0};
            double[] bbtBiasInp = {0.7496, 0.3796, 0.7256, 0.1628};
            double[][] bbtInp = {{0.9562, 0.7762, 0.1623, 0.2886},
                                 {0.1962, 0.6133, 0.0311, 0.9711}};
            double bbtBiasHidden = 0.9505;
            double[] bbtHidden = {0.228, 0.9585, 0.6799, 0.055};
            
//            for(int i = 0; i < bbtBiasInp.length; i++){
//                double rNumb = ((Math.random() * 5 + 1) - 3)/ 10;
//                double r = Math.pow(10, 2);
//                bbtBiasInp[i] = Math.round(rNumb * r) / r;
//                if(bbtBiasInp[i] == 0){
//                    bbtBiasInp[i] = 0.1;
//                }
//            }
            
//            for(int i = 0; i < bbtInp.length; i++){
//                for(int j = 0; j < bbtInp[0].length; j++){
//                    double rNumb = ((Math.random() * 5 + 1) - 3)/ 10;
//                    double r = Math.pow(10, 2);
//                    bbtInp[i][j] = Math.round(rNumb * r) / r;
//                    if(bbtInp[i][j] == 0){
//                        bbtInp[i][j] = 0.1;
//                    }
//                }
//            }
            
//            for(int i = 0; i < bbtHidden.length; i++){
//                double rNumb = ((Math.random() * 5 + 1) - 3) / 10;
//                double r = Math.pow(10, 2);
//                bbtHidden[i] = Math.round(rNumb * r) / r;
//                if(bbtHidden[i] == 0){
//                    bbtHidden[i] = 0.1;
//                }
//            }
            
//            double rNumb = ((Math.random() * 5 + 1) - 3) / 10;
//            double r = Math.pow(10, 2);
//            bbtBiasHidden = Math.round(rNumb * r) / r;
//            if(bbtBiasHidden == 0){
//                bbtBiasHidden = 0.1;
//            }
            
            double[] actual = new double[dataTrainOut.length];
            
            int epoch = 0;
            double error = 0.0;
            double kuadratError = 0;
            double jmlKuadratError = 0;
            
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
                System.out.println("-----------------");
                for(int j = 0; j < dataTrainInp.length; j++){
                    
                    i = j % dataTrainInp.length;
                    x = backpro.x(dataTrainInp, i);
                    
                    z_in = backpro.z_in(bbtBiasInp, bbtInp, x);
                    
                    z = backpro.z(z_in);
                    
                    y_in = backpro.y_in(z, bbtBiasHidden, bbtHidden);
                    
                    y = backpro.y(y_in);
                    actual[i] = y;
                    
                    error = backpro.error(dataTrainOut, actual, i);
                    kuadratError = backpro.kuadratError(error);
                    jmlKuadratError = jmlKuadratError + kuadratError;
                    
                    deltaY = backpro.deltaY(dataTrainOut, actual, i);
    //                System.out.println("Delta Y = "+deltaY);
    //
    //                System.out.println("\nDelta Z\n------------------------");
                    deltaZ = backpro.deltaZ(z, deltaY, bbtHidden);
    //                for(int j = 0; j < deltaZ.length; j++){
    //                    System.out.println("Z["+(j+1)+"] = "+deltaZ[j]);
    //                }
    //                
    //                System.out.println("\nBobot Baru Hidden To Output\n------------------------");
                    deltaBbtHidden = backpro.deltaBbtHidden(z, deltaY, kb);
                    deltaBbtBiasHidden = backpro.deltaBbtBiasHidden(nilaiBiasHidden, deltaY, kb);
                    updateBbtHidden = backpro.updateBbtHidden(bbtHidden, deltaBbtHidden);
                    updateBbtBiasHidden = backpro.updateBbtBiasHidden(bbtBiasHidden, deltaBbtBiasHidden);
    //                for(int j = 0; j < updateBbtHidden.length; j++){
    //                    System.out.println("W["+(j+1)+"] = "+updateBbtHidden[j]);
    //                }
    //                
    //                System.out.println("\nBobot Baru Input To Hidden\n------------------------");
                    deltaBbtInp = backpro.deltaBbtInput(bbtInp, deltaZ, x, kb);
                    deltaBbtBiasInp = backpro.deltaBbtBiasInp(nilaiBiasInp, deltaZ, kb);
                    updateBbtInp = backpro.updateBbtInp(bbtInp, deltaBbtInp);
                    updateBbtBiasInp = backpro.updateBbtBiasInp(bbtBiasInp, deltaBbtBiasInp);
    //                for(int j = 0; j < updateBbtInp.length; j++){
    //                    for(int k = 0; k < updateBbtInp[0].length; k++){
    //                        System.out.println("W["+(j+1)+"]["+(k+1)+"] = "+updateBbtInp[j][k]);
    //                    }
    //                }
                    mse = backpro.mse(actual, dataTrainOut);
                    bbtInp = updateBbtInp;
                    bbtBiasInp = updateBbtBiasInp;
                    bbtHidden = updateBbtHidden;
                    bbtBiasHidden = updateBbtBiasHidden;
                    
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
                        System.out.println("\n  ͦ Bobot Akhir input ke hidden : ");
                        for(int a = 0; a < bbtInp.length; a++){
                            for(int b = 0; b < bbtInp[0].length; b++){
                                System.out.print("\t"+bbtInp[a][b]);
                            }
                            System.out.println("");
                        }
                        System.out.println("\n  ͦ Bobot Akhir bias ke hidden : ");
                        for(int a = 0; a < bbtBiasInp.length; a++){
                            System.out.print("\t"+bbtBiasInp[a]);
                        }
                        System.out.println("\n  ͦ Bobot Akhir hidden ke output : ");
                        for(int a = 0; a < bbtHidden.length; a++){
                            System.out.println("\t"+bbtHidden[a]);
                        }
                        System.out.println("\n  ͦ Bobot Akhir bias ke output : ");
                        System.out.println("\t"+bbtBiasHidden);
                        System.out.println("");
                    }
                }
                System.out.println("MSE : "+df2.format(mse));
                System.out.println("-----------------");
                //System.out.println("");
                if(mse < sse){
                    break;
                }
            }
            System.out.println("target\tactual");
            System.out.println("------------------------");
            for(int a = 0; a < dataTrainOut.length; a++){
                System.out.println(dataTrainOut[a]+"\t"+df2.format(actual[a]));
            }
        }
    }
}
