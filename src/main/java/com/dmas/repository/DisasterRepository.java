package com.dmas.repository;

import com.dmas.model.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DisasterRepository extends JpaRepository<Disaster, Long> {
    List<Disaster> findByStatusOrderByCreatedAtDesc(Disaster.Status status);
    List<Disaster> findAllByOrderByCreatedAtDesc();
    List<Disaster> findByCategoryOrderByCreatedAtDesc(Disaster.Category category);
}
