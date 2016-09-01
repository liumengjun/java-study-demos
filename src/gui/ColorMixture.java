package gui;

import java.awt.Color;

public class ColorMixture {
	public static void main(String[] args) throws Exception {
		Color cA = Color.blue;
		Color cB = Color.red;
		// Color test = Color.cyan;
		Color cC = mixingColor3(cA, cB);
		System.out.println(cA);
		System.out.println(cB);
		// System.out.println(test);
		System.out.println(cC);
	}

	public static Color mixingColor(Color a, Color b) {
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.getColorSpace());
		System.out.println(b.getColorSpace());

		int aa = a.getAlpha();
		int ar = a.getRed();
		int ag = a.getGreen();
		int ab = a.getBlue();

		int ba = b.getAlpha();
		int br = b.getRed();
		int bg = b.getGreen();
		int bb = b.getBlue();

		int ca = aa | ba;
		int cr = ar | br;
		int cg = ag | bg;
		int cb = ab | bb;

		Color c = new Color(cr, cg, cb, ca);
		return c;
	}

	public static Color mixingColor2(Color a, Color b) {
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.getColorSpace());
		System.out.println(b.getColorSpace());

		float aa = a.getAlpha() / 255.0F;
		float ar = a.getRed() / 255.0F;
		float ag = a.getGreen() / 255.0F;
		float ab = a.getBlue() / 255.0F;

		float ba = b.getAlpha() / 255.0F;
		float br = b.getRed() / 255.0F;
		float bg = b.getGreen() / 255.0F;
		float bb = b.getBlue() / 255.0F;

		float fd = 1 - aa;

		float ca = aa + ba * fd;
		float cr = ar + br * fd;
		float cg = ag + bg * fd;
		float cb = ab + bb * fd;

		Color c = new Color(cr, cg, cb, ca);
		return c;
	}

	public static Color mixingColor3(Color a, Color b) {
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.getColorSpace());
		System.out.println(b.getColorSpace());

		int aa = a.getAlpha();
		int ar = a.getRed();
		int ag = a.getGreen();
		int ab = a.getBlue();

		int ba = b.getAlpha();
		int br = b.getRed();
		int bg = b.getGreen();
		int bb = b.getBlue();

		float[] a_hsbvals = Color.RGBtoHSB(ar, ag, ab, null);
		float[] b_hsbvals = Color.RGBtoHSB(br, bg, bb, null);

		float ch = (a_hsbvals[0] * a_hsbvals[1] + b_hsbvals[0] * b_hsbvals[1])
				/ (a_hsbvals[1] + b_hsbvals[1]);
		// float cb = (a_hsbvals[2] + b_hsbvals[2]);
		float cb = Math.min(1.0F, a_hsbvals[2] + b_hsbvals[2]);
		float cs = (a_hsbvals[1] * a_hsbvals[2] + b_hsbvals[1] * b_hsbvals[2])
				/ cb;

		int cc = Color.HSBtoRGB(ch, cs, cb);
		System.out.println(Integer.toHexString(cc));
		Color c = new Color(cc);
		return c;
	}
}
