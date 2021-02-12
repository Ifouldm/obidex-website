package com.obidex.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio implements Persistable {
    @Id
    String id;
    String title;
    String[] images;
    Tech[] tech;
    String description;
    String githubLink;
    String productLink;
    @Version
    int version;
    @CreatedDate
    Date dateCreated;
    @LastModifiedDate
    Date dateModified;

    @Override
    public boolean isNew() {
        return (id == null || id.equals(""));
    }
}

