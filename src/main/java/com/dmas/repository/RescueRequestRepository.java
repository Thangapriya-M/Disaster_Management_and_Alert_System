package com.dmas.repository;

import com.dmas.model.RescueRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RescueRequestRepository extends JpaRepository<RescueRequest, Long> {
    List<RescueRequest> findAllByOrderBySubmittedAtDesc();
    List<RescueRequest> findByStatusOrderBySubmittedAtDesc(RescueRequest.RequestStatus status);
    List<RescueRequest> findByCitizenIdOrderBySubmittedAtDesc(Long citizenId);
    List<RescueRequest> findByAssignedResponderIdOrderBySubmittedAtDesc(Long responderId);
}
