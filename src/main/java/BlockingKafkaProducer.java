import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

/**
 * 
 */

/**
 * @author Prakash
 *
 */
public class BlockingKafkaProducer {
    public static void main(String args[]) {
	System.out.println("Starting kafka producer");
	Properties producerPros = new Properties();
	Producer kafkaProducer = new KafkaProducer(producerPros);
    }
}
