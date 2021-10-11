package com.mb.caching.hazelcast.computing;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.cluster.Member;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.map.IMap;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// It can to be used in a Razberry which doesn't have the memory to calculate
// The idea is not to do a quick calculation
public class TaskProducer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient();
        final IMap<Integer, Integer> inputMap = hazelcastInstance.getMap("inputMap");

        // Producer: Generate Data in cache
        for (int i=0; i<=20000; i++){
            inputMap.put(i,i);
        }

        // Reducer: Execute distributed computing
        final IExecutorService executorService = hazelcastInstance.getExecutorService("default");
        final Map<Member, Future<Integer>> memberWorkers = executorService.submitToAllMembers(new SumTask());

        int reduceSum=0;
        final long start = System.currentTimeMillis();
        for (Member member : memberWorkers.keySet()) {
            System.out.println("--------------------------------------------");
            System.out.println("member Address => " + member.getAddress());
            final Integer memberSum = memberWorkers.get(member).get();
            System.out.println("member sum = " + memberSum);
            reduceSum+=memberSum;
            System.out.println("--------------------------------------------");
        }
        final long end = System.currentTimeMillis();
        System.out.println("Total sum = " + reduceSum);
        System.out.println("Calculation time = " + (end-start));


    }
}
