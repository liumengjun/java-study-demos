package com.three_d.mofangv2;

import java.awt.*;
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.*;

class MyMainFrame extends JFrame
{

	// public TotlePanel totlePanel=new TotlePanel();

	public MyMainFrame()
	{
		// ======================
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());

		JPanel aPanel = new JPanel();
		aPanel.setLayout(new BorderLayout());
		c.add(aPanel, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				MyMainFrame.this.dispose();
				System.exit(0);
			}
		});

		this.setTitle("魔方");

	}

}
