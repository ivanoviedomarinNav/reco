package com.ucr.reco.service;

import com.ucr.reco.model.User;
import com.ucr.reco.model.dto.UserDTO;
import com.ucr.reco.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserJpaRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User add(UserDTO user) {
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getRole() == null) {
            return null;
        }
        if (repository.existsByEmail(user.getEmail())) {
            return null;
        }
        User userTemporal = new User();
        userTemporal.setName(user.getName());
        userTemporal.setEmail(user.getEmail());
        userTemporal.setPassword(user.getPassword());
        userTemporal.setRole(user.getRole());
        return repository.save(userTemporal);
    }

    public User getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public User update(UserDTO user) {
        User userExits = repository.getByEmail(user.getEmail());
        if (userExits != null) {
            if (user.getName() != null) {
                userExits.setName(user.getName());
            }
            if (user.getPassword() != null) {
                userExits.setPassword(user.getPassword());
            }
            if (user.getRole() != null) {
                userExits.setRole(user.getRole());
            }

        } else {
            return null;
        }
        return repository.save(userExits);
    }

    public User delete(Integer id) {
        User userExits = repository.findById(id).orElse(null);
        if (userExits != null) {
            repository.deleteById(id);
            return userExits;
        }
        return null;
    }

    public User changePassword(String email, String newPassword) {
        User userExits = repository.getByEmail(email);
        if (userExits != null) {
            userExits.setPassword(newPassword);
            return repository.save(userExits);
        }
        return null;
    }
}
