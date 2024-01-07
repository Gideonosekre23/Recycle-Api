package com.Ossolutions.RecycleApi.controller;

import com.Ossolutions.RecycleApi.Model.EducationalResource;
import com.Ossolutions.RecycleApi.Service.EducationalResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/educational-resources")
public class EducationalResourceController {

    @Autowired
    private EducationalResourceService educationalResourceService;

    @GetMapping
    public ResponseEntity<List<EducationalResource>> getAllEducationalResources() {
        List<EducationalResource> resources = educationalResourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<EducationalResource>> getResourcesByType(@PathVariable String type) {
        List<EducationalResource> resources = educationalResourceService.getResourcesByType(type);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<EducationalResource>> searchResources(@PathVariable String query) {
        List<EducationalResource> resources = educationalResourceService.searchResources(query);
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<EducationalResource> createEducationalResource(@RequestBody EducationalResource resource) {
        EducationalResource createdResource = educationalResourceService.createResource(resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
    }
}
