package com.mb.caching.ehcache.controller;

import com.mb.caching.ehcache.model.Student;
import com.mb.caching.ehcache.repository.StudentRepository;
import com.mb.caching.ehcache.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    private final StudentRepository studentRepository;

    @GetMapping("/{id:.+}")
    public Student findStudent(@PathVariable("id") String id){
          return studentService.find( Long.valueOf(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Student createStudent(@RequestBody Student student){
       return  studentService.create(student);
    }

    @PutMapping( value="/{id:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student updateStudent(@PathVariable("id") String id, @RequestParam String lastName) throws CloneNotSupportedException {
        // If we use this statement we have the change when the search student is does with other thread
        // Student student = studentService.find(Long.valueOf(id));

        // If we use this statement we haven't the change when the search student is does with other thread
        Student student = studentRepository.findById(Long.valueOf(id)).orElseThrow(IllegalArgumentException::new);
        student.setLastName(lastName);
        final Student clone =(Student) student.clone();
        System.out.println(clone);
        return studentRepository.save(student);
    }
}
