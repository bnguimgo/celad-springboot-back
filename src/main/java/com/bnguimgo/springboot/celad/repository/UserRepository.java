package com.bnguimgo.springboot.celad.repository;

import com.bnguimgo.springboot.celad.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

  Set<User> findByFirstNameIgnoreCaseOrLastNameIgnoreCase(String firstName, String lastName);
}
