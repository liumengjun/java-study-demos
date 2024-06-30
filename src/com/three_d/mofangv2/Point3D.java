package com.three_d.mofangv2;

import java.io.Serializable;

class Point3D implements Serializable
{

	// ======================================
	// 使用Ax=y ，由x计算y。若要由y求A，只需用x=A-1y,例如90度的反变换矩阵为-90度，等价于180度
	// -----------------------------------------
	public static int[][] bianHuanJuZhenX90 = { { 1, 0, 0 }, { 0, 0, -1 },
			{ 0, 1, 0 }, };

	// ----------------------------------
	// -----------------------------------------
	public static int[][] bianHuanJuZhenX180 = { { 1, 0, 0 }, { 0, -1, 0 },
			{ 0, 0, -1 }, };

	// ----------------------------------
	// -----------------------------------------
	public static int[][] bianHuanJuZhenX270 = { { 1, 0, 0 }, { 0, 0, 1 },
			{ 0, -1, 0 }, };

	// ----------------------------------
	// ======================================

	// -----------------------------------------
	public static int[][] bianHuanJuZhenY90 = { { 0, 0, 1 }, { 0, 1, 0 },
			{ -1, 0, 0 }, };

	// ----------------------------------

	// -----------------------------------------
	public static int[][] bianHuanJuZhenY180 = { { -1, 0, 0 }, { 0, 1, 0 },
			{ 0, 0, -1 }, };

	// ----------------------------------
	// -----------------------------------------
	public static int[][] bianHuanJuZhenY270 = { { 0, 0, -1 }, { 0, 1, 0 },
			{ 1, 0, 0 }, };

	// ----------------------------------
	// ======================================
	// -----------------------------------------
	public static int[][] bianHuanJuZhenZ90 = { { 0, -1, 0 }, { 1, 0, 0 },
			{ 0, 0, 1 }, };

	// ----------------------------------
	// -----------------------------------------
	public static int[][] bianHuanJuZhenZ180 = { { -1, 0, 0 }, { 0, -1, 0 },
			{ 0, 0, 1 }, };

	// ----------------------------------
	// -----------------------------------------
	public static int[][] bianHuanJuZhenZ270 = { { 0, 1, 0 }, { -1, 0, 0 },
			{ 0, 0, 1 }, };

	// ----------------------------------
	// =================================
	int x = 0;

	int y = 0;

	int z = 0;

	Point3D(int px, int py, int pz)
	{
		this.x = px;
		this.y = py;
		this.z = pz;
	}

	String myToString()
	{
		return ("x=" + String.valueOf(this.x) + "  y=" + String.valueOf(this.y)
				+ "  z=" + String.valueOf(this.z));
	}

	void setValue(int px, int py, int pz)
	{
		this.x = px;
		this.y = py;
		this.z = pz;
	}

	void point3DRotate(int fangShi, int jiaoDU)
	{//计算旋转后的坐标,矩阵运算

		if (fangShi == 0)
		{
			this.point3DRotateX(jiaoDU);
		} else if (fangShi == 1)
		{
			this.point3DRotateY(jiaoDU);
		} else if (fangShi == 2)
		{
			this.point3DRotateZ(jiaoDU);
		} else
		{
			System.out.println("you must set it with 012--"
					+ String.valueOf(fangShi));
		}
	}

	int[] acculate(int[][] juZhen, int[] shuRu, int n)
	{
		// N为维数

		int[] shuChu = new int[n];
		for (int i = 0; i < n; i++)
		{
			shuChu[i] = 0;
			// 每次计算一个分量
			for (int j = 0; j < n; j++)
			{
				shuChu[i] += juZhen[i][j] * shuRu[j];
				// 每次分量由三个数相加
			}
		}

		return shuChu;
	}

	void point3DRotateX(int jiaoDU)
	{//计算旋转后的坐标

		int[] shuRu = new int[3];
		shuRu[0] = this.x;
		shuRu[1] = this.y;
		shuRu[2] = this.z;
		int[] jieGuo = new int[3];

		if ((jiaoDU == 90) || (jiaoDU == -270))
		{

			jieGuo = this.acculate(bianHuanJuZhenX90, shuRu, 3);
		}

		else if ((jiaoDU == 180) || (jiaoDU == -180))
		{

			jieGuo = this.acculate(bianHuanJuZhenX180, shuRu, 3);
		}

		else if ((jiaoDU == 270) || (jiaoDU == -90))
		{

			jieGuo = this.acculate(bianHuanJuZhenX270, shuRu, 3);
		} else
		{

		}

		this.x = jieGuo[0];
		this.y = jieGuo[1];
		this.z = jieGuo[2];

	}

	void point3DRotateY(int jiaoDU)
	{

		int[] shuRu = new int[3];
		shuRu[0] = this.x;
		shuRu[1] = this.y;
		shuRu[2] = this.z;

		int[] jieGuo = new int[3];

		if ((jiaoDU == 90) || (jiaoDU == -270))
		{

			jieGuo = this.acculate(bianHuanJuZhenY90, shuRu, 3);
		}

		else if ((jiaoDU == 180) || (jiaoDU == -180))
		{

			jieGuo = this.acculate(bianHuanJuZhenY180, shuRu, 3);
		}

		else if ((jiaoDU == 270) || (jiaoDU == -90))
		{

			jieGuo = this.acculate(bianHuanJuZhenY270, shuRu, 3);
		} else
		{

		}

		this.x = jieGuo[0];
		this.y = jieGuo[1];
		this.z = jieGuo[2];
	}

	void point3DRotateZ(int jiaoDU)
	{

		int[] shuRu = new int[3];
		shuRu[0] = this.x;
		shuRu[1] = this.y;
		shuRu[2] = this.z;

		int[] jieGuo = new int[3];

		if ((jiaoDU == 90) || (jiaoDU == -270))
		{

			jieGuo = this.acculate(bianHuanJuZhenZ90, shuRu, 3);
		}

		else if ((jiaoDU == 180) || (jiaoDU == -180))
		{

			jieGuo = this.acculate(bianHuanJuZhenZ180, shuRu, 3);
		}

		else if ((jiaoDU == 270) || (jiaoDU == -90))
		{

			jieGuo = this.acculate(bianHuanJuZhenZ270, shuRu, 3);
		} else
		{

		}

		this.x = jieGuo[0];
		this.y = jieGuo[1];
		this.z = jieGuo[2];
	}
}
