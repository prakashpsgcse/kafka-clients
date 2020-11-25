package com.prakash.kafka.clients.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Prakash
 *
 */
public class BlockingKafkaProducer {
	private static final Logger logger
			= LoggerFactory.getLogger(BlockingKafkaProducer.class);
    public static void main(String args[]) {

		ProducerWithResponse();
    }

    public static void SimpleProducer(){
		logger.debug("Starting kafka producer");
		Properties producerPros = new Properties();
		producerPros.put("bootstrap.servers", "localhost:9092");
		producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerPros.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(producerPros);
		ProducerRecord<String,String> message=new ProducerRecord<>("prakash-kafka-clients","key-from-app","value from app");
		producer.send(message);
		logger.debug("closig producer");
		producer.close();
	}
	public static void ProducerWithResponse(){
		logger.debug("Starting kafka producer");
		Properties producerPros = new Properties();
		producerPros.put("bootstrap.servers", "localhost:9092");
		producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerPros.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(producerPros);
		ProducerRecord<String,String> message=new ProducerRecord<>("prakash-kafka-clients","key-from-app","value from app");
		Future<RecordMetadata> brokerResponse=producer.send(message);

		while(!brokerResponse.isDone()){
			logger.debug("Record is added to batch .Waiting for batch to picked by I/O thread to send and received ack from broker");
		}
		logger.debug("ACK received from broker");
		try {
			System.out.println("Message published to partition : {} and Offset: {} "+brokerResponse.get().offset()+brokerResponse.get().partition());

			logger.debug("Message published to partition : {} and Offset: {} ",brokerResponse.get().offset(),brokerResponse.get().partition());
		} catch (InterruptedException|ExecutionException e) {
			e.printStackTrace();
		}
		logger.debug("closig producer");
		producer.close();
	}
}
