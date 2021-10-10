package com.mb.caching.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Map;
import java.util.Random;

public class SimpleMemberHazelcast {

    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        Map<Integer, String> data = hazelcastInstance.getMap("clients");

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
             int key = random.nextInt(1000000);
             data.put(key,"Client" + key);
        }
        data.put(3,"Bedril Moussakat");
        data.put(4,"Betsaleel Illumine");

        System.out.println("Client 3=> " + data.get(3));
        System.out.println("Client 4=> " + data.get(4));
        System.out.println("Size => " + data.size());




    }
}
