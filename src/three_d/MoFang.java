package three_d;

//存为MoFang.java

import java.applet.Applet;
import java.awt.*;
import com.sun.j3d.utils.applet.MainFrame;
import java.awt.BorderLayout;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.picking.behaviors.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame.*;
import javax.swing.*;

class mySimpleUniverse extends Applet {
	BranchGroup createSceneGraph(Canvas3D canvas)

	{

		//System.out.print("**1**");

		//创建变换组,无用的t3D
		Transform3D t3d = new Transform3D();
		TransformGroup trans = new TransformGroup(t3d);
		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//创建分枝组
		BranchGroup objRoot = new BranchGroup();

		//测试
		//SomeShape3D.book3D( this, trans);
		SomeShape3D.addText3DDonghua(trans, "魔方",
				new Point3f(-7.0f, 6.0f, 6.0f), 0.1f, new Color3f(1.0f, 0.0f,
						0.0f), 1);

		//初始化数据结构
		System.out.println("\n\n载入方块，并向变换组中加入每个方块的坐标系和方块...");
		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {
					int[] p;
					p = Position.getPxyzFromPositionAy(i, j, k,
							MoFang.positionArray);
					MoFang.blockArray[i][j][k] = new Block(i, j, k, p[0], p[1],
							p[2], trans, t3d, objRoot, this);
				}
		System.out.println("加入每个方块的坐标系和方块,完成.\n");

		//创建大坐标轴,自动加到主坐标系
		SomeShape3D.zuoBiaoZhuBigXShape3D(trans);
		SomeShape3D.zuoBiaoZhuBigYShape3D(trans);
		SomeShape3D.zuoBiaoZhuBigZShape3D(trans);

		//创建边界对象
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100);

		//创建鼠标pick行为，加到分支组objRoot
		PickRotateBehavior pickRotate = new PickRotateBehavior(objRoot, canvas,
				bounds);
		PickTranslateBehavior pickTranslate = new PickTranslateBehavior(
				objRoot, canvas, bounds);
		PickZoomBehavior pickZoom = new PickZoomBehavior(objRoot, canvas,
				bounds);
		//objRoot.addChild(pickRotate);
		objRoot.addChild(pickTranslate);
		//objRoot.addChild(pickZoom);

		//创建鼠标旋转行为
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(trans);
		behavior.setSchedulingBounds(bounds);

		//创建鼠标平移行为
		//MouseTranslate myMouseRotate=new MouseTranslate();
		//myMouseRotate.setTransformGroup(trans);
		//myMouseRotate.setSchedulingBounds(bounds);

		//创建鼠标缩放行为
		MouseZoom myMouseZoom = new MouseZoom();
		myMouseZoom.setTransformGroup(trans);
		myMouseZoom.setSchedulingBounds(bounds);

		//创建键盘默认行为
		KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(trans);
		keyNavBeh.setSchedulingBounds(bounds);
		objRoot.addChild(keyNavBeh);

		//白色背景
		Background bg = new Background(new Color3f(0.0f, 0.0f, 0.0f));
		bg.setApplicationBounds(bounds);
		objRoot.addChild(bg);

		//创建带材质的背景
		//TextureLoader bgTexture=new TextureLoader("bg3.jpg",this);
		//Background bg=new Background(bgTexture.getImage());
		//bg.setApplicationBounds(bounds);
		//trans.addChild(shape1);//3D物体 加到 变换组
		//trans.addChild(shape2);//3D物体 加到 变换组
		objRoot.addChild(trans);
		//变换组 加到 分枝组
		objRoot.addChild(behavior);
		//鼠标行为  加到 分枝组
		//objRoot.addChild(myMouseRotate);
		//objRoot.addChild(myMouseZoom);
		//objRoot.addChild(bg);//背景 加到 分枝组
		//编译
		objRoot.compile();
		//回送创建好的带3D物体的分枝组
		return objRoot;

	}

	mySimpleUniverse() {
		//创建带控制的画布
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);
		//创建以画布为依托的简单场景图对象，没有多个Locale
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		//创建分支组对象
		BranchGroup scene = createSceneGraph(c);
		//组装，分支组 对象加到 场景图
		u.addBranchGraph(scene);

		//带场景图的画布 加到 本applet中
		setLayout(new BorderLayout());
		add("Center", c);
	}

	//测试码
	//public static void main(String aregs[])
	//{new MainFrame(new mySimpleUniverse(),200,200);//加applet到应用程序界面
	//}
}

class SomeShape3D {
	public static float zuoBiaoZhouSmallDingDian = 0.09f;//小坐标顶点位置
	public static float zuoBiaoZhouSmallDingXi = 0.02f;//小坐标顶点伞的半径
	public static float zuoBiaoZhouSmallDingChang = 0.07f;//小坐标顶点伞的长度
	public static float zuoBiaoZhouSmallWeiDian = -0.09f;//小坐标尾巴的位置
	public static float zuoBiaoZhouBigDingDian = 1.0f;//大坐标顶点位置
	public static float zuoBiaoZhouBigDingXi = 0.04f;//大坐标顶点伞的半径
	public static float zuoBiaoZhouBigDingChang = 0.8f;//大坐标顶点伞的长度
	public static float zuoBiaoZhouBigWeiDian = -1.0f;//大坐标尾巴的位置
	public static float fangKuaiBanJing = 0.18f;//每个方块的半径

	public static void zuoBiaoZhuBigXShape3D(TransformGroup trans) {

		//创建大坐标轴对象

		int i;
		float x1, x2, y1, y2, z1, z2;

		SomeShape3D.addText3DDonghua(trans, "X", new Point3f(
				zuoBiaoZhouBigDingDian * 10, 0.0f, 0.0f), 0.1f,
				Block.mianColor[0], 0);

		Point3f[] vert = new Point3f[41];
		Color3f[] colors = new Color3f[41];
		for (i = 0; i < 27; i++) {
			if (i == 0) {
				vert[i] = new Point3f(zuoBiaoZhouBigDingDian, 0.0f, 0.0f);
				colors[i] = Block.mianColor[0];
			} else {
				z1 = (float) (zuoBiaoZhouBigDingXi * Math.cos(i * 2 * Math.PI
						/ 25));
				x1 = zuoBiaoZhouBigDingChang;
				y1 = (float) (zuoBiaoZhouBigDingXi * Math.sin(i * 2 * Math.PI
						/ 25));
				vert[i] = new Point3f(x1, y1, z1);
				colors[i] = Block.mianColor[0];
			}
		}

		for (i = 0; i < 14; i++) {
			if (i == 0) {
				vert[27 + i] = new Point3f(zuoBiaoZhouBigDingDian, 0.0f, 0.0f);
				colors[27 + i] = Block.mianColor[0];
			} else {
				z1 = (float) (0.01f * Math.cos(i * 2 * Math.PI / 12));
				x1 = zuoBiaoZhouBigWeiDian;
				y1 = (float) (0.01f * Math.sin(i * 2 * Math.PI / 12));
				vert[27 + i] = new Point3f(x1, y1, z1);
				colors[27 + i] = Block.mianColor[1];
			}
		}
		int count[] = new int[2];
		count[0] = 27;
		count[1] = 14;

		TriangleFanArray tri = new TriangleFanArray(vert.length,
				TriangleFanArray.COORDINATES | TriangleStripArray.COLOR_3,
				count);
		tri.setCoordinates(0, vert);
		tri.setColors(0, colors);

		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		//polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttrib);

		Shape3D shape = new Shape3D(tri, app);
		System.out.print("zuoBiaoZhuBigX 创建 完成\n");

		trans.addChild(shape);

		//到这里，大坐标轴对象创建完成

	}

	public static void zuoBiaoZhuBigYShape3D(TransformGroup trans) {

		//创建大坐标轴对象

		int i;
		float x1, x2, y1, y2, z1, z2;

		SomeShape3D
				.addText3DDonghua(trans, "Y", new Point3f(-1.0f,
						zuoBiaoZhouBigDingDian * 10, 0.0f), 0.1f,
						Block.mianColor[2], 0);

		Point3f[] vert = new Point3f[41];
		Color3f[] colors = new Color3f[41];
		for (i = 0; i < 27; i++) {
			if (i == 0) {
				vert[i] = new Point3f(0.0f, zuoBiaoZhouBigDingDian, 0.0f);
				colors[i] = Block.mianColor[2];
			} else {
				x1 = (float) (zuoBiaoZhouBigDingXi * Math.cos(i * 2 * Math.PI
						/ 25));
				y1 = zuoBiaoZhouBigDingChang;
				z1 = (float) (zuoBiaoZhouBigDingXi * Math.sin(i * 2 * Math.PI
						/ 25));
				vert[i] = new Point3f(x1, y1, z1);
				colors[i] = Block.mianColor[2];
			}
		}

		for (i = 0; i < 14; i++) {
			if (i == 0) {
				vert[27 + i] = new Point3f(0.0f, zuoBiaoZhouBigDingDian, 0.0f);
				colors[27 + i] = Block.mianColor[2];
			} else {
				x1 = (float) (0.01f * Math.cos(i * 2 * Math.PI / 12));
				y1 = zuoBiaoZhouBigWeiDian;
				z1 = (float) (0.01f * Math.sin(i * 2 * Math.PI / 12));
				vert[27 + i] = new Point3f(x1, y1, z1);
				colors[27 + i] = Block.mianColor[3];
			}
		}
		int count[] = new int[2];
		count[0] = 27;
		count[1] = 14;

		TriangleFanArray tri = new TriangleFanArray(vert.length,
				TriangleFanArray.COORDINATES | TriangleStripArray.COLOR_3,
				count);
		tri.setCoordinates(0, vert);
		tri.setColors(0, colors);
		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		//polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttrib);

		Shape3D shape = new Shape3D(tri, app);

		System.out.print("zuoBiaoZhuBigY 创建 完成\n");

		trans.addChild(shape);

		//到这里，大坐标轴对象创建完成

	}

	public static void zuoBiaoZhuBigZShape3D(TransformGroup trans) {

		//创建大坐标轴对象

		int i;
		float x1, x2, y1, y2, z1, z2;
		SomeShape3D.addText3DDonghua(trans, "Z", new Point3f(-1.0f, 0.0f,
				zuoBiaoZhouBigDingDian * 10), 0.1f, Block.mianColor[4], 0);

		Point3f[] vert = new Point3f[41];
		Color3f[] colors = new Color3f[41];
		for (i = 0; i < 27; i++) {
			if (i == 0) {
				vert[i] = new Point3f(0.0f, 0.0f, zuoBiaoZhouBigDingDian);
				colors[i] = Block.mianColor[4];
			} else {
				y1 = (float) (zuoBiaoZhouBigDingXi * Math.cos(i * 2 * Math.PI
						/ 25));
				z1 = zuoBiaoZhouBigDingChang;
				x1 = (float) (zuoBiaoZhouBigDingXi * Math.sin(i * 2 * Math.PI
						/ 25));
				vert[i] = new Point3f(x1, y1, z1);
				colors[i] = Block.mianColor[4];
			}
		}

		for (i = 0; i < 14; i++) {
			if (i == 0) {
				vert[27 + i] = new Point3f(0.0f, 0.0f, zuoBiaoZhouBigDingDian);
				colors[27 + i] = Block.mianColor[4];
			} else {
				y1 = (float) (0.01f * Math.cos(i * 2 * Math.PI / 12));
				z1 = zuoBiaoZhouBigWeiDian;
				x1 = (float) (0.01f * Math.sin(i * 2 * Math.PI / 12));
				vert[27 + i] = new Point3f(x1, y1, z1);
				colors[27 + i] = Block.mianColor[5];
			}
		}
		int count[] = new int[2];
		count[0] = 27;
		count[1] = 14;

		TriangleFanArray tri = new TriangleFanArray(vert.length,
				TriangleFanArray.COORDINATES | TriangleStripArray.COLOR_3,
				count);
		tri.setCoordinates(0, vert);
		tri.setColors(0, colors);
		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		//polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttrib);

		Shape3D shape = new Shape3D(tri, app);
		System.out.print("zuoBiaoZhuBigZ 创建 完成\n");

		trans.addChild(shape);
		//到这里，大坐标轴对象创建完成
	}

	public static void zuoBiaoZhuSmallXShape3D(TransformGroup trans) {
		//创建小坐标轴对象

		int i;
		float x1, x2, y1, y2, z1, z2;

		Point3f[] vert = new Point3f[41];
		Color3f[] colors = new Color3f[41];
		for (i = 0; i < 27; i++) {
			if (i == 0) {
				vert[i] = new Point3f(zuoBiaoZhouSmallDingDian, 0.0f, 0.0f);
				colors[i] = Block.mianColor[0];
			} else {
				z1 = (float) (zuoBiaoZhouSmallDingXi * Math.cos(i * 2 * Math.PI
						/ 25));
				x1 = zuoBiaoZhouSmallDingChang;
				y1 = (float) (zuoBiaoZhouSmallDingXi * Math.sin(i * 2 * Math.PI
						/ 25));
				vert[i] = new Point3f(x1, y1, z1);
				colors[i] = Block.mianColor[0];
			}
		}

		for (i = 0; i < 14; i++) {
			if (i == 0) {
				vert[27 + i] = new Point3f(zuoBiaoZhouSmallDingDian, 0.0f, 0.0f);
				colors[27 + i] = Block.mianColor[0];
			} else {
				z1 = (float) (0.005f * Math.cos(i * 2 * Math.PI / 12));
				x1 = zuoBiaoZhouSmallWeiDian;
				y1 = (float) (0.005f * Math.sin(i * 2 * Math.PI / 12));
				vert[27 + i] = new Point3f(x1, y1, z1);
				colors[27 + i] = Block.mianColor[1];
			}
		}

		int count[] = new int[2];
		count[0] = 27;
		count[1] = 14;

		TriangleFanArray tri = new TriangleFanArray(vert.length,
				TriangleFanArray.COORDINATES | TriangleStripArray.COLOR_3,
				count);
		tri.setCoordinates(0, vert);
		tri.setColors(0, colors);
		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		//polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttrib);

		Shape3D shape = new Shape3D(tri, app);

		//System.out.print("zuoBiaoZhuSmallX 创建 完成");

		trans.addChild(shape);

		//到这里，小坐标轴对象创建完成

	}

	public static void zuoBiaoZhuSmallYShape3D(TransformGroup trans) {

		//创建小坐标轴对象

		int i;
		float x1, x2, y1, y2, z1, z2;

		Point3f[] vert = new Point3f[41];
		Color3f[] colors = new Color3f[41];
		for (i = 0; i < 27; i++) {
			if (i == 0) {
				vert[i] = new Point3f(0.0f, zuoBiaoZhouSmallDingDian, 0.0f);
				colors[i] = Block.mianColor[2];
			} else {
				x1 = (float) (zuoBiaoZhouSmallDingXi * Math.cos(i * 2 * Math.PI
						/ 25));
				y1 = zuoBiaoZhouSmallDingChang;
				z1 = (float) (zuoBiaoZhouSmallDingXi * Math.sin(i * 2 * Math.PI
						/ 25));
				vert[i] = new Point3f(x1, y1, z1);
				colors[i] = Block.mianColor[2];
			}
		}

		for (i = 0; i < 14; i++) {
			if (i == 0) {
				vert[27 + i] = new Point3f(0.0f, zuoBiaoZhouSmallDingDian, 0.0f);
				colors[27 + i] = Block.mianColor[2];
			} else {
				x1 = (float) (0.005f * Math.cos(i * 2 * Math.PI / 12));
				y1 = zuoBiaoZhouSmallWeiDian;
				z1 = (float) (0.005f * Math.sin(i * 2 * Math.PI / 12));
				vert[27 + i] = new Point3f(x1, y1, z1);
				colors[27 + i] = Block.mianColor[3];
			}
		}
		int count[] = new int[2];
		count[0] = 27;
		count[1] = 14;

		TriangleFanArray tri = new TriangleFanArray(vert.length,
				TriangleFanArray.COORDINATES | TriangleStripArray.COLOR_3,
				count);
		tri.setCoordinates(0, vert);
		tri.setColors(0, colors);
		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		//polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttrib);

		Shape3D shape = new Shape3D(tri, app);
		//System.out.print("zuoBiaoZhuSmallY 创建 完成");

		trans.addChild(shape);

		//到这里，小坐标轴对象创建完成

	}

	public static void zuoBiaoZhuSmallZShape3D(TransformGroup trans) {

		//创建小坐标轴对象

		int i;
		float x1, x2, y1, y2, z1, z2;

		Point3f[] vert = new Point3f[41];
		Color3f[] colors = new Color3f[41];
		for (i = 0; i < 27; i++) {
			if (i == 0) {
				vert[i] = new Point3f(0.0f, 0.0f, zuoBiaoZhouSmallDingDian);
				colors[i] = Block.mianColor[4];
			} else {
				y1 = (float) (zuoBiaoZhouSmallDingXi * Math.cos(i * 2 * Math.PI
						/ 25));
				z1 = zuoBiaoZhouSmallDingChang;
				x1 = (float) (zuoBiaoZhouSmallDingXi * Math.sin(i * 2 * Math.PI
						/ 25));
				vert[i] = new Point3f(x1, y1, z1);
				colors[i] = Block.mianColor[4];
			}
		}

		for (i = 0; i < 14; i++) {
			if (i == 0) {
				vert[27 + i] = new Point3f(0.0f, 0.0f, zuoBiaoZhouSmallDingDian);
				colors[27 + i] = Block.mianColor[4];
			} else {
				y1 = (float) (0.005f * Math.cos(i * 2 * Math.PI / 12));
				z1 = zuoBiaoZhouSmallWeiDian;
				x1 = (float) (0.005f * Math.sin(i * 2 * Math.PI / 12));
				vert[27 + i] = new Point3f(x1, y1, z1);
				colors[27 + i] = Block.mianColor[5];
			}
		}
		int count[] = new int[2];
		count[0] = 27;
		count[1] = 14;

		TriangleFanArray tri = new TriangleFanArray(vert.length,
				TriangleFanArray.COORDINATES | TriangleStripArray.COLOR_3,
				count);
		tri.setCoordinates(0, vert);
		tri.setColors(0, colors);

		Appearance app = new Appearance();

		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		//polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		app.setPolygonAttributes(polyAttrib);

		Shape3D shape = new Shape3D(tri, app);

		//System.out.print("zuoBiaoZhuSmallZ 创建 完成");

		trans.addChild(shape);

		//到这里，小坐标轴对象创建完成

	}

	public static Shape3D shapeMaker(Component observer, String filename,
			Point3f[] p) {

		//用材质，四顶点数组创建一个四边面，需要applet对象observer

		//创建贴图和外观
		TextureLoader loader = new TextureLoader(filename, observer);
		ImageComponent2D myImage = loader.getImage();
		Texture myTex = loader.getTexture();
		myTex.setImage(0, myImage);
		Appearance appear = new Appearance();
		appear.setTexture(myTex);

		//四边形对象
		//QuadArray tri=new QuadArray(dingdian.length,QuadArray.COORDINATES|QuadArray.COLOR_3|QuadArray.TEXTURE_COORDINATE_2);
		QuadArray tri = new QuadArray(4, QuadArray.COORDINATES
				| QuadArray.TEXTURE_COORDINATE_2);//GeometryArray
		tri.setCoordinates(0, p);
		//tri.setColors(0,color);

		//给四边形对象配材质
		TexCoord2f texCoords = new TexCoord2f();//材质坐标
		texCoords.set(0.0f, 1.0f);//取左下角
		tri.setTextureCoordinate(0, 0, texCoords);//为左上角
		texCoords.set(0.0f, 0.0f);//
		tri.setTextureCoordinate(0, 1, texCoords);//
		texCoords.set(1.0f, 0.0f);//
		tri.setTextureCoordinate(0, 2, texCoords);//
		texCoords.set(1.0f, 1.0f);//
		tri.setTextureCoordinate(0, 3, texCoords);//

		Shape3D shape = new Shape3D(tri, appear);
		return shape;

		//到这里，6个面对象创建完成
	}

	public static void addText3DDonghua(TransformGroup parentTrg,
			String textString, Point3f myPoint3f, float sl,
			Color3f ambientColor, int donghua) {
		//s1定scale，myPoint3f定位置，daxiao是大小
		//字的左下角默认左下角在中点，当tl=0.1时，要向左移10才到左端

		//自定义trg
		Transform3D trgtra = new Transform3D();
		TransformGroup trg = new TransformGroup(trgtra);
		trg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		trg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//trg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		double tessellation = -0.0;
		String fontName = "vadana";
		// Create the root of the branch graph
		// Create a Transformgroup to scale all objects so they
		// appear in the scene.
		TransformGroup objScale = new TransformGroup();
		Transform3D t3d = new Transform3D();
		// Assuming uniform size chars, set scale to fit string in view

		t3d.setScale(sl);

		objScale.setTransform(t3d);
		trg.addChild(objScale);

		// Create the transform group node and initialize it to the
		// identity.  Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime.  Add it to the
		// root of the subgraph.
		TransformGroup objTrans = new TransformGroup();
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		objScale.addChild(objTrans);

		Font3D f3d;
		if (tessellation > 0.0) {
			f3d = new Font3D(new Font(fontName, Font.PLAIN, 2), tessellation,
					new FontExtrusion());
		} else {
			f3d = new Font3D(new Font(fontName, Font.PLAIN, 2),
					new FontExtrusion());
		}
		Text3D txt = new Text3D(f3d, textString, myPoint3f);
		Shape3D sh = new Shape3D();
		Appearance app = new Appearance();
		Material mm = new Material();
		mm.setLightingEnable(true);
		app.setMaterial(mm);
		sh.setGeometry(txt);
		sh.setAppearance(app);
		objTrans.addChild(sh);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// Set up the ambient light

		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(bounds);
		trg.addChild(ambientLightNode);

		// Set up the directional lights
		Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
		Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
		Color3f light2Color = new Color3f(1.0f, 1.0f, 0.9f);
		Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);

		DirectionalLight light1 = new DirectionalLight(light1Color,
				light1Direction);
		light1.setInfluencingBounds(bounds);
		trg.addChild(light1);

		DirectionalLight light2 = new DirectionalLight(light2Color,
				light2Direction);
		light2.setInfluencingBounds(bounds);
		trg.addChild(light2);

		if (donghua == 1) {
			//给trg（自定义）,加上旋转插件
			Alpha alpha1 = new Alpha(-1, Alpha.INCREASING_ENABLE
					| Alpha.DECREASING_ENABLE, 0, 0, 5000, 300, 100000, 5000,
					300, 100000);
			RotationInterpolator myRoTate = new RotationInterpolator(alpha1,
					trg, trgtra, 0.0f, (float) Math.PI * 30);
			myRoTate.setSchedulingBounds(bounds);
			trg.addChild(myRoTate);
			trgtra.rotZ(Math.PI / 2);
			trg.setTransform(trgtra);
			System.out.println("\n文本 动画 方案:" + donghua);
		}
		parentTrg.addChild(trg);
	}

}

//由于java数组的局限,整个程序使用0,1,2空间,为了方便计算,计算时要进行坐标变换

public class MoFang {
	//该类含魔方的数据表示,blockArray为计算单步必需的，Position为便于人的视觉（用于操作和
	//显示输出）而添加的辅助结构，实际上，我们可以总是从blockArray算出来，但计算量大得惊人
	public static Block[][][] blockArray = new Block[3][3][3];
	//设魔方27块
	public static Position[][][] positionArray = new Position[3][3][3];

	//魔方27个绝对位置

	//用于处理对魔方的操作，共27种操作
	//doType:'X','Y','Z'
	//Floor:-1,0,1
	//totateArg:90 180 -90
	public static void doIt(char doType, int Floor, int totateArg) {
		System.out.println("\n处理操作:" + doType);
		System.out.println("层数:" + (Floor - 1));
		System.out.println("角度:" + totateArg + "\n");

		if (!Block.closeDonghua) //动画打开才等待
		{

			while (Block.yunXingThread != 0) {
				System.out.print('.');

			}

		}
		switch (doType) {
		case 'Z':
			for (int i = 0; i <= 2; i++)
				for (int j = 0; j <= 2; j++)
				//for(int k=0;k<=2;k++)
				{
					if (Block.closeDonghua)//动画关闭才变换中间块
					{
						if (i == 1 && j == 1) {
							continue;
						}
					}

					int changBlockX = positionArray[i][j][Floor].x;
					int changBlockY = positionArray[i][j][Floor].y;
					int changBlockZ = positionArray[i][j][Floor].z;
					Block changBlock = blockArray[changBlockX][changBlockY][changBlockZ];
					changBlock.xyzChange('Z', totateArg);

				}

			break;
		case 'Y':
			for (int i = 0; i <= 2; i++)
				//for(int j=0;j<=2;j++)
				for (int k = 0; k <= 2; k++) {
					if (Block.closeDonghua) {
						if (i == 1 && k == 1) {
							continue;
						}
					}
					int changBlockX = positionArray[i][Floor][k].x;
					int changBlockY = positionArray[i][Floor][k].y;
					int changBlockZ = positionArray[i][Floor][k].z;
					Block changBlock = blockArray[changBlockX][changBlockY][changBlockZ];
					changBlock.xyzChange('Y', totateArg);
				}
			break;

		case 'X':
			//for(int i=0;i<=2;i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {
					if (Block.closeDonghua) {
						if (j == 1 && k == 1) {
							continue;
						}
					}
					int changBlockX = positionArray[Floor][j][k].x;
					int changBlockY = positionArray[Floor][j][k].y;
					int changBlockZ = positionArray[Floor][j][k].z;
					Block changBlock = blockArray[changBlockX][changBlockY][changBlockZ];
					changBlock.xyzChange('X', totateArg);
				}
			break;

		default:
			System.out.println("无效的操作");
		}

		someBlockNewToOld();
	}

	//开始人工智能计算解法
	//select:选择电脑的计算方案
	public static void autoStart(int select) {
	}

	//输出每个位置上的块号
	public static void showPosition() {
		System.out.println("\n每个位置上的块号:");
		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {
					//System.out.println("Block"+i+","+j+","+k+"    "+blockArray[i][j][k].x+","+blockArray[i][j][k].y+","+blockArray[i][j][k].z);
					System.out.println("Position:" + (i - 1) + "," + (j - 1)
							+ "," + (k - 1) + "  的块号是：  "
							+ (positionArray[i][j][k].x - 1) + ","
							+ (positionArray[i][j][k].y - 1) + ","
							+ (positionArray[i][j][k].z - 1));

				}
	}

	//把刚才记录的有变化（来了新的块）的位置 存储到 没个position的块号中
	public static void someBlockNewToOld() {
		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {
					if (positionArray[i][j][k].haveNew) {
						positionArray[i][j][k].newToOld();
					}
				}
	}

	public static void myWait() {
		try {

			System.in.read();

			System.in.read();
			//暂停
		} catch (Exception e) {
		}
	}

	//测试用代码
	public static void someTest() {

		doIt('Y', 0 + 1, 90);
		//        //new MoFang().showPosition();
		MoFang.myWait();
		doIt('Y', 0 + 1, -90);
		MoFang.myWait();
		// new MoFang().showPosition();

		doIt('Z', -1 + 1, -90);
		//new MoFang().showPosition();
		MoFang.myWait();

		doIt('Z', -1 + 1, 90);
		//      new MoFang().showPosition();
		//MoFang.myWait();
		//new MoFang().showPosition();

		doIt('X', 1 + 1, 90);
		MoFang.myWait();
		//new MoFang().showPosition();
		doIt('X', 1 + 1, -90);
		// new MoFang().showPosition();
		MoFang.myWait();

		//showPosition();
	}

	//魔方自动随机变化多少条
	public static void ranGet(int num) {
		char selectChar = 'E';//'X','Y','Z'
		int layer;//-1,0,1
		int jiaoDu = 0;//90,-19,180

		for (int i = 0; i < num; i++) {
			//选xyz
			int select = (int) ((Math.random() * 10) % 3);
			if (select == 0) {
				selectChar = 'X';
			}
			if (select == 1) {
				selectChar = 'Y';
			}
			if (select == 2) {
				selectChar = 'Z';
			}

			//
			layer = (int) ((Math.random() * 10) % 3);//0,1,2
			layer -= 1;//-1,0,1

			//
			int jiao = (int) ((Math.random() * 10) % 3);//0,1,2
			if (jiao == 0) {
				jiaoDu = 90;
			}
			if (jiao == 1) {
				jiaoDu = -90;
			}
			if (jiao == 2) {
				jiaoDu = 180 - 90;
			}
			System.out.println("\n*******************************\nRandom Generater:"
							+ (i + 1) + " of " + num);
			System.out.print(selectChar);
			System.out.print("," + layer + "," + jiaoDu + "\n按任意键开始动画?\n");

			myWait();

			doIt(selectChar, layer + 1, jiaoDu);
		}
	}

	//从三维图形界面输入操作参数
	public static void graphicStart() {
		JFrame myframe = new JFrame();
		myframe.setVisible(true);

	}

	public static void main(String[] args) {

		//初始化位置数组，也可以从文件中加载,其实应该把所有关键信息从文件载入，关闭时保存
		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {

					MoFang.positionArray[i][j][k] = new Position(i, j, k);
				}
		System.out.println("初始化位置数组完成.最初全在原位置\n");

		//showPosition();

		new MainFrame(new mySimpleUniverse(), 200, 200);
		//加applet到应用程序界面
		//someTest();
		//graphicStart();
		
		myWait();
		ranGet(30);
		//
		//
	}

}

class Block implements Runnable {
	//计算已画的块数
	private static int whickBlockPainted = 0;

	//计算生成的第几块
	private static int BlockCreated = 0;

	//每面的颜色,排列为:+x,-x,+y,-y,+z,-z,no
	public static Color3f[] mianColor = { new Color3f(1.0f, 0.0f, 0.0f),
			new Color3f(0.0f, 1.0f, 0.0f), new Color3f(0.0f, 0.0f, 1.0f),
			new Color3f(1.0f, 1.0f, 0.0f), new Color3f(1.0f, 0.0f, 1.0f),
			new Color3f(0.0f, 1.0f, 1.0f), new Color3f(0.2f, 0.2f, 0.2f) };
	//每面的材质,排列为:+x,-x,+y,-y,+z,-z,no
	public static String[] mianImageFile = new String[7];

	//块偏移量
	private static float kuaiZhongXinWeizhi = 0.4f;

	//创建材质时要用的,仅用于他,他是一个applet对象
	Component observer;

	//该块的编号
	private int blockIdX;
	private int blockIdY;
	private int blockIdZ;

	//该块的位置
	private int x;
	private int y;
	private int z;

	//该块的坐标轴点,最初全为(1,1,1),表示与外坐标一致
	private MyPoint xvec;
	private MyPoint yvec;
	private MyPoint zvec;

	//position=new Verctor3f(0.0f,0.0f,0.0f);

	//该块的角度,限定为从-359到359,模360可以限定为一周,如果模180,则只能表示半个圆,不行
	//int anglex ;
	//int angley ;
	//int anglez ;

	//添加物品的变换组
	public TransformGroup transGroup;
	public Transform3D trans;

	//用于位置，角度变动的变换组
	public TransformGroup transGroupx;
	public Transform3D transx;
	public TransformGroup transGroupy;
	public Transform3D transy;
	public TransformGroup transGroupz;
	public Transform3D transz;
	public TransformGroup transGroupp;
	public Transform3D transp;

	//新的方块位置
	int[] nexyz = { 0, 0, 0 };

	//动画相关
	int totateArg;
	boolean canNew = false;
	char selectedC;
	Thread myThread;
	public static boolean closeDonghua = false; //为true使用线程动画，为false直接调用

	private static int selectDonghuaId = 0;//取ID变量，取值从0~8,每次从0开始取，正好9个，取完后变为10（每取一个自动加1），这时才让所有线程一起动
	private int myDonghuaId;
	private static int donghuaDelay = 50; //绝对延迟,
	public static int whileDelay = 20;//加快同步系统性能延迟,

	//公共控制变量
	public static int yunXingThread = 0;//运行的线程数,启动线程时加1，退出时减一。可以控制主线程等待，直到为0时才开始做所有的xyzChange，否则等待

	//动画时的多线程要用它,因为主线程仍在跑,x,y,z会变化,只能用它保存
	int yuanx;
	int yuany;
	int yuanz;

	//当前位置x点的偏移
	MyPoint chaxvec;
	//当前位置y点的偏移
	MyPoint chayvec;
	//当前位置z点的偏移
	MyPoint chazvec;

	//构造函数,给初值
	public Block(int i, int j, int k, int px, int py, int pz,
			TransformGroup parentTransGroup, Transform3D t3d,
			BranchGroup objRoot, Component obServer1) {

		blockIdX = i;
		blockIdY = j;
		blockIdZ = k;

		x = px;
		y = py;
		z = pz;

		//计算轴向量 ：块坐标加坐标偏移
		xvec = new MyPoint((blockIdX - 1) + 1, (blockIdY - 1), (blockIdZ - 1));
		yvec = new MyPoint((blockIdX - 1), (blockIdY - 1) + 1, (blockIdZ - 1));
		zvec = new MyPoint((blockIdX - 1), (blockIdY - 1), (blockIdZ - 1) + 1);
		//System.out.println("轴点："+(zoux)+(zouy)+(zouz));
		//System.out.println("轴向量："+(zoux-(x-1))+(zouy-(y-1))+(zouz-(z-1)));

		observer = obServer1;

		//anglex=0 ;
		//angley=0 ;
		//anglez=0 ;

		trans = new Transform3D();
		transGroup = new TransformGroup(trans);
		transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		transp = new Transform3D();
		transGroupp = new TransformGroup(transp);
		transGroupp.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transGroupp.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//transGroupp.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		transx = new Transform3D();
		transGroupx = new TransformGroup(transx);
		transGroupx.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transGroupx.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//transGroupx.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		transy = new Transform3D();
		transGroupy = new TransformGroup(transy);
		transGroupy.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transGroupy.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//transGroupy.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		transz = new Transform3D();
		transGroupz = new TransformGroup(transz);
		transGroupz.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transGroupz.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//transGroupz.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		//用已知信息组装变换组,,
		//注意这里的顺序，先平移，再旋转，或先x,先y的顺序是不一样的，这里
		//必须是先在原位置自转好方向，再平移，这样，平移不会改变它的自转方向，
		//如果先平移，再转，转动会带动平移，刚才的平移无任何意义。
		//还要注意是先绕x转，再绕y转，再绕z转
		//换了顺序，是不一样的，例如x,y,z和z,y,x的结果不一样
		//但是，可以证明x,y和y,x的顺序无关性,当都只左转或右转90度时，很容易证明
		//如果有180度，可以分成两次90度操作
		parentTransGroup.addChild(transGroupp);
		transGroupp.addChild(transGroupz);
		transGroupz.addChild(transGroupy);
		transGroupy.addChild(transGroupx);
		transGroupx.addChild(transGroup);
		// 测试,直接加到根,没有鼠标功能
		//objRoot.addChild(transGroup);
		//System.out.println("\n\n\n处理第"+BlockCreated+"块");
		//System.out.println("块"+(blockIdX-1)+(blockIdY-1)+(blockIdZ-1)+"的坐标系添加完成");
		BlockCreated++;
		//if (BlockCreated==14)//||BlockCreated==3//||BlockCreated==2||BlockCreated==20||BlockCreated==16
		add3DCube(x - 1, y - 1, z - 1, transGroup);
		//建小坐标轴
		SomeShape3D.zuoBiaoZhuSmallXShape3D(transGroup);
		SomeShape3D.zuoBiaoZhuSmallYShape3D(transGroup);
		SomeShape3D.zuoBiaoZhuSmallZShape3D(transGroup);
		//测试主变换组的平移
		//Shape3D shape1=SomeShape3D.FlowerShape3D();
		//parentTransGroup.addChild(shape1);
		//t3d.setTranslation(new Vector3f(0.0f,1.0f,0.0f));
		//parentTransGroup.setTransform(t3d);
		//测试子变换组的平移(不行,不起作用)
		//Shape3D shape2=SomeShape3D.SanShape3D();
		//transGroup.addChild(shape2);
		//trans.setTranslation(new Vector3f(0.0f,-1f,0.0f));
		//transGroup.setTransform(t3d);
		//创建鼠标行为
		//MouseRotate behavior1=new MouseRotate();
		//behavior1.setTransformGroup(transGroup);
		//behavior1.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),100));
		//objRoot.addChild(behavior1);//鼠标行为  加到 分枝组
		//设置初始值,显示输出
		float fx;
		float fy;
		float fz;
		fx = (float) kuaiZhongXinWeizhi * (x - 1);
		fy = (float) kuaiZhongXinWeizhi * (y - 1);
		fz = (float) kuaiZhongXinWeizhi * (z - 1);
		//变换量
		transx.rotX(Math.toRadians(0));//anglex
		transy.rotY(Math.toRadians(0));
		transz.rotZ(Math.toRadians(0));
		transp.setTranslation(new Vector3f(fx, fy, fz));
		//生效
		transGroupp.setTransform(transp);
		transGroupx.setTransform(transx);
		transGroupy.setTransform(transy);
		transGroupz.setTransform(transz);
	}

	int[] jisuanNextXYZ(char doType, int totateArg, int oldx, int oldy, int oldz)
	//需要变量x,y,z,doType,totateArg,输出返回到数组
	{
		//System.out.println("计算下一个点。。。");
		//用于计算
		int newz = 0;
		int newy = 0;
		int newx = 0;
		//返回时的标准格式
		int[] nextXYZ = { 0, 0, 0 };

		//计算时接收调用外面函数的返回函数值
		int[] myShunShiNext = { 0, 0 };
		if (totateArg == 0) {
			//当totateArg为0时，没有可用的if匹配,返回原位置
			newx = oldx;
			newy = oldy;
			newz = oldz;
			//返回结果
			nextXYZ[0] = newx;
			nextXYZ[1] = newy;
			nextXYZ[2] = newz;
			//System.out.println("\n变为位置："+newx+newy+newz);
			return nextXYZ;
		}
		//
		//System.out.println("\n原位置："+oldx+oldy+oldz);
		//System.out.println("绕："+doType+"转"+totateArg);

		switch (doType) {
		case 'Z':
			newz = oldz;
			if (totateArg == 90) {
				myShunShiNext = quXiaYiGe(oldx, oldy, 1);
			} else if (totateArg == 180) {
				myShunShiNext = quXiaYiGe(oldx, oldy, 2);
			} else if (totateArg == -90) {
				myShunShiNext = quXiaYiGe(oldx, oldy, 3);
			}
			newx = myShunShiNext[0];
			newy = myShunShiNext[1];
			break;

		case 'Y':
			newy = oldy;
			if (totateArg == 90) {
				myShunShiNext = quXiaYiGe(oldz, oldx, 1);
			} else if (totateArg == 180) {
				myShunShiNext = quXiaYiGe(oldz, oldx, 2);
			} else if (totateArg == -90) {
				myShunShiNext = quXiaYiGe(oldz, oldx, 3);
			}
			newz = myShunShiNext[0];
			newx = myShunShiNext[1];
			break;
		case 'X':
			newx = oldx;
			if (totateArg == 90) {
				myShunShiNext = quXiaYiGe(oldy, oldz, 1);
			} else if (totateArg == 180) {
				myShunShiNext = quXiaYiGe(oldy, oldz, 2);
			} else if (totateArg == -90) {
				myShunShiNext = quXiaYiGe(oldy, oldz, 3);
			}
			newy = myShunShiNext[0];
			newz = myShunShiNext[1];
			break;
		}
		//返回结果
		nextXYZ[0] = newx;
		nextXYZ[1] = newy;
		nextXYZ[2] = newz;
		//System.out.println("\n变为位置："+newx+newy+newz);
		return nextXYZ;
	}

	boolean fangXiangCorrect(int argx, int argy, int argz) {//用三个差向量判断自转
	//System.out.println("方向判断。。。");
		//(blockIdX-1),(blockIdY-1),(blockIdZ-1)为初始点位置，分别加1得到三个初始向量
		//xvec,yvec,zvec为保存的变化
		//减去当前的方块位置，得到每个方向点当前向量chaxvec,chayvec,chazvec
		//X点
		int[] p1X = jisuanNextXYZ('X', argx, 1, 0, 0);//原始x点便移
		int[] p2X = jisuanNextXYZ('Y', argy, p1X[0], p1X[1], p1X[2]);
		int[] p3X = jisuanNextXYZ('Z', argz, p2X[0], p2X[1], p2X[2]);//新的x点便移
		//Y点
		int[] p1Y = jisuanNextXYZ('X', argx, 0, 1, 0);
		int[] p2Y = jisuanNextXYZ('Y', argy, p1Y[0], p1Y[1], p1Y[2]);
		int[] p3Y = jisuanNextXYZ('Z', argz, p2Y[0], p2Y[1], p2Y[2]);
		//Z点
		int[] p1Z = jisuanNextXYZ('X', argx, 0, 0, 1);
		int[] p2Z = jisuanNextXYZ('Y', argy, p1Z[0], p1Z[1], p1Z[2]);
		int[] p3Z = jisuanNextXYZ('Z', argz, p2Z[0], p2Z[1], p2Z[2]);
		//System.out.println("坐标系:"+chaX+"   "+chaY+"   "+chaZ);
		// 新的x点便移=当前位置x点的偏移
		if (((p3X[0] == chaxvec.x) && (p3X[1] == chaxvec.y) && (p3X[2] == chaxvec.z))
				&& ((p3Y[0] == chayvec.x) && (p3Y[1] == chayvec.y) && (p3Y[2] == chayvec.z))
				&& ((p3Z[0] == chazvec.x) && (p3Z[1] == chazvec.y) && (p3Z[2] == chazvec.z))) {
			System.out.println("坐标轴到位了");
			return true;
		} else {
			//System.out.println("坐标轴没到位");
			return false;
		}
	}

	boolean weiZhiCorrect(int aidx, int aidy, int aidz, int argx, int argy,
			int argz) {
		int[] p1 = jisuanNextXYZ('X', argx, blockIdX - 1, blockIdY - 1,
				blockIdZ - 1);
		int[] p2 = jisuanNextXYZ('Y', argy, p1[0], p1[1], p1[2]);
		int[] p3 = jisuanNextXYZ('Z', argz, p2[0], p2[1], p2[2]);
		if ((p3[0] == aidx) && (p3[1] == aidy) && (p3[2] == aidz)) {
			System.out.println("位置对了");
			return true;
		} else {
			System.out.println("位置不对");
			return false;
		}
	}

	//仅计算逆时针旋转时的坐标变化,当转逆时针-90度时,转化为3个逆时针90度
	int[] quXiaYiGe(int num1, int num2, int n) {
		int[][] xiangXianZhi = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } }; //一，二，三，四,0,1,2,3
		int[][] zouShangZhi = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } }; //
		int[] result = { 0, 0 };
		//System.out.println("旋转次数为:"+n);
		//System.out.println(num1+" , "+num2+"旋转到:");
		int temp = 0;
		for (int i = 0; i < n; i++) {
			//循环一次转一次
			if (num1 == 0 && num2 == 0)//
			{
				num1 = 0;
				num2 = 0;
				//System.out.println("0");
			} else if (num1 > 0 && num2 > 0)//一象限转到二象限
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				//System.out.println("1");
			} else if (num1 < 0 && num2 > 0)//二象限转到三象限
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				////System.out.println("2");
			} else if (num1 < 0 && num2 < 0)//三象限转到四象限
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				////System.out.println("3");
			} else if (num1 > 0 && num2 < 0)//四象限转到一象限
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				//System.out.println("4");
			} else if (num1 > 0 && num2 == 0)//X上到Y上
			{
				temp = num1;
				num1 = 0;
				num2 = temp;
				//System.out.println("5");
			} else if (num1 == 0 && num2 > 0)//Y上到X上
			{
				temp = num1;
				num1 = -num2;
				num2 = 0;
				//System.out.println("6");
			} else if (num1 < 0 && num2 == 0)//
			{
				temp = num1;
				num1 = 0;
				num2 = temp;
				//System.out.println("7");
			} else if (num1 == 0 && num2 < 0)//
			{
				temp = num1;
				num1 = -num2;
				num2 = 0;
				//System.out.println("8");
			} else {
				System.out.println("no");
			}
		}
		//System.out.println(num1+" , "+num2);
		result[0] = num1;
		result[1] = num2;
		return result;
	}

	public void run() {
		//System.out.println("我的动画开始了");
		//动画协调,取一个id，并放出令牌
		myDonghuaId = selectDonghuaId++;
		selectDonghuaId %= 9;
		yunXingThread++;
		switch (selectedC) {
		case 'Z': {
			startDonghuaZ();
			break;
		}
		case 'Y': {
			startDonghuaY();
			break;
		}
		case 'X': {
			startDonghuaX();
			break;
		}
		}
		//最终位置
		while (!canNew)//当canNew=true时，输出到3D界面，否则只计算
		{//System.out.println(" 我在等待计算出新值...");
		}
		//变换生效
		transGroupp.setTransform(transp);
		transGroupx.setTransform(transx);
		transGroupy.setTransform(transy);
		transGroupz.setTransform(transz);
		canNew = false;
		//System.out.println("我的动画完成了");
		yunXingThread--;
	}

	//用来从一个原坐标和结果坐标来 搜索 3个x,y,z顺序的旋转操作，返回到needRotate[3]中
	//使用了int[] jisuanNextXYZ(char doType,int totateArg,int oldx,int oldy,int oldz)
	int[] shouSuoXYZRotate(int oldx, int oldy, int oldz, int aidx, int aidy,
			int aidz) {//搜索自转，不看位置，只看三个向量对不对
		System.out.println("块原始位置:" + oldx + "," + oldy + "," + oldz);
		System.out.println("块目标位置:" + aidx + "," + aidy + "," + aidz);
		//存放结果
		int needRotatex = 0;
		int needRotatey = 0;
		int needRotatez = 0;
		//转换返回
		int[] needRotate = { 0, 0, 0 };
		int num = 0;
		wancheng: for (int j = 0; j <= 3; j++) {
			int toArg = 0;//j=3时取0
			if (j == 1) {
				toArg = 90;
			}
			if (j == 2) {
				toArg = -90;
			}
			if (j == 3) {
				toArg = 180;
			}
			for (int jj = 0; jj <= 3; jj++) {
				int ttoArg = 0;
				if (jj == 1) {
					ttoArg = 90;
				}
				if (jj == 2) {
					ttoArg = -90;
				}
				if (jj == 3) {
					ttoArg = 180;
				}
				for (int jjj = 0; jjj <= 3; jjj++) {
					int tttoArg = 0;
					if (jjj == 1) {
						tttoArg = 90;
					}
					if (jjj == 2) {
						tttoArg = -90;
					}
					if (jjj == 3) {
						tttoArg = 180;
					}
					//System.out.println("验证xyz旋转.."+toArg+"  "+ttoArg+"  "+tttoArg);
					// 这个打开 有的会找不到
					//boolean myBoolean1=weiZhiCorrect(nexyz[0],nexyz[1],nexyz[2],toArg,ttoArg,tttoArg);
					boolean myBoolean = fangXiangCorrect(toArg, ttoArg, tttoArg);
					//MoFang.myWait();
					if (myBoolean) {
						needRotatex = toArg;
						needRotatey = ttoArg;
						needRotatez = tttoArg;
						needRotate[0] = needRotatex;
						needRotate[1] = needRotatey;
						needRotate[2] = needRotatez;
						num++;
						System.out.println("*****找到了x,y,z旋转即可*****" + toArg
								+ " , " + ttoArg + " , " + tttoArg + "");
						//return needRotate;
					}
				}
			}
		}
		if (num == 0) {
			System.out.println("没有适合的xyz旋转,不旋转..");
			MoFang.myWait();
		} else if (num >= 2) {
			System.out
					.println("###########################找到了num=" + num + "个");
		}
		return needRotate;
	}

	void startDonghuaX() {
		//动画模块
		int chuJiao;
		int oneTime;
		int bianJiao;
		int nowJiao;
		//测试
		//totateArg=90;
		chuJiao = getChujiao(yuany, yuanz);
		if (totateArg == -90)
			oneTime = -15;
		else
			oneTime = 15;
		//System.out.println("chuJiao="+chuJiao);
		//System.out.println("totateArg="+totateArg);
		//nowJiao=chuJiao+bianJiao ;
		//这是目前角度公式，可见，据坐标可以判断它的角度嘛
		for (bianJiao = 0; bianJiao != (totateArg + oneTime); bianJiao += oneTime) {
			if (!closeDonghua) {
				while (myDonghuaId != selectDonghuaId)//动画同步相关轮到自己时才执行，把令牌交给下一个
				{
					;//System.out.println("我是"+myDonghuaId+"号,而令牌现在是"+selectDonghuaId+"号,我要等...");
					try {
						//System.in.read();//暂停
						myThread.sleep(whileDelay);
					} catch (Exception e) {
					}
				}
			}
			//计算x,y和角度输出
			float fx;
			float fy;
			float fz;
			//半径
			float r;
			if (yuany == 0 && yuanz == 0)
				r = 0;//(x,y)不变，故不用计算，直接给(0,0),初角返回任何值都可;
			else {
				if (yuany == 0 || yuanz == 0)
					r = 1.0f;
				else
					r = 1.414f;
			}
			nowJiao = chuJiao + bianJiao;
			//使用初角直为了计算当前（x,y）位置,当前jiaodu1与他无关，只与老角有关
			//(-1,-1)距离原点为根2,约1.732
			//0.3指定了1点在0.3,-0.3处
			fy = (float) kuaiZhongXinWeizhi * r
					* (float) Math.cos(Math.PI * nowJiao / 180);
			fz = (float) kuaiZhongXinWeizhi * r
					* (float) Math.sin(Math.PI * nowJiao / 180);
			fx = (float) kuaiZhongXinWeizhi * (yuanx);
			//z坐标不变,这里的z早减过了1
			//计算出了全部数据,OK,开始刷新
			//用变换量
			//transz.rotZ(Math.toRadians(anglez));
			//transy.rotY(Math.toRadians(angley));
			//transx.rotX(Math.toRadians(bianJiao+oldJiaoDu));
			transp.setTranslation(new Vector3f(fx, fy, fz));
			//生效
			//transGroupz.setTransform(transz);
			//transGroupy.setTransform(transy);
			//transGroupx.setTransform(transx);
			transGroupp.setTransform(transp);
			//停不了,只能使用多线程或定时
			//System.out.println("bianJiao="+bianJiao);
			try {
				//System.in.read();//暂停
				//
				myThread.sleep(donghuaDelay);
			} catch (Exception e) {
			}
			//在这里才放出令牌
			selectDonghuaId++;
			selectDonghuaId %= 9;
		}
	}

	void startDonghuaY() {
		//动画模块
		int chuJiao;
		int oneTime;
		int bianJiao;
		int nowJiao;
		//测试
		//totateArg=90;
		chuJiao = getChujiao(yuanz, yuanx);
		if (totateArg == -90)
			oneTime = -15;
		else
			oneTime = 15;
		//System.out.println("chuJiao="+chuJiao);
		//System.out.println("totateArg="+totateArg);
		//nowJiao=chuJiao+bianJiao ;
		//这是目前角度公式，可见，据坐标可以判断它的角度嘛
		for (bianJiao = 0; bianJiao != (totateArg + oneTime); bianJiao += oneTime) {
			if (!closeDonghua) {
				while (myDonghuaId != selectDonghuaId)//动画同步相关轮到自己时才执行，把令牌交给下一个
				{
					;//System.out.println("我是"+myDonghuaId+"号,而令牌现在是"+selectDonghuaId+"号,我要等...");
					try {
						//System.in.read();//暂停
						myThread.sleep(whileDelay);
					} catch (Exception e) {
					}
				}
			}
			//计算x,y和角度输出
			float fx;
			float fy;
			float fz;
			//半径
			float r;
			if (yuanz == 0 && yuanx == 0)
				r = 0;//(x,y)不变，故不用计算，直接给(0,0),初角返回任何值都可;
			else {
				if (yuanz == 0 || yuanx == 0)
					r = 1.0f;
				else
					r = 1.414f;
			}
			nowJiao = chuJiao + bianJiao;
			//使用初角直为了计算当前（x,y）位置,当前jiaodu1与他无关，只与老角有关
			//(-1,-1)距离原点为根2,约1.732
			//0.3指定了1点在0.3,-0.3处
			fz = (float) kuaiZhongXinWeizhi * r
					* (float) Math.cos(Math.PI * nowJiao / 180);
			fx = (float) kuaiZhongXinWeizhi * r
					* (float) Math.sin(Math.PI * nowJiao / 180);
			fy = (float) kuaiZhongXinWeizhi * (yuany);
			//z坐标不变,这里的z早减过了1
			//计算出了全部数据,OK,开始刷新
			//用变换量
			//transz.rotZ(Math.toRadians(anglez));
			//transy.rotY(Math.toRadians(bianJiao+oldJiaoDu));
			//transx.rotX(Math.toRadians(anglex));
			transp.setTranslation(new Vector3f(fx, fy, fz));
			//生效
			//transGroupz.setTransform(transz);
			//transGroupy.setTransform(transy);
			//transGroupx.setTransform(transx);
			transGroupp.setTransform(transp);
			//停不了,只能使用多线程或定时
			//System.out.println("bianJiao="+bianJiao);
			try {
				//System.in.read();//暂停
				//
				myThread.sleep(donghuaDelay);
			} catch (Exception e) {
			}
			//在这里才放出令牌
			selectDonghuaId++;
			selectDonghuaId %= 9;
		}
	}

	void startDonghuaZ() {
		//动画模块
		int chuJiao;
		int oneTime;
		int bianJiao;
		int nowJiao;
		//测试
		//totateArg=90;
		chuJiao = getChujiao(yuanx, yuany);
		if (totateArg == -90)
			oneTime = -15;
		else
			oneTime = 15;
		//System.out.println("chuJiao="+chuJiao);
		//System.out.println("totateArg="+totateArg);
		//nowJiao=chuJiao+bianJiao ;
		//这是目前角度公式，可见，据坐标可以判断它的角度嘛
		for (bianJiao = 0; bianJiao != (totateArg + oneTime); bianJiao += oneTime) {
			if (!closeDonghua) {
				while (myDonghuaId != selectDonghuaId)//动画同步相关轮到自己时才执行，把令牌交给下一个
				{
					;//System.out.println("我是"+myDonghuaId+"号,而令牌现在是"+selectDonghuaId+"号,我要等...");
					try {
						//System.in.read();//暂停
						myThread.sleep(whileDelay);
					} catch (Exception e) {
					}
				}
			}
			//计算x,y和角度输出
			float fx;
			float fy;
			float fz;
			//半径
			float r;
			if (yuanx == 0 && yuany == 0)
				r = 0;//(x,y)不变，故不用计算，直接给(0,0),初角返回任何值都可;
			else {
				if (yuanx == 0 || yuany == 0)
					r = 1.0f;
				else
					r = 1.414f;
			}
			nowJiao = chuJiao + bianJiao;
			//使用初角直为了计算当前（x,y）位置,当前jiaodu1与他无关，只与老角有关
			//(-1,-1)距离原点为根2,约1.732
			//0.3指定了1点在0.3,-0.3处
			fx = (float) kuaiZhongXinWeizhi * r
					* (float) Math.cos(Math.PI * nowJiao / 180);
			fy = (float) kuaiZhongXinWeizhi * r
					* (float) Math.sin(Math.PI * nowJiao / 180);
			fz = (float) kuaiZhongXinWeizhi * (yuanz);
			//z坐标不变,这里的z早减过了1
			//计算出了全部数据,OK,开始刷新
			//用变换量
			//transz.rotZ(Math.toRadians());
			//transy.rotY(Math.toRadians());
			//transx.rotX(Math.toRadians());
			transp.setTranslation(new Vector3f(fx, fy, fz));
			//生效
			//transGroupz.setTransform(transz);
			//transGroupy.setTransform(transy);
			//transGroupx.setTransform(transx);
			transGroupp.setTransform(transp);
			//停不了,只能使用多线程或定时

			//System.out.println("bianJiao="+bianJiao);
			try {
				//System.in.read();//暂停
				//
				myThread.sleep(donghuaDelay);
			} catch (Exception e) {
			}
			//在这里才放出令牌
			selectDonghuaId++;
			selectDonghuaId %= 9;

		}
	}

	//center点仅用来计算颜色
	void add3DCube(int centerx, int centery, int centerz,
			TransformGroup myTransGroup) {
		//System.out.println("正在画该块.....");
		//颜色数据结构
		int[] compare = new int[6];
		compare[0] = centerx;
		//x
		compare[1] = centerx;
		//x
		compare[2] = centery;
		//y
		compare[3] = centery;
		//y
		compare[4] = centerz;
		//z
		compare[5] = centerz;
		//z
		int[] compareWith = new int[6];
		compareWith[0] = 1;
		compareWith[1] = -1;
		compareWith[2] = 1;
		compareWith[3] = -1;
		compareWith[4] = 1;
		compareWith[5] = -1;
		Color3f presentMianColor;
		//面图
		String presentImageFile;
		mianImageFile[0] = "IMG\\coverRight.jpg";
		mianImageFile[1] = "IMG\\coverLeft.jpg";
		mianImageFile[2] = "IMG\\coverUp.jpg";
		mianImageFile[3] = "IMG\\coverDown.jpg";
		mianImageFile[4] = "IMG\\coverFront.jpg";
		mianImageFile[5] = "IMG\\coverBehind.jpg";
		mianImageFile[6] = "IMG\\coverCenter.jpg";
		//点数据结构
		Vector3f mianxin = new Vector3f();
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
		//通过for,集合到三个数组
		Point3f[][] vert = new Point3f[6][4];
		Color3f[] color = new Color3f[6];
		String[] imageFile = new String[6];
		for (int i = 0; i <= 5; i++) {
			//计算该面 颜色和贴图
			if (compare[i] == compareWith[i]) {
				presentMianColor = mianColor[i];
				presentImageFile = mianImageFile[i];
			} else {
				presentMianColor = mianColor[6];
				presentImageFile = mianImageFile[6];
				//如果颜色为白色，不画该面
				//continue则跳过不画
				//continue ;
			}
			try {
				//System.in.read();//暂停
			} catch (Exception e) {
			}
			//计算该面 面心
			mianxin.x = mianxinpianyi[i].x;
			mianxin.y = mianxinpianyi[i].y;
			mianxin.z = mianxinpianyi[i].z;
			//计算该面 四个点
			Vector3f[] dingdian = new Vector3f[4];
			for (int j = 0; j <= 3; j++) {
				dingdian[j] = new Vector3f();
				if (i == 0 || i == 1) {
					dingdian[j].x = mianxin.x + dingdianPianyiX[j].x;
					dingdian[j].y = mianxin.y + dingdianPianyiX[j].y;
					dingdian[j].z = mianxin.z + dingdianPianyiX[j].z;
				} else if (i == 2 || i == 3) {
					dingdian[j].x = mianxin.x + dingdianPianyiY[j].x;
					dingdian[j].y = mianxin.y + dingdianPianyiY[j].y;
					dingdian[j].z = mianxin.z + dingdianPianyiY[j].z;
				} else if (i == 4 || i == 5) {
					dingdian[j].x = mianxin.x + dingdianPianyiZ[j].x;
					dingdian[j].y = mianxin.y + dingdianPianyiZ[j].y;
					dingdian[j].z = mianxin.z + dingdianPianyiZ[j].z;
				}
			}
			//用顶点和颜色画 该面
			//建面方法一,把vector3D对象传进去，在里面转化为float数组
			//Shape3D shape=SomeShape3D.mian1of6CubeShape3D(observer,dingdian,presentImageFile,presentMianColor);
			//建面方法二，把vector3D在这里转化为point3f数组，再传进去，转换更简单
			Point3f[] vert1 = new Point3f[4];
			Point3f[] vert2 = new Point3f[4];
			//4个点的信息
			for (int k = 0; k <= 3; k++) {
				vert1[k] = new Point3f(SomeShape3D.fangKuaiBanJing
						* dingdian[k].x, SomeShape3D.fangKuaiBanJing
						* dingdian[k].y, SomeShape3D.fangKuaiBanJing
						* dingdian[k].z);
				vert2[3 - k] = new Point3f(SomeShape3D.fangKuaiBanJing
						* dingdian[k].x, SomeShape3D.fangKuaiBanJing
						* dingdian[k].y, SomeShape3D.fangKuaiBanJing
						* dingdian[k].z);
			}
			Shape3D shape1 = SomeShape3D.shapeMaker(observer, presentImageFile,
					vert1);
			Shape3D shape2 = SomeShape3D.shapeMaker(observer, presentImageFile,
					vert2);
			//两方法结果一样
			//消失现象 可以避免，原因是各面衔接点不重合，dingdian[k].x乘上SomeShape3D.fangKuaiBanJing后有数据问题
			//挂到自己的坐标系
			myTransGroup.addChild(shape1);
			myTransGroup.addChild(shape2);
			//测试3

			// for(int j=0;j<=3;j++)
			//  {
			// vert[i][j]=new Point3f(SomeShape3D.fangKuaiBanJing*dingdian[j].x ,SomeShape3D.fangKuaiBanJing*dingdian[j].y,SomeShape3D.fangKuaiBanJing*dingdian[j].z);
			// }
			//color[i]=presentMianColor;
			// imageFile[i]=presentImageFile;
		}
		//测试3
		// SomeShape3D.box3D(observer,myTransGroup,vert,color,imageFile);
		//System.out.println("第"+whickBlockPainted+"块完毕。");
		System.out.print('.');
		whickBlockPainted++;
	}

	//该块改变位置
	//Floor:0,1，2
	//totateArg:90 180 -90
	//为了方便计算，先平移(坐标全减1),变换后,再平移还原(坐标全加1)
	//该块改变位置
	//Floor:0,1，2
	//totateArg:90 180 -90
	//为了方便计算，先平移(坐标全减1),变换后,再平移还原(坐标全加1)
	void xyzChange(char doType, int mYtotateArg) {
		//
		totateArg = mYtotateArg;
		//在处理过程中，原坐标先平移
		x -= 1;
		y -= 1;
		z -= 1;
		//动画预处理
		yuanx = x;
		yuany = y;
		yuanz = z;

		if (closeDonghua) {//startDonghuaX();
		} else
		//换为用线程执行
		{
			selectedC = doType;
			myThread = new Thread(this, "Rotate");
			myThread.start();
		}
		System.out.println("新轴点x计算并保存。。。");
		//新轴点计算并保存
		//System.out.println("。。。"+xvec.x+xvec.y+xvec.z);
		int[] newvecx = jisuanNextXYZ(doType, totateArg, xvec.x, xvec.y, xvec.z);
		//System.out.println("。。。");
		xvec.x = newvecx[0];
		xvec.y = newvecx[1];
		xvec.z = newvecx[2];
		System.out.println("新轴点y计算并保存。。。");
		int[] newvecy = jisuanNextXYZ(doType, totateArg, yvec.x, yvec.y, yvec.z);
		yvec.x = newvecy[0];
		yvec.y = newvecy[1];
		yvec.z = newvecy[2];
		int[] newvecz = jisuanNextXYZ(doType, totateArg, zvec.x, zvec.y, zvec.z);
		zvec.x = newvecz[0];
		zvec.y = newvecz[1];
		zvec.z = newvecz[2];
		System.out.println("新坐标计算并保存。。。");
		//新坐标计算
		nexyz = jisuanNextXYZ(doType, totateArg, x, y, z);
		//输出处理结果
		System.out.println("\n块" + (blockIdX - 1) + (blockIdY - 1)
				+ (blockIdZ - 1) + "从" + x + y + z + "  变到  " + nexyz[0]
				+ nexyz[1] + nexyz[2]);
		//新坐标保存
		x = nexyz[0] + 1;
		y = nexyz[1] + 1;
		z = nexyz[2] + 1;
		System.out.println("搜寻旋转角度量。。。");
		//搜寻旋转角度量
		//当前位置x点的偏移
		chaxvec = new MyPoint(xvec.x - nexyz[0], xvec.y - nexyz[1], xvec.z
				- nexyz[2]);
		//当前位置y点的偏移
		chayvec = new MyPoint(yvec.x - nexyz[0], yvec.y - nexyz[1], yvec.z
				- nexyz[2]);
		//当前位置z点的偏移
		chazvec = new MyPoint(zvec.x - nexyz[0], zvec.y - nexyz[1], zvec.z
				- nexyz[2]);
		System.out.println("---------x点的偏移" + (xvec.x - nexyz[0]) + "  "
				+ (xvec.y - nexyz[1]) + "  " + (xvec.z - nexyz[2]));
		System.out.println("---------y点的偏移" + (yvec.x - nexyz[0]) + "  "
				+ (yvec.y - nexyz[1]) + "  " + (yvec.z - nexyz[2]));
		System.out.println("---------z点的偏移" + (zvec.x - nexyz[0]) + "  "
				+ (zvec.y - nexyz[1]) + "  " + (zvec.z - nexyz[2]));
		//MoFang.myWait();
		int[] xZJD = shouSuoXYZRotate(yuanx, yuany, yuanz, nexyz[0], nexyz[1],
				nexyz[2]);
		//重新在3D界面中输出
		//1,计算坐标
		float fx;
		float fy;
		float fz;
		fx = (float) kuaiZhongXinWeizhi * (x - 1);
		fy = (float) kuaiZhongXinWeizhi * (y - 1);
		fz = (float) kuaiZhongXinWeizhi * (z - 1);
		//变换量
		transp.setTranslation(new Vector3f(fx, fy, fz));
		//MoFang.myWait();
		transx.rotX(Math.toRadians(xZJD[0]));//anglex
		transy.rotY(Math.toRadians(xZJD[1]));
		transz.rotZ(Math.toRadians(xZJD[2]));
		//生效
		if (closeDonghua) { //变换生效
			transGroupx.setTransform(transx);
			transGroupy.setTransform(transy);
			transGroupz.setTransform(transz);
			transGroupp.setTransform(transp);
		}
		//不刷新，在动画后刷新，可以输出了，但在动画后
		canNew = true;
		//同时更改positionArray
		MoFang.positionArray[(nexyz[0] + 1)][(nexyz[1] + 1)][(nexyz[2] + 1)]
				.setBlockId(blockIdX, blockIdY, blockIdZ);
	}

	int getChujiao(int xyzQian, int xyzHou) {
		int value = 0;
		if ((xyzQian == -1) && (xyzHou == -1)) {
			value = 3 * (-1) * 45;
		} else if ((xyzQian == -1) && (xyzHou == 1)) {
			value = 5 * (-1) * 45;
		} else if ((xyzQian == 1) && (xyzHou == -1)) {
			value = 1 * (-1) * 45;
		} else if ((xyzQian == 1) && (xyzHou == 1)) {
			value = 7 * (-1) * 45;
		} else if ((xyzQian == 0) && (xyzHou == -1)) {
			value = 2 * (-1) * 45;
		} else if ((xyzQian == 0) && (xyzHou == 1)) {
			value = 6 * (-1) * 45;
		} else if ((xyzQian == -1) && (xyzHou == 0)) {
			value = 4 * (-1) * 45;
		} else if ((xyzQian == 1) && (xyzHou == 0)) {
			value = 0 * (-1) * 45;
		} else if ((xyzQian == 0) && (xyzHou == 0))
			System.out.println("中间块,返回任意初角");
		else
			System.out.println("参数错误x=" + xyzQian + "  y=" + xyzHou);
		return value;
	}
}

class Position {
	int x;
	int y;
	int z;
	int newx;
	int newy;
	int newz;
	boolean haveNew = false;

	public Position(int i, int j, int k) {
		x = i;
		y = j;
		z = k;
	}

	void setBlockId(int blockIdX, int blockIdY, int blockIdZ) {
		newx = blockIdX;
		newy = blockIdY;
		newz = blockIdZ;
		haveNew = true;
		//只要设为true,马上会执行newToOld,使他回到false
	}

	void newToOld() {
		x = newx;
		y = newy;
		z = newz;

		haveNew = false;
		//保证了每一次操作后全为false
	}

	//得到某块u位置
	public static int[] getPxyzFromPositionAy(int x, int y, int z,
			Position[][][] positionArray) {
		int[] p = { 0, 0, 0 };

		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {
					if ((positionArray[i][j][k].x == x)
							&& (positionArray[i][j][k].y == y)
							&& (positionArray[i][j][k].z == z)) {
						p[0] = i;
						p[1] = j;
						p[2] = k;

						return p;
					}
				}
		System.out.println("该方块没有对应的位置");
		return p;
		//这里不该返回任何东西，该抛出异常
	}
}

//Point3f,Vector3f不好用，我自定义的三维变量
class MyPoint {
	int x;
	int y;
	int z;

	//构造函数一
	MyPoint(int getx, int gety, int getz) {
		x = getx;
		y = gety;
		z = getz;
	}

	//构造函数二，转化float为int
	MyPoint(float getx, float gety, float getz) {
		x = (int) getx;
		y = (int) gety;
		z = (int) getz;
	}
}
