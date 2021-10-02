package com.xms.app.massage.repository;

import com.xms.app.massage.enums.ServiceTypeEnum;
import com.xms.app.massage.model.Customer;
import com.xms.app.massage.model.HealthFund;
import com.xms.app.massage.model.Practitioner;
import com.xms.app.massage.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query("select t from Treatment t where t.id in :treatmentIds and t.active = true")
    List<Treatment> findAllByTreatmentIds(@Param("treatmentIds") List<Long> treatmentIds);

    @Query("select distinct c from Treatment t join t.customer c join c.healthFund hf join t.item itm where t.serviceDate between :sDate and :sDate and t.practitioner = :practitioner " +
            "and hf = :healthFund and itm.type = :type")
    List<Customer> findAllCustomers(@Param("sDate") LocalDate sDate, @Param("practitioner") Practitioner practitioner,
                                    @Param("healthFund") HealthFund healthFund, @Param("type") ServiceTypeEnum type);

    @Query("select distinct c from Treatment t join t.customer c join t.item itm where t.serviceDate between :sDate and :sDate and t.practitioner = :practitioner " +
            "and itm.type = :type")
    List<Customer> findAllCustomers(@Param("sDate") LocalDate sDate, @Param("practitioner") Practitioner practitioner, @Param("type") ServiceTypeEnum type);

    @Query("select distinct t.practitioner from Treatment t where t.serviceDate between :sDate and :sDate")
    List<Practitioner> findAllProviders(@Param("sDate") LocalDate sDate);

    @Query("select distinct hf from Treatment t join t.customer c join c.healthFund hf where t.serviceDate between :sDate and :sDate")
    List<HealthFund> findAllHealthFunds(@Param("sDate") LocalDate sDate);

    @Query("select sum(t.expenseAmt) from Treatment t join t.customer c join c.healthFund hf join t.item itm where t.serviceDate between :sDate and :sDate and t.practitioner = :practitioner " +
            "and hf = :healthFund and itm.type = :type")
    BigDecimal getExpenseAmtSum(@Param("sDate") LocalDate sDate, @Param("practitioner") Practitioner practitioner,
                                @Param("healthFund") HealthFund healthFund, @Param("type") ServiceTypeEnum type);

    @Query("select sum(t.expenseAmt) from Treatment t join t.customer c join t.item itm where t.serviceDate between :sDate and :sDate and t.practitioner = :practitioner " +
            "and itm.type = :type")
    BigDecimal getExpenseAmtSum(@Param("sDate") LocalDate sDate, @Param("practitioner") Practitioner practitioner,
                                @Param("type") ServiceTypeEnum type);

    @Query("select sum(t.claimedAmt) from Treatment t join t.customer c join c.healthFund hf join t.item itm where t.serviceDate between :sDate and :sDate and t.practitioner = :practitioner " +
            "and hf = :healthFund and itm.type = :type")
    BigDecimal getClaimedAmtSum(@Param("sDate") LocalDate sDate, @Param("practitioner") Practitioner practitioner,
                                @Param("healthFund") HealthFund healthFund, @Param("type") ServiceTypeEnum type);

    @Query("select sum(t.claimedAmt) from Treatment t join t.customer c join t.item itm where t.serviceDate between :sDate and :sDate and t.practitioner = :practitioner " +
            "and itm.type = :type")
    BigDecimal getClaimedAmtSum(@Param("sDate") LocalDate sDate, @Param("practitioner") Practitioner practitioner,
                                @Param("type") ServiceTypeEnum type);

}