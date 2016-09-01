package iostream;

/**
 * @version 1.01 2004-05-11
 * @author Cay Horstmann
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.zip.CRC32;

/**
 * This program computes the CRC checksum of a file. Usage: java NIOTest filename
 */
public class NIOTest {
	static int buffer_size = 8 * 1024;
	public static long checksumInputStream(String filename) throws IOException {
		InputStream in = new FileInputStream(filename);
		CRC32 crc = new CRC32();

		byte[] buf = new byte[buffer_size];
		int c;
		while ((c = in.read(buf)) != -1) {
			crc.update(buf, 0, c);
		}
		return crc.getValue();
	}

	public static long checksumBufferedInputStream(String filename) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(filename), buffer_size);
		CRC32 crc = new CRC32();

		byte[] buf = new byte[buffer_size];
		int c;
		while ((c = in.read(buf)) != -1) {
			crc.update(buf, 0, c);
		}
		return crc.getValue();
	}

	public static long checksumRandomAccessFile(String filename) throws IOException {
		RandomAccessFile file = new RandomAccessFile(filename, "r");
		long length = file.length();
		CRC32 crc = new CRC32();

		byte[] buf = new byte[buffer_size];
		for (long p = 0; p < length;) {
			file.seek(p);
			int c = file.read(buf);
			if (c == -1) {
				break;
			}
			crc.update(buf, 0, c);
			p += c;
		}
		return crc.getValue();
	}

	public static long checksumMappedFile(String filename) throws IOException {
		FileInputStream in = new FileInputStream(filename);
		FileChannel channel = in.getChannel();

		CRC32 crc = new CRC32();
		int length = (int) channel.size();
		MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);

//		System.out.println(buffer.position());
//		System.out.println(buffer.remaining());
		byte[] buf = new byte[buffer_size];
		for (int p = 0; p < length;) {
			int c = buf.length;
			if (c > buffer.remaining()) {
				c = buffer.remaining();
			}
			buffer.get(buf, 0, c);
//			System.out.println(buffer.position());
//			System.out.println(buffer.remaining());
			crc.update(buf, 0, c);
			p += buf.length;
		}
		return crc.getValue();
	}
	
	public static void main(String[] args) throws IOException {
		//test1(args);
		test2(args);
	}

	public static void test1(String[] args) throws IOException {
		buffer_size = 128*1024;
		long start, end, crcValue;
		args = new String[]{"G:/movie/天空之城.MP4"};
		System.out.println("Input Stream:");
		start = System.currentTimeMillis();
		crcValue = checksumInputStream(args[0]);
		end = System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end - start) + " milliseconds");

		System.out.println("Buffered Input Stream:");
		start = System.currentTimeMillis();
		crcValue = checksumBufferedInputStream(args[0]);
		end = System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end - start) + " milliseconds");

		System.out.println("Mapped File:");
		start = System.currentTimeMillis();
		crcValue = checksumMappedFile(args[0]);
		end = System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end - start) + " milliseconds");

		System.out.println("Random Access File:");
		start = System.currentTimeMillis();
		crcValue = checksumRandomAccessFile(args[0]);
		end = System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end - start) + " milliseconds");
	}
	

	public static long checksumRandomAccessData(File fileInput, long[] locs, int[] size) throws IOException {
		if (locs==null || locs.length==0 || size==null || size.length==0) {
			return 0;
		}
		RandomAccessFile file = new RandomAccessFile(fileInput, "r");
		CRC32 crc = new CRC32();
		long length = file.length();

		byte[] buf = new byte[8*1024];
		for (int p = 0; p < locs.length; p++) {
			if (size.length<=p){
				break;
			}
			if (size[p]==0 || size[p]>buf.length || locs[p]+size[p]>length) {
				continue;
			}
			file.seek(locs[p]);
			file.read(buf, 0, size[p]);
			crc.update(buf, 0, size[p]);
		}
		file.close();
		return crc.getValue();
	}
	public static long checksumRandomAccessDataViaFileChannel(File fileInput, long[] locs, int[] size) throws IOException {
		if (locs==null || locs.length==0 || size==null || size.length==0) {
			return 0;
		}
		RandomAccessFile in = new RandomAccessFile(fileInput, "r");
		//FileInputStream in = new FileInputStream(fileInput);
		FileChannel channel = in.getChannel();
		CRC32 crc = new CRC32();
		long length = channel.size();
		MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);

		byte[] buf = new byte[8*1024];
		for (int p = 0; p < locs.length; p++) {
			if (size.length<=p){
				break;
			}
			if (size[p]==0 || size[p]>buf.length || locs[p]+size[p]>length) {
				continue;
			}
			buffer.position((int)locs[p]);
			buffer.get(buf, 0, size[p]);
			crc.update(buf, 0, size[p]);
		}
		channel.close();
		in.close();
		return crc.getValue();
	}
	
	public static void test2(String[] args) throws IOException {
		final int data_count = 1024*1024;
		final int max_size = 16;
		//File f = new File("G:/movie/天空之城.MP4");
		File f = new File("G:/TEMP/未命名UVS9.mpg");
		//File f = new File("G:/TEMP/哈哈哈.wav");
		long[] locs = new long[data_count];
		int[] size = new int[data_count];
		long length = f.length();
		Random rand = new Random();
		Random rand2 = new Random();
		for (int i=0; i<data_count; i++) {
//			locs[i] = (long)(rand.nextDouble()*length);
			locs[i] = rand.nextLong()%length;
			if (locs[i]<0) {
				locs[i]+=length;
			}
			size[i] = rand2.nextInt(max_size)+1;
		}
		long start, end, crcValue;

		System.out.println("Mapped File:");
		start = System.currentTimeMillis();
		crcValue = checksumRandomAccessDataViaFileChannel(f, locs, size);
		end = System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end - start) + " milliseconds");
		
		System.out.println("Random Access File:");
		start = System.currentTimeMillis();
		crcValue = checksumRandomAccessData(f, locs, size);
		end = System.currentTimeMillis();
		System.out.println(Long.toHexString(crcValue));
		System.out.println((end - start) + " milliseconds");
		
		System.out.println("可得出结论，如果内存足够大(或文件较小)，可用Mapped File。IO次数越多Mapped File的优势越明显。");
	}
}
