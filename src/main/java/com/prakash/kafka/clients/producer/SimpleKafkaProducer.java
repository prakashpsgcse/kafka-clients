package com.prakash.kafka.clients.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Prakash
 *
 */
public class SimpleKafkaProducer {
	private static final Logger logger
			= LoggerFactory.getLogger(SimpleKafkaProducer.class);
    public static void main(String args[]) {
		//SimpleProducer();
		BlockingProducer();
		//NonBlockingProducerWithCallback();
    }

    public static void SimpleProducer(){
		logger.info("Starting kafka producer");
		Properties producerPros = new Properties();
		producerPros.put("bootstrap.servers", "192.168.43.126:9092");
		producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerPros.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(producerPros);
		ProducerRecord<String,String> message=new ProducerRecord<>("prakash-kafka-clients","key-from-app","value from app");
		producer.send(message);
		logger.info("closig producer");
		producer.close();
	}
	public static void BlockingProducer(){
		logger.info("Starting kafka producer");
		Properties producerPros = new Properties();
		producerPros.put("bootstrap.servers", "192.168.43.126:9092");
		producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerPros.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(producerPros);
		ProducerRecord<String,String> message=new ProducerRecord<>("prakash-kafka-clients","key-from-app","value from app");
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

	public static void NonBlockingProducerWithCallback(){
		logger.info("Starting kafka producer");
		Properties producerPros = new Properties();
		producerPros.put("bootstrap.servers", "192.168.43.126:9092");
		producerPros.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerPros.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(producerPros);
		ProducerRecord<String,String> message=new ProducerRecord<>("prakash-kafka-clients","key-from-app","value from app");
		Future<RecordMetadata> brokerResponse=producer.send(message, new Callback() {
			{
				System.out.println("Instance init block : Call back registered");
			}
			@Override
			public void onCompletion(RecordMetadata recordMetadata, Exception e) {
				Logger logger
						= LoggerFactory.getLogger(SimpleKafkaProducer.class);
				if(null==e) {
					logger.info("Callback: Received ACK from broker");
					logger.info("Callback: Message published to partition : {} and Offset: {} ", recordMetadata.offset(), recordMetadata.partition());
				}else{
					logger.error("Callback: Not able to publish message to kafka {}",e.getMessage());
				}
			}
		});

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
