/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rendering;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Wahyu Imam
 */
public class kanan extends DefaultTableCellRenderer{
    public Component getTableCellRendererComponent(JTable table, Object values,
            boolean isSelected, boolean hasFocus, int row, int column){
        super.getTableCellRendererComponent(table, values, isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.RIGHT);
        return this;
    }
}
