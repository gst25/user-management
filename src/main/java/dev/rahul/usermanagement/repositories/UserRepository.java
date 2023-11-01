package dev.rahul.usermanagement.repositories;

import dev.rahul.usermanagement.models.User;
import jakarta.persistence.Id;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
