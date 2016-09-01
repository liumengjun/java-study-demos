package frame.scroll;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/*
<APPLET
    CODE=viewport.class
    WIDTH=700
    HEIGHT=200 >
</APPLET>
*/

public class viewport extends JApplet 
{
    public viewport() 
    {
        Container contentPane = getContentPane();

        JViewport jviewport = new JViewport();
        JPanel jpanel = new JPanel();

        jpanel.add(new JLabel(new ImageIcon("viewport.jpg")));

        jviewport.setView(jpanel);

        contentPane.add(new JScrollPane(jviewport), BorderLayout.CENTER);
        contentPane.add(new buttonpanel(jviewport), BorderLayout.SOUTH);
    }
}

class buttonpanel extends JPanel implements ActionListener
{ 
    JViewport jviewport;

    JButton button1 = new JButton("Scroll left");
    JButton button2 = new JButton("Scroll up");
    JButton button3 = new JButton("Scroll down");
    JButton button4 = new JButton("Scroll right");

    public buttonpanel(JViewport vport) 
    {
        jviewport = vport;

        add(button1);
        add(button2);
        add(button3);
        add(button4);

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) 
    {
        Point position = jviewport.getViewPosition();

        if(e.getSource() == button1) position.x += 10;    
        else if(e.getSource() == button2) position.y += 10;    
        else if(e.getSource() == button3) position.y -= 10;    
        else if(e.getSource() == button4) position.x -= 10;    

        jviewport.setViewPosition(position);
    }
}
