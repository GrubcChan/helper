package com.example.helper.repos;

import com.example.helper.models.Status;
import com.example.helper.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Repository
public interface StatusRepo extends JpaRepository<Status, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Status s SET s.name = :name, s.reaction_time = :reaction_time WHERE s.id = :id")
    int updateStatus(@Param("name") String name, @Param("reaction_time") Long reaction_time, @Param("id") Long id);

    Status findByName(String name);
}
