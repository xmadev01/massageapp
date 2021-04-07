package com.xms.app.massage.repository;

import com.xms.app.massage.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query("select t from Treatment t join t.customer c order by c.firstName asc, c.middleName asc, c.lastName asc")
    List<Treatment> findAllOrderByCustomerNameAsc();

    @Query("select t from Treatment t join t.customer c order by c.firstName desc, c.middleName desc, c.lastName desc")
    List<Treatment> findAllOrderByCustomerNameDesc();

}
