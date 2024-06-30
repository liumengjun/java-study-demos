package com.three_d.mofangv2;

import javax.media.j3d.Transform3D;

class Movie implements Runnable
// extends Thread
{
	public static int delayNumber = 50;

	public static int changeJiaoDu = 5;

	// 每次旋转的参数
	int typeXYZ012 = 0;

	int layer012 = 0;

	int jiaoDu = 0;

	BranchGroup2 aBranchGroup3;

	Movie(BranchGroup2 abBranchGroup3)
	{
		this.aBranchGroup3 = abBranchGroup3;
	}

	public void run()
	{
		// TimeRun.rotateTimes++;
		this.dongHua();
	}

	synchronized void dongHua()
	{
		if (this.typeXYZ012 == 0)
		{
			// MoFang.aMofangStatusMessage.moFang3DRotate (0,layer012,jiaoDu);

			this.dongHuaX(this.layer012, this.jiaoDu);

		} else if (this.typeXYZ012 == 1)
		{
			this.dongHuaY(this.layer012, this.jiaoDu);
		} else if (this.typeXYZ012 == 2)
		{
			this.dongHuaZ(this.layer012, this.jiaoDu);
		} else
		{

		}

		// 状态数据刷新到窗体和场景
		// MoFang.aMofangStatusMessage.outToATable();

	}

	void dongHuaX(int layer, int jiaoDu)
	{
		// 先转移，再旋转。结果无用。直接设置魔方到新的状态

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					if (this.aBranchGroup3.centerPointSaved[i][j][k].x == layer - 1)
					{
						this.aBranchGroup3.blockBranchGroup[i][j][k].detach();
					}

				}
			}
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					if (this.aBranchGroup3.centerPointSaved[i][j][k].x == layer - 1)

					{
						try
						{
							this.aBranchGroup3.helpTransformGroup
									.addChild(this.aBranchGroup3.blockBranchGroup[i][j][k]);
						}

						catch (Exception e)
						{

							// e.printStackTrace();
						}
					}
				}
			}
		}

		this.aBranchGroup3.helpTransform3D = new Transform3D();

		if (jiaoDu > 0)
		{
			for (int nowJiaoDu = 0; nowJiaoDu <= jiaoDu; nowJiaoDu += changeJiaoDu)
			{
				// nowJiaoDu+=1;
				this.aBranchGroup3.helpTransform3D.rotX(Math
						.toRadians(nowJiaoDu));
				this.aBranchGroup3.helpTransformGroup
						.setTransform(this.aBranchGroup3.helpTransform3D);

				try
				{
					Thread.sleep(delayNumber);
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}

		} else
		{
			for (int nowJiaoDu = 0; nowJiaoDu >= jiaoDu; nowJiaoDu -= changeJiaoDu)
			{
				// nowJiaoDu+=1;
				this.aBranchGroup3.helpTransform3D.rotX(Math
						.toRadians(nowJiaoDu));
				this.aBranchGroup3.helpTransformGroup
						.setTransform(this.aBranchGroup3.helpTransform3D);

				try
				{
					Thread.sleep(delayNumber);
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (this.aBranchGroup3.centerPointSaved[i][j][k].x == layer - 1)
					// if(blockBranchGroup[i][j][k].blockCenter.x==layer-1)
					{
						this.aBranchGroup3.blockBranchGroup[i][j][k].detach();
						// aBranchGroup3.rootTransformGroup.addChild(aBranchGroup3.blockBranchGroup[i][j][k]);
					}
				}
			}
		}

		BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
		aBranchGroup3.setStatus(MoFang.aMofangStatusMessage.dataMessage);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (aBranchGroup3.centerPointSaved[i][j][k].x == layer - 1)
					{
						// aBranchGroup3.blockBranchGroup[i][j][k].detach();
						try
						{
							aBranchGroup3.rootTransformGroup
									.addChild(aBranchGroup3.blockBranchGroup[i][j][k]);
						}

						catch (Exception e)
						{

							// e.printStackTrace();
						}
					}
				}
			}
		}

	}

	void dongHuaY(int layer, int jiaoDu)
	{
		// 先转移，再旋转。结果无用。直接设置魔方到新的状态

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					if (this.aBranchGroup3.centerPointSaved[i][j][k].y == layer - 1)
					{
						this.aBranchGroup3.blockBranchGroup[i][j][k].detach();
					}

				}
			}
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					if (this.aBranchGroup3.centerPointSaved[i][j][k].y == layer - 1)
					// if(blockBranchGroup[i][j][k].blockCenter.x==layer-1)
					{
						try
						{
							this.aBranchGroup3.helpTransformGroup
									.addChild(this.aBranchGroup3.blockBranchGroup[i][j][k]);
						}

						catch (Exception e)
						{

							// e.printStackTrace();
						}
					}
				}
			}
		}

		this.aBranchGroup3.helpTransform3D = new Transform3D();

		if (jiaoDu > 0)
		{
			for (int nowJiaoDu = 0; nowJiaoDu <= jiaoDu; nowJiaoDu += 10)
			{
				// nowJiaoDu+=1;
				this.aBranchGroup3.helpTransform3D.rotY(Math
						.toRadians(nowJiaoDu));
				this.aBranchGroup3.helpTransformGroup
						.setTransform(this.aBranchGroup3.helpTransform3D);

				try
				{
					Thread.sleep(delayNumber);
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}

		} else
		{
			for (int nowJiaoDu = 0; nowJiaoDu >= jiaoDu; nowJiaoDu -= changeJiaoDu)
			{
				// nowJiaoDu+=1;
				this.aBranchGroup3.helpTransform3D.rotY(Math
						.toRadians(nowJiaoDu));
				this.aBranchGroup3.helpTransformGroup
						.setTransform(this.aBranchGroup3.helpTransform3D);

				try
				{
					Thread.sleep(delayNumber);
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (this.aBranchGroup3.centerPointSaved[i][j][k].y == layer - 1)
					// if(blockBranchGroup[i][j][k].blockCenter.x==layer-1)
					{
						this.aBranchGroup3.blockBranchGroup[i][j][k].detach();
						// aBranchGroup3.rootTransformGroup.addChild(aBranchGroup3.blockBranchGroup[i][j][k]);
					}
				}
			}
		}

		BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
		aBranchGroup3.setStatus(MoFang.aMofangStatusMessage.dataMessage);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (aBranchGroup3.centerPointSaved[i][j][k].y == layer - 1)
					// if(blockBranchGroup[i][j][k].blockCenter.x==layer-1)
					{
						try
						{

							aBranchGroup3.rootTransformGroup
									.addChild(aBranchGroup3.blockBranchGroup[i][j][k]);
						}

						catch (Exception e)
						{

							// e.printStackTrace();
						}
					}
				}
			}
		}

	}

	void dongHuaZ(int layer, int jiaoDu)
	{

		// 先转移，再旋转。结果无用。直接设置魔方到新的状态

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (this.aBranchGroup3.centerPointSaved[i][j][k].z == layer - 1)
					{
						this.aBranchGroup3.blockBranchGroup[i][j][k].detach();
					}
				}
			}
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					if (this.aBranchGroup3.centerPointSaved[i][j][k].z == layer - 1)
					// if(blockBranchGroup[i][j][k].blockCenter.x==layer-1)
					{
						try
						{

							this.aBranchGroup3.helpTransformGroup
									.addChild(this.aBranchGroup3.blockBranchGroup[i][j][k]);
						}

						catch (Exception e)
						{

							// e.printStackTrace();
						}

					}
				}
			}
		}

		this.aBranchGroup3.helpTransform3D = new Transform3D();
		// aBranchGroup3.helpTransform3D.rotZ(Math.toRadians(jiaoDu));
		// aBranchGroup3.helpTransformGroup.setTransform(aBranchGroup3.helpTransform3D);
		if (jiaoDu > 0)
		{
			for (int nowJiaoDu = 0; nowJiaoDu <= jiaoDu; nowJiaoDu += changeJiaoDu)
			{
				// nowJiaoDu+=1;
				this.aBranchGroup3.helpTransform3D.rotZ(Math
						.toRadians(nowJiaoDu));
				this.aBranchGroup3.helpTransformGroup
						.setTransform(this.aBranchGroup3.helpTransform3D);

				try
				{
					Thread.sleep(delayNumber);
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}

		} else
		{
			for (int nowJiaoDu = 0; nowJiaoDu >= jiaoDu; nowJiaoDu -= changeJiaoDu)
			{
				// nowJiaoDu+=1;
				this.aBranchGroup3.helpTransform3D.rotZ(Math
						.toRadians(nowJiaoDu));
				this.aBranchGroup3.helpTransformGroup
						.setTransform(this.aBranchGroup3.helpTransform3D);

				try
				{
					Thread.sleep(delayNumber);
				} catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (this.aBranchGroup3.centerPointSaved[i][j][k].z == layer - 1)
					// if(blockBranchGroup[i][j][k].blockCenter.x==layer-1)
					{
						this.aBranchGroup3.blockBranchGroup[i][j][k].detach();
						// aBranchGroup3.rootTransformGroup.addChild(aBranchGroup3.blockBranchGroup[i][j][k]);
					}
				}
			}
		}

		BranchGroup2 aBranchGroup3 = (BranchGroup2) (MoFang.testPanelAddedScene3D.branchGroupArray[3]);
		aBranchGroup3.setStatus(MoFang.aMofangStatusMessage.dataMessage);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (aBranchGroup3.centerPointSaved[i][j][k].z == layer - 1)
					// if(blockBranchGroup[i][j][k].blockCenter.x==layer-1)
					{
						// aBranchGroup3.blockBranchGroup[i][j][k].detach();
						try
						{

							aBranchGroup3.rootTransformGroup
									.addChild(aBranchGroup3.blockBranchGroup[i][j][k]);
						}

						catch (Exception e)
						{
							// e.printStackTrace();
						}
					}
				}
			}
		}

	}
}
