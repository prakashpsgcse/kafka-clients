package com.prakash.kafka.clients.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class StorePartitioner implements Partitioner {
    private static final Logger logger
            = LoggerFactory.getLogger(StorePartitioner.class);
    private String storeNumerSeperator;
    @Override
    public int partition(String topicName, Object key, byte[] keyInBytes, Object message, byte[] messageInBytes, Cluster cluster) {
       logger.debug("Cluster MetaData constructed: {}",cluster);
        if(null!=key && key instanceof String){
           String messagekey=(String) key;
            String storePartition=messagekey.split(storeNumerSeperator)[0];
            logger.debug("key: {} Partition number: {} ",messagekey,storePartition);
            return Integer.parseInt(storePartition);
        }
        return 0;
    }

    @Override
    public void close() {
logger.debug("Closing custom StorePartitioner  ");
    }

    @Override
    public void configure(Map<String, ?> map) {
        logger.debug("Configuring custom StorePartitioner  ");
        storeNumerSeperator=map.get("store.partition.seperator").toString();
    }
}
