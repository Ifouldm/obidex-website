package com.obidex.webserver.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("${app.upload.location}")
    private String uploadLocation;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        log.info("Uploads directory registered to {}", uploadLocation);
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadLocation);
    }
}
