package com.mb.caching.ehcache.repository;

import com.mb.caching.ehcache.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
