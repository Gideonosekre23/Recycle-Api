package com.Ossolutions.RecycleApi.Repository;

import com.Ossolutions.RecycleApi.Model.EducationalResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// EducationalResourceRepository.java
public interface EducationalResourceRepository extends JpaRepository<EducationalResource, Long> {
    List<EducationalResource> findByTypeContainingIgnoreCase(String type);
    List<EducationalResource> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}
