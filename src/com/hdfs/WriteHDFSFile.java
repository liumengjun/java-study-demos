package com.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

public class WriteHDFSFile {

    public static void main(String[] args) throws IOException {
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
            Path path = new Path("tmp/newfile2.txt");
            FSDataOutputStream out = fs.create(path);
            out.writeUTF("hello, I'm cheyo. I'm rocky. rocky is my new English name");
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
