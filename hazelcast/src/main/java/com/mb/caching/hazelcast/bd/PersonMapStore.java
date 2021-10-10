package com.mb.caching.hazelcast.bd;

import com.hazelcast.map.MapStore;

import java.util.Collection;
import java.util.Map;

/**
 * This is use like a listener when we manipulate cache(map)
 * It is useful when we store data in another database
 */
public class PersonMapStore implements MapStore<Integer,Person> {

    @Override
    public void store(Integer integer, Person person) {
        System.out.println("**************STORE**************************");
        System.out.println(person);

    }

    @Override
    public void storeAll(Map<Integer, Person> map) {
        System.out.println("**************STORE ALL**************************");
        map.forEach(((integer, person) -> System.out.println(person)));

    }

    /**
     * Take this id (integer) and delete object with id in database
     * @param integer
     */
    @Override
    public void delete(Integer integer) {

        System.out.println("**************DELETE**************************");
        System.out.println("key = " + integer);
    }

    @Override
    public void deleteAll(Collection<Integer> collection) {
        System.out.println("**************DELETE ALL**************************");
        collection.forEach(System.out::println);

    }

    /**
     * Take this id (integer) and retrieve object with id in database
     * @param integer
     * @return Person
     */
    @Override
    public Person load(Integer integer) {
        System.out.println("**************LOAD**************************");
        System.out.println("key = " + integer);
        return null;
    }

    @Override
    public Map<Integer, Person> loadAll(Collection<Integer> collection) {
        return null;
    }

    @Override
    public Iterable<Integer> loadAllKeys() {
        return null;
    }
}
