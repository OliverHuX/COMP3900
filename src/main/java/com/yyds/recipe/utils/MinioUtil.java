package com.yyds.recipe.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public Object statObject(String bucketName, String objectName) {
        if (bucketExists(bucketName)) {
            return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        }
        return null;
    }

    @SneakyThrows
    public boolean makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean removeBucket(String bucketName) {
        if (bucketExists(bucketName)) {
            for (Result<Item> listObject : listObjects(bucketName)) {
                Item item = listObject.get();
                if (item.size() > 0) {
                    return false;
                }
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return !bucketExists(bucketName);
        }
        return false;
    }

    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {
        if (bucketExists(bucketName)) {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        List<String> listObjectNames = new ArrayList<>();
        if (bucketExists(bucketName)) {
            for (Result<Item> listObject : listObjects(bucketName)) {
                Item item = listObject.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    @SneakyThrows
    public boolean putObject(String bucketName, String objectName, String contentType, InputStream stream) {
        if (bucketExists(bucketName)) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(stream, stream.available(), -1)
                    .contentType(contentType)
                    .build());
            return statObject(bucketName, objectName) != null;
        }
        return false;
    }

    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        if (bucketExists(bucketName)) {
            if (statObject(bucketName, objectName) != null) {
                return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
            }
        }
        return null;
    }

    @SneakyThrows
    public boolean getObject(String bucketName, String objectName, String fileName) {
        if (bucketExists(bucketName)) {
            if (statObject(bucketName, objectName) != null) {
                minioClient.downloadObject(DownloadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(fileName)
                        .build());
            }
            return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        if (bucketExists(bucketName)) {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }
        return false;
    }

    @SneakyThrows
    public List<String> removeObject(String bucketName, List<DeleteObject> objectList) {
        LinkedList<String> deleteErrorList = new LinkedList<>();
        if (bucketExists(bucketName)) {
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                    .bucket(bucketName)
                    .objects(objectList)
                    .build()
            );
            for (Result<DeleteError> result : results) {
                deleteErrorList.add(result.get().objectName());
            }
        }
        return deleteErrorList;
    }


    @SneakyThrows
    public String presignedGetObject(String bucketName, String objectName, Integer expires) {
        if (bucketExists(bucketName)) {
            if (expires < 1 || expires > 7) {
                throw new Exception();
            }
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    // .expiry(expires)
                    .build()
            );
        }
        return null;
    }

    @SneakyThrows
    public String presignedPutObject(String bucketName, String objectName, Integer expires) {
        if (bucketExists(bucketName)) {
            if (expires < 1 || expires > 7) {
                throw new Exception();
            }
            HashMap<String, String> reqParams = new HashMap<>();
            reqParams.put("response-content-type", "application/json");
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expires)
                    .extraQueryParams(reqParams)
                    .build()
            );
        }
        return null;
    }
}
