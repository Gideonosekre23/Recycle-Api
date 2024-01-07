package com.Ossolutions.RecycleApi.controller;
import com.Ossolutions.RecycleApi.Model.RecyclingCenter;
import com.Ossolutions.RecycleApi.Service.RecyclingCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recycling-centers")
public class RecyclingCenterController {

    private final RecyclingCenterService recyclingCenterService;

    @Autowired
    public RecyclingCenterController(RecyclingCenterService recyclingCenterService) {
        this.recyclingCenterService = recyclingCenterService;
    }

    @GetMapping("/getallcenter")
    public List<RecyclingCenter> getAllCenters() {
        return recyclingCenterService.getAllCenters();
    }

    @GetMapping("/filter")
    public List<RecyclingCenter> filterCenters(
            @RequestParam(required = false) String centerName,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) List<String> materials
    ) {
        return recyclingCenterService.filterCenters(centerName, location, materials);
    }

    @PostMapping ("/addcenter")
    public RecyclingCenter addNewCenter(@RequestBody RecyclingCenter recyclingCenter) {
        return recyclingCenterService.addNewCenter(recyclingCenter);
    }

    // Other endpoints if needed
}
