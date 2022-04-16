package com.ajou.travely.repository;

import com.ajou.travely.domain.Travel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class JpaTravelRepository implements TravelRepository{
    private final EntityManager em;

    public JpaTravelRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Travel save(Travel travel) {
        return null;
    }
}
