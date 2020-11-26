# kafka-clients
kafka producer and consumer sample codes 

## Basic concepts of kafka-clients
 * Very first step for producer/Consumer is to connect to cluster and get MetaData
 * In _bootstrap.server_ accepts LoadBalance/DNS/Broker ips .This will be used for initial connections only .
 * Client will connect to first ip and get others brokers IP's in cluster 
 * When its not able to connect with first DNS/LB/IP then it will try with next one in list [never give 2 cluster's ip/dns : its not HA]
 * Second Step will be get Topic Metadata like who is leader for which partition 
 * Producer/Consumer will send/receive data to partition leader 
 * When there is Leader change kafka-clients will automatically recovers 
 * Retry mechanisim is available 

## Basic Producer Concepts 
* Producer is always Async 
* Producer has Buffer[per partition] so records will be added to buffer first then background I/O thread will send it to topic partition leader 
* When **_Producer.Send()_** is called producer thread will add record to buffer and returns 
* Either you can make your program as Blocking or Non-Blocking[register CallBack]
* When records is sent to broker , it will write its log segment and returns Partition and Offset Info
* For Kafka everything is bytes , So you can send any data to kafka with correct Key/Value Serilizer[converts to byte[]]

### Samle producer code 
   * [Producer code ](src/main/java/com/prakash/kafka/clients/producer/SimleKafkaProducer.java)
   * `SimpleProducer()` for sending data but not waiting for ACK from leader broker
   * `BlockingProducer()` for sending data and it will block current thread for response 
   * `NonBlockingProducerWithCallback()` this will register call back and process next req .Callback is executed when it get response