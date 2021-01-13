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
import com.amazonaws.services.s3.model.S3ObjectSummary;
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
    @Value("${aws.accessKey:#{environment.AWS_ACCESS_KEY}}")
    private String accessKey;
    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.secretKey:#{environment.AWS_SECRET_KEY}}")
    private String secretKey;

    /**
     * Initialise AWS client for storage and populate the endpoint for stored data.
     */
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

    /**
     * Generate a File from a MultipartFile for storage in S3 data bucket
     *
     * @param file The MultipartFile to be stored
     * @return The File result of the MultipartFile
     * @throws IOException
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    /**
     * Generates a list of stored filenames within the associated bucket in String format
     *
     * @return List of String filenames
     */
    private List<String> getFileList() {
        ListObjectsV2Result bucketList = s3client.listObjectsV2(bucketName);
        return bucketList.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public void init() {
        //init performed in PostConstruct
    }

    /**
     * Stores the given {@link File} in S3 using the given filename as the file maybe a temp file with a arbitrary filename
     *
     * @param file     The {@link File} to be stored
     * @param filename The filename to use
     */
    @Override
    public void store(File file, String filename) {
        try {
            s3client.putObject(bucketName, filename, file);
        } catch (Exception e) {
            log.error("Unable to convert multipart file into file ");
            e.printStackTrace();
        }
    }

    /**
     * Stores the given {@link MultipartFile} in S3 using the current filename
     *
     * @param file The {@link MultipartFile} to be stored
     */
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

    /**
     * The stream of {@link Path} urls for the key(s) (filename(s)) within the bucket
     *
     * @return A Stream of {@link Path}s
     */
    @Override
    public Stream<Path> loadAll() {
        return getFileList().stream().map(this::load);
    }

    /**
     * Returns a {@link Path} associated with the given filename
     *
     * @param filename The filename to be looked up
     * @return the {@link java.nio.file.Path} for the given filename
     */
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

    /**
     * The resource associated with the given filename
     *
     * @param filename The filename to be looked up
     * @return The {@link Resource} associated with the given filename
     */
    @Override
    public Resource loadAsResource(String filename) {
        if (!getFileList().contains(filename))
            throw new StorageFileNotFoundException("File not found for: " + filename);
        return new UrlResource(s3client.getUrl(bucketName, filename));
    }

    /**
     * Delete all files in the current bucket
     */
    @Override
    public void deleteAll() {
        getFileList().forEach(file -> s3client.deleteObject(bucketName, file));
    }

    /**
     * The full path of the current bucket
     *
     * @return The full path of the current bucket
     */
    @Override
    public String getFullPath() {
        return fullpath;
    }
}
