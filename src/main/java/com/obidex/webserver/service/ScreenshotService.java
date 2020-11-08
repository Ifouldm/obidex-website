package com.obidex.webserver.service;

import com.amazonaws.services.s3.AmazonS3;
import com.obidex.webserver.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Service
public class ScreenshotService {

    @Autowired
    private StorageService storageService;

    private AmazonS3 s3client;

    @Value("${screenshot.accesskey}")
    private String ssaccessKey;
    @Value("${screenshot.address}")
    private String apiflashEndpoint;
    @Value("${screenshot.options}")
    private String options;

    /**
     * Take a screenshot of the given url and store it using the filename provided
     *
     * @param subjecturl URL of the website to be captured
     * @param name       Filename to store screenshot
     * @return full filename with extension for the stored file
     */
    public String getScreenshot(String subjecturl, String name) {
        String filename = name.substring(0, Math.min(name.length(), 10));
        URL url = null;
        try {
            url = new URL(String.format("%s?access_key=%s&url=%s%s", apiflashEndpoint, ssaccessKey, subjecturl, options));
        } catch (MalformedURLException e) {
            log.error("URL building error");
            return "error";
        }
        File tempFile = null;
        try {
            tempFile = File.createTempFile(filename, ".png");
            tempFile.deleteOnExit();
        } catch (IOException e) {
            log.error("Error creating temporary file");
        }
        try (InputStream inputStream = url.openStream(); OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, length);
            }
            storageService.store(tempFile, filename + ".png");
        } catch (Exception e) {
            log.error("Unable to take screenshot for url: {}", subjecturl);
            log.error(e.getMessage());
            return "error";
        }
        return filename + ".png";
    }

    /**
     * Returns the end point for the storage location
     *
     * @return the end point for the storage location
     */
    public String getLocation() {
        return storageService.getFullPath();
    }
}
