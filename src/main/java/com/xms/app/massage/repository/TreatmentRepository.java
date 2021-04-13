package com.xms.app.massage.repository;

import com.xms.app.massage.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query("select t from Treatment t where t.serviceDate between :startDate and :endDate order by t.serviceDate asc")
    List<Treatment> findAllOrderByServiceDateAsc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select t from Treatment t where t.serviceDate between :startDate and :endDate order by t.serviceDate desc")
    List<Treatment> findAllOrderByServiceDateDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select t from Treatment t join t.customer c where t.serviceDate between :startDate and :endDate order by c.firstName asc, c.middleName asc, c.lastName asc")
    List<Treatment> findAllOrderByCustomerNameAsc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select t from Treatment t join t.customer c where t.serviceDate between :startDate and :endDate order by c.firstName desc, c.middleName desc, c.lastName desc")
    List<Treatment> findAllOrderByCustomerNameDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select t from Treatment t join t.practitioner p where t.serviceDate between :startDate and :endDate order by p.firstName asc, p.lastName asc")
    List<Treatment> findAllOrderByPractitionerAsc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select t from Treatment t join t.practitioner p where t.serviceDate between :startDate and :endDate order by p.firstName asc, p.lastName desc")
    List<Treatment> findAllOrderByPractitionerDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select t from Treatment t join t.customer c join c.healthFund h where t.serviceDate between :startDate and :endDate order by h.name asc")
    List<Treatment> findAllOrderByHealthFundAsc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select t from Treatment t join t.customer c join c.healthFund h where t.serviceDate between :startDate and :endDate order by h.name desc")
    List<Treatment> findAllOrderByHealthFundDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}