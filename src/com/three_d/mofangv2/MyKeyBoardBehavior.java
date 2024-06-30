package com.three_d.mofangv2;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Stack;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;

class MyKeyBoardBehavior extends Behavior
{

	// double oldArg;//记录以前的转角
	double oldScale;

	double oldPositionX;

	double oldPositionY;

	double oldPositionZ;

	static double liangScale;

	static double liangOldPositionX;

	static double liangOldPositionY;

	static double liangOldPositionZ;

	static boolean[] isShow = new boolean[10];

	static int biaoHaoStatusNow;

	// biaoHaoStatus={"MoRen","ZiDingYi","NoBiaoHao"}
	// -----------------
	static int touMingMode;

	// 声明堆栈 用来记录和还原用户操作
	static int[] bianhuantype = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 };

	static Stack recordStack = new Stack();

	// NONE,TransparencyAttributes.FASTEST,TransparencyAttributes.NICEST,TransparencyAttributes.SCREEN_DOOR,TransparencyAttributes.BLENDED
	int[] transparencyMode = { TransparencyAttributes.NONE,
			TransparencyAttributes.FASTEST, TransparencyAttributes.NICEST,
			TransparencyAttributes.SCREEN_DOOR, TransparencyAttributes.BLENDED };

	float[] touMingDu = { 0.2f, 0.4f, 0.6f, 0.8f };

	// 0.2,0.4,0.6,0.8
	MyKeyBoardBehavior()
	{

		this.oldScale = 1.0f;
		this.oldPositionX = 0.0f;
		this.oldPositionY = 0.0f;
		this.oldPositionZ = 0.0f;

		liangScale = 0.1f;
		liangOldPositionX = 0.1f;
		liangOldPositionY = 0.1f;
		liangOldPositionZ = 0.1f;

		isShow[0] = MoFang.controlLayerArray[0];
		// 背景层开关
		isShow[1] = MoFang.controlLayerArray[1];
		// 字幕层
		isShow[2] = true;
		isShow[3] = true;
		// 内表面
		isShow[4] = true;
		// 大坐标轴
		isShow[5] = true;
		// 小坐标轴

		biaoHaoStatusNow = 0;
		// 最初设为默认标号
		touMingMode = 0;
		// 最初设为不透明

		// oldScale
		// public static boolean[]
		// controlLayerArray={false,false,false,true,false,false,false,false};
		// ;

	}

	public void initialize()
	{
		this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED));
	}

	public void processStimulus(Enumeration criteria)
	{

		WakeupOnAWTEvent ev;
		WakeupCriterion genericEvt;
		AWTEvent[] events;
		KeyEvent event;
		// Vector3f position=new Vector3f();

		while (criteria.hasMoreElements())
		{
			genericEvt = (WakeupCriterion) criteria.nextElement();
			if (genericEvt instanceof WakeupOnAWTEvent)
			{
				ev = (WakeupOnAWTEvent) genericEvt;
				events = ev.getAWTEvent();
				if (events[0] instanceof KeyEvent)
				{
					event = (KeyEvent) events[0];

					if (event.getKeyCode() == KeyEvent.VK_F1)
					{
						recordStack.clear();

					}
					if (event.getKeyCode() == KeyEvent.VK_F2)
					{
						Thread myThread = new Thread(new Runnable()
						{
							public void run()
							{
								while (!recordStack.empty())
								{

									int operateI = ((Integer) recordStack.pop())
											.intValue();
									switch (operateI)
									{

									case 1:
										// 状态数据变化
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup1 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup1.dongHua(0, 0, -90);
										break;
									case 2:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup21 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup21.dongHua(0, 0, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup22 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup22.dongHua(0, 0, -90);
										break;
									case 3:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 0, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup3.dongHua(0, 0, 90);
										break;
									case 4:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup4 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup4.dongHua(0, 1, -90);
										break;
									case 5:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup51 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup51.dongHua(0, 1, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup52 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup52.dongHua(0, 1, -90);
										break;
									case 6:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 1, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup6 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup6.dongHua(0, 1, 90);
										break;
									case 7:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup7 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup7.dongHua(0, 2, -90);
										break;
									case 8:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup81 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup81.dongHua(0, 2, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup82 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup82.dongHua(0, 2, -90);
										break;
									case 9:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(0, 2, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup9 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup9.dongHua(0, 2, 90);
										break;
									/** **************************************************************************** */
									case 10:
										// 状态数据变化
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup10 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup10.dongHua(1, 0, -90);
										break;
									case 11:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup111 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup111.dongHua(1, 0, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup112 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup112.dongHua(1, 0, -90);
										break;
									case 12:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 0, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup12 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup12.dongHua(1, 0, 90);
										break;
									case 13:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup13 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup13.dongHua(1, 1, -90);
										break;
									case 14:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup141 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup141.dongHua(1, 1, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup142 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup142.dongHua(1, 1, -90);
										break;
									case 15:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 1, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup15 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup15.dongHua(1, 1, 90);
										break;
									case 16:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup16 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup16.dongHua(1, 2, -90);
										break;
									case 17:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup171 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup171.dongHua(1, 2, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup172 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup172.dongHua(1, 2, -90);
										break;
									case 18:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(1, 2, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup18 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup18.dongHua(1, 2, 90);
										break;

									/** ********************************************************************* */

									case 19:
										// 状态数据变化
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup19 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup19.dongHua(2, 0, -90);
										break;
									case 20:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup201 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup201.dongHua(2, 0, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 0, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup202 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup202.dongHua(2, 0, -90);
										break;
									case 21:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 0, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup211 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup211.dongHua(2, 0, 90);
										break;
									case 22:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup221 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup221.dongHua(2, 1, -90);
										break;
									case 23:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup231 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup231.dongHua(2, 1, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 1, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup232 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup232.dongHua(2, 1, -90);
										break;
									case 24:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 1, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup24 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup24.dongHua(2, 1, 90);
										break;
									case 25:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup25 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup25.dongHua(2, 2, -90);
										break;
									case 26:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup261 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup261.dongHua(2, 2, -90);

										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 2, -90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup262 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup262.dongHua(2, 2, -90);
										break;
									case 27:
										MoFang.aMofangStatusMessage
												.moFang3DRotate(2, 2, 90);

										// 虚拟动画表演
										BranchGroup2 aBranchGroup27 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
										aBranchGroup27.dongHua(2, 2, 90);
										break;

									default:

										break;
									}

									try
									{
										Thread.sleep(2000);
									} catch (InterruptedException e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}

						});
						myThread.start();

					} else if (event.getKeyCode() == KeyEvent.VK_F3)
					{

						this.oldScale += 0.1f;
						TransformGroup rootTransformGroup = ((BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3])).rootTransformGroup;
						// Transform3D
						Transform3D t1 = new Transform3D();
						rootTransformGroup.getTransform(t1);
						// rootTransform3D=rootTransformGroup.
						t1.setScale(this.oldScale);

						rootTransformGroup.setTransform(t1);

					} else if (event.getKeyCode() == KeyEvent.VK_F4)
					{

						this.oldScale -= 0.1f;
						// new TransformGroup.
						// Transform3D
						// rootTransform3D//=(BranchGroup3)(MoFang.testPanelAddedScene3D.branchGroupArray[3]).;
						TransformGroup rootTransformGroup = ((BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3])).rootTransformGroup;
						// Transform3D
						Transform3D t1 = new Transform3D();
						rootTransformGroup.getTransform(t1);
						// rootTransform3D=rootTransformGroup.
						t1.setScale(this.oldScale);

						rootTransformGroup.setTransform(t1);

					}

					else if (event.getKeyCode() == 'Q')
					{

						// 状态数据变化
						MoFang.aMofangStatusMessage.moFang3DRotate(0, 0, 90);

						// 虚拟动画表演
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 0, 90);

						recordStack.add(new Integer(1));
					} else if (event.getKeyCode() == 'W')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 0, 180);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 0, 180);

						recordStack.add(new Integer(2));
					} else if (event.getKeyCode() == 'E')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 0, -90);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 0, -90);

						recordStack.add(new Integer(3));
					} else if (event.getKeyCode() == 'A')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 1, 90);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 1, 90);

						recordStack.add(new Integer(4));
					} else if (event.getKeyCode() == 'S')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 1, 180);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 1, 180);

						recordStack.add(new Integer(5));
					} else if (event.getKeyCode() == 'D')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 1, -90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 1, -90);

						recordStack.add(new Integer(6));
					} else if (event.getKeyCode() == 'Z')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 2, 90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 2, 90);

						recordStack.add(new Integer(7));
					} else if (event.getKeyCode() == 'X')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 2, 180);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 2, 180);

						recordStack.add(new Integer(8));
					} else if (event.getKeyCode() == 'C')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(0, 2, -90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(0, 2, -90);

						recordStack.add(new Integer(9));
					} else if (event.getKeyCode() == 'R')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 0, 90);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 0, 90);

						recordStack.add(new Integer(10));
					} else if (event.getKeyCode() == 'T')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 0, 180);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 0, 180);

						recordStack.add(new Integer(11));
					} else if (event.getKeyCode() == 'Y')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 0, -90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 0, -90);

						recordStack.add(new Integer(12));
					} else if (event.getKeyCode() == 'F')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 1, 90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 1, 90);

						recordStack.add(new Integer(13));
					} else if (event.getKeyCode() == 'G')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 1, 180);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 1, 180);

						recordStack.add(new Integer(14));

					} else if (event.getKeyCode() == 'H')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 1, -90);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 1, -90);

						recordStack.add(new Integer(15));
					} else if (event.getKeyCode() == 'V')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 2, 90);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 2, 90);

						recordStack.add(new Integer(16));
					} else if (event.getKeyCode() == 'B')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 2, 180);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 2, 180);

						recordStack.add(new Integer(17));
					} else if (event.getKeyCode() == 'N')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(1, 2, -90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(1, 2, -90);

						recordStack.add(new Integer(18));
					} else if (event.getKeyCode() == 'U')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 0, 90);

						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 0, 90);

						recordStack.add(new Integer(19));
					} else if (event.getKeyCode() == 'I')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 0, 180);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 0, 180);

						recordStack.add(new Integer(20));
					} else if (event.getKeyCode() == 'O')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 0, -90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 0, -90);

						recordStack.add(new Integer(21));
					} else if (event.getKeyCode() == 'J')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 1, 90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 1, 90);

						recordStack.add(new Integer(22));
					} else if (event.getKeyCode() == 'K')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 1, 180);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 1, 180);

						recordStack.add(new Integer(23));
					} else if (event.getKeyCode() == 'L')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 1, -90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 1, -90);

						recordStack.add(new Integer(24));
					} else if (event.getKeyCode() == 'M')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 2, 90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 2, 90);

						recordStack.add(new Integer(25));
					} else if (event.getKeyCode() == ',')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 2, 180);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 2, 180);

						recordStack.add(new Integer(26));
					} else if (event.getKeyCode() == '.')
					{

						MoFang.aMofangStatusMessage.moFang3DRotate(2, 2, -90);
						BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
						aBranchGroup3.dongHua(2, 2, -90);

						recordStack.add(new Integer(27));
					}

				}
			}
			this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED));
		}

	}
}
