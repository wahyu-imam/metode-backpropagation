/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Wahyu Imam
 */
public class koneksi {
    Connection con;
    
    public Connection getKoneksi(){
        String username = "root";
        String pass = "";
        String url = "jdbc:mysql://localhost/emas";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, pass);
            //System.out.println("Koneksi Berhasil");
        } catch (Exception e) {
            System.out.println("Gagal Konek : "+e);
        }
        return con;
    }
}
