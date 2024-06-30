package com.three_d.mofangv2;//Finishing www.codefans.net

import java.awt.*;
import java.applet.Applet;
import java.awt.BorderLayout;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;

class PanelAddedScene3D extends Applet
{
	// 用线程方式启动
	// 用Applet可以当做面板加到窗体中，还可以作为网页
	// 该类代表了一个状态的魔方
	int layerNumber = 8;

	SimpleUniverse u;

	Canvas3D c;

	BranchGroup[] branchGroupArray = new BranchGroup[this.layerNumber];

	boolean[] controlArray;

	String[] typeArray;

	PanelAddedScene3D()
	{
		// public static

		boolean[] getControlArray = { true, true, true, true, true, true, true,
				true };
		this.controlArray = getControlArray;
	}

	void prepare()
	{
		// Applet thisApplet=this;
		// -----------------
		this.setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		this.c = new Canvas3D(config);
		this.add("Center", this.c);

		this.u = new SimpleUniverse(this.c);

		this.u.getViewingPlatform().setNominalViewingTransform();
		// u.addChild(TransGroup);
	}

	void addAllbranchGroupToU(boolean[] controlLayerArray)
	{

		this.controlArray = controlLayerArray;
		if (this.controlArray[1] == true)
		{
			// MyPrintln.println("c1.." + typeArray[1]);
			this.branchGroupArray[1] = this.newMyBranchGroup(this.typeArray[1]);
			this.u.addBranchGraph(this.branchGroupArray[1]);
		}
		if (this.controlArray[3] == true)
		{
			// MyPrintln.println("c3.." + typeArray[3]);
			this.branchGroupArray[3] = this.newMyBranchGroup(this.typeArray[3]);
			this.u.addBranchGraph(this.branchGroupArray[3]);
			// 这里过后，不许再removeChild

		}
	}

	public BranchGroup newMyBranchGroup(String type)
	{
        if (type.compareTo("1") == 0)
		{
			return new BranchGroup1(this);
		}
        else if (type.compareTo("3") == 0)
		{
			return new BranchGroup2(this);
		}
		else
		{
			return null;
		}
	}

}
