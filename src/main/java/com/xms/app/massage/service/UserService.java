package com.xms.app.massage.service;

import com.xms.app.massage.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);
    Optional<User> findByUserCode(String userCode);
    void saveUser(final User user);
}
