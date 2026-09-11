package com.example.videorental.controller;

import com.example.videorental.model.Visitor;
import com.example.videorental.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService service;

    @GetMapping
    public List<Visitor> getAll(@RequestParam(required = false) Visitor.VisitorStatus status) {
        if (status != null) {
            return service.getVisitorsByStatus(status);
        }
        return service.getAllVisitors();
    }

    @GetMapping("/{id}")
    public Visitor getById(@PathVariable Long id) {
        return service.getVisitorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Visitor create(@RequestBody Visitor visitor) {
        return service.createVisitor(visitor);
    }

    @PutMapping("/{id}")
    public Visitor update(@PathVariable Long id, @RequestBody Visitor visitor) {
        return service.updateVisitor(id, visitor);
    }

    @PatchMapping("/{id}/terminate")
    public Visitor terminate(@PathVariable Long id) {
        return service.terminateVisitor(id);
    }

    @PatchMapping("/{id}/issue-disk")
    public Visitor issueDisk(@PathVariable Long id) {
        return service.issueDisk(id);
    }

    @PatchMapping("/{id}/return-disk")
    public Visitor returnDisk(@PathVariable Long id) {
        return service.returnDisk(id);
    }

    @GetMapping("/report")
    public Map<String, Long> getReport() {
        return service.getReport();
    }
}