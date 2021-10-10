package com.mb.caching.ehcache.services;

import com.mb.caching.ehcache.model.Student;


public interface StudentService {

   Student find(Long id);

   Student create(Student student);
}
