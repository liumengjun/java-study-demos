package com.three_d.mofangv2;

import java.util.Vector;

class BlockRotateStatusAndSerch
{
	// 注意，这里面的矩阵全部是x列向量，y列向量,z列向量
	// ------
	public static String souSuoType = "";

	public static int shenDuControl = 0;

	// ----------
	// BlockRotateStatusTableAndSouSuo.getShortLuJing();
	public static Vector bianHuanTable = new Vector();

	public static Vector statusJuZhenList = new Vector();

	// statusJuZhenNoList.
	// public static Vector statusJuZhenList=new Vector();
	public static int[][] statusJuZhen = { { 1, 0, 0 }, { 0, 1, 0 },
			{ 0, 0, 1 }, };

	// XYZ列向量
	public static int[][][] chengShuZhen = { Point3D.bianHuanJuZhenX90,
			Point3D.bianHuanJuZhenX180, Point3D.bianHuanJuZhenX270,
			Point3D.bianHuanJuZhenY90, Point3D.bianHuanJuZhenY180,
			Point3D.bianHuanJuZhenY270, Point3D.bianHuanJuZhenZ90,
			Point3D.bianHuanJuZhenZ180, Point3D.bianHuanJuZhenZ270, };

	public static Vector[][] souSuoBiao = new Vector[24][24];

	// 用来添加数字串,最后一个数字为数字个数
	public static int souSuoBiaoFeiNullGeShu = 0;

	//

	public static boolean haveCreated = false;

	public static int findInList(int[][] inArray)
	{
		int[][] intArray;
		int i;
		for (i = 0; i < statusJuZhenList.size(); i++)
		{
			intArray = (int[][]) (statusJuZhenList.get(i));
			if (comparyintArray(inArray, intArray, 3, 3) == 0)
			{
				return i;
			}
		}

		return -1;
	}

	//
	public static Vector getAllLuJingShenDuControll(int[][] oldArray,
			int[][] newArray, int aShenDuControl)
	{
		if (haveCreated == false)
		{
			haveCreated = true;
			//
			setupBianHuanBiao();
			setupSouSuoBiao();

		} else
		{
		}
		int row = 0;
		int column = 0;
		// ========
		if ((souSuoType.compareTo("shenDuControlGetAll") != 0)
				|| (shenDuControl != aShenDuControl))
		{
			souSuoType = "shenDuControlGetAll";
			shenDuControl = aShenDuControl;
			setupSouSuoBiao();
		}
		// =============
		row = findInList(oldArray);
		column = findInList(newArray);

		Vector findedVector = (souSuoBiao[row][column]);

		return findedVector;
	}

	public static Vector getShortLuJing(int[][] oldArray, int[][] newArray)
	{
		if (haveCreated == false)
		{
			haveCreated = true;
			//
			setupBianHuanBiao();

		} else
		{
		}
		int row = 0;
		int column = 0;

		// ========
		if (souSuoType != "short")
		{
			souSuoType = "short";
			// shenDuControl=aShenDuControl;
			setupSouSuoBiao();
		}
		// =============
		row = findInList(oldArray);
		column = findInList(newArray);

		Vector findedVector = (Vector) (souSuoBiao[row][column].get(1));

		return findedVector;
	}

	public static void setupSouSuoBiaoDiGui(int status, int shenDu, int hang,
			Vector luJing)
	{
		// hang only for output

		for (int nextstatusXiaBiao = 0; nextstatusXiaBiao < 9; nextstatusXiaBiao++)
		{
			Vector[] newluJing = new Vector[9];
			newluJing[nextstatusXiaBiao] = (Vector) (luJing.clone());
			newluJing[nextstatusXiaBiao].add(String.valueOf(nextstatusXiaBiao));

			int[] nextStatusArray = (int[]) (bianHuanTable.get(status));
			int nextstatus = nextStatusArray[nextstatusXiaBiao];
			//
			// ===============================================

			if (souSuoBiao[hang][nextstatus].size() == 0)
			{
				// souSuoBiao[hang][nextstatus].remove (0);
				souSuoBiao[hang][nextstatus].add(0, String.valueOf(shenDu));
				souSuoBiao[hang][nextstatus].add(1,
						newluJing[nextstatusXiaBiao]);

				souSuoBiaoFeiNullGeShu++;
				// MyPrintln.println("共计:"+String.valueOf
				// (souSuoBiaoFeiNullGeShu));

			}

			else
			{
				// 按序放到正确位置

				// souSuoBiao[hang][nextstatus].add(String.valueOf(shenDu));
				// souSuoBiao[hang][nextstatus].add(newluJing[nextstatusXiaBiao]);
				// /=======
				int i1;
				for (i1 = 0; i1 < souSuoBiao[hang][nextstatus].size(); i1 += 2)
				{
					String oldshenDu = (String) (souSuoBiao[hang][nextstatus]
							.get(i1));
					if (shenDu < Integer.parseInt(oldshenDu))
					{
						break;
					}
				}
				// --------
				souSuoBiao[hang][nextstatus].add(i1, String.valueOf(shenDu));
				souSuoBiao[hang][nextstatus].add(i1 + 1,
						newluJing[nextstatusXiaBiao]);

				// ======

				// oldshenDu.
			}

		}
		// =======================================
		// 将调用和条件改变位置到上面，则是深度优先
		// =====================================

		shenDu++;

		if (((souSuoType.compareTo("shenDuControlGetAll") == 0) && (shenDu <= shenDuControl))
				|| ((souSuoType.compareTo("short") == 0) && (souSuoBiaoHangFull(hang) != 1)))
		// if(souSuoBiaoHangFull(hang)!=1)//行满则返回
		// 该条件是得到最短路径的方法。只得到一个。如果采用广度优先搜索，那么第一个结果就是需要的结果
		// / if(shenDu<=2)//该条件是得到所有3步内的方法，最短的自动放到最前面
		{
			// int[]nextStatusArray=bianHuanTable.get(status);
			for (int nextstatusXiaBiao = 0; nextstatusXiaBiao < 9; nextstatusXiaBiao++)
			{
				Vector[] newluJing = new Vector[9];
				newluJing[nextstatusXiaBiao] = (Vector) (luJing.clone());
				newluJing[nextstatusXiaBiao].add(String
						.valueOf(nextstatusXiaBiao));

				int[] nextStatusArray = (int[]) (bianHuanTable.get(status));
				int nextstatus = nextStatusArray[nextstatusXiaBiao];
				setupSouSuoBiaoDiGui(nextstatus, shenDu, hang,
						newluJing[nextstatusXiaBiao]);

				// =====================

			}
		}
	}

	public static int souSuoBiaoHangFull(int hang)
	{

		for (int j = 0; j < 24; j++)
		{
			if (souSuoBiao[hang][j].size() == 0)
			{
				return -1;
			}
		}

		return 1;
	}

	public static int souSuoBiaoFull()
	{
		for (int i = 0; i < 24; i++)
		{
			for (int j = 0; j < 24; j++)
			{
				if (souSuoBiao[i][j].size() == 0)
				{
					return -1;
				}
			}
		}

		return 1;
	}

	public static void setupSouSuoBiao()
	{
		for (int i = 0; i < 24; i++)
		{
			souSuoBiao[i] = new Vector[24];
			for (int j = 0; j < 24; j++)
			{
				souSuoBiao[i][j] = new Vector();
			}
		}

		for (int i = 0; i < 24; i++)
		{
			// 对角元肯定为空，只有数字0，表示不需步数
			souSuoBiao[i][i].add(String.valueOf(0));
			// Vector luJing=new Vector();
			souSuoBiao[i][i].add(new Vector());
		}

		for (int hang = 0; hang < 24; hang++)
		{
			int status = hang;
			Vector luJing = new Vector();
			setupSouSuoBiaoDiGui(status, 1, hang, luJing);
		}

		// ------
		if (souSuoBiaoFull() == 1)
		{

		}

	}

	public static void setupBianHuanBiao()
	{

		statusJuZhenList.add(statusJuZhen);
		// statusJuZhenNoList.add(0);

		// boolean exitFlag=false;
		int i = 0;
		int[][] intArray;
		// intArray=(int[][])(statusJuZhenList.get(i));
		while (i + 1 <= statusJuZhenList.size())
		{
			// pritlnbianHuanTable();

			// System.out.println(statusJuZhenList.size ());
			intArray = (int[][]) (statusJuZhenList.get(i));
			// MyPrintln.println(intArray,3,3);
			int[] statusJuZhenNo = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

			for (int j = 0; j < 9; j++)
			{
				//
				int[][] outArray;

				outArray = jiSuanJuZhen(intArray, chengShuZhen[j]);
				int outNumber;
				outNumber = addTolist(outArray);
				statusJuZhenNo[j] = outNumber;

			}
			bianHuanTable.add(statusJuZhenNo);

			i++;
		}

	}

	static int[][] jiSuanJuZhen(int[][] intArray, int[][] aChengShuZhen)
	{
		// 输入矩阵放右边
		int[][] outArray = { { 0, 0, 0, }, { 0, 0, 0, }, { 0, 0, 0 }, };
		//
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				outArray[i][j] = 0;
				for (int k = 0; k < 3; k++)
				{
					outArray[i][j] += aChengShuZhen[i][k] * intArray[k][j];
				}
			}
		}
		return outArray;
	}

	static int addTolist(int[][] inArray)
	{

		int[][] intArray;
		int i;
		for (i = 0; i < statusJuZhenList.size(); i++)
		{
			intArray = (int[][]) (statusJuZhenList.get(i));
			if (comparyintArray(inArray, intArray, 3, 3) == 0)
			{
				return i;
			}
		}

		statusJuZhenList.add(inArray);
		return i;
	}

	static int comparyintArray(int[][] inArray1, int[][] inArray2, int hang,
			int lie)
	{
		for (int i = 0; i < hang; i++)
		{
			for (int j = 0; j < lie; j++)
			{
				if (inArray1[i][j] != inArray2[i][j])
				{
					return -1;
				}
			}
		}
		return 0;
	}

	static Vector calculateControledRotateSelf(int[] saiXuanJi, int[][] test1,
			int[][] test2, int position, int teDingTypeXYZ012, int shenDu)
	{

		Vector myVectorArray = BlockRotateStatusAndSerch
				.getAllLuJingShenDuControll(test1, test2, shenDu);

		Vector myVector;

		if (myVectorArray.size() != 0)
		{
			int k1;
			for (k1 = 1; k1 < myVectorArray.size(); k1 += 2)
			// 定当前路径的位置---------------
			{
				// saiXuanJi[]
				myVector = (Vector) (myVectorArray.get(k1));
				// 取出当前路径
				// int goNextK1=0;
				int y1;
				for (y1 = 0; y1 < myVector.size(); y1++)
				{
					// 对每个节点进行检查
					String typeNow = (String) (myVector.get(y1));
					// if(typeNow.compareTo ("0")==0)
					int intType;
					intType = Integer.parseInt(typeNow);
					// for(int z1=0;z1<9;z1++)
					// {
					if (saiXuanJi[intType] == 0)
					{
						// 该种类被拒绝了,该路经无效
						// goNextK1=1;
						break;
					}

					//
					// int position,int teDingTypeXYZ012
					if (position == y1)
					{
						// confined
						if (intType / 3 == teDingTypeXYZ012)
						{
							;
						} else
						{
							// 该位置种类不合要求或者非0，1，2的种类
							break;
						}
					} else if ((position == -2) && (y1 == myVector.size() - 1))
					{
						// !=y1
						if (intType / 3 == teDingTypeXYZ012)
						{
							;
						} else
						{
							// 该位置种类不合要求或者非0，1，2的种类
							break;
						}
					} else
					{
						;
						// 没这个position
					}

				}
				if (y1 < myVector.size())
				{

					continue;
				} else
				{
					// 路径有效

					return myVector;
				}
			}
		}
		return new Vector();
		// 返回空的

	}

}
