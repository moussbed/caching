package com.mb.caching.services.impl;

import com.mb.caching.services.CalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final CacheManager cacheManager;

    @Override
    @Cacheable(value = "areaOfCircleCache",key = "#radius", condition = "#radius > 5")
    public double areaOfCircle(int radius) {
        log.info("calculate the area of a circle with a radius of {}", radius);
        return Math.PI * Math.pow(radius, 2);
    }

    @Override
    @Cacheable(value = "multiplyCache", keyGenerator = "multiplyKeyGenerator")
    public double multiply(int factor1, int factor2) {
        log.info("multiply factor1 {} with factor2 {}", factor1,factor2);
        return factor1 * factor2;
    }

    @Override
    @CacheEvict(cacheNames = {"areaOfCircleCache", "multiplyCache"}, allEntries = true)
    public void evictCache() {

    }

    @Override
    public void clearAllCache() {
        for(String name:cacheManager.getCacheNames()){
            Objects.requireNonNull(cacheManager.getCache(name)).clear();            // clear cache by name
        }
    }
}
