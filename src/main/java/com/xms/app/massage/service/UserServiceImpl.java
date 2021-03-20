package com.xms.app.massage.service;

import com.xms.app.massage.model.User;
import com.xms.app.massage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUserCode(String userCode) {
        return userRepository.findByUserCode(userCode);
    }

    @Override
    public void saveUser(final User user) {
        userRepository.save(user);
    }
}
