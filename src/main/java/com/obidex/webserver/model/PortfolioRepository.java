package com.obidex.webserver.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "portfolio", path = "portfolio")
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
}
