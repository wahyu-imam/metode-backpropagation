/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import view.utama;
/**
 *
 * @author Wahyu Imam
 */
public class index {

    public String getTglSekarang(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public void headerTable(JTable table) {
        Font font = new Font("Ebrima", Font.BOLD, 12);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(font);
        header.setBackground(Color.LIGHT_GRAY);
    }
    
    public String formatUang(int uang){
        String formatUang = null;
        DecimalFormat indo = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        indo.setDecimalFormatSymbols(formatRp);
        String money = indo.format(uang);
        String[] split = money.split(",");
        formatUang = split[0];
        return formatUang;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        utama utama = new utama();
        utama.setVisible(true);
    }
    
}
