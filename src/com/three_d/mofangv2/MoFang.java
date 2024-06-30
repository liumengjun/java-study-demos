package com.three_d.mofangv2;

public class MoFang
{
	// 控制量
	public static String[] newTypeArray = { "0", "1", "2", "3", "4", "5", "6",
			"7" };

	public static boolean[] controlLayerArray = { true, true, false, true,
			false, false, false, false };

	// 整个程序三个组件
	// 三维显示模块
	public static PanelAddedScene3D testPanelAddedScene3D = new PanelAddedScene3D();

	// 魔方状态数据及变化模块
	public static MofangStatusMessage aMofangStatusMessage = new MofangStatusMessage(
			"new");;

	// 窗体和界面
	public static MyMainFrame theMainFrame = new MyMainFrame();

	public static void main(String args[])
	{

		// 显示场景
		testPanelAddedScene3D.typeArray = newTypeArray;
		testPanelAddedScene3D.prepare();
		testPanelAddedScene3D.addAllbranchGroupToU(controlLayerArray);

		theMainFrame.add(testPanelAddedScene3D);

		// 显示窗体
		theMainFrame.setSize(650, 650);
		theMainFrame.setVisible(true);

		// 初始状态数据显示到窗体和场景

		BranchGroup2 aBranchGroup2 = (BranchGroup2) (testPanelAddedScene3D.branchGroupArray[3]);
		aBranchGroup2.setStatus(aMofangStatusMessage.dataMessage);

		theMainFrame.repaint();

		aBranchGroup2 = (BranchGroup2) (testPanelAddedScene3D.branchGroupArray[3]);
		aBranchGroup2.setStatus(aMofangStatusMessage.dataMessage);

	}
}
