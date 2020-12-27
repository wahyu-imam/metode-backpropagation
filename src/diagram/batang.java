/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagram;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import koneksi.koneksi;

/**
 *
 * @author Wahyu Imam
 */
public class batang {
    koneksi koneksi = new koneksi();
    String sql = "SELECT `jarak_data_out`, MIN(error) FROM `pengujian_jst`, `pelatihan_jst`\n" +
                "WHERE `pelatihan_jst`.`kode_pelatihan`=`pengujian_jst`.`kode_pelatihan`\n" +
                "GROUP BY `jarak_data_out`";
    
    public int jml(){
        int jml = 0;
        try {
            String sql2 = "SELECT `jarak_data_out` FROM `pengujian_jst`, `pelatihan_jst`\n" +
                "WHERE `pelatihan_jst`.`kode_pelatihan`=`pengujian_jst`.`kode_pelatihan`\n" +
                "GROUP BY `jarak_data_out` ORDER BY `jarak_data_out` DESC LIMIT 1";
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql2);
            while(rs.next()){
                jml = Integer.parseInt(rs.getString(1));
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error jml batang : "+e);
        }
        return jml;
    }
    
    public double[][] data(int jml){
        double[][] data = new double[jml+1][2];
        try {
            Connection con = koneksi.getKoneksi();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
               for(int j = 0; j < data.length; j++){
                   int i = Integer.parseInt(rs.getString(1));
                   if(j == i){
                       data[j][0] = Double.parseDouble(rs.getString(1));
                       data[j][1] = Double.parseDouble(rs.getString(2));
                       //System.out.println(data[j][0]+"\t"+data[j][1]);
                   }
               }
            }
            con.close(); stm.close(); rs.close();
        } catch (Exception e) {
            System.out.println("error jarakData batang : "+e);
        }
        return data;
    }
    
    public void perBandinganOutput(double[][] data){
        DefaultCategoryDataset line = new DefaultCategoryDataset();
        for(int i = 0; i < data.length; i++){
            for(int j = 0; j < data[0].length; j++){
                if(data[i][0] != 0.0){
                    line.setValue(data[i][1], "Min Error", ""+data[i][0]);
                }
            }
        }
        JFreeChart chart = ChartFactory.createBarChart3D("Grafik Perbandingan Jarak Data Output", "Jarak Data", "Min Error", line);
        ChartFrame frame = new ChartFrame("frame chart", chart);
        frame.setVisible(true);
        frame.setBounds(500, 200, 1300, 500);
        frame.setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        batang btg = new batang();
        btg.perBandinganOutput(btg.data(btg.jml()));
    }
}
