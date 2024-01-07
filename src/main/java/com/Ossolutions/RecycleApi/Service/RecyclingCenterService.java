package com.Ossolutions.RecycleApi.Service;

import com.Ossolutions.RecycleApi.Model.RecyclingCenter;
import com.Ossolutions.RecycleApi.Repository.RecyclingCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecyclingCenterService {

    private final RecyclingCenterRepository recyclingCenterRepository;

    @Autowired
    public RecyclingCenterService(RecyclingCenterRepository recyclingCenterRepository) {
        this.recyclingCenterRepository = recyclingCenterRepository;
    }

    public List<RecyclingCenter> getAllCenters() {
        return recyclingCenterRepository.findAll();
    }

    public List<RecyclingCenter> filterCenters(String centerName, String location, List<String> materials) {
        if (centerName != null && location != null && materials != null) {
            // Filter by all three parameters
            return recyclingCenterRepository.findByCenterNameAndLocationAndAcceptedMaterialsIn(centerName, location, materials);
        } else if (centerName != null && location != null) {
            // Filter by centerName and location
            return recyclingCenterRepository.findByCenterNameAndLocation(centerName, location);
        } else if (materials != null) {
            // Filter by accepted materials
            return recyclingCenterRepository.findByAcceptedMaterialsIn(materials);
        } else {
            // No filtering, return all centers
            return getAllCenters();
        }
    }

    public RecyclingCenter addNewCenter(RecyclingCenter recyclingCenter) {
        // Check if the current user is an admin
        if (isAdmin()) {
            // Implement logic to add a new recycling center
            // You may set additional properties or perform validation before saving
            return recyclingCenterRepository.save(recyclingCenter);
        } else {
            // Handle the case where the user is not authorized to add a new center
            throw new RuntimeException("Only administrators can add new recycling centers.");
        }
    }

    private boolean isAdmin() {
        // Get the current authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            // Check if the user has the "admin" role
            return ((UserDetails) principal).getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("admin"));
        }

        return false;
    }




    public Optional<RecyclingCenter> getRecyclingCenterByName(String centerName) {
        return recyclingCenterRepository.getRecyclingCenterByName(centerName);
    }
}
