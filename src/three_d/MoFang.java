package three_d;

//��ΪMoFang.java

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

		//�����任��,���õ�t3D
		Transform3D t3d = new Transform3D();
		TransformGroup trans = new TransformGroup(t3d);
		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//������֦��
		BranchGroup objRoot = new BranchGroup();

		//����
		//SomeShape3D.book3D( this, trans);
		SomeShape3D.addText3DDonghua(trans, "ħ��",
				new Point3f(-7.0f, 6.0f, 6.0f), 0.1f, new Color3f(1.0f, 0.0f,
						0.0f), 1);

		//��ʼ�����ݽṹ
		System.out.println("\n\n���뷽�飬����任���м���ÿ�����������ϵ�ͷ���...");
		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {
					int[] p;
					p = Position.getPxyzFromPositionAy(i, j, k,
							MoFang.positionArray);
					MoFang.blockArray[i][j][k] = new Block(i, j, k, p[0], p[1],
							p[2], trans, t3d, objRoot, this);
				}
		System.out.println("����ÿ�����������ϵ�ͷ���,���.\n");

		//������������,�Զ��ӵ�������ϵ
		SomeShape3D.zuoBiaoZhuBigXShape3D(trans);
		SomeShape3D.zuoBiaoZhuBigYShape3D(trans);
		SomeShape3D.zuoBiaoZhuBigZShape3D(trans);

		//�����߽����
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100);

		//�������pick��Ϊ���ӵ���֧��objRoot
		PickRotateBehavior pickRotate = new PickRotateBehavior(objRoot, canvas,
				bounds);
		PickTranslateBehavior pickTranslate = new PickTranslateBehavior(
				objRoot, canvas, bounds);
		PickZoomBehavior pickZoom = new PickZoomBehavior(objRoot, canvas,
				bounds);
		//objRoot.addChild(pickRotate);
		objRoot.addChild(pickTranslate);
		//objRoot.addChild(pickZoom);

		//���������ת��Ϊ
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(trans);
		behavior.setSchedulingBounds(bounds);

		//�������ƽ����Ϊ
		//MouseTranslate myMouseRotate=new MouseTranslate();
		//myMouseRotate.setTransformGroup(trans);
		//myMouseRotate.setSchedulingBounds(bounds);

		//�������������Ϊ
		MouseZoom myMouseZoom = new MouseZoom();
		myMouseZoom.setTransformGroup(trans);
		myMouseZoom.setSchedulingBounds(bounds);

		//��������Ĭ����Ϊ
		KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(trans);
		keyNavBeh.setSchedulingBounds(bounds);
		objRoot.addChild(keyNavBeh);

		//��ɫ����
		Background bg = new Background(new Color3f(0.0f, 0.0f, 0.0f));
		bg.setApplicationBounds(bounds);
		objRoot.addChild(bg);

		//���������ʵı���
		//TextureLoader bgTexture=new TextureLoader("bg3.jpg",this);
		//Background bg=new Background(bgTexture.getImage());
		//bg.setApplicationBounds(bounds);
		//trans.addChild(shape1);//3D���� �ӵ� �任��
		//trans.addChild(shape2);//3D���� �ӵ� �任��
		objRoot.addChild(trans);
		//�任�� �ӵ� ��֦��
		objRoot.addChild(behavior);
		//�����Ϊ  �ӵ� ��֦��
		//objRoot.addChild(myMouseRotate);
		//objRoot.addChild(myMouseZoom);
		//objRoot.addChild(bg);//���� �ӵ� ��֦��
		//����
		objRoot.compile();
		//���ʹ����õĴ�3D����ķ�֦��
		return objRoot;

	}

	mySimpleUniverse() {
		//���������ƵĻ���
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);
		//�����Ի���Ϊ���еļ򵥳���ͼ����û�ж��Locale
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		//������֧�����
		BranchGroup scene = createSceneGraph(c);
		//��װ����֧�� ����ӵ� ����ͼ
		u.addBranchGraph(scene);

		//������ͼ�Ļ��� �ӵ� ��applet��
		setLayout(new BorderLayout());
		add("Center", c);
	}

	//������
	//public static void main(String aregs[])
	//{new MainFrame(new mySimpleUniverse(),200,200);//��applet��Ӧ�ó������
	//}
}

class SomeShape3D {
	public static float zuoBiaoZhouSmallDingDian = 0.09f;//С���궥��λ��
	public static float zuoBiaoZhouSmallDingXi = 0.02f;//С���궥��ɡ�İ뾶
	public static float zuoBiaoZhouSmallDingChang = 0.07f;//С���궥��ɡ�ĳ���
	public static float zuoBiaoZhouSmallWeiDian = -0.09f;//С����β�͵�λ��
	public static float zuoBiaoZhouBigDingDian = 1.0f;//�����궥��λ��
	public static float zuoBiaoZhouBigDingXi = 0.04f;//�����궥��ɡ�İ뾶
	public static float zuoBiaoZhouBigDingChang = 0.8f;//�����궥��ɡ�ĳ���
	public static float zuoBiaoZhouBigWeiDian = -1.0f;//������β�͵�λ��
	public static float fangKuaiBanJing = 0.18f;//ÿ������İ뾶

	public static void zuoBiaoZhuBigXShape3D(TransformGroup trans) {

		//���������������

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
		System.out.print("zuoBiaoZhuBigX ���� ���\n");

		trans.addChild(shape);

		//���������������󴴽����

	}

	public static void zuoBiaoZhuBigYShape3D(TransformGroup trans) {

		//���������������

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

		System.out.print("zuoBiaoZhuBigY ���� ���\n");

		trans.addChild(shape);

		//���������������󴴽����

	}

	public static void zuoBiaoZhuBigZShape3D(TransformGroup trans) {

		//���������������

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
		System.out.print("zuoBiaoZhuBigZ ���� ���\n");

		trans.addChild(shape);
		//���������������󴴽����
	}

	public static void zuoBiaoZhuSmallXShape3D(TransformGroup trans) {
		//����С���������

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

		//System.out.print("zuoBiaoZhuSmallX ���� ���");

		trans.addChild(shape);

		//�����С��������󴴽����

	}

	public static void zuoBiaoZhuSmallYShape3D(TransformGroup trans) {

		//����С���������

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
		//System.out.print("zuoBiaoZhuSmallY ���� ���");

		trans.addChild(shape);

		//�����С��������󴴽����

	}

	public static void zuoBiaoZhuSmallZShape3D(TransformGroup trans) {

		//����С���������

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

		//System.out.print("zuoBiaoZhuSmallZ ���� ���");

		trans.addChild(shape);

		//�����С��������󴴽����

	}

	public static Shape3D shapeMaker(Component observer, String filename,
			Point3f[] p) {

		//�ò��ʣ��Ķ������鴴��һ���ı��棬��Ҫapplet����observer

		//������ͼ�����
		TextureLoader loader = new TextureLoader(filename, observer);
		ImageComponent2D myImage = loader.getImage();
		Texture myTex = loader.getTexture();
		myTex.setImage(0, myImage);
		Appearance appear = new Appearance();
		appear.setTexture(myTex);

		//�ı��ζ���
		//QuadArray tri=new QuadArray(dingdian.length,QuadArray.COORDINATES|QuadArray.COLOR_3|QuadArray.TEXTURE_COORDINATE_2);
		QuadArray tri = new QuadArray(4, QuadArray.COORDINATES
				| QuadArray.TEXTURE_COORDINATE_2);//GeometryArray
		tri.setCoordinates(0, p);
		//tri.setColors(0,color);

		//���ı��ζ��������
		TexCoord2f texCoords = new TexCoord2f();//��������
		texCoords.set(0.0f, 1.0f);//ȡ���½�
		tri.setTextureCoordinate(0, 0, texCoords);//Ϊ���Ͻ�
		texCoords.set(0.0f, 0.0f);//
		tri.setTextureCoordinate(0, 1, texCoords);//
		texCoords.set(1.0f, 0.0f);//
		tri.setTextureCoordinate(0, 2, texCoords);//
		texCoords.set(1.0f, 1.0f);//
		tri.setTextureCoordinate(0, 3, texCoords);//

		Shape3D shape = new Shape3D(tri, appear);
		return shape;

		//�����6������󴴽����
	}

	public static void addText3DDonghua(TransformGroup parentTrg,
			String textString, Point3f myPoint3f, float sl,
			Color3f ambientColor, int donghua) {
		//s1��scale��myPoint3f��λ�ã�daxiao�Ǵ�С
		//�ֵ����½�Ĭ�����½����е㣬��tl=0.1ʱ��Ҫ������10�ŵ����

		//�Զ���trg
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
			//��trg���Զ��壩,������ת���
			Alpha alpha1 = new Alpha(-1, Alpha.INCREASING_ENABLE
					| Alpha.DECREASING_ENABLE, 0, 0, 5000, 300, 100000, 5000,
					300, 100000);
			RotationInterpolator myRoTate = new RotationInterpolator(alpha1,
					trg, trgtra, 0.0f, (float) Math.PI * 30);
			myRoTate.setSchedulingBounds(bounds);
			trg.addChild(myRoTate);
			trgtra.rotZ(Math.PI / 2);
			trg.setTransform(trgtra);
			System.out.println("\n�ı� ���� ����:" + donghua);
		}
		parentTrg.addChild(trg);
	}

}

//����java����ľ���,��������ʹ��0,1,2�ռ�,Ϊ�˷������,����ʱҪ��������任

public class MoFang {
	//���ຬħ�������ݱ�ʾ,blockArrayΪ���㵥������ģ�PositionΪ�����˵��Ӿ������ڲ�����
	//��ʾ���������ӵĸ����ṹ��ʵ���ϣ����ǿ������Ǵ�blockArray�����������������þ���
	public static Block[][][] blockArray = new Block[3][3][3];
	//��ħ��27��
	public static Position[][][] positionArray = new Position[3][3][3];

	//ħ��27������λ��

	//���ڴ����ħ���Ĳ�������27�ֲ���
	//doType:'X','Y','Z'
	//Floor:-1,0,1
	//totateArg:90 180 -90
	public static void doIt(char doType, int Floor, int totateArg) {
		System.out.println("\n�������:" + doType);
		System.out.println("����:" + (Floor - 1));
		System.out.println("�Ƕ�:" + totateArg + "\n");

		if (!Block.closeDonghua) //�����򿪲ŵȴ�
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
					if (Block.closeDonghua)//�����رղű任�м��
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
			System.out.println("��Ч�Ĳ���");
		}

		someBlockNewToOld();
	}

	//��ʼ�˹����ܼ���ⷨ
	//select:ѡ����Եļ��㷽��
	public static void autoStart(int select) {
	}

	//���ÿ��λ���ϵĿ��
	public static void showPosition() {
		System.out.println("\nÿ��λ���ϵĿ��:");
		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {
					//System.out.println("Block"+i+","+j+","+k+"    "+blockArray[i][j][k].x+","+blockArray[i][j][k].y+","+blockArray[i][j][k].z);
					System.out.println("Position:" + (i - 1) + "," + (j - 1)
							+ "," + (k - 1) + "  �Ŀ���ǣ�  "
							+ (positionArray[i][j][k].x - 1) + ","
							+ (positionArray[i][j][k].y - 1) + ","
							+ (positionArray[i][j][k].z - 1));

				}
	}

	//�Ѹղż�¼���б仯�������µĿ飩��λ�� �洢�� û��position�Ŀ����
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
			//��ͣ
		} catch (Exception e) {
		}
	}

	//�����ô���
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

	//ħ���Զ�����仯������
	public static void ranGet(int num) {
		char selectChar = 'E';//'X','Y','Z'
		int layer;//-1,0,1
		int jiaoDu = 0;//90,-19,180

		for (int i = 0; i < num; i++) {
			//ѡxyz
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
			System.out.print("," + layer + "," + jiaoDu + "\n���������ʼ����?\n");

			myWait();

			doIt(selectChar, layer + 1, jiaoDu);
		}
	}

	//����άͼ�ν��������������
	public static void graphicStart() {
		JFrame myframe = new JFrame();
		myframe.setVisible(true);

	}

	public static void main(String[] args) {

		//��ʼ��λ�����飬Ҳ���Դ��ļ��м���,��ʵӦ�ð����йؼ���Ϣ���ļ����룬�ر�ʱ����
		for (int i = 0; i <= 2; i++)
			for (int j = 0; j <= 2; j++)
				for (int k = 0; k <= 2; k++) {

					MoFang.positionArray[i][j][k] = new Position(i, j, k);
				}
		System.out.println("��ʼ��λ���������.���ȫ��ԭλ��\n");

		//showPosition();

		new MainFrame(new mySimpleUniverse(), 200, 200);
		//��applet��Ӧ�ó������
		//someTest();
		//graphicStart();
		
		myWait();
		ranGet(30);
		//
		//
	}

}

class Block implements Runnable {
	//�����ѻ��Ŀ���
	private static int whickBlockPainted = 0;

	//�������ɵĵڼ���
	private static int BlockCreated = 0;

	//ÿ�����ɫ,����Ϊ:+x,-x,+y,-y,+z,-z,no
	public static Color3f[] mianColor = { new Color3f(1.0f, 0.0f, 0.0f),
			new Color3f(0.0f, 1.0f, 0.0f), new Color3f(0.0f, 0.0f, 1.0f),
			new Color3f(1.0f, 1.0f, 0.0f), new Color3f(1.0f, 0.0f, 1.0f),
			new Color3f(0.0f, 1.0f, 1.0f), new Color3f(0.2f, 0.2f, 0.2f) };
	//ÿ��Ĳ���,����Ϊ:+x,-x,+y,-y,+z,-z,no
	public static String[] mianImageFile = new String[7];

	//��ƫ����
	private static float kuaiZhongXinWeizhi = 0.4f;

	//��������ʱҪ�õ�,��������,����һ��applet����
	Component observer;

	//�ÿ�ı��
	private int blockIdX;
	private int blockIdY;
	private int blockIdZ;

	//�ÿ��λ��
	private int x;
	private int y;
	private int z;

	//�ÿ���������,���ȫΪ(1,1,1),��ʾ��������һ��
	private MyPoint xvec;
	private MyPoint yvec;
	private MyPoint zvec;

	//position=new Verctor3f(0.0f,0.0f,0.0f);

	//�ÿ�ĽǶ�,�޶�Ϊ��-359��359,ģ360�����޶�Ϊһ��,���ģ180,��ֻ�ܱ�ʾ���Բ,����
	//int anglex ;
	//int angley ;
	//int anglez ;

	//�����Ʒ�ı任��
	public TransformGroup transGroup;
	public Transform3D trans;

	//����λ�ã��Ƕȱ䶯�ı任��
	public TransformGroup transGroupx;
	public Transform3D transx;
	public TransformGroup transGroupy;
	public Transform3D transy;
	public TransformGroup transGroupz;
	public Transform3D transz;
	public TransformGroup transGroupp;
	public Transform3D transp;

	//�µķ���λ��
	int[] nexyz = { 0, 0, 0 };

	//�������
	int totateArg;
	boolean canNew = false;
	char selectedC;
	Thread myThread;
	public static boolean closeDonghua = false; //Ϊtrueʹ���̶߳�����Ϊfalseֱ�ӵ���

	private static int selectDonghuaId = 0;//ȡID������ȡֵ��0~8,ÿ�δ�0��ʼȡ������9����ȡ����Ϊ10��ÿȡһ���Զ���1������ʱ���������߳�һ��
	private int myDonghuaId;
	private static int donghuaDelay = 50; //�����ӳ�,
	public static int whileDelay = 20;//�ӿ�ͬ��ϵͳ�����ӳ�,

	//�������Ʊ���
	public static int yunXingThread = 0;//���е��߳���,�����߳�ʱ��1���˳�ʱ��һ�����Կ������̵߳ȴ���ֱ��Ϊ0ʱ�ſ�ʼ�����е�xyzChange������ȴ�

	//����ʱ�Ķ��߳�Ҫ����,��Ϊ���߳�������,x,y,z��仯,ֻ����������
	int yuanx;
	int yuany;
	int yuanz;

	//��ǰλ��x���ƫ��
	MyPoint chaxvec;
	//��ǰλ��y���ƫ��
	MyPoint chayvec;
	//��ǰλ��z���ƫ��
	MyPoint chazvec;

	//���캯��,����ֵ
	public Block(int i, int j, int k, int px, int py, int pz,
			TransformGroup parentTransGroup, Transform3D t3d,
			BranchGroup objRoot, Component obServer1) {

		blockIdX = i;
		blockIdY = j;
		blockIdZ = k;

		x = px;
		y = py;
		z = pz;

		//���������� �������������ƫ��
		xvec = new MyPoint((blockIdX - 1) + 1, (blockIdY - 1), (blockIdZ - 1));
		yvec = new MyPoint((blockIdX - 1), (blockIdY - 1) + 1, (blockIdZ - 1));
		zvec = new MyPoint((blockIdX - 1), (blockIdY - 1), (blockIdZ - 1) + 1);
		//System.out.println("��㣺"+(zoux)+(zouy)+(zouz));
		//System.out.println("��������"+(zoux-(x-1))+(zouy-(y-1))+(zouz-(z-1)));

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
		//����֪��Ϣ��װ�任��,,
		//ע�������˳����ƽ�ƣ�����ת������x,��y��˳���ǲ�һ���ģ�����
		//����������ԭλ����ת�÷�����ƽ�ƣ�������ƽ�Ʋ���ı�������ת����
		//�����ƽ�ƣ���ת��ת�������ƽ�ƣ��ղŵ�ƽ�����κ����塣
		//��Ҫע��������xת������yת������zת
		//����˳���ǲ�һ���ģ�����x,y,z��z,y,x�Ľ����һ��
		//���ǣ�����֤��x,y��y,x��˳���޹���,����ֻ��ת����ת90��ʱ��������֤��
		//�����180�ȣ����Էֳ�����90�Ȳ���
		parentTransGroup.addChild(transGroupp);
		transGroupp.addChild(transGroupz);
		transGroupz.addChild(transGroupy);
		transGroupy.addChild(transGroupx);
		transGroupx.addChild(transGroup);
		// ����,ֱ�Ӽӵ���,û����깦��
		//objRoot.addChild(transGroup);
		//System.out.println("\n\n\n�����"+BlockCreated+"��");
		//System.out.println("��"+(blockIdX-1)+(blockIdY-1)+(blockIdZ-1)+"������ϵ������");
		BlockCreated++;
		//if (BlockCreated==14)//||BlockCreated==3//||BlockCreated==2||BlockCreated==20||BlockCreated==16
		add3DCube(x - 1, y - 1, z - 1, transGroup);
		//��С������
		SomeShape3D.zuoBiaoZhuSmallXShape3D(transGroup);
		SomeShape3D.zuoBiaoZhuSmallYShape3D(transGroup);
		SomeShape3D.zuoBiaoZhuSmallZShape3D(transGroup);
		//�������任���ƽ��
		//Shape3D shape1=SomeShape3D.FlowerShape3D();
		//parentTransGroup.addChild(shape1);
		//t3d.setTranslation(new Vector3f(0.0f,1.0f,0.0f));
		//parentTransGroup.setTransform(t3d);
		//�����ӱ任���ƽ��(����,��������)
		//Shape3D shape2=SomeShape3D.SanShape3D();
		//transGroup.addChild(shape2);
		//trans.setTranslation(new Vector3f(0.0f,-1f,0.0f));
		//transGroup.setTransform(t3d);
		//���������Ϊ
		//MouseRotate behavior1=new MouseRotate();
		//behavior1.setTransformGroup(transGroup);
		//behavior1.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),100));
		//objRoot.addChild(behavior1);//�����Ϊ  �ӵ� ��֦��
		//���ó�ʼֵ,��ʾ���
		float fx;
		float fy;
		float fz;
		fx = (float) kuaiZhongXinWeizhi * (x - 1);
		fy = (float) kuaiZhongXinWeizhi * (y - 1);
		fz = (float) kuaiZhongXinWeizhi * (z - 1);
		//�任��
		transx.rotX(Math.toRadians(0));//anglex
		transy.rotY(Math.toRadians(0));
		transz.rotZ(Math.toRadians(0));
		transp.setTranslation(new Vector3f(fx, fy, fz));
		//��Ч
		transGroupp.setTransform(transp);
		transGroupx.setTransform(transx);
		transGroupy.setTransform(transy);
		transGroupz.setTransform(transz);
	}

	int[] jisuanNextXYZ(char doType, int totateArg, int oldx, int oldy, int oldz)
	//��Ҫ����x,y,z,doType,totateArg,������ص�����
	{
		//System.out.println("������һ���㡣����");
		//���ڼ���
		int newz = 0;
		int newy = 0;
		int newx = 0;
		//����ʱ�ı�׼��ʽ
		int[] nextXYZ = { 0, 0, 0 };

		//����ʱ���յ������溯���ķ��غ���ֵ
		int[] myShunShiNext = { 0, 0 };
		if (totateArg == 0) {
			//��totateArgΪ0ʱ��û�п��õ�ifƥ��,����ԭλ��
			newx = oldx;
			newy = oldy;
			newz = oldz;
			//���ؽ��
			nextXYZ[0] = newx;
			nextXYZ[1] = newy;
			nextXYZ[2] = newz;
			//System.out.println("\n��Ϊλ�ã�"+newx+newy+newz);
			return nextXYZ;
		}
		//
		//System.out.println("\nԭλ�ã�"+oldx+oldy+oldz);
		//System.out.println("�ƣ�"+doType+"ת"+totateArg);

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
		//���ؽ��
		nextXYZ[0] = newx;
		nextXYZ[1] = newy;
		nextXYZ[2] = newz;
		//System.out.println("\n��Ϊλ�ã�"+newx+newy+newz);
		return nextXYZ;
	}

	boolean fangXiangCorrect(int argx, int argy, int argz) {//�������������ж���ת
	//System.out.println("�����жϡ�����");
		//(blockIdX-1),(blockIdY-1),(blockIdZ-1)Ϊ��ʼ��λ�ã��ֱ��1�õ�������ʼ����
		//xvec,yvec,zvecΪ����ı仯
		//��ȥ��ǰ�ķ���λ�ã��õ�ÿ������㵱ǰ����chaxvec,chayvec,chazvec
		//X��
		int[] p1X = jisuanNextXYZ('X', argx, 1, 0, 0);//ԭʼx�����
		int[] p2X = jisuanNextXYZ('Y', argy, p1X[0], p1X[1], p1X[2]);
		int[] p3X = jisuanNextXYZ('Z', argz, p2X[0], p2X[1], p2X[2]);//�µ�x�����
		//Y��
		int[] p1Y = jisuanNextXYZ('X', argx, 0, 1, 0);
		int[] p2Y = jisuanNextXYZ('Y', argy, p1Y[0], p1Y[1], p1Y[2]);
		int[] p3Y = jisuanNextXYZ('Z', argz, p2Y[0], p2Y[1], p2Y[2]);
		//Z��
		int[] p1Z = jisuanNextXYZ('X', argx, 0, 0, 1);
		int[] p2Z = jisuanNextXYZ('Y', argy, p1Z[0], p1Z[1], p1Z[2]);
		int[] p3Z = jisuanNextXYZ('Z', argz, p2Z[0], p2Z[1], p2Z[2]);
		//System.out.println("����ϵ:"+chaX+"   "+chaY+"   "+chaZ);
		// �µ�x�����=��ǰλ��x���ƫ��
		if (((p3X[0] == chaxvec.x) && (p3X[1] == chaxvec.y) && (p3X[2] == chaxvec.z))
				&& ((p3Y[0] == chayvec.x) && (p3Y[1] == chayvec.y) && (p3Y[2] == chayvec.z))
				&& ((p3Z[0] == chazvec.x) && (p3Z[1] == chazvec.y) && (p3Z[2] == chazvec.z))) {
			System.out.println("�����ᵽλ��");
			return true;
		} else {
			//System.out.println("������û��λ");
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
			System.out.println("λ�ö���");
			return true;
		} else {
			System.out.println("λ�ò���");
			return false;
		}
	}

	//��������ʱ����תʱ������仯,��ת��ʱ��-90��ʱ,ת��Ϊ3����ʱ��90��
	int[] quXiaYiGe(int num1, int num2, int n) {
		int[][] xiangXianZhi = { { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } }; //һ������������,0,1,2,3
		int[][] zouShangZhi = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } }; //
		int[] result = { 0, 0 };
		//System.out.println("��ת����Ϊ:"+n);
		//System.out.println(num1+" , "+num2+"��ת��:");
		int temp = 0;
		for (int i = 0; i < n; i++) {
			//ѭ��һ��תһ��
			if (num1 == 0 && num2 == 0)//
			{
				num1 = 0;
				num2 = 0;
				//System.out.println("0");
			} else if (num1 > 0 && num2 > 0)//һ����ת��������
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				//System.out.println("1");
			} else if (num1 < 0 && num2 > 0)//������ת��������
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				////System.out.println("2");
			} else if (num1 < 0 && num2 < 0)//������ת��������
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				////System.out.println("3");
			} else if (num1 > 0 && num2 < 0)//������ת��һ����
			{
				temp = num1;
				num1 = -num2;
				num2 = temp;
				//System.out.println("4");
			} else if (num1 > 0 && num2 == 0)//X�ϵ�Y��
			{
				temp = num1;
				num1 = 0;
				num2 = temp;
				//System.out.println("5");
			} else if (num1 == 0 && num2 > 0)//Y�ϵ�X��
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
		//System.out.println("�ҵĶ�����ʼ��");
		//����Э��,ȡһ��id�����ų�����
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
		//����λ��
		while (!canNew)//��canNew=trueʱ�������3D���棬����ֻ����
		{//System.out.println(" ���ڵȴ��������ֵ...");
		}
		//�任��Ч
		transGroupp.setTransform(transp);
		transGroupx.setTransform(transx);
		transGroupy.setTransform(transy);
		transGroupz.setTransform(transz);
		canNew = false;
		//System.out.println("�ҵĶ��������");
		yunXingThread--;
	}

	//������һ��ԭ����ͽ�������� ���� 3��x,y,z˳�����ת���������ص�needRotate[3]��
	//ʹ����int[] jisuanNextXYZ(char doType,int totateArg,int oldx,int oldy,int oldz)
	int[] shouSuoXYZRotate(int oldx, int oldy, int oldz, int aidx, int aidy,
			int aidz) {//������ת������λ�ã�ֻ�����������Բ���
		System.out.println("��ԭʼλ��:" + oldx + "," + oldy + "," + oldz);
		System.out.println("��Ŀ��λ��:" + aidx + "," + aidy + "," + aidz);
		//��Ž��
		int needRotatex = 0;
		int needRotatey = 0;
		int needRotatez = 0;
		//ת������
		int[] needRotate = { 0, 0, 0 };
		int num = 0;
		wancheng: for (int j = 0; j <= 3; j++) {
			int toArg = 0;//j=3ʱȡ0
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
					//System.out.println("��֤xyz��ת.."+toArg+"  "+ttoArg+"  "+tttoArg);
					// ����� �еĻ��Ҳ���
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
						System.out.println("*****�ҵ���x,y,z��ת����*****" + toArg
								+ " , " + ttoArg + " , " + tttoArg + "");
						//return needRotate;
					}
				}
			}
		}
		if (num == 0) {
			System.out.println("û���ʺϵ�xyz��ת,����ת..");
			MoFang.myWait();
		} else if (num >= 2) {
			System.out
					.println("###########################�ҵ���num=" + num + "��");
		}
		return needRotate;
	}

	void startDonghuaX() {
		//����ģ��
		int chuJiao;
		int oneTime;
		int bianJiao;
		int nowJiao;
		//����
		//totateArg=90;
		chuJiao = getChujiao(yuany, yuanz);
		if (totateArg == -90)
			oneTime = -15;
		else
			oneTime = 15;
		//System.out.println("chuJiao="+chuJiao);
		//System.out.println("totateArg="+totateArg);
		//nowJiao=chuJiao+bianJiao ;
		//����Ŀǰ�Ƕȹ�ʽ���ɼ�������������ж����ĽǶ���
		for (bianJiao = 0; bianJiao != (totateArg + oneTime); bianJiao += oneTime) {
			if (!closeDonghua) {
				while (myDonghuaId != selectDonghuaId)//����ͬ������ֵ��Լ�ʱ��ִ�У������ƽ�����һ��
				{
					;//System.out.println("����"+myDonghuaId+"��,������������"+selectDonghuaId+"��,��Ҫ��...");
					try {
						//System.in.read();//��ͣ
						myThread.sleep(whileDelay);
					} catch (Exception e) {
					}
				}
			}
			//����x,y�ͽǶ����
			float fx;
			float fy;
			float fz;
			//�뾶
			float r;
			if (yuany == 0 && yuanz == 0)
				r = 0;//(x,y)���䣬�ʲ��ü��㣬ֱ�Ӹ�(0,0),���Ƿ����κ�ֵ����;
			else {
				if (yuany == 0 || yuanz == 0)
					r = 1.0f;
				else
					r = 1.414f;
			}
			nowJiao = chuJiao + bianJiao;
			//ʹ�ó���ֱΪ�˼��㵱ǰ��x,y��λ��,��ǰjiaodu1�����޹أ�ֻ���Ͻ��й�
			//(-1,-1)����ԭ��Ϊ��2,Լ1.732
			//0.3ָ����1����0.3,-0.3��
			fy = (float) kuaiZhongXinWeizhi * r
					* (float) Math.cos(Math.PI * nowJiao / 180);
			fz = (float) kuaiZhongXinWeizhi * r
					* (float) Math.sin(Math.PI * nowJiao / 180);
			fx = (float) kuaiZhongXinWeizhi * (yuanx);
			//z���겻��,�����z�������1
			//�������ȫ������,OK,��ʼˢ��
			//�ñ任��
			//transz.rotZ(Math.toRadians(anglez));
			//transy.rotY(Math.toRadians(angley));
			//transx.rotX(Math.toRadians(bianJiao+oldJiaoDu));
			transp.setTranslation(new Vector3f(fx, fy, fz));
			//��Ч
			//transGroupz.setTransform(transz);
			//transGroupy.setTransform(transy);
			//transGroupx.setTransform(transx);
			transGroupp.setTransform(transp);
			//ͣ����,ֻ��ʹ�ö��̻߳�ʱ
			//System.out.println("bianJiao="+bianJiao);
			try {
				//System.in.read();//��ͣ
				//
				myThread.sleep(donghuaDelay);
			} catch (Exception e) {
			}
			//������ŷų�����
			selectDonghuaId++;
			selectDonghuaId %= 9;
		}
	}

	void startDonghuaY() {
		//����ģ��
		int chuJiao;
		int oneTime;
		int bianJiao;
		int nowJiao;
		//����
		//totateArg=90;
		chuJiao = getChujiao(yuanz, yuanx);
		if (totateArg == -90)
			oneTime = -15;
		else
			oneTime = 15;
		//System.out.println("chuJiao="+chuJiao);
		//System.out.println("totateArg="+totateArg);
		//nowJiao=chuJiao+bianJiao ;
		//����Ŀǰ�Ƕȹ�ʽ���ɼ�������������ж����ĽǶ���
		for (bianJiao = 0; bianJiao != (totateArg + oneTime); bianJiao += oneTime) {
			if (!closeDonghua) {
				while (myDonghuaId != selectDonghuaId)//����ͬ������ֵ��Լ�ʱ��ִ�У������ƽ�����һ��
				{
					;//System.out.println("����"+myDonghuaId+"��,������������"+selectDonghuaId+"��,��Ҫ��...");
					try {
						//System.in.read();//��ͣ
						myThread.sleep(whileDelay);
					} catch (Exception e) {
					}
				}
			}
			//����x,y�ͽǶ����
			float fx;
			float fy;
			float fz;
			//�뾶
			float r;
			if (yuanz == 0 && yuanx == 0)
				r = 0;//(x,y)���䣬�ʲ��ü��㣬ֱ�Ӹ�(0,0),���Ƿ����κ�ֵ����;
			else {
				if (yuanz == 0 || yuanx == 0)
					r = 1.0f;
				else
					r = 1.414f;
			}
			nowJiao = chuJiao + bianJiao;
			//ʹ�ó���ֱΪ�˼��㵱ǰ��x,y��λ��,��ǰjiaodu1�����޹أ�ֻ���Ͻ��й�
			//(-1,-1)����ԭ��Ϊ��2,Լ1.732
			//0.3ָ����1����0.3,-0.3��
			fz = (float) kuaiZhongXinWeizhi * r
					* (float) Math.cos(Math.PI * nowJiao / 180);
			fx = (float) kuaiZhongXinWeizhi * r
					* (float) Math.sin(Math.PI * nowJiao / 180);
			fy = (float) kuaiZhongXinWeizhi * (yuany);
			//z���겻��,�����z�������1
			//�������ȫ������,OK,��ʼˢ��
			//�ñ任��
			//transz.rotZ(Math.toRadians(anglez));
			//transy.rotY(Math.toRadians(bianJiao+oldJiaoDu));
			//transx.rotX(Math.toRadians(anglex));
			transp.setTranslation(new Vector3f(fx, fy, fz));
			//��Ч
			//transGroupz.setTransform(transz);
			//transGroupy.setTransform(transy);
			//transGroupx.setTransform(transx);
			transGroupp.setTransform(transp);
			//ͣ����,ֻ��ʹ�ö��̻߳�ʱ
			//System.out.println("bianJiao="+bianJiao);
			try {
				//System.in.read();//��ͣ
				//
				myThread.sleep(donghuaDelay);
			} catch (Exception e) {
			}
			//������ŷų�����
			selectDonghuaId++;
			selectDonghuaId %= 9;
		}
	}

	void startDonghuaZ() {
		//����ģ��
		int chuJiao;
		int oneTime;
		int bianJiao;
		int nowJiao;
		//����
		//totateArg=90;
		chuJiao = getChujiao(yuanx, yuany);
		if (totateArg == -90)
			oneTime = -15;
		else
			oneTime = 15;
		//System.out.println("chuJiao="+chuJiao);
		//System.out.println("totateArg="+totateArg);
		//nowJiao=chuJiao+bianJiao ;
		//����Ŀǰ�Ƕȹ�ʽ���ɼ�������������ж����ĽǶ���
		for (bianJiao = 0; bianJiao != (totateArg + oneTime); bianJiao += oneTime) {
			if (!closeDonghua) {
				while (myDonghuaId != selectDonghuaId)//����ͬ������ֵ��Լ�ʱ��ִ�У������ƽ�����һ��
				{
					;//System.out.println("����"+myDonghuaId+"��,������������"+selectDonghuaId+"��,��Ҫ��...");
					try {
						//System.in.read();//��ͣ
						myThread.sleep(whileDelay);
					} catch (Exception e) {
					}
				}
			}
			//����x,y�ͽǶ����
			float fx;
			float fy;
			float fz;
			//�뾶
			float r;
			if (yuanx == 0 && yuany == 0)
				r = 0;//(x,y)���䣬�ʲ��ü��㣬ֱ�Ӹ�(0,0),���Ƿ����κ�ֵ����;
			else {
				if (yuanx == 0 || yuany == 0)
					r = 1.0f;
				else
					r = 1.414f;
			}
			nowJiao = chuJiao + bianJiao;
			//ʹ�ó���ֱΪ�˼��㵱ǰ��x,y��λ��,��ǰjiaodu1�����޹أ�ֻ���Ͻ��й�
			//(-1,-1)����ԭ��Ϊ��2,Լ1.732
			//0.3ָ����1����0.3,-0.3��
			fx = (float) kuaiZhongXinWeizhi * r
					* (float) Math.cos(Math.PI * nowJiao / 180);
			fy = (float) kuaiZhongXinWeizhi * r
					* (float) Math.sin(Math.PI * nowJiao / 180);
			fz = (float) kuaiZhongXinWeizhi * (yuanz);
			//z���겻��,�����z�������1
			//�������ȫ������,OK,��ʼˢ��
			//�ñ任��
			//transz.rotZ(Math.toRadians());
			//transy.rotY(Math.toRadians());
			//transx.rotX(Math.toRadians());
			transp.setTranslation(new Vector3f(fx, fy, fz));
			//��Ч
			//transGroupz.setTransform(transz);
			//transGroupy.setTransform(transy);
			//transGroupx.setTransform(transx);
			transGroupp.setTransform(transp);
			//ͣ����,ֻ��ʹ�ö��̻߳�ʱ

			//System.out.println("bianJiao="+bianJiao);
			try {
				//System.in.read();//��ͣ
				//
				myThread.sleep(donghuaDelay);
			} catch (Exception e) {
			}
			//������ŷų�����
			selectDonghuaId++;
			selectDonghuaId %= 9;

		}
	}

	//center�������������ɫ
	void add3DCube(int centerx, int centery, int centerz,
			TransformGroup myTransGroup) {
		//System.out.println("���ڻ��ÿ�.....");
		//��ɫ���ݽṹ
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
		//��ͼ
		String presentImageFile;
		mianImageFile[0] = "IMG\\coverRight.jpg";
		mianImageFile[1] = "IMG\\coverLeft.jpg";
		mianImageFile[2] = "IMG\\coverUp.jpg";
		mianImageFile[3] = "IMG\\coverDown.jpg";
		mianImageFile[4] = "IMG\\coverFront.jpg";
		mianImageFile[5] = "IMG\\coverBehind.jpg";
		mianImageFile[6] = "IMG\\coverCenter.jpg";
		//�����ݽṹ
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
		//ͨ��for,���ϵ���������
		Point3f[][] vert = new Point3f[6][4];
		Color3f[] color = new Color3f[6];
		String[] imageFile = new String[6];
		for (int i = 0; i <= 5; i++) {
			//������� ��ɫ����ͼ
			if (compare[i] == compareWith[i]) {
				presentMianColor = mianColor[i];
				presentImageFile = mianImageFile[i];
			} else {
				presentMianColor = mianColor[6];
				presentImageFile = mianImageFile[6];
				//�����ɫΪ��ɫ����������
				//continue����������
				//continue ;
			}
			try {
				//System.in.read();//��ͣ
			} catch (Exception e) {
			}
			//������� ����
			mianxin.x = mianxinpianyi[i].x;
			mianxin.y = mianxinpianyi[i].y;
			mianxin.z = mianxinpianyi[i].z;
			//������� �ĸ���
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
			//�ö������ɫ�� ����
			//���淽��һ,��vector3D���󴫽�ȥ��������ת��Ϊfloat����
			//Shape3D shape=SomeShape3D.mian1of6CubeShape3D(observer,dingdian,presentImageFile,presentMianColor);
			//���淽��������vector3D������ת��Ϊpoint3f���飬�ٴ���ȥ��ת������
			Point3f[] vert1 = new Point3f[4];
			Point3f[] vert2 = new Point3f[4];
			//4�������Ϣ
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
			//���������һ��
			//��ʧ���� ���Ա��⣬ԭ���Ǹ����νӵ㲻�غϣ�dingdian[k].x����SomeShape3D.fangKuaiBanJing������������
			//�ҵ��Լ�������ϵ
			myTransGroup.addChild(shape1);
			myTransGroup.addChild(shape2);
			//����3

			// for(int j=0;j<=3;j++)
			//  {
			// vert[i][j]=new Point3f(SomeShape3D.fangKuaiBanJing*dingdian[j].x ,SomeShape3D.fangKuaiBanJing*dingdian[j].y,SomeShape3D.fangKuaiBanJing*dingdian[j].z);
			// }
			//color[i]=presentMianColor;
			// imageFile[i]=presentImageFile;
		}
		//����3
		// SomeShape3D.box3D(observer,myTransGroup,vert,color,imageFile);
		//System.out.println("��"+whickBlockPainted+"����ϡ�");
		System.out.print('.');
		whickBlockPainted++;
	}

	//�ÿ�ı�λ��
	//Floor:0,1��2
	//totateArg:90 180 -90
	//Ϊ�˷�����㣬��ƽ��(����ȫ��1),�任��,��ƽ�ƻ�ԭ(����ȫ��1)
	//�ÿ�ı�λ��
	//Floor:0,1��2
	//totateArg:90 180 -90
	//Ϊ�˷�����㣬��ƽ��(����ȫ��1),�任��,��ƽ�ƻ�ԭ(����ȫ��1)
	void xyzChange(char doType, int mYtotateArg) {
		//
		totateArg = mYtotateArg;
		//�ڴ�������У�ԭ������ƽ��
		x -= 1;
		y -= 1;
		z -= 1;
		//����Ԥ����
		yuanx = x;
		yuany = y;
		yuanz = z;

		if (closeDonghua) {//startDonghuaX();
		} else
		//��Ϊ���߳�ִ��
		{
			selectedC = doType;
			myThread = new Thread(this, "Rotate");
			myThread.start();
		}
		System.out.println("�����x���㲢���档����");
		//�������㲢����
		//System.out.println("������"+xvec.x+xvec.y+xvec.z);
		int[] newvecx = jisuanNextXYZ(doType, totateArg, xvec.x, xvec.y, xvec.z);
		//System.out.println("������");
		xvec.x = newvecx[0];
		xvec.y = newvecx[1];
		xvec.z = newvecx[2];
		System.out.println("�����y���㲢���档����");
		int[] newvecy = jisuanNextXYZ(doType, totateArg, yvec.x, yvec.y, yvec.z);
		yvec.x = newvecy[0];
		yvec.y = newvecy[1];
		yvec.z = newvecy[2];
		int[] newvecz = jisuanNextXYZ(doType, totateArg, zvec.x, zvec.y, zvec.z);
		zvec.x = newvecz[0];
		zvec.y = newvecz[1];
		zvec.z = newvecz[2];
		System.out.println("��������㲢���档����");
		//���������
		nexyz = jisuanNextXYZ(doType, totateArg, x, y, z);
		//���������
		System.out.println("\n��" + (blockIdX - 1) + (blockIdY - 1)
				+ (blockIdZ - 1) + "��" + x + y + z + "  �䵽  " + nexyz[0]
				+ nexyz[1] + nexyz[2]);
		//�����걣��
		x = nexyz[0] + 1;
		y = nexyz[1] + 1;
		z = nexyz[2] + 1;
		System.out.println("��Ѱ��ת�Ƕ���������");
		//��Ѱ��ת�Ƕ���
		//��ǰλ��x���ƫ��
		chaxvec = new MyPoint(xvec.x - nexyz[0], xvec.y - nexyz[1], xvec.z
				- nexyz[2]);
		//��ǰλ��y���ƫ��
		chayvec = new MyPoint(yvec.x - nexyz[0], yvec.y - nexyz[1], yvec.z
				- nexyz[2]);
		//��ǰλ��z���ƫ��
		chazvec = new MyPoint(zvec.x - nexyz[0], zvec.y - nexyz[1], zvec.z
				- nexyz[2]);
		System.out.println("---------x���ƫ��" + (xvec.x - nexyz[0]) + "  "
				+ (xvec.y - nexyz[1]) + "  " + (xvec.z - nexyz[2]));
		System.out.println("---------y���ƫ��" + (yvec.x - nexyz[0]) + "  "
				+ (yvec.y - nexyz[1]) + "  " + (yvec.z - nexyz[2]));
		System.out.println("---------z���ƫ��" + (zvec.x - nexyz[0]) + "  "
				+ (zvec.y - nexyz[1]) + "  " + (zvec.z - nexyz[2]));
		//MoFang.myWait();
		int[] xZJD = shouSuoXYZRotate(yuanx, yuany, yuanz, nexyz[0], nexyz[1],
				nexyz[2]);
		//������3D���������
		//1,��������
		float fx;
		float fy;
		float fz;
		fx = (float) kuaiZhongXinWeizhi * (x - 1);
		fy = (float) kuaiZhongXinWeizhi * (y - 1);
		fz = (float) kuaiZhongXinWeizhi * (z - 1);
		//�任��
		transp.setTranslation(new Vector3f(fx, fy, fz));
		//MoFang.myWait();
		transx.rotX(Math.toRadians(xZJD[0]));//anglex
		transy.rotY(Math.toRadians(xZJD[1]));
		transz.rotZ(Math.toRadians(xZJD[2]));
		//��Ч
		if (closeDonghua) { //�任��Ч
			transGroupx.setTransform(transx);
			transGroupy.setTransform(transy);
			transGroupz.setTransform(transz);
			transGroupp.setTransform(transp);
		}
		//��ˢ�£��ڶ�����ˢ�£���������ˣ����ڶ�����
		canNew = true;
		//ͬʱ����positionArray
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
			System.out.println("�м��,�����������");
		else
			System.out.println("��������x=" + xyzQian + "  y=" + xyzHou);
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
		//ֻҪ��Ϊtrue,���ϻ�ִ��newToOld,ʹ���ص�false
	}

	void newToOld() {
		x = newx;
		y = newy;
		z = newz;

		haveNew = false;
		//��֤��ÿһ�β�����ȫΪfalse
	}

	//�õ�ĳ��uλ��
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
		System.out.println("�÷���û�ж�Ӧ��λ��");
		return p;
		//���ﲻ�÷����κζ��������׳��쳣
	}
}

//Point3f,Vector3f�����ã����Զ������ά����
class MyPoint {
	int x;
	int y;
	int z;

	//���캯��һ
	MyPoint(int getx, int gety, int getz) {
		x = getx;
		y = gety;
		z = getz;
	}

	//���캯������ת��floatΪint
	MyPoint(float getx, float gety, float getz) {
		x = (int) getx;
		y = (int) gety;
		z = (int) getz;
	}
}
