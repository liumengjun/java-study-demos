package com.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

public class UploadHDFSFile {

    public static void main(String[] args) throws IOException {
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
            Path src = new Path("text/SD.TXT");
            Path dst = new Path("tmp/file1.txt");
            fs.copyFromLocalFile(src, dst);
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
