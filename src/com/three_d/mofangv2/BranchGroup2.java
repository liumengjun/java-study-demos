package com.three_d.mofangv2;

import java.applet.Applet;
import java.awt.Component;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
//import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.PickTool;

class BranchGroup2 extends BranchGroup
{
	// false表示没有被选中，不显示;true选中，显示
	boolean[][][] blockSelected = {
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, },
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, },
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, }, };

	boolean[][][] bufferBlockSelected = {
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, },
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, },
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, }, };

	boolean[][][] lastBufferBlockSelected = {
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, },
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, },
			{ { false, false, false, }, { false, false, false, },
					{ false, false, false, }, }, };

	// 0表示仅显示被选方块，1表示选中了等于x的所在层，2为等于y的所在层,3为等于z的所在层
	int[][][] blockSelectedStatus = {
			{ { 0, 0, 0, }, { 0, 0, 0 }, { 0, 0, 0 }, },
			{ { 0, 0, 0, }, { 0, 0, 0 }, { 0, 0, 0 }, },
			{ { 0, 0, 0, }, { 0, 0, 0 }, { 0, 0, 0 }, }, };

	// ------------------------------------
	//透明性设置
	TransparencyAttributes[][][][] biaoMianTransparencyAttributes = new TransparencyAttributes[3][3][3][6];

	// ========================

	public static float zuoBiaoZhouSmallDingDian = 0.09f;

	// 小坐标顶点位置
	public static float zuoBiaoZhouSmallDingXi = 0.02f;

	// 小坐标顶点伞的半径
	public static float zuoBiaoZhouSmallDingChang = 0.07f;

	// 小坐标顶点伞的长度
	public static float zuoBiaoZhouSmallWeiDian = -0.09f;

	// 小坐标尾巴的位置

	public static float zuoBiaoZhouBigDingDian = 1.0f;

	// 大坐标顶点位置
	public static float zuoBiaoZhouBigDingXi = 0.04f;

	// 大坐标顶点伞的半径
	public static float zuoBiaoZhouBigDingChang = 0.8f;

	// 大坐标顶点伞的长度
	public static float zuoBiaoZhouBigWeiDian = -1.0f;

	// 大坐标尾巴的位置

	// -----------
	//默认块编号和
	int[][][] blockBianHaoMoRen = { { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, },
			{ { 9, 10, 11 }, { 12, 13, 14 }, { 15, 16, 17 }, },
			{ { 18, 19, 20 }, { 21, 22, 23 }, { 24, 25, 26 }, },

	};
//自定义块编号
	int[][][] blockBianHaoZiDingYi = {
			{ { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, },
			{ { 9, 10, 11 }, { 12, 13, 14 }, { 15, 16, 17 }, },
			{ { 18, 19, 20 }, { 21, 22, 23 }, { 24, 25, 26 }, },

	};

	Text3D[][][] text3DArray;// 文本

	// ==============================

	public static float fangKuaiBanJing = 0.12f;

	// 每面的颜色,排列为:+x,-x,+y,-y,+z,-z,no
	public static Color3f[] mianColor = { new Color3f(1.0f, 0.0f, 0.0f),
			new Color3f(0.0f, 1.0f, 0.0f), new Color3f(0.0f, 0.0f, 1.0f),
			new Color3f(1.0f, 1.0f, 0.0f), new Color3f(1.0f, 0.0f, 1.0f),
			new Color3f(0.0f, 1.0f, 1.0f), new Color3f(0.2f, 0.2f, 0.2f) };

	String[] mianImageFile = new String[7];

	// 块偏移量
	static float kuaiZhongXinWeizhi = 0.25f;

	// 创建材质时要用的,仅用于他,他是一个applet对象
	Component observer;

	// ===============================================

	// BoundingSphere bounds=new BoundingSphere(new Point3d(0.0,0.0,0.0),1.0f);
	// ========================
	// 全局变量分类地放在这里
	TransformGroup rootTransformGroup;

	// TransformGroup meTransformGroup;
	TransformGroup helpTransformGroup;

	// ==============
	BranchGroup[][][] blockBranchGroup;

	BranchGroup daZuoBiaoWaiTaoBranchGroup;

	TransformGroup[][][] blockTransformGroup;

	TransformGroup[][][] blockTransformGroup1;

	TransformGroup[][][] blockTransformGroup2;

	BranchGroup[][][] neiBiaoMianWaiTaoBranchGroup;

	BranchGroup[][][] waiBiaoMianWaiTaoBranchGroup;

	BranchGroup[][][] xiaoZuoBiaoWaiTaoBranchGroup;

	BranchGroup[][][] blockBianHaoBranchGroup;

	BranchGroup[][][] showBlockSelectedBranchGroup;

	// ===========
	Transform3D helpTransform3D;

	Transform3D[][][] blockTransform3D;

	Transform3D[][][] blockTransform3D1;

	Transform3D[][][] blockTransform3D2;

	// -----------------
	// Shape3D[][][] shape3DArray;
	// -----------------
	// ColorCube aColorCube;
	//
	Point3D[][][] centerPointSaved;

	// Applet theApplet;

	//

	// =====================================
	BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1.0f);

	// ==========================

	Shape3D shapeMaker(Component observer, String filename, Point3f[] p, int i,
			int j, int k, int l, boolean isBiaoMian)
	{

		// 创建贴图和外观
		TextureLoader loader = new TextureLoader(filename, observer);
		ImageComponent2D myImage = loader.getImage();
		Texture myTex = loader.getTexture();
		myTex.setImage(0, myImage);

		// 使四边形正反面均可见，否则只能从正面看见
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);

		this.biaoMianTransparencyAttributes[i][j][k][l] = new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.4f);
		this.biaoMianTransparencyAttributes[i][j][k][l]
				.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
		this.biaoMianTransparencyAttributes[i][j][k][l]
				.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		this.biaoMianTransparencyAttributes[i][j][k][l]
				.setCapability(TransparencyAttributes.ALLOW_MODE_READ);
		this.biaoMianTransparencyAttributes[i][j][k][l]
				.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);

		Material mm = new Material();
		mm.setLightingEnable(false);

		Appearance appear = new Appearance();
		appear.setTexture(myTex);
		appear.setPolygonAttributes(polyAttrib);
		appear.setMaterial(mm);
		// appear.setPointAttributes(pa);

		if (isBiaoMian == true)
		{
			appear
					.setTransparencyAttributes(this.biaoMianTransparencyAttributes[i][j][k][l]);
		}

		QuadArray tri = new QuadArray(4, GeometryArray.COORDINATES
				| GeometryArray.TEXTURE_COORDINATE_2);
		// GeometryArray
		tri.setCoordinates(0, p);
		// tri.setColors(0,color);

		// 给四边形对象配材质
		TexCoord2f texCoords = new TexCoord2f();
		// 材质坐标
		texCoords.set(0.0f, 1.0f);
		// 取左下角
		tri.setTextureCoordinate(0, 0, texCoords);
		// 为左上角
		texCoords.set(0.0f, 0.0f);
		//
		tri.setTextureCoordinate(0, 1, texCoords);
		//
		texCoords.set(1.0f, 0.0f);
		//
		tri.setTextureCoordinate(0, 2, texCoords);
		//
		texCoords.set(1.0f, 1.0f);
		//
		tri.setTextureCoordinate(0, 3, texCoords);
		//
		// ------------------------
		// --------------------
		Shape3D shape = new Shape3D(tri, appear);
		// ---------------------------

		PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
		// shape.setPickable(false);

		// -----------------------------

		//

		return shape;
	}

	// center点仅用来计算颜色
	// 给变换组加上一个方块，不同的位置有各自的结果
	void add3DCube(int centerx, int centery, int centerz, Group myTransGroup,
			boolean onlyBiaoMian)
	{

		int[] compare = new int[6];
		compare[0] = centerx;
		// x
		compare[1] = centerx;
		// x
		compare[2] = centery;
		// y
		compare[3] = centery;
		// y
		compare[4] = centerz;
		// z
		compare[5] = centerz;
		// z

		int[] compareWith = new int[6];
		compareWith[0] = 1;
		compareWith[1] = -1;
		compareWith[2] = 1;
		compareWith[3] = -1;
		compareWith[4] = 1;
		compareWith[5] = -1;

		// 面图
		String presentImageFile;

		this.mianImageFile[0] = "images/rubik/coverRight.jpg";
		this.mianImageFile[1] = "images/rubik/coverLeft.jpg";
		this.mianImageFile[2] = "images/rubik/coverUp.jpg";
		this.mianImageFile[3] = "images/rubik/coverDown.jpg";
		this.mianImageFile[4] = "images/rubik/coverFront.jpg";
		this.mianImageFile[5] = "images/rubik/coverBehind.jpg";
		this.mianImageFile[6] = "images/rubik/coverCenter.jpg";

		// 点数据结构
		Vector3f mianxin = new Vector3f();
       //面心偏移量
		Vector3f[] mianxinpianyi = new Vector3f[6];
		mianxinpianyi[0] = new Vector3f(1, 0, 0);
		mianxinpianyi[1] = new Vector3f(-1, 0, 0);
		mianxinpianyi[2] = new Vector3f(0, 1, 0);
		mianxinpianyi[3] = new Vector3f(0, -1, 0);
		mianxinpianyi[4] = new Vector3f(0, 0, 1);
		mianxinpianyi[5] = new Vector3f(0, 0, -1);

		Vector3f[] dingdianPianyiX = new Vector3f[4];

		dingdianPianyiX[0] = new Vector3f(0.0f, 1.0f, 1.0f);
		dingdianPianyiX[1] = new Vector3f(0.0f, -1.0f, 1.0f);
		dingdianPianyiX[2] = new Vector3f(0.0f, -1.0f, -1.0f);
		dingdianPianyiX[3] = new Vector3f(0.0f, 1.0f, -1.0f);

		Vector3f[] dingdianPianyiY = new Vector3f[4];
		dingdianPianyiY[0] = new Vector3f(1.0f, 0.0f, 1.0f);
		dingdianPianyiY[1] = new Vector3f(1.0f, 0.0f, -1.0f);
		dingdianPianyiY[2] = new Vector3f(-1.0f, 0.0f, -1.0f);
		dingdianPianyiY[3] = new Vector3f(-1.0f, 0.0f, 1.0f);

		Vector3f[] dingdianPianyiZ = new Vector3f[4];
		dingdianPianyiZ[0] = new Vector3f(1.0f, 1.0f, 0.0f);
		dingdianPianyiZ[1] = new Vector3f(-1.0f, 1.0f, 0.0f);
		dingdianPianyiZ[2] = new Vector3f(-1.0f, -1.0f, 0.0f);
		dingdianPianyiZ[3] = new Vector3f(1.0f, -1.0f, 0.0f);

		for (int i = 0; i <= 5; i++)
		{

			// 计算该面 颜色和贴图
			if (compare[i] == compareWith[i])
			{
				presentImageFile = this.mianImageFile[i];
				if (onlyBiaoMian == false)
				{
					continue;
				}
			} else
			{
				presentImageFile = this.mianImageFile[6];
				// 6号颜色是白色，如果颜色为白色，不画该面
				// continue则跳过不画
				if (onlyBiaoMian == true)
				{
					continue;
				}
			}

			try
			{

			} catch (Exception e)
			{
			}

			// 计算该面 面心
			mianxin.x = mianxinpianyi[i].x;
			mianxin.y = mianxinpianyi[i].y;
			mianxin.z = mianxinpianyi[i].z;

			// 计算该面 四个点
			Vector3f[] dingdian = new Vector3f[4];

			for (int j = 0; j <= 3; j++)
			{

				dingdian[j] = new Vector3f();

				if ((i == 0) || (i == 1))
				{//左右面
					dingdian[j].x = mianxin.x + dingdianPianyiX[j].x;
					dingdian[j].y = mianxin.y + dingdianPianyiX[j].y;
					dingdian[j].z = mianxin.z + dingdianPianyiX[j].z;
				}
				else if ((i == 2) || (i == 3))
				{//上下面
					dingdian[j].x = mianxin.x + dingdianPianyiY[j].x;
					dingdian[j].y = mianxin.y + dingdianPianyiY[j].y;
					dingdian[j].z = mianxin.z + dingdianPianyiY[j].z;
				}
				else if ((i == 4) || (i == 5))
				{//前后面i为面编号
					dingdian[j].x = mianxin.x + dingdianPianyiZ[j].x;
					dingdian[j].y = mianxin.y + dingdianPianyiZ[j].y;
					dingdian[j].z = mianxin.z + dingdianPianyiZ[j].z;
				}

			}

			Point3f[] vertArray = new Point3f[4];
			// Point3f[] vert2=new Point3f[4];
			// 4个点的信息
			for (int k = 0; k <= 3; k++)
			{
				// 三个面为正向面，三个面为负向面
				// if(i==0||i==2||i==4)
				// {

				vertArray[3 - k] = new Point3f(fangKuaiBanJing * dingdian[k].x,
						fangKuaiBanJing * dingdian[k].y, fangKuaiBanJing
								* dingdian[k].z);

			}

			if (onlyBiaoMian == true)
			{
				Shape3D shape = this.shapeMaker(this.observer,
						presentImageFile, vertArray, centerx + 1, centery + 1,
						centerz + 1, i, true);
				myTransGroup.addChild(shape);
			} else
			{
				Shape3D shape = this.shapeMaker(this.observer,
						presentImageFile, vertArray, centerx + 1, centery + 1,
						centerz + 1, i, false);
				myTransGroup.addChild(shape);
			}

		}

	}
///////////////////////////////here we go!///////////////////////////
	BranchGroup2(Applet atheApplet)
	{

		this.setCapability(BranchGroup.ALLOW_DETACH);

		// =====================================
		// 创建结点-组对象
		this.rootTransformGroup = new TransformGroup();

		// 设置结点-组对象的功能-rootTransformGroup-能用程序重设位置和某个方向的旋转量
		this.rootTransformGroup
				.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.rootTransformGroup
				.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		// neiBiaoMianWaiTao
		this.rootTransformGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.rootTransformGroup.setCapability(Group.ALLOW_CHILDREN_READ);
		this.rootTransformGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
		// rootTransformGroup.setCapability(BranchGroup.ENABLE_PICK_REPORTING);

		// -----------------------------
		this.helpTransformGroup = new TransformGroup();
		// 用于动态旋转
		this.helpTransformGroup
				.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.helpTransformGroup
				.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		this.helpTransformGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.helpTransformGroup.setCapability(Group.ALLOW_CHILDREN_READ);
		this.helpTransformGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);

		this.helpTransformGroup.setPickable(false);
		// ======================
		this.daZuoBiaoWaiTaoBranchGroup = new BranchGroup();
		this.daZuoBiaoWaiTaoBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		this.daZuoBiaoWaiTaoBranchGroup.setPickable(false);
		// ============
		// 这里测试自定义键盘事件
		MyKeyBoardBehavior aMyBehavior = new MyKeyBoardBehavior();
		aMyBehavior.setSchedulingBounds(this.bounds);
		this.addChild(aMyBehavior);
		// =============
		this.blockBranchGroup = new BranchGroup[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.blockBranchGroup[i][j][k] = new BranchGroup();
					this.blockBranchGroup[i][j][k]
							.setCapability(BranchGroup.ALLOW_DETACH);
					this.blockBranchGroup[i][j][k]
							.setCapability(Node.ENABLE_PICK_REPORTING);
				}
			}
		}
		// ====================================
		this.blockTransformGroup = new TransformGroup[3][3][3];

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.blockTransformGroup[i][j][k] = new TransformGroup();
					// 仅用于定每块的新的位置用它重新画,自转的方式忽略，重建该块的几何图完成
					// 当然每次只重新生成9个块
					this.blockTransformGroup[i][j][k]
							.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
					this.blockTransformGroup[i][j][k]
							.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

				}
			}

		}

		this.blockTransformGroup1 = new TransformGroup[3][3][3];
		// blockTransform3D1=new Transform3D[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.blockTransformGroup1[i][j][k] = new TransformGroup();
					// blockTransform3D1[i][j][k]=new Transform3D();
					// blockTransform3D1[i][j][k].rotX(Math.toRadians(0));

					this.blockTransformGroup1[i][j][k]
							.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
					this.blockTransformGroup1[i][j][k]
							.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
					// blockTransformGroup1[i][j][k].setCapability(TransformGroup.ENABLE_PICK_REPORTING);

				}
			}

		}

		this.blockTransformGroup2 = new TransformGroup[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.blockTransformGroup2[i][j][k] = new TransformGroup();

					this.blockTransformGroup2[i][j][k]
							.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
					this.blockTransformGroup2[i][j][k]
							.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

					this.blockTransformGroup2[i][j][k]
							.setCapability(Group.ALLOW_CHILDREN_EXTEND);
					this.blockTransformGroup2[i][j][k]
							.setCapability(Group.ALLOW_CHILDREN_READ);
					this.blockTransformGroup2[i][j][k]
							.setCapability(Group.ALLOW_CHILDREN_WRITE);

				}
			}

		}
        //设置灰色间隔骨架
		this.neiBiaoMianWaiTaoBranchGroup = new BranchGroup[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.neiBiaoMianWaiTaoBranchGroup[i][j][k] = new BranchGroup();

					this.neiBiaoMianWaiTaoBranchGroup[i][j][k]
							.setCapability(BranchGroup.ALLOW_DETACH);

				}
			}

		}
		this.waiBiaoMianWaiTaoBranchGroup = new BranchGroup[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.waiBiaoMianWaiTaoBranchGroup[i][j][k] = new BranchGroup();

					this.waiBiaoMianWaiTaoBranchGroup[i][j][k]
							.setCapability(BranchGroup.ALLOW_DETACH);

				}
			}

		}
		this.xiaoZuoBiaoWaiTaoBranchGroup = new BranchGroup[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.xiaoZuoBiaoWaiTaoBranchGroup[i][j][k] = new BranchGroup();

					this.xiaoZuoBiaoWaiTaoBranchGroup[i][j][k]
							.setCapability(BranchGroup.ALLOW_DETACH);
					this.xiaoZuoBiaoWaiTaoBranchGroup[i][j][k]
							.setPickable(false);
				}
			}
			// /
		}

		this.blockBianHaoBranchGroup = new BranchGroup[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.blockBianHaoBranchGroup[i][j][k] = new BranchGroup();

					this.blockBianHaoBranchGroup[i][j][k]
							.setCapability(BranchGroup.ALLOW_DETACH);
				}
			}
			// /
		}

		this.showBlockSelectedBranchGroup = new BranchGroup[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.showBlockSelectedBranchGroup[i][j][k] = new BranchGroup();

					this.showBlockSelectedBranchGroup[i][j][k]
							.setCapability(BranchGroup.ALLOW_DETACH);
					this.showBlockSelectedBranchGroup[i][j][k]
							.setPickable(false);
				}
			}
			// /
		}

		// meTransformGroup=new TransformGroup();
		// 设置结点-组对象的功能-meTransformGroup-能用程序重设位置和某个方向的旋转量
		// meTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// meTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		// =======================================
		// 创建叶子-物体对象
		// aColorCube=new ColorCube(0.5f);
		// ===============================
		// 创建叶子-行为对象
		// 鼠标旋转行为
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(this.rootTransformGroup);
		behavior.setSchedulingBounds(this.bounds);
		this.addChild(behavior);

		// =====================================
		// -----------------------------------------
		// 结点和叶子-由上到下组装或者由下至上组装.
		// 由上到下
		this.addChild(this.rootTransformGroup);
		this.rootTransformGroup.addChild(this.helpTransformGroup);

		this.rootTransformGroup.addChild(this.daZuoBiaoWaiTaoBranchGroup);

		// 27个块先装到根上。用动画时才装9个到helpTransformGroup上
		// zuoBiaoZhuBigXShape3D(daZuoBiaoWaiTaoBranchGroup);
        //设置背景
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.rootTransformGroup
							.addChild(this.blockBranchGroup[i][j][k]);

				}
			}
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.blockBranchGroup[i][j][k]
							.addChild(this.blockTransformGroup[i][j][k]);
					this.blockTransformGroup[i][j][k]
							.addChild(this.blockTransformGroup1[i][j][k]);
					this.blockTransformGroup1[i][j][k]
							.addChild(this.blockTransformGroup2[i][j][k]);

				}
			}
			// /
		}

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.blockTransformGroup2[i][j][k]
							.addChild(this.neiBiaoMianWaiTaoBranchGroup[i][j][k]);
					this.blockTransformGroup2[i][j][k]
							.addChild(this.xiaoZuoBiaoWaiTaoBranchGroup[i][j][k]);
					this.blockTransformGroup2[i][j][k]
							.addChild(this.waiBiaoMianWaiTaoBranchGroup[i][j][k]);
					this.blockTransformGroup2[i][j][k]
							.addChild(this.blockBianHaoBranchGroup[i][j][k]);

					// 去掉了该行，表示最初不显示所有选择框，否则全显示，是病冻效果
					// blockTransformGroup2[i][j][k].addChild(showBlockSelectedBranchGroup[i][j][k]);

				}
			}

		}
		// ------------------------
		// shape3DArray=new Shape3D[3][3][3];
  //加魔方各块之间的灰色间隔，间隔的骨架
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					// /shape3DArray[i][j][k]=new Shape3D;
					// if(j==0)
					this.add3DCube(i - 1, j - 1, k - 1,
							this.neiBiaoMianWaiTaoBranchGroup[i][j][k], false);
					// add3DCube(i-1,j-1,k-1,blockTransformGroup[i][j][k],false);

				}
			}
			// /
		}
       //显示含有给定的颜色的各小方块
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					// /shape3DArray[i][j][k]=new Shape3D;
					// if(j==0)
					this.add3DCube(i - 1, j - 1, k - 1,
							this.waiBiaoMianWaiTaoBranchGroup[i][j][k], true);
					// add3DCube(i-1,j-1,k-1,blockTransformGroup[i][j][k],true);

				}
			}
			// /
		}
		 //编译或者不编译，可能是进行优化。之后不允许使用removeChild，只有分支组能使用
		this.compile();
	}

	void showAllBlockSelectedStatus()
	{

		// ================缓冲清空
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					this.bufferBlockSelected[i][j][k] = false;

				}
			}
		}

		// =================生成缓冲，找出应该显示的块
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (this.blockSelected[i][j][k] == true)
					{
						// 对四种选择状态进行处理
						if (this.blockSelectedStatus[i][j][k] == 0)
						{
							// 仅选中该块状态
							this.bufferBlockSelected[i][j][k] = true;

						} else if (this.blockSelectedStatus[i][j][k] == 1)
						{
							// 选中该层状态，=x同层的也显示

							for (int l = 0; l < 3; l++)
							{
								for (int m = 0; m < 3; m++)
								{
									for (int n = 0; n < 3; n++)
									{
										if (MoFang.aMofangStatusMessage.dataMessage[l][m][n].blockCenter.x == MoFang.aMofangStatusMessage.dataMessage[i][j][k].blockCenter.x)
										{
											this.bufferBlockSelected[l][m][n] = true;
										}
									}
								}
							}
						}

						else if (this.blockSelectedStatus[i][j][k] == 2)
						{
							// 选中该层状态，=y同层的也显示

							for (int l = 0; l < 3; l++)
							{
								for (int m = 0; m < 3; m++)
								{
									for (int n = 0; n < 3; n++)
									{
										if (MoFang.aMofangStatusMessage.dataMessage[l][m][n].blockCenter.y == MoFang.aMofangStatusMessage.dataMessage[i][j][k].blockCenter.y)
										{
											this.bufferBlockSelected[l][m][n] = true;
										}
									}
								}
							}
						} else if (this.blockSelectedStatus[i][j][k] == 3)
						{
							// 选中该层状态，=z同层的也显示

							for (int l = 0; l < 3; l++)
							{
								for (int m = 0; m < 3; m++)
								{
									for (int n = 0; n < 3; n++)
									{
										if (MoFang.aMofangStatusMessage.dataMessage[l][m][n].blockCenter.z == MoFang.aMofangStatusMessage.dataMessage[i][j][k].blockCenter.z)
										{
											this.bufferBlockSelected[l][m][n] = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

	void setStatus(BlockMessage[][][] dataMessage)
	{
		// MofangStatusMessage
		this.setRotateSelf(dataMessage);
		this.setPosition(dataMessage);

	}

	void setPosition(BlockMessage[][][] dataMessage)
	{
		// --------
		this.centerPointSaved = new Point3D[3][3][3];
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					this.centerPointSaved[i][j][k] = new Point3D(
							dataMessage[i][j][k].blockCenter.x,
							dataMessage[i][j][k].blockCenter.y,
							dataMessage[i][j][k].blockCenter.z);
				}
				// ---------
			}
		}

		this.blockTransform3D = new Transform3D[3][3][3];

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					float fx;
					float fy;
					float fz;

					fx = kuaiZhongXinWeizhi
							* dataMessage[i][j][k].blockCenter.x;
					fy = kuaiZhongXinWeizhi
							* dataMessage[i][j][k].blockCenter.y;
					fz = kuaiZhongXinWeizhi
							* dataMessage[i][j][k].blockCenter.z;
					Vector3f newVector3f = new Vector3f(fx, fy, fz);
					this.blockTransform3D[i][j][k] = new Transform3D();
					this.blockTransform3D[i][j][k].setTranslation(newVector3f);
					this.blockTransformGroup[i][j][k]
							.setTransform(this.blockTransform3D[i][j][k]);
				}
			}
		}
	}

	void setRotateSelf(BlockMessage[][][] dataMessage)
	{
		//
		this.blockTransform3D1 = new Transform3D[3][3][3];
		this.blockTransform3D2 = new Transform3D[3][3][3];
		// blockTransform3D1=new Transform3D[3][3][3];
		// blockTransform3D2=new Transform3D[3][3][3];

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{

					// =================
					int[][] test1 = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
					int[][] test2 = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
					test2[0][0] = dataMessage[i][j][k].blockEx.x;
					test2[1][0] = dataMessage[i][j][k].blockEx.y;
					test2[2][0] = dataMessage[i][j][k].blockEx.z;

					test2[0][1] = dataMessage[i][j][k].blockEy.x;
					test2[1][1] = dataMessage[i][j][k].blockEy.y;
					test2[2][1] = dataMessage[i][j][k].blockEy.z;

					test2[0][2] = dataMessage[i][j][k].blockEz.x;
					test2[1][2] = dataMessage[i][j][k].blockEz.y;
					test2[2][2] = dataMessage[i][j][k].blockEz.z;

					// Vector
					// myVector=BlockRotateStatusTableAndSouSuo.getShortLuJing(test1,test2);

					int[] controlInt = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
					Vector myVector = BlockRotateStatusAndSerch
							.calculateControledRotateSelf(controlInt, test1,
									test2, -1, -1, 2);

					// ===============
					if (myVector.size() == 0)
					{

						this.blockTransformGroup2[i][j][k]
								.setTransform(new Transform3D());

						this.blockTransformGroup1[i][j][k]
								.setTransform(new Transform3D());

					} else
					{

						String type22 = (String) (myVector.get(0));
						int type2 = Integer.parseInt(type22);
						this.blockTransform3D2[i][j][k] = new Transform3D();
						if (type2 == 0)
						{
							this.blockTransform3D2[i][j][k].rotX(Math
									.toRadians(90));
						} else if (type2 == 1)
						{
							this.blockTransform3D2[i][j][k].rotX(Math
									.toRadians(180));
						} else if (type2 == 2)
						{
							this.blockTransform3D2[i][j][k].rotX(Math
									.toRadians(-90));
						} else if (type2 == 3)
						{
							this.blockTransform3D2[i][j][k].rotY(Math
									.toRadians(90));
						} else if (type2 == 4)
						{
							this.blockTransform3D2[i][j][k].rotY(Math
									.toRadians(180));
						} else if (type2 == 5)
						{
							this.blockTransform3D2[i][j][k].rotY(Math
									.toRadians(-90));
						} else if (type2 == 6)
						{
							this.blockTransform3D2[i][j][k].rotZ(Math
									.toRadians(90));
						} else if (type2 == 7)
						{
							this.blockTransform3D2[i][j][k].rotZ(Math
									.toRadians(180));
						} else if (type2 == 8)
						{
							this.blockTransform3D2[i][j][k].rotZ(Math
									.toRadians(-90));
						} else
						{
							System.out.println(type2
									+ " is a error rotate string ");
							// blockTransform3D2[i][j][k].rotZ(Math.toRadians(270));
						}
						this.blockTransformGroup2[i][j][k]
								.setTransform(this.blockTransform3D2[i][j][k]);

						if (myVector.size() == 1)
						{
							// MyPrintln.println("1用零变换");
							this.blockTransformGroup1[i][j][k]
									.setTransform(new Transform3D());

						} else
						{

							String type11 = (String) (myVector.get(1));
							int type1 = Integer.parseInt(type11);

							this.blockTransform3D1[i][j][k] = new Transform3D();
							// --------

							if (type1 == 0)
							{
								this.blockTransform3D1[i][j][k].rotX(Math
										.toRadians(90));
							} else if (type1 == 1)
							{
								this.blockTransform3D1[i][j][k].rotX(Math
										.toRadians(180));
							} else if (type1 == 2)
							{
								this.blockTransform3D1[i][j][k].rotX(Math
										.toRadians(-90));
							} else if (type1 == 3)
							{
								this.blockTransform3D1[i][j][k].rotY(Math
										.toRadians(90));
							} else if (type1 == 4)
							{
								this.blockTransform3D1[i][j][k].rotY(Math
										.toRadians(180));
							} else if (type1 == 5)
							{
								this.blockTransform3D1[i][j][k].rotY(Math
										.toRadians(-90));
							} else if (type1 == 6)
							{
								this.blockTransform3D1[i][j][k].rotZ(Math
										.toRadians(90));
							} else if (type1 == 7)
							{
								this.blockTransform3D1[i][j][k].rotZ(Math
										.toRadians(180));
							} else if (type1 == 8)
							{
								this.blockTransform3D1[i][j][k].rotZ(Math
										.toRadians(-90));
							} else
							{
								System.out.println(type1
										+ " is a error rotate string ");
								// blockTransform3D2[i][j][k].rotZ(Math.toRadians(270));
							}

							this.blockTransformGroup1[i][j][k]
									.setTransform(this.blockTransform3D1[i][j][k]);

						}
					}
				}
			}
		}
	}

	void dongHua(String typeXYZ012String, String layer012String,
			String jiaoDuString)
	{
		int jiaoDu = Integer.parseInt(jiaoDuString);
		int typeXYZ012 = Integer.parseInt(typeXYZ012String);
		int layer012 = Integer.parseInt(layer012String);
		// =Integer.parseInt (typeXYZ012String);
		this.dongHua(typeXYZ012, layer012, jiaoDu);
	}

	void dongHua(int atypeXYZ012, int alayer012, int ajiaoDu)
	{

		Movie aDongHuaAll = new Movie(this);
		aDongHuaAll.typeXYZ012 = atypeXYZ012;
		aDongHuaAll.layer012 = alayer012;
		aDongHuaAll.jiaoDu = ajiaoDu;

		new Thread(aDongHuaAll, "aDongHuaAll").start();
	}

}
