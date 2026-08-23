package com.abdellah.spring_auth_starter.repository;



import com.abdellah.spring_auth_starter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE  u.email = ?1")
    Optional<User> findByEmail(String email);

}
