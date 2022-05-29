package com.application.DaoLayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoAccess extends JpaRepository<Photo,Integer> {
}
