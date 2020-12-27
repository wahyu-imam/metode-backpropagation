package diagram;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Wahyu Imam
 */
public class garis {
    public void garisTargetActual(double[] target, double[] actual){
        DefaultCategoryDataset line = new DefaultCategoryDataset();
        for(int i = 0; i < target.length; i++){
            line.setValue(target[i], "Target", ""+(i+1));
            line.setValue(actual[i], "Actual", ""+(i+1));
        }
        JFreeChart chart = ChartFactory.createLineChart("Grafik Perbagingan Target dan Actual", 
                "Iterasi", "Output", line);
        ChartFrame frame = new ChartFrame("frame chart", chart);
        frame.setVisible(true);
        frame.setBounds(500, 200, 1300, 500);
        frame.setLocationRelativeTo(null);
    }
    
    public void garisError(double[] error){
        DefaultCategoryDataset line = new DefaultCategoryDataset();
        for(int i = 0; i < error.length; i++){
            line.setValue(error[i], "Error", ""+(i+1));
        }
        JFreeChart chart = ChartFactory.createLineChart("Grafik Error", "Iterasi", "Output", line);
        ChartFrame frame = new ChartFrame("frame chart", chart);
        frame.setVisible(true);
        frame.setBounds(500, 200, 1300, 500);
        frame.setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        double[] target = {0.12421, 0.1232, 0.2343, 0.3423, 0.12343};
        double[] actual = {0.11343, 0.2311, 0.2434, 0.2432, 0.11143};
        garis g = new garis();
        g.garisTargetActual(target, actual);
        g.garisError(target);
    }
}
