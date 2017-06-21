package gui.swing.table;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;

/*
<APPLET
    CODE = tableadd.class
    WIDTH = 350
    HEIGHT = 280 >
</APPLET>
*/

public class tableadd extends JApplet
{
    Object[] data = new Object[5];

    DefaultTableModel defaulttablemodel = new DefaultTableModel();
    JTable jtable = new JTable(defaulttablemodel);

    public void init() 
    {
        for(int column = 0; column < 5; column++){
            defaulttablemodel.addColumn("Column " + column);
        }

        for(int row = 0; row < 5; row++) {
            for(int column = 0; column < 5; column++) {
                data[column] = "Cell " + row + "," + column;
            }
            defaulttablemodel.addRow(data);
        }
        //jtable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        getContentPane().add(new JScrollPane(jtable), BorderLayout.CENTER);
        getContentPane().add(new jpanel(), BorderLayout.SOUTH);
    }

    class jpanel extends JPanel implements ActionListener
    {
        private JButton jbutton1 = new JButton("Create new row"),
                        jbutton2 = new JButton("Create new column");

        public jpanel() 
        {
            add(jbutton1);
            add(jbutton2);

            jbutton1.addActionListener(this); 
            jbutton2.addActionListener(this); 

        }
        
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() == jbutton1) {
                int numberrows = defaulttablemodel.getRowCount();
                int numbercolumns = defaulttablemodel.getColumnCount();

                Object[] data = new Object[numbercolumns];

                for(int column = 0; column < numbercolumns; column++) {
                    data[column] = "Cell " + numberrows + "," + column;
                }
                defaulttablemodel.addRow(data);

            } else if(e.getSource() == jbutton2) {
                int numberrows = defaulttablemodel.getRowCount();
                int numbercolumns = defaulttablemodel.getColumnCount();
                defaulttablemodel.addColumn("Column " + numbercolumns);

                for(int row = 0; row < numberrows; row++) {
                    defaulttablemodel.setValueAt("Cell " + row + "," + numbercolumns, row, numbercolumns);
                }
                
                jtable.sizeColumnsToFit(0);
            }
        }
    }
}

