package fileio;


import java.io.*;

public class Directory {
	
	public static void main(String[] args) throws IOException {
		/**
		 * Ŀ¼�ж�
		 */
		File curDir = new File("./student");
		boolean isDirectory;
		isDirectory = curDir.isDirectory();
		System.out.println("isDirectory:"+isDirectory);
		System.out.println("exists:"+curDir.exists());
		curDir.mkdirs();
		isDirectory = curDir.isDirectory();
		System.out.println("isDirectory:"+isDirectory);
		System.out.println("exists:"+curDir.exists());
		System.out.println(curDir.getPath());
		
		String datafilename = curDir.getPath()+File.separator+"student.txt";
		System.out.println(datafilename);
		File datafile = new File(datafilename);
		isDirectory = datafile.isDirectory();
		System.out.println("isDirectory:"+isDirectory);
		System.out.println("exists:"+datafile.exists());
		datafile.createNewFile();
		isDirectory = datafile.isDirectory();
		System.out.println("isDirectory:"+isDirectory);
		System.out.println("exists:"+datafile.exists());
		System.out.println(datafile.getPath());
		//datafile.setReadOnly();
		System.out.println(datafile.getFreeSpace());
		
		/**
		 * RandomAccessFile��д�ļ�
		 */
		RandomAccessFile randFile = new RandomAccessFile(datafile,"rw");
		System.out.println("now length:"+randFile.length());
		
		randFile.setLength(0);
		System.out.println("now length:"+randFile.length());
		randFile.seek(45);
		System.out.println("Now at:"+randFile.getFilePointer());
		randFile.writeUTF("Hello");//д����7���ֽ�
		System.out.println("Now at:"+randFile.getFilePointer());
		System.out.println("now length:"+randFile.length());
		
		/*
		randFile.setLength(0);
		System.out.println("now length:"+randFile.length());
		randFile.seek(45);
		System.out.println("Now at:"+randFile.getFilePointer());
		randFile.writeBytes("Hello");//д����5���ֽ�
		System.out.println("Now at:"+randFile.getFilePointer());
		System.out.println("now length:"+randFile.length());
		*/
		
		/*
		randFile.setLength(0);
		System.out.println("now length:"+randFile.length());
		randFile.seek(45);
		System.out.println("Now at:"+randFile.getFilePointer());
		randFile.writeChars("Hello");//д����10���ֽ�
		System.out.println("Now at:"+randFile.getFilePointer());
		System.out.println("now length:"+randFile.length());
		randFile.close();
		*/
	}
}
