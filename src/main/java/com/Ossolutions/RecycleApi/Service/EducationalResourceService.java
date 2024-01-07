package com.Ossolutions.RecycleApi.Service;

import com.Ossolutions.RecycleApi.Model.EducationalResource;
import com.Ossolutions.RecycleApi.Repository.EducationalResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// EducationalResourceService.java
@Service
public class EducationalResourceService {

    @Autowired
    private EducationalResourceRepository educationalResourceRepository;

    public List<EducationalResource> getAllResources() {
        return educationalResourceRepository.findAll();
    }

    public List<EducationalResource> getResourcesByType(String type) {
        return educationalResourceRepository.findByTypeContainingIgnoreCase(type);
    }

    public List<EducationalResource> searchResources(String query) {
        return educationalResourceRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
    }

    public EducationalResource createResource(EducationalResource resource) {
        return educationalResourceRepository.save(resource);
    }
}
