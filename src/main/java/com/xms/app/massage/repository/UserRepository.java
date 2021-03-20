package com.xms.app.massage.repository;

import com.xms.app.massage.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select u from User u where u.userCode = :userCode and u.active = true")
    Optional<User> findByUserCode(@Param("userCode") String userCode);
}
