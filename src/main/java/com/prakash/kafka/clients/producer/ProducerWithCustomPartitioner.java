package com.prakash.kafka.clients.producer;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Prakash
 *
 */
public class ProducerWithCustomPartitioner {
	private static final Logger logger
			= LoggerFactory.getLogger(ProducerWithCustomPartitioner.class);
    public static void main(String args[]) {
		BlockingProducer();
    }


	public static void BlockingProducer(){
		logger.info("Starting kafka producer");
		Properties producerPros = new Properties();
		producerPros.put("bootstrap.servers", "localhost:9092");
		producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerPros.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerPros.put("partitioner.class", "com.prakash.kafka.clients.producer.StorePartitioner");
		producerPros.put("store.partition.seperator", "-");
		Producer<String, String> producer = new KafkaProducer<>(producerPros);
		ProducerRecord<String,String> message=new ProducerRecord<>("prakash-kafka-clients","0-DC","DC store data");
		Future<RecordMetadata> brokerResponse=producer.send(message);

		while(!brokerResponse.isDone()){
			logger.debug("Record is added to batch .Waiting for batch to picked by I/O thread to send and received ack from broker");
		}
		logger.info("ACK received from broker");
		try {
			logger.info("Message published to partition : {} and Offset: {} ",brokerResponse.get().offset(),brokerResponse.get().partition());
		} catch (InterruptedException|ExecutionException e) {
			logger.error("Not able to publish message to kafka {}",e.getMessage());
		}
		logger.debug("closig producer");
		producer.close();
	}


}
