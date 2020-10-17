package com.obidex.webserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class ScreenshotService {

    @Value("${screenshot.accesskey}")
    private String accessKey;
    @Value("${screenshot.address}")
    private String apiflashEndpoint;
    @Value("${screenshot.options}")
    private String options;
    @Value("${app.upload.location}")
    private String uploadLocation;

    private Path directory;

    @PostConstruct
    public void init() {
        // path to project root
        directory = Paths.get(uploadLocation);
        if (!directory.toFile().isDirectory()) {
            if (directory.toFile().mkdir())
                log.info("Uploads directory created: {}", directory.toAbsolutePath());
            else
                log.error("Unable to create upload directory {}", directory);
        } else
            log.trace("Uploads directory: {} exists", directory.toAbsolutePath());
    }

    public String getScreenshot(String subjecturl, String name) {
        String filename = name.substring(0, Math.min(name.length(), 10)) + ".png";
        URL url = null;
        try {
            url = new URL(String.format("%s?access_key=%s&url=%s%s", apiflashEndpoint, accessKey, subjecturl, options));
        } catch (MalformedURLException e) {
            log.error("URL building error");
            return "error";
        }
        try (InputStream inputStream = url.openStream(); OutputStream outputStream = new FileOutputStream(directory.resolve(filename).toString())) {
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, length);
            }
        } catch (Exception e) {
            log.error("Unable to take screenshot for url: {}", subjecturl);
            return "error";
        }
        return filename;
    }
}
