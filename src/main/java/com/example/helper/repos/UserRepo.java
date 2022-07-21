package com.example.helper.repos;

import com.example.helper.models.Role;
import com.example.helper.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
