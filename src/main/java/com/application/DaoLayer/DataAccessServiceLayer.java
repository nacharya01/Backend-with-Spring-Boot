package com.application.DaoLayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataAccessServiceLayer extends CrudRepository<Student,Integer> {
    public Optional<Student> findByEmail(String email);
    public Optional<Student> findByName(String name);
}
