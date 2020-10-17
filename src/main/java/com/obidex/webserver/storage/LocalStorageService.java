package com.obidex.webserver.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
@Service
public class LocalStorageService implements StorageService {

    @Value("${app.upload.location}")
    private String uploadLocation;

    private Path directory;

    @PostConstruct
    @Override
    public void init() {
        // path to project root
        directory = Paths.get(uploadLocation);
        if (!directory.toFile().isDirectory()) {
            if (directory.toFile().mkdir())
                log.info("Uploads directory created: {}", directory.toAbsolutePath());
            else
                log.error("Unable to create upload directory {}", directory);
        } else
            log.info("Uploads directory: {} exists", directory.toAbsolutePath());
    }

    @Override
    public void store(MultipartFile file) {
        try {
            Path destination = directory.resolve(file.getOriginalFilename());
            byte[] bytes = file.getBytes();
            Files.write(destination, bytes);
            log.info("File stored {} -> {}", file.getOriginalFilename(), destination);
        } catch (IOException e) {
            log.error("Unable to store file {} {}", file.getOriginalFilename(), e);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        File[] files = directory.toFile().listFiles();
        return files != null ? Stream.of(files).map(File::toPath) : Stream.empty();
    }

    @Override
    public Path load(String filename) {
        return Path.of(directory.toString(), filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        return new PathResource(load(filename));
    }

    @Override
    public void deleteAll() {
        File[] files = directory.toFile().listFiles();
        for (File file : files) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
