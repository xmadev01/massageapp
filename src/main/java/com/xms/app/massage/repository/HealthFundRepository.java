package com.xms.app.massage.repository;

import com.xms.app.massage.model.HealthFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HealthFundRepository extends JpaRepository<HealthFund, Long> {

    @Query("select hf from HealthFund hf where lower(hf.name) = :name and hf.active = true")
    Optional<HealthFund> findByName(@Param("name") String name);

}
