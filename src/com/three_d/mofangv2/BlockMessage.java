package com.three_d.mofangv2;

import java.io.Serializable;

class BlockMessage implements Serializable
{
	Point3D blockCenter;

	Point3D blockEx;

	Point3D blockEy;

	Point3D blockEz;

	BlockMessage(BlockMessage aBlockMessage)
	{
		this.blockCenter = new Point3D(aBlockMessage.blockCenter.x,
				aBlockMessage.blockCenter.y, aBlockMessage.blockCenter.z);
		this.blockEx = new Point3D(aBlockMessage.blockEx.x,
				aBlockMessage.blockEx.y, aBlockMessage.blockEx.z);
		this.blockEy = new Point3D(aBlockMessage.blockEy.x,
				aBlockMessage.blockEy.y, aBlockMessage.blockEy.z);
		this.blockEz = new Point3D(aBlockMessage.blockEz.x,
				aBlockMessage.blockEz.y, aBlockMessage.blockEz.z);
	}

	BlockMessage(int centerX, int centerY, int centerZ)
	{
		this.blockCenter = new Point3D(centerX, centerY, centerZ);
		this.blockEx = new Point3D(1, 0, 0);
		this.blockEy = new Point3D(0, 1, 0);
		this.blockEz = new Point3D(0, 0, 1);
	}

	BlockMessage(int centerX, int centerY, int centerZ, int exX, int exY,
			int exZ, int eyX, int eyY, int eyZ, int ezX, int ezY, int ezZ)
	{
		this.blockCenter = new Point3D(centerX, centerY, centerZ);
		this.blockEx = new Point3D(exX, exY, exZ);
		this.blockEy = new Point3D(eyX, eyY, eyZ);
		this.blockEz = new Point3D(ezX, ezY, ezZ);
	}

	void setBlockMessage(int centerX, int centerY, int centerZ, int exX,
			int exY, int exZ, int eyX, int eyY, int eyZ, int ezX, int ezY,
			int ezZ)
	{
		this.blockCenter.x = centerX;
		this.blockCenter.y = centerY;
		this.blockCenter.z = centerZ;

		this.blockEx.x = exX;
		this.blockEx.y = exY;
		this.blockEx.z = exZ;

		this.blockEy.x = eyX;
		this.blockEy.y = eyY;
		this.blockEy.z = eyZ;

		this.blockEz.x = ezX;
		this.blockEz.y = ezY;
		this.blockEz.z = ezZ;
	}

	void setBlockMessage(BlockMessage aBlockMessage)
	{
		this.blockCenter.x = aBlockMessage.blockCenter.x;
		this.blockCenter.y = aBlockMessage.blockCenter.y;
		this.blockCenter.z = aBlockMessage.blockCenter.z;

		this.blockEx.x = aBlockMessage.blockEx.x;
		this.blockEx.y = aBlockMessage.blockEx.y;
		this.blockEx.z = aBlockMessage.blockEx.z;

		this.blockEy.x = aBlockMessage.blockEy.x;
		this.blockEy.y = aBlockMessage.blockEy.y;
		this.blockEy.z = aBlockMessage.blockEy.z;

		this.blockEz.x = aBlockMessage.blockEz.x;
		this.blockEz.y = aBlockMessage.blockEz.y;
		this.blockEz.z = aBlockMessage.blockEz.z;
	}

	void block3DRotate(int fangShi012, int jiaoDU)
	{
		this.blockCenter.point3DRotate(fangShi012, jiaoDU);
		this.blockEx.point3DRotate(fangShi012, jiaoDU);
		this.blockEy.point3DRotate(fangShi012, jiaoDU);
		this.blockEz.point3DRotate(fangShi012, jiaoDU);
	}
}
