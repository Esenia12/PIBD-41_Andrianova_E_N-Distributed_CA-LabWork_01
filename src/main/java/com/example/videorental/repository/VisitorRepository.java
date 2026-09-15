package com.example.videorental.repository;

import com.example.videorental.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    List<Visitor> findByStatus(Visitor.VisitorStatus status);
    
    long countByHasDiskTrue();
}