package com.obidex.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    @Id
    private String id;
    private String name;
    private String image;
    private String url;
    private String description;
}
