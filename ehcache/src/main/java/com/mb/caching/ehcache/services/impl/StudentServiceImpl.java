package com.mb.caching.ehcache.services.impl;

import com.mb.caching.ehcache.model.Student;
import com.mb.caching.ehcache.repository.StudentRepository;
import com.mb.caching.ehcache.services.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
//@CacheConfig(cacheNames = "studentCache")
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    @Cacheable
    public Student find(Long id) {
        log.info("Find by id student with id {} in database", id);
        return studentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    //@CachePut(key = "#result.id")
    public Student create(Student student) {
        log.info("Creating student with firstName={}, lastName={} and courseOfStudies={}",
                student.getFirstName(),student.getLastName(), student.getCourseOfStudies());
        return studentRepository.save(student);
    }
}
