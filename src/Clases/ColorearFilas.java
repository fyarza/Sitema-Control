
package Clases;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


public class ColorearFilas extends DefaultTableCellRenderer {
    

@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean Selected, boolean hasFocus, int row, int col){
    
super.getTableCellRendererComponent(table, value, Selected, hasFocus, row, col);


    switch (table.getValueAt(row, 2).toString()) {
        case "S":
            //setBackground(Color.BLUE);
            setForeground(Color.BLUE);
            break;
        case "P":
            setForeground(Color.GREEN);
            break;
        case "E":
            
            setForeground(Color.RED);
            break;
        default:
            break;
    }
 return this;

}
}
