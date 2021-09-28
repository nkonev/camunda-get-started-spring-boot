package org.camunda.bpm.getstarted.loanapproval;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MortgageApplicationRepository extends CrudRepository<MortgageApplication, UUID> {
    @Query("select * from mortgage_application where user_id = :userId order by created_date_time desc limit 1")
    Optional<MortgageApplication> findLastActual(@Param("userId") UUID userId);

    @Modifying
    @Query("delete from mortgage_application where user_id = :userId and id = :appId")
    void deleteByUserIdAndAppId(@Param("userId") UUID userId, @Param("appId") UUID appId);
}
