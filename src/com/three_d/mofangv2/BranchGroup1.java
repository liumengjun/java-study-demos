package com.three_d.mofangv2;

import java.applet.Applet;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

class BranchGroup1 extends BranchGroup
{
	int labelNumberLeftMax = 5;

	int labelNumberLeft = 0;

	int labelNumberDownMax = 3;

	int labelNumberDown = 0;

	BranchGroup1(Applet theApplet)
	{

		this.creteQueueLeftAndDown(22);

		MyTimerBehavior runTime = new MyTimerBehavior(this, 1000);
		runTime.setSchedulingBounds(new BoundingSphere());
		this.addChild(runTime);

		this.setCapability(BranchGroup.ALLOW_DETACH);
		this.compile();

	}

	void addLabelLeft(float thex1, float they1, float thez1, int theFontSize,
			int which)
	{

		float x1;
		float y1;
		float z1;
		x1 = thex1;
		y1 = they1;
		z1 = thez1;
		TransformGroup numberTransformGroup = new TransformGroup();
		Transform3D numberTransform3D = new Transform3D();

		Material mm = new Material();
		mm.setLightingEnable(true);
		// app.setMaterial(mm);

		// 左上角位置

		numberTransform3D.setTranslation(new Vector3f(x1, y1, z1 - 1.1f));

		numberTransformGroup.setTransform(numberTransform3D);
		this.addChild(numberTransformGroup);

	}

	void addLabelDown(float thex1, float they1, float thez1, int theFontSize,
			int which)
	{
		float x1;
		float y1;
		float z1;
		x1 = thex1;
		y1 = they1;
		z1 = thez1;
		TransformGroup numberTransformGroup = new TransformGroup();
		Transform3D numberTransform3D = new Transform3D();

		Material mm = new Material();
		mm.setLightingEnable(true);
		// app.setMaterial(mm);

		numberTransform3D.setTranslation(new Vector3f(x1, y1, z1 - 1.1f));

		numberTransformGroup.setTransform(numberTransform3D);
		this.addChild(numberTransformGroup);

	}

	public void creteQueueLeftAndDown(int fontSize)
	{

		float x1;
		float y1;
		float z1;

		x1 = -1.4f;
		// 加在左边的第一个label的坐标(x1,y1,z1),下面的坐标自动计算
		y1 = 1.20f;
		z1 = 0.0f;
		for (int i = 0; i < this.labelNumberLeftMax; i++, y1 -= 0.10f)
		{
			// 左边起始位置(x1,y1,z1)，每次向下便宜0.2f
			this.addLabelLeft(x1, y1, z1, fontSize, i);
			// x,y,z分别为左上角的右，上，近
		}

		x1 = -1.4f;
		// 加在下边的第一个label的坐标(x1,y1,z1),下面的坐标自动计算
		y1 = -1.1f;
		z1 = 0.0f;
		// 创建下边的队列
		for (int i = 0; i < this.labelNumberDownMax; i++, y1 -= 0.10f)
		{
			// 右边起始位置(x1,y1,z1)，每次向下便宜0.2f
			this.addLabelDown(x1, y1, z1, fontSize, i);
			// x,y,z分别为左上角的右，上，近
		}
	}
}
