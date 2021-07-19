package com.mb.caching.services;

import com.mb.caching.model.Student;


public interface StudentService {

   Student find(Long id);

   Student create(Student student);
}
