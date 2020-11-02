package com.obidex.webserver.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    private AmazonS3 s3client;
    private String fullpath;

    @Value("${aws.endpoint}")
    private String endpoint;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.secretKey}")
    private String secretKey;

    @PostConstruct
    public void serviceInit() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        String region = Region.EU_London.getFirstRegionId();
        s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
        fullpath = "http://" + bucketName + '.' + endpoint + '/';
        log.info("S3 bucket path registered to: {}", fullpath);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private List<String> getFileList() {
        ListObjectsV2Result bucketList = s3client.listObjectsV2(bucketName);
        return bucketList.getObjectSummaries()
                .stream()
                .map(file -> file.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public void init() {
        //init performed in PostConstruct
    }

    @Override
    public void store(File file, String filename) {
        try {
            s3client.putObject(bucketName, filename, file);
        } catch (Exception e) {
            log.error("Unable to convert multipart file into file ");
            e.printStackTrace();
        }
    }

    @Override
    public void store(MultipartFile file) {
        try {
            File strFile = convertMultiPartToFile(file);
            PutObjectResult putObjectResult = s3client.putObject(bucketName, file.getOriginalFilename(), strFile);
            log.info(putObjectResult.toString());
        } catch (Exception e) {
            log.error("Unable to convert multipart file into file ");
            e.printStackTrace();
        }
    }

    @Override
    public Stream<Path> loadAll() {
        // return the stream of urls for each key (filename) within the bucket
        return getFileList().stream().map(file -> load(file));
    }

    @Override
    public Path load(String filename) {
        if (!getFileList().contains(filename))
            throw new StorageFileNotFoundException("File not found for: " + filename);
        // return the url for the given filename
        try {
            return Paths.get(s3client.getUrl(bucketName, filename).toURI());
        } catch (URISyntaxException e) {
            log.error("Error loading Path for filename: {}", filename);
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        if (!getFileList().contains(filename))
            throw new StorageFileNotFoundException("File not found for: " + filename);
        return new UrlResource(s3client.getUrl(bucketName, filename));
    }

    @Override
    public void deleteAll() {
        getFileList().forEach(file -> s3client.deleteObject(bucketName, file));
    }

    @Override
    public String getFullPath() {
        return fullpath;
    }
}
