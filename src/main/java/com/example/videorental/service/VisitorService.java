package com.example.videorental.service;

import com.example.videorental.exception.VisitorNotFoundException;
import com.example.videorental.model.Visitor;
import com.example.videorental.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.example.videorental.exception.VisitorNotFoundException;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository repository;

    @Transactional(readOnly = true)
    public List<Visitor> getAllVisitors() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Visitor> getVisitorsByStatus(Visitor.VisitorStatus status) {
        return repository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Visitor getVisitorById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new VisitorNotFoundException(id));
    }

    @Transactional
    public Visitor createVisitor(Visitor visitor) {
        if (repository.findAll().stream().anyMatch(v -> v.getPhone().equals(visitor.getPhone()))) {
            throw new RuntimeException("Phone already exists");
        }
        visitor.setStatus(Visitor.VisitorStatus.ACTIVE);
        visitor.setHasDisk(false);
        return repository.save(visitor);
    }

    @Transactional
    public Visitor updateVisitor(Long id, Visitor updated) {
        Visitor existing = getVisitorById(id);
        existing.setFullName(updated.getFullName());
        existing.setPhone(updated.getPhone());
        return repository.save(existing);
    }

    @Transactional
    public Visitor terminateVisitor(Long id) {
        Visitor visitor = getVisitorById(id);
        if (visitor.getStatus() == Visitor.VisitorStatus.INACTIVE) {
            throw new RuntimeException("Visitor is already inactive");
        }
        if (Boolean.TRUE.equals(visitor.getHasDisk())) {
            throw new RuntimeException("Cannot terminate: visitor still holds a disk");
        }
        visitor.setStatus(Visitor.VisitorStatus.INACTIVE);
        return repository.save(visitor);
    }

    @Transactional
    public Visitor issueDisk(Long id) {
        Visitor visitor = getVisitorById(id);
        if (visitor.getStatus() == Visitor.VisitorStatus.INACTIVE) {
            throw new RuntimeException("Cannot issue disk: visitor is inactive");
        }
        if (Boolean.TRUE.equals(visitor.getHasDisk())) {
            throw new RuntimeException("Visitor already holds a disk");
        }
        visitor.setHasDisk(true);
        return repository.save(visitor);
    }

    @Transactional
    public Visitor returnDisk(Long id) {
        Visitor visitor = getVisitorById(id);
        if (!Boolean.TRUE.equals(visitor.getHasDisk())) {
            throw new RuntimeException("Visitor does not hold a disk");
        }
        visitor.setHasDisk(false);
        return repository.save(visitor);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getReport() {
        Map<String, Long> report = new LinkedHashMap<>();
        report.put("withDisk", repository.countByHasDiskTrue());
        report.put("totalRegistered", repository.count());
        return report;
    }

    @Transactional
    public void deleteVisitor(Long id) {
        Visitor visitor = getVisitorById(id);
        if (Boolean.TRUE.equals(visitor.getHasDisk())) {
            throw new RuntimeException("Cannot delete: visitor still holds a disk");
        }
        repository.delete(visitor);
    }
}