package com.mb.caching.ehcache.controller;

import com.mb.caching.ehcache.services.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculationService calculationService;


    @GetMapping(path = "/calculate/areaOfCircle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> areaOfCircle(@RequestParam int radius) {

        double result = calculationService.areaOfCircle(radius);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/calculate/multiply", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> multiply(@RequestParam int factor1, @RequestParam int factor2) {
        double result = calculationService.multiply(factor1,factor2);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/evictCache")
    public void evictCache() {
         calculationService.evictCache();
    }

    // clear all cache using cache manager
    @DeleteMapping(value = "/clearAllCache")
    public void clearAllCache(){
        calculationService.clearAllCache();
    }
}
