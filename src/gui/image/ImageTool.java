package gui.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import sun.awt.image.ToolkitImage;

public class ImageTool {
	public static void main(String[] args) throws Exception {
		test();
	}

	public static void test() throws Exception {
		String name = "image/SymbolImg.png";
		name = "image/SymbolImg_AOR.gif";
		File imageFile = new File(name);
		System.out.println(imageFile.exists());

		Image image = readImageFileHorizontalCropedAtIndex(imageFile, 2, 32);
		// Image image = readImageFileVerticalCropedAtIndex(imageFile, 0, 32/3);
		System.out.println(image.getClass());
		System.out.println(getImageWidth(image) + "*" + getImageHeight(image));

		int w = 32;
		int h = 32;
		int[] pixels = convertImage2Pixel(image, w, h);
		// image = convertPixel2Image(pixels, w, h);
		// image = convertImageBigger(pixels, w, h, 2.0F);
		// image = convertImageBigger(pixels, w, h, 0.5F);

		Color color = Color.orange;
		Color filterColor = Color.black;
		Color bgColor = new Color(0,128,128);
		int x = 8;
		int y = 20;
		int aw = 17;
		int ah = 8;
		int[] newPixels = setAreaByColorInPixels(pixels, w, h, color, x, y, aw, ah, filterColor);
		newPixels = createTransparentImage(newPixels, w, h, bgColor);
		image = convertPixel2Image(newPixels, w, h);
		image = convertImageBigger(image, w, h, 1.5F);
		System.out.println(image);

		// BufferedImage bufImage = convert2BufferedImage(image);
		// writeImage(bufImage, new File("image/temp.gif"), true);

		ImageIcon icon = new ImageIcon(image);
		System.out.println(icon.getClass());
		System.out.println(icon.getIconWidth() + "*" + icon.getIconHeight());

		writeImageIcon(icon, new File("image/temp.gif"), true);
		JOptionPane.showConfirmDialog(null, "232", "YoSi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
				icon);
	}

	/**
	 * 尝试得到指定图像的高度
	 * 
	 * @param image
	 * @return
	 */
	public static int getImageHeight(Image image) {
		int height = -1;
		if (image instanceof BufferedImage) {
			height = ((BufferedImage) image).getHeight();
		} else if (image instanceof ToolkitImage) {
			height = ((ToolkitImage) image).getHeight();
		} else if (image instanceof VolatileImage) {
			height = ((VolatileImage) image).getHeight();
		} else {
			try {
				height = image.getHeight(null);
			} catch (Exception e) {
			}
		}
		return height;
	}

	/**
	 * 尝试得到指定图像的宽度
	 * 
	 * @param image
	 * @return
	 */
	public static int getImageWidth(Image image) {
		int width = -1;
		if (image instanceof BufferedImage) {
			width = ((BufferedImage) image).getWidth();
		} else if (image instanceof ToolkitImage) {
			width = ((ToolkitImage) image).getWidth();
		} else if (image instanceof VolatileImage) {
			width = ((VolatileImage) image).getWidth();
		} else {
			try {
				width = image.getWidth(null);
			} catch (Exception e) {
			}
		}
		return width;
	}

	/**
	 * 读取图像文件中的图像，一般返回的BufferedImage[]长度是1，当时动态GIF图象时，返回多个BufferedImage
	 * 
	 * @param imageFile
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage[] readImageFile(File imageFile) throws IOException {
		String name = imageFile.getName();
		String suffix = name.substring(name.lastIndexOf('.') + 1);
		ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
		if (reader == null) {
			return null;
		}

		ImageInputStream imageIn = ImageIO.createImageInputStream(imageFile);
		reader.setInput(imageIn);

		int count = reader.getNumImages(true);
		BufferedImage[] images = new BufferedImage[count];

		for (int i = 0; i < count; i++) {
			images[i] = reader.read(i);
		}

		return images;
	}

	/**
	 * 读取图像文件中的图像，以宽度为w橫向分割图像（h为图像总高度），返回分割后的第(i+1)个图像
	 * 
	 * @param imageFile
	 * @param index
	 * @param width
	 * @return
	 * @throws IOException
	 */
	public static Image readImageFileHorizontalCropedAtIndex(File imageFile, int index, int width) throws IOException {
		BufferedImage[] images = readImageFile(imageFile);
		BufferedImage image = images[0];
		int h = image.getHeight();
		return cropImageHorizontal(images[0], index, width, h);
	}

	/**
	 * 读取图像文件中的图像，以高度为h纵向分割图像（w为图像总宽度），返回分割后的第(i+1)个图像
	 * 
	 * @param imageFile
	 * @param index
	 * @param height
	 * @return
	 * @throws IOException
	 */
	public static Image readImageFileVerticalCropedAtIndex(File imageFile, int index, int height) throws IOException {
		BufferedImage[] images = readImageFile(imageFile);
		BufferedImage image = images[0];
		int w = image.getWidth();
		return cropImageVertical(images[0], index, w, height);
	}

	/**
	 * 以宽度为w橫向分割图像（h为图像总高度），返回分割后的第(i+1)个图像
	 * 
	 * @param image
	 * @param i
	 * @param w
	 * @param h
	 * @return
	 */
	public static Image cropImageHorizontal(Image image, int i, int w, int h) {
		ImageFilter filter = new CropImageFilter(i * w, 0, w, h);
		FilteredImageSource filteredImageSource = new FilteredImageSource(image.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(filteredImageSource);
	}

	/**
	 * 以高度为h纵向分割图像（w为图像总宽度），返回分割后的第(i+1)个图像
	 * 
	 * @param image
	 * @param i
	 * @param w
	 * @param h
	 * @return
	 */
	public static Image cropImageVertical(Image image, int i, int w, int h) {
		ImageFilter filter = new CropImageFilter(0, i * h, w, h);
		FilteredImageSource filteredImageSource = new FilteredImageSource(image.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(filteredImageSource);
	}

	/**
	 * 把图像转换为像素数组，每个像素值是一个int型的ARGB颜色值
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @return
	 */
	public static int[] convertImage2Pixel(Image image, int w, int h) {
		int[] pixel = new int[w * h];

		try {
			PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixel, 0, w);
			pg.grabPixels();
		} catch (Exception exp) {
			System.out.println("catch Exception : ObjectImage convertPixel");
			exp.printStackTrace();
		}

		return pixel;
	}

	/**
	 * 把像素数组转换为图像，每个像素值是一个int型的ARGB颜色值
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @return
	 */
	public static Image convertPixel2Image(int[] pixels, int w, int h) {
		if (!check(pixels, w, h)) {
			return null;
		}
		MemoryImageSource imageSource = new MemoryImageSource(w, h, pixels, 0, w);
		return Toolkit.getDefaultToolkit().createImage(imageSource);
	}

	/**
	 * 把图像（w*h）中矩形区域Rectangle(x, y, aw, ah)的颜色值设置成color<br/>
	 * 若原来颜色值为filterColor,则保留,若filterColor为null,则统一设定为color
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @param color
	 * @param x
	 * @param y
	 * @param aw
	 * @param ah
	 * @param filterColor
	 * @return
	 */
	public static Image setImageAreaByColor(Image image, int w, int h, Color color, int x, int y, int aw, int ah,
			Color filterColor) {
		int[] pixels = convertImage2Pixel(image, w, h);
		return setAreaByColorPixels2Image(pixels, w, h, color, x, y, aw, ah, filterColor);
	}

	/**
	 * 把图像（w*h）中矩形区域Rectangle(x, y, aw, ah)的颜色值设置成color<br/>
	 * 若原来颜色值为filterColor,则保留,若filterColor为null,则统一设定为color<br/>
	 * 并返回以像素数值表示的图像
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @param color
	 * @param x
	 * @param y
	 * @param aw
	 * @param ah
	 * @param filterColor
	 * @return
	 */
	public static int[] setImageAreaByColor2Pixels(Image image, int w, int h, Color color, int x, int y, int aw,
			int ah, Color filterColor) {
		int[] pixels = convertImage2Pixel(image, w, h);
		return setAreaByColorInPixels(pixels, w, h, color, x, y, aw, ah, filterColor);
	}

	/**
	 * 把以像素数值表示的图像（w*h）中矩形区域Rectangle(x, y, aw, ah)的颜色值设置成color<br/>
	 * 若原来颜色值为filterColor,则保留,若filterColor为null,则统一设定为color<br/>
	 * 并返回图像
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param color
	 * @param x
	 * @param y
	 * @param aw
	 * @param ah
	 * @param filterColor
	 * @return
	 */
	public static Image setAreaByColorPixels2Image(int[] pixels, int w, int h, Color color, int x, int y, int aw,
			int ah, Color filterColor) {
		int[] newPixels = setAreaByColorInPixels(pixels, w, h, color, x, y, aw, ah, filterColor);
		return convertPixel2Image(newPixels, w, h);
	}

	/**
	 * 把以像素数值表示的图像（w*h）中矩形区域Rectangle(x, y, aw, ah)的颜色值设置成color<br/>
	 * 若原来颜色值为filterColor,则保留,若filterColor为null,则统一设定为color
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param color
	 * @param x
	 * @param y
	 * @param aw
	 * @param ah
	 * @param filterColor
	 * @return
	 */
	public static int[] setAreaByColorInPixels(int[] pixels, int w, int h, Color color, int x, int y, int aw, int ah,
			Color filterColor) {
		final int PIXELS_LENGTH = w * h;
		int[] newPixels = new int[PIXELS_LENGTH];
		System.arraycopy(pixels, 0, newPixels, 0, PIXELS_LENGTH);

		int colorVal = color.getRGB();
		if (filterColor != null) {
			// 启用过滤色的情况，像素颜色值等于过滤色时，不改变原来的颜色值
			int filterVal = filterColor.getRGB();
			for (int i = 0; i < aw; i++) {
				for (int j = 0; j < ah; j++) {
					int pos = y * w + j * w + x + i;
					if (pos < PIXELS_LENGTH) {
						if (pixels[pos] == filterVal) {
							continue;
						} else {
							newPixels[pos] = colorVal;
						}
					}
				}
			}
		} else {
			// 没有过滤色的情况
			for (int i = 0; i < aw; i++) {
				for (int j = 0; j < ah; j++) {
					int pos = y * w + j * w + x + i;
					if (pos < PIXELS_LENGTH) {
						newPixels[pos] = colorVal;
					}
				}
			}
		}
		return newPixels;
	}

	/**
	 * 转换以像素数值表示的图像（w*h）为透明图像： 把颜色值为bgColor的像素设为透明
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param bgColor
	 * @return
	 */
	public static int[] createTransparentImage(int[] pixels, int w, int h, Color bgColor) {
		if (!check(pixels, w, h) || bgColor == null) {
			return null;
		}
		int[] newpixels = new int[w * h];
		int bgVal = bgColor.getRGB();
		for (int i = 0; i < newpixels.length; i++) {
			if (pixels[i] == bgVal) {
				continue;
			} else {
				newpixels[i] = pixels[i];
			}
		}
		return newpixels;
	}

	/**
	 * 把图像（w*h）缩放scale倍
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @param scale
	 * @return
	 */
	public static Image convertImageBigger(Image image, int w, int h, float scale) {
		int[] pixels = convertImage2Pixel(image, w, h);
		return convertImageBigger(pixels, w, h, scale);
	}

	/**
	 * 把以像素数值表示的图像（w*h）缩放scale倍
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param scale
	 * @return
	 */
	public static Image convertImageBigger(int[] pixels, int w, int h, float scale) {
		if (!check(pixels, w, h)) {
			return null;
		}
		if (scale < 0 || scale > 16) {
			return null;
		}
		MemoryImageSource imageSource = new MemoryImageSource(w, h, pixels, 0, w);
		Image image = Toolkit.getDefaultToolkit().createImage(imageSource);
		int newW = (int) (w * scale);
		int newH = (int) (h * scale);
		if (newW < 1) {
			newW = 1;
		}
		if (newH < 1) {
			newH = 1;
		}
		return image.getScaledInstance(newW, newH, Image.SCALE_DEFAULT);
	}

	/**
	 * 把图像写入文件
	 * 
	 * @param image
	 * @param imageFile
	 * @param overExist
	 * @return
	 */
	public static boolean writeImage(RenderedImage image, File imageFile, boolean overExist) {
		String name = imageFile.getName();
		String suffix = name.substring(name.lastIndexOf('.') + 1);
		if (imageFile.exists() && !overExist) {
			return false;
		}
		try {
			ImageIO.write(image, suffix, imageFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 把icon写入文件
	 * 
	 * @param imageIcon
	 * @param imageFile
	 * @param overExist
	 * @return
	 */
	public static boolean writeImageIcon(ImageIcon imageIcon, File imageFile, boolean overExist) {
		String name = imageFile.getName();
		String suffix = name.substring(name.lastIndexOf('.') + 1);
		if (imageFile.exists() && !overExist) {
			return false;
		}
		try {
			Image image = imageIcon.getImage();
			// System.out.println(image);
			BufferedImage bufImage = convert2BufferedImage(image);
			ImageIO.write(bufImage, suffix, imageFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 得到图像的类型为BufferedImage的图像
	 * 
	 * @param image
	 * @return
	 */
	public static BufferedImage convert2BufferedImage(Image image) {
		BufferedImage bufImage;
		// convert Image to bufferedImage
		if (image instanceof BufferedImage) {
			bufImage = (BufferedImage) image;
		} else if (image instanceof ToolkitImage) {
			bufImage = ((ToolkitImage) image).getBufferedImage();
		} else if (image instanceof VolatileImage) {
			bufImage = ((VolatileImage) image).getSnapshot();
		} else {
			bufImage = null;
		}
		return bufImage;
	}

	private static boolean check(int[] pixels, int w, int h) {
		if (pixels == null || w < 1 || h < 1 || pixels.length < w * h) {
			return false;
		}
		return true;
	}

}
