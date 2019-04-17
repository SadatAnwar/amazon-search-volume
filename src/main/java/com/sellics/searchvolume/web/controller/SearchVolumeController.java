package com.sellics.searchvolume.web.controller;

import com.sellics.searchvolume.service.SearchVolumeEstimationService;
import com.sellics.searchvolume.web.dto.SearchVolumeEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchVolumeController
{
    private final SearchVolumeEstimationService searchVolumeEstimationService;

    @Autowired
    public SearchVolumeController(SearchVolumeEstimationService searchVolumeEstimationService)
    {
        this.searchVolumeEstimationService = searchVolumeEstimationService;
    }

    @GetMapping("/estimate")
    public SearchVolumeEstimate searchVolumeEstimate(@RequestParam(name = "keyword") String keyword)
    {
        return searchVolumeEstimationService.estimateVolume(keyword);
    }
}
