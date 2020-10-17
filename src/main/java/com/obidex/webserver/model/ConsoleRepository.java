package com.obidex.webserver.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "consoles", path = "consoles")
public interface ConsoleRepository extends MongoRepository<Console, String> {
    Optional<Console> findByConsoleId(@Param("id") String id);
}
