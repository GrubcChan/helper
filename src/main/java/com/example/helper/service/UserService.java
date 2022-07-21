package com.example.helper.service;

import com.example.helper.models.Role;
import com.example.helper.models.User;
import com.example.helper.repos.UserRepo;
import com.example.helper.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    private final TelegramBot telegramBot;

    private final PasswordEncoder passwordEncoder;

    public UserService(TelegramBot telegramBot, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.telegramBot = telegramBot;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findById(Long id) {
        return userRepo.findById(id).get();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }


    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getChat_id())) {
            String message = String.format(
                    "Hello, %s! \n " +
                            "Welcome to HelpDesk. Please? visit newt link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            telegramBot.sendMessage(user.getChat_id(), message);
        }

        return true;
    }

    public void updateUser(Long id, Map<String, String> form, String username, String password, String chat_id) {
        User user = userRepo.findById(id).get();

        String userChatId = user.getChat_id();
        String userPassword = user.getPassword();
        String userUsername = user.getUsername();

        if (!StringUtils.isEmpty(password) && !password.equals(userPassword)){
            user.setPassword(passwordEncoder.encode(password));
        }

        if (!StringUtils.isEmpty(username) && !username.equals(userUsername)){
            user.setUsername(username);
        }

        boolean isChatIdChanged = ((chat_id != null && !chat_id.equals(userChatId)) || (userChatId != null && !userChatId.equals(chat_id)));

        if(isChatIdChanged){
            user.setChat_id(chat_id);
            if (!StringUtils.isEmpty(chat_id)){
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                System.out.println(key);
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);

        if (isChatIdChanged) {
            String message = String.format(
                    "Hello, %s! \n " +
                            "Welcome to HelpDesk. Please? visit newt link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            telegramBot.sendMessage(user.getChat_id(), message);
        }
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepo.save(user);

        return true;
    }

    public void updatePersonalArea(User user, String username, String password, String chat_id) {
        String userChatId = user.getChat_id();
        String userPassword = user.getPassword();
        String userUsername = user.getUsername();

        boolean isChatIdChanged = ((chat_id != null && !chat_id.equals(userChatId)) || (userChatId != null && !userChatId.equals(chat_id)));

        if(isChatIdChanged){
            user.setChat_id(chat_id);
            if (!StringUtils.isEmpty(chat_id)){
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!StringUtils.isEmpty(password) && !password.equals(userPassword)){
            user.setPassword(passwordEncoder.encode(password));
        }

        if (!StringUtils.isEmpty(username) && !username.equals(userUsername)){
            user.setUsername(username);
        }

        userRepo.save(user);

        if (isChatIdChanged) {
            String message = String.format(
                    "Hello, %s! \n " +
                            "Welcome to HelpDesk. Please? visit newt link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            telegramBot.sendMessage(user.getChat_id(), message);
        }
    }

//    public List<User> findByRole(Role role);
}
