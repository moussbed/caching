package com.mb.caching.ehcache.services;

public interface CalculationService {

  double areaOfCircle(int radius);

  void evictCache();

  double multiply(int factor1, int factor2);

  void clearAllCache();
}
