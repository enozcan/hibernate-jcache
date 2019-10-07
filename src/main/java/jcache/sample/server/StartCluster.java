package jcache.sample.server;

import com.hazelcast.core.Hazelcast;

public class StartCluster {

    public static void main(String[] args){
        Hazelcast.newHazelcastInstance();
    }
}
