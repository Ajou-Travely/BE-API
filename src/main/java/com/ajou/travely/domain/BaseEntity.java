package com.ajou.travely.domain;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {
    protected LocalDateTime createdAt;
}
