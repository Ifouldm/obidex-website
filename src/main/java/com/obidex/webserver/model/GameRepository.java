package com.obidex.webserver.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:8000")
@RepositoryRestResource(collectionResourceRel = "game", path = "games")
public interface GameRepository extends MongoRepository<Game, String> {
}
