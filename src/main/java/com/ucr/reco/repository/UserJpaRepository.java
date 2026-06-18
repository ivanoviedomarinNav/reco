package com.ucr.reco.repository;

import com.ucr.reco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    User getByEmail(String email);
}
