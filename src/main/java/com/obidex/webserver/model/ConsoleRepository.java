package com.obidex.webserver.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8000")
@RepositoryRestResource(collectionResourceRel = "consoles", path = "consoles")
public interface ConsoleRepository extends MongoRepository<Console, String> {
    @Override
    List<Console> findAll();
}
