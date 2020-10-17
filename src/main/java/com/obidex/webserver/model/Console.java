package com.obidex.webserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "consoles")
public class Console {
    @Id
    private String consoleId;
    private String name;
    private String description;
    private String photoUrl;
    private String brand;
}
