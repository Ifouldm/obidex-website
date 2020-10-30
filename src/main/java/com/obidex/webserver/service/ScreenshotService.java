package com.obidex.webserver.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Service
public class ScreenshotService {

    private AmazonS3 s3client;
    private String fullpath;

    @Value("${screenshot.accesskey}")
    private String ssaccessKey;
    @Value("${screenshot.address}")
    private String apiflashEndpoint;
    @Value("${screenshot.options}")
    private String options;
    @Value("${app.upload.location}")
    private String uploadLocation;

    @Value("${aws.endpoint}")
    private String endpoint;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        String region = Region.EU_London.getFirstRegionId();
        s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
        fullpath = "http://" + bucketName + '.' + endpoint + '/';
        log.info("Bookmark bucket path registered to: {}", fullpath);
    }

    public String getScreenshot(String subjecturl, String name) {
        String filename = name.substring(0, Math.min(name.length(), 10)) + ".png";
        URL url = null;
        try {
            url = new URL(String.format("%s?access_key=%s&url=%s%s", apiflashEndpoint, ssaccessKey, subjecturl, options));
        } catch (MalformedURLException e) {
            log.error("URL building error");
            return "error";
        }
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".tmp");
        } catch (IOException e) {
            log.error("Error creating temporary file");
        }
        tempFile.deleteOnExit();
        try (InputStream inputStream = url.openStream(); OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, length);
            }
            s3client.putObject(bucketName, filename, tempFile);
        } catch (Exception e) {
            log.error("Unable to take screenshot for url: {}", subjecturl);
            log.error(e.getMessage());
            return "error";
        }
        return filename;
    }

    public String getLocation() {
        return fullpath;
    }
}
