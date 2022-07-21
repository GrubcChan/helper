package com.example.helper.models;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Вы не ввели имя пользователя!")
    @Size(min=1, max=255, message = "Сообщение слишком большое! (максимум 255 символов)")
    private String tag;


    @NotEmpty(message = "Вы не ввели имя пользователя!")
    @Size(min=1, max=2048, message = "Сообщение слишком большое! (максимум 2048 символов)")
    private String text;

    private Timestamp date_create;

    private boolean active;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="status_id")
    private Status status;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User author;

    public Message() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDate_create() {
        return date_create;
    }

    public void setDate_create(Timestamp date_create) {
        this.date_create = date_create;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
