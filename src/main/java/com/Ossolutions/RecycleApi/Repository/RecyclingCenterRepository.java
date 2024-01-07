package com.Ossolutions.RecycleApi.Repository;

import com.Ossolutions.RecycleApi.Model.RecyclingCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecyclingCenterRepository extends JpaRepository<RecyclingCenter, Long> {

    List<RecyclingCenter> findByCenterNameAndLocationAndAcceptedMaterialsIn(String centerName, String location, List<String> materials);

    List<RecyclingCenter> findByCenterNameAndLocation(String centerName, String location);

    List<RecyclingCenter> findByAcceptedMaterialsIn(List<String> materials);

    @Query("SELECT rc FROM RecyclingCenter rc WHERE rc.centerName = :centerName")
    Optional<RecyclingCenter> getRecyclingCenterByName(String centerName);
}
