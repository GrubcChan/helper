package com.example.helper.repos;

import com.example.helper.models.Message;
import com.example.helper.models.Status;
import com.example.helper.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.tag = :tag, m.text = :text, m.date_create = :date_create, m.status = :status, m.author = :author WHERE m.id = :id")
    int updateUser(@Param("tag") String tag, @Param("text") String text, @Param("date_create") Timestamp date_create, @Param("status") Status status, @Param("author") User author, @Param("id") Long id);

//    List<Message> findByTag(String tag);
    List<Message> findByStatus(Status status);

    List<Message> findByTagAndStatus(String tag, Status status);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.status = :status WHERE m.id = :id")
    int updateStatus(@Param("status") Status status, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.active = :active WHERE m.id = :id")
    void setActive(@Param("active") boolean active, @Param("id") Long id);
}
