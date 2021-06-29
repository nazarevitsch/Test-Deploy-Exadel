package com.exadel.discount.platform.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

@Component
public class AmazonService {

    private String awsBucketName;

    private AWSCredentials credentials;
    private AmazonS3 s3client;


    public AmazonService(@Value("${aws.name}") String awsBucketName,
                         @Value("${aws.secret}") String awsSecret,
                         @Value("${aws.access}") String awsAccess) {
        this.awsBucketName = awsBucketName;
        credentials = new BasicAWSCredentials(awsAccess, awsSecret);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    public String uploadFile(MultipartFile file) throws Exception{
        String[] parts = file.getOriginalFilename().split("\\.");
        ObjectMetadata metadata = new ObjectMetadata();
        String name = (new Date().getTime()) + "." + parts[parts.length - 1];
        metadata.addUserMetadata("discount", name);
        PutObjectRequest putObjectRequest = new PutObjectRequest(awsBucketName, name, file.getInputStream(), metadata);
        s3client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        return s3client.getUrl(awsBucketName, name).toString();
    }
}
