package com.obidex.webserver.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "bookmarks", path = "bookmarks")
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {
}
