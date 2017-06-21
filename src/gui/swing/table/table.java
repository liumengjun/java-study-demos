package gui.swing.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/*
<APPLET
    CODE = table.class
    WIDTH = 350
    HEIGHT = 280 >
</APPLET>
*/

public class table extends JApplet
{
    Object[] data = new Object[5];

    DefaultTableModel defaulttablemodel = new DefaultTableModel();
    JTable jtable = new JTable(defaulttablemodel);

    public void init() 
    {
        for(int column = 0; column < 5; column++){
            defaulttablemodel.addColumn("Column " + column);
        }

        for(int row = 0; row < 10; row++) {
            for(int column = 0; column < 5; column++) {
                data[column] = "Cell " + row + "," + column;
            }
            defaulttablemodel.addRow(data);
        }
        getContentPane().add(new JScrollPane(jtable));
    }
}

