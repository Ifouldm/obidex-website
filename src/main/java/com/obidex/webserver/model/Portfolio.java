package com.obidex.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {
    @Id
    String id;
    String title;
    String[] images;
    Tech[] tech;
    String description;
    String githubLink;
    String productLink;
    Date dateCreated;
    Date dateModified;
}

