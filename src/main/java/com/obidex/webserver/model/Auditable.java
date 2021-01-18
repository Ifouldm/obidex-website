package com.obidex.webserver.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
public class Auditable {
    @CreatedDate
    private Date createdDate;
    @LastModifiedDate
    private Date modifiedDate;
}
