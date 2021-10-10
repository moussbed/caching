package com.mb.caching.ehcache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student  implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @ElementCollection
    private Set<String> courseOfStudies =new TreeSet<>();


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
