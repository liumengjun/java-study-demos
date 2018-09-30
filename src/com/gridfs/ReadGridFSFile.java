package com.gridfs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.types.ObjectId;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class ReadGridFSFile {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        MongoClient mongoClient = MongoClients.create(); // localhost单例
        MongoDatabase db = mongoClient.getDatabase("gridfs");

        //单例即可，创建多个实例并无影响，内部间接使用了mongoClient作为通讯支撑
        GridFSBucket gridFS = GridFSBuckets.create(db);//可以指定bucket名字，默认值为“fs”

        // 指定ObjectId下载文件
        ObjectId fileId = new ObjectId("5bb04711448eab564aebf95a");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gridFS.downloadToStream(fileId, out);
        System.out.println(out.toString("GBK"));
    }
}
