package com.example.helper.service;

import com.example.helper.dao.MessageDAO;
import com.example.helper.models.Message;
import com.example.helper.models.Status;
import com.example.helper.models.User;
import com.example.helper.repos.MessageRepo;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class MessageService {
    private final MessageRepo messageRepo;

    private final MessageDAO messageDAO;


    public MessageService(MessageRepo messageRepo, MessageDAO messageDAO) {
        this.messageRepo = messageRepo;
        this.messageDAO = messageDAO;
    }

    public Iterable<Message> index(String filter, String str_status, Status status) {
        List<Message> messages;

        boolean all = status != null && !str_status.isEmpty() && !str_status.equals("all");
        if (filter != null && !filter.isEmpty()) {
            if (all) {
                messages = messageDAO.searchByStatus(filter, status);
            } else {
                messages = messageDAO.search(filter);
            }
        } else {
            if (all) {
                messages = messageRepo.findByStatus(status);
            } else {
                messages = messageRepo.findAll();
            }
        }
        return messages;
    }

    public Message findById(Long id) {
        return messageRepo.findById(id).get();
    }

    public void deleteById(Long id) {
        messageRepo.deleteById(id);
    }

    public boolean updateMessage(String tag, String text, Status status, User user, Long id) {
        Message message = messageRepo.findById(id).get();

        int count_row = messageRepo.updateUser(tag, text, new Timestamp(System.currentTimeMillis()), status, user, id);

        messageDAO.add_tsv_text(id);

        return Objects.equals(message.getStatus().getId(), status.getId());
    }

    public void addMessage(String tag, String text, Status status, User user) {
        Message message = new Message();

        message.setTag(tag);
        message.setText(text);
        message.setDate_create(new Timestamp(System.currentTimeMillis()));
        message.setStatus(status);
        message.setActive(true);
        message.setAuthor(user);

        messageRepo.save(message);

        messageDAO.add_tsv_text_all();
    }

    public boolean updateStatus(Status status, Long id) {
        Message message = messageRepo.findById(id).get();

        int count_row = messageRepo.updateStatus(status, id);

        return Objects.equals(message.getStatus().getId(), status.getId());
    }

    public List<Message> findAll() {
        return messageRepo.findAll();
    }

    public void setActive(boolean b, Long id) {
        messageRepo.setActive(b, id);
    }
}
