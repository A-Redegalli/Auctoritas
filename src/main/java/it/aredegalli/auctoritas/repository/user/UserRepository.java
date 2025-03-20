package it.aredegalli.auctoritas.repository.user;

import it.aredegalli.auctoritas.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}