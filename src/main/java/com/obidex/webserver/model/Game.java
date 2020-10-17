package com.obidex.webserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "games")
public class Game {
    @Id
    private String gameId;
    @Field("_name")
    private String gameName;
    private String description;
    private String manufacturer;
    private String developer;
    private String year;
    private String genre;
    private String score;
    private String players;
    private String console;
}
