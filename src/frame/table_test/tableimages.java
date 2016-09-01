package frame.table_test;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/*
<APPLET
    CODE = tableimages.class
    WIDTH = 600
    HEIGHT = 280 >
</APPLET>
*/

public class tableimages extends JApplet
{
    String[] columns = {"Sandwich", "Available", "Price", "Date", "Image"};

    Date date = (new GregorianCalendar(2000, 9, 2)).getTime();

    Object[][] data = {
        {"Ham", new Boolean(false), new Float(4.99), date, new ImageIcon("table.jpg")},

        {"BBQ", new Boolean(true), new Float(5.99), date, new ImageIcon("table.jpg")},

        {"Turkey", new Boolean(false), new Float(4.99), date, new ImageIcon("table.jpg")},

        {"Watercress", new Boolean(true), new Float(4.99), date, new ImageIcon("table.jpg")},

        {"Cheese", new Boolean(false), new Float(4.99), date, new ImageIcon("table.jpg")},

        {"Beef", new Boolean(true), new Float(4.99), date, new ImageIcon("table.jpg")}
    };
    
    JTable jtable = new JTable(new newModel(data, columns));

    public void init() 
    {
        getContentPane().add(new JScrollPane(jtable));
    }
}

class newModel extends DefaultTableModel 
{
    public newModel(Object[][] data, Object[] columns) 
    {
        super(data, columns);
    }

    public boolean isCellEditable(int row, int col) 
    {
        return false;
    }

    public Class getColumnClass(int column) 
    {
        Vector v = (Vector) dataVector.elementAt(0);

        return v.elementAt(column).getClass();
    }
}

