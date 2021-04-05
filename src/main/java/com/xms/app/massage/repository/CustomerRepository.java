package com.xms.app.massage.repository;

import com.xms.app.massage.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select cust from Customer cust where cust.firstName = :firstName and cust.middleName = :middleName and cust.lastName = :lastName and cust.active = true")
    Optional<Customer> findByFirstNameLastName(@Param("firstName") String firstName, @Param("middleName") String middleName, @Param("lastName") String lastName);
}
