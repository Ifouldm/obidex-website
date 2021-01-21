package com.obidex.webserver.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "consoles")
public class Console {
    private String id;
    private String name;
    private String brand;
    private String description;
    private String photoUrl;
    private String logoUrl;
}
