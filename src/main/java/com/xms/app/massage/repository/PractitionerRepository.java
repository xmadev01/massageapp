package com.xms.app.massage.repository;

import com.xms.app.massage.model.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {

    @Query("select p from Practitioner p where p.firstName = :firstName and p.lastName = :lastName and p.active = true")
    Optional<Practitioner> findByFirstNameLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
