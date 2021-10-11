package com.mb.caching.hazelcast.bd;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.util.SetUtil;
import com.hazelcast.map.IMap;
import org.apache.commons.lang3.ArrayUtils;

import java.time.LocalDateTime;
import java.util.*;

public class MemberHazelcast {

    private static final  String CACHE_NAME= "MyCache";

    public static void main(String[] args) {
        final Config config = new Config();
        final MapConfig aDefault = config.getMapConfig("default");
        aDefault.setName(CACHE_NAME)
                .getMapStoreConfig()
                .setEnabled(true)
                .setClassName(PersonMapStore.class.getName())
                .setProperty("binary","false");
        config.addMapConfig(aDefault);

        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        final IMap<Integer, Person> cache = hazelcastInstance.getMap(CACHE_NAME);
        final Person john_peter = new Person(1L, "John Peter", LocalDateTime.now().minusYears(1));
        cache.put(1, john_peter);
        final Person richard_linus = new Person(2L, "Richard Linus", LocalDateTime.now().minusYears(3));
        cache.put(2, richard_linus);

        final Person person = cache.get(1);

        final Map<Integer, Person> map = new HashMap<>();
        map.put(3, new Person(3L,"Micheal ", LocalDateTime.now()));
        map.put(4, new Person(4L,"Stephan", LocalDateTime.now().minusMonths(8)));
        cache.putAll(map);

        cache.remove(1);
        cache.remove(2);
        final Map<Integer, Person> personMap = cache.getAll(new HashSet<>(Arrays.asList(3, 4)));
        personMap.forEach((integer, pers) -> System.out.println("person1 = " + pers));


    }
}
