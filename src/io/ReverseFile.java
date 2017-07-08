package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;

public class ReverseFile {

	private static class CharArrayWrapper {
		private char[] buf;
		private int len;

		private CharArrayWrapper(char[] buf, int len) {
			this.buf = buf.clone();
			this.len = len;
		}

		private CharArrayWrapper reverse() {
			for (int i = 0; i < len / 2; i++) {
				char c = buf[i];
				buf[i] = buf[len - 1 - i];
				buf[len - 1 - i] = c;
			}
			return this;
		}
	}

	public static void reverseFile(File srcFile, File destFile) {
		String charset = FileCharset.get_charset(srcFile);
		final int BUF_SIZE = 256;
		char[] cbuf = new char[BUF_SIZE];
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), charset));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), charset));
			// read into bufs
			LinkedList<CharArrayWrapper> bufs = new LinkedList<>();
			int len = -1;
			while ((len = reader.read(cbuf)) != -1) {
				bufs.add(new CharArrayWrapper(cbuf, len));
			}
			System.out.println("read times: " + bufs.size());

			// write from bufs
			for (Iterator<CharArrayWrapper> itr = bufs.descendingIterator(); itr.hasNext();) {
				CharArrayWrapper bw = itr.next().reverse();
				// System.out.println(new String(bw.buf, 0, bw.len));
				writer.write(bw.buf, 0, bw.len);
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		File srcFile = new File("text/text_file2.txt");
		File destFile = new File("temp/reverse_text_file2.txt");
		reverseFile(srcFile, destFile);
	}
}
