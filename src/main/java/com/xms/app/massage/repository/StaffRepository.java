package com.xms.app.massage.repository;

import com.xms.app.massage.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("select staff from Staff staff where staff.firstName = :firstName and staff.lastName = :lastName and staff.active = true")
    Optional<Staff> findByFirstNameLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
