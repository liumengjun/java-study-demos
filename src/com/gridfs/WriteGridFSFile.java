package com.gridfs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.types.ObjectId;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WriteGridFSFile {

    public static void main(String[] args) throws FileNotFoundException {
        MongoClient mongoClient = MongoClients.create(); // localhost单例
        MongoDatabase db = mongoClient.getDatabase("gridfs");

        //单例即可，创建多个实例并无影响，内部间接使用了mongoClient作为通讯支撑
        GridFSBucket gridFS = GridFSBuckets.create(db);//可以指定bucket名字，默认值为“fs”

        // 上传本地文件
        FileInputStream inputStream = new FileInputStream("text/SD.TXT");
        ObjectId fileId = gridFS.uploadFromStream("text-sd.txt", inputStream);
        System.out.println(fileId.toHexString());//此字符串ID，可供application使用
    }
}
