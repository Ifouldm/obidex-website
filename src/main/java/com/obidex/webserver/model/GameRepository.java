package com.obidex.webserver.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "game", path = "game")
public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByGameName(@Param("name") String gameName);

    Game findByGameId(@Param("id") String id);
}
