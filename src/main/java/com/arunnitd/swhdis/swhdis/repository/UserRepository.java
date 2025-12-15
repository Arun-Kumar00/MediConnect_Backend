package com.arunnitd.swhdis.swhdis.repository;

// package com.medical.woundhealing.repository;

// import com.medical.woundhealing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.Role role);

    List<User> findByIsActive(Boolean isActive);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
