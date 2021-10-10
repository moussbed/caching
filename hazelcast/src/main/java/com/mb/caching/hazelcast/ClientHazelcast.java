package com.mb.caching.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHazelcast {

    static Logger LOGGER = Logger.getLogger(MemberHazelcast.class.getName());

    public static void main(String[] args) {
        final ClientConfig clientConfig = new ClientConfig();
        final HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        final IMap<Integer, String> clients = hazelcastInstance.getMap("clients");

        LOGGER.log(Level.INFO," Client 4 => "+ clients.get(4));
        LOGGER.log(Level.INFO," Map size => " + clients.size() );

    }
}
