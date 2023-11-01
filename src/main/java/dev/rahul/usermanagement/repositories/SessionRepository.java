package dev.rahul.usermanagement.repositories;

import dev.rahul.usermanagement.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByTokenAndId(String token, Long userId);

    List<Session> findByUsersId(Long id);
}
