package com.Ossolutions.RecycleApi.Repository;

import com.Ossolutions.RecycleApi.Model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByNameIn(List<String> materialNames);
}
