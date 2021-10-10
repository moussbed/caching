package com.mb.caching.hazelcast.computing;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.map.IMap;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class SumTask implements Callable<Integer>, Serializable, HazelcastInstanceAware {

    private transient HazelcastInstance hazelcastInstance;

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
          this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Integer call() throws Exception {
        final IMap<Integer, Integer> input = hazelcastInstance.getMap("inputMap");

        return input.localKeySet()
                .stream()
                .peek(i -> System.out.println("Calculating for key i => " + i))
                .reduce(0, Integer::sum);
    }
}
