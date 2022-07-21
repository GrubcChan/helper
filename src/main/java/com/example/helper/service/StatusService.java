package com.example.helper.service;

import com.example.helper.models.Status;
import com.example.helper.repos.StatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private final StatusRepo statusRepo;

    public StatusService(StatusRepo statusRepo) {
        this.statusRepo = statusRepo;
    }

    public List<Status> findAll() {
        return statusRepo.findAll();
    }

    public Status findById(Long id) {
        return statusRepo.findById(id).get();
    }

    public void addStatus(String name, Long count_day, Long count_hour, Long count_minute, Long count_cec) {
        Status status = new Status();
        status.setName(name);

        Long one_day = 1000L * 60L * 60L * 24L;
        Long one_hour = 1000L * 60L * 60L;
        Long one_minute = 1000L * 60L;
        Long one_cec = 1000L;

        status.setReaction_time((one_day * count_day) + (one_hour * count_hour) + (one_minute * count_minute) + (one_cec * count_cec));
        statusRepo.save(status);
    }

    public void updateStatus(Long id, String name, Long count_day, Long count_hour, Long count_minute, Long count_cec) {
        Long one_day = 1000L * 60L * 60L * 24L;
        Long one_hour = 1000L * 60L * 60L;
        Long one_minute = 1000L * 60L;
        Long one_cec = 1000L;
        long l = (one_day * count_day) + (one_hour * count_hour) + (one_minute * count_minute) + (one_cec * count_cec);
        int i = statusRepo.updateStatus(name, l, id);
    }

    public void deleteById(Long id) {
        statusRepo.deleteById(id);
    }

    public Status findByName(String status) {
        return statusRepo.findByName(status);
    }
}
