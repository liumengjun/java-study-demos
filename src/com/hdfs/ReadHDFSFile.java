package com.hdfs;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;

import java.io.*;
import java.net.URI;

public class ReadHDFSFile {

    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
            Path file = new Path("tmp/file1.txt");
            FSDataInputStream getIt = fs.open(file);
            BufferedReader d = new BufferedReader(new InputStreamReader(getIt, "GBK"));
            String s = "";
            while ((s = d.readLine()) != null) {
                System.out.println(s);
            }
            d.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
