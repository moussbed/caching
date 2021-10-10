package com.mb.caching.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.Console;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MemberHazelcast {

    static Logger LOGGER = Logger.getLogger(MemberHazelcast.class.getName());

    public static void main(String[] args) {


        final Config config = new Config();
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        Map<Integer,String> data = hazelcastInstance.getMap("test");

        final Console console = System.console();

        while (true){
            final String entry = console.readLine(">");
            final String[] command = entry.split(" ");
            switch (command[0]){
                case "map" :
                    data = hazelcastInstance.getMap(command[1]);
                    break;
                case "set" :
                    data.put(Integer.valueOf(command[1]),command[2]);
                    break;
                case "get" :
                    LOGGER.log(Level.INFO, " Object" + command[1] +" => " + data.get(Integer.valueOf(command[1])));
                    break;
                case "size" :
                    LOGGER.log(Level.INFO, "Size  =>  " + data.size());
                    break;
            }
            System.out.println();
        }
    }
}
