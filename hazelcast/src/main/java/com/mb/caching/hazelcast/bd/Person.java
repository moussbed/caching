package com.mb.caching.hazelcast.bd;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Person implements Serializable {

    private Long id;

    private String name;

    private LocalDateTime birthDay;

    public Person() {
    }

    public Person(Long id, String name, LocalDateTime birthDay) {
        this.id = id;
        this.name = name;
        this.birthDay = birthDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDateTime birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("birthDay=" + birthDay)
                .toString();
    }
}
