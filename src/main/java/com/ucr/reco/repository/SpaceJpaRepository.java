package com.ucr.reco.repository;

import com.ucr.reco.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceJpaRepository extends JpaRepository<Space, Integer> {
    Space getByName(String name);
}
