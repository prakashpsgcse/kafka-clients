# kafka-clients
Kafka clients basic concepts .Producer/Consumer sample codes 

## Basic concepts of kafka-clients
 * Very first step for producer/Consumer is connect to Cluster and get MetaData(broker Id,IP etc).
 * **_bootstrap.server_** config accepts LoadBalance/DNS/Broker IP's .This will be used for initial connections only.
 * Client connects to first DNS/LB/IP and get brokers IP's in the cluster. 
 * Failed to connect with first DNS/LB/IP then it picks next one in list [never give 2 cluster's ip/dns : its not HA].
 * Second step is to get Topic Metadata (leader for partition etc).
 * Producer/Consumer always send/receive data to/from partition leader. 
 * When there is Leader change[due to broker failure/reassignment] kafka-clients automatically recovers.
 * Retry is available for failure. 

## Basic Producer Concepts 
* Producer is always Async 
* Producer has Buffer[per partition] so records will be added to buffer first then background I/O thread will send it to topic partition leader 
* When **_Producer.Send()_** is called producer thread adds record to buffer and returns 
* Either you can make your program as Blocking or Non-Blocking[register CallBack]
* When records sent to broker , Broker return Offset and partition Info after writing in log segments. 
* For Kafka everything is bytes , So you can send any data to kafka with correct Key/Value Serializer[converts to byte[]]

### Samle producer code 
   *  [Producer code ](src/main/java/com/prakash/kafka/clients/producer/SimpleKafkaProducer.java)
   * **`SimpleProducer()`** for sending data but not waiting for ACK from leader broker
   * **`BlockingProducer()`** for sending data and it will block current thread for response 
   * **`NonBlockingProducerWithCallback()`** this will register call back and process next req .Callback is executed when it get response
   
## Partitioner 
 * When partition is provided use it 
 * When key is provided use hash to get partition [key with same hash will be sent to same partition]
 * Nothing then use Default partitioner [Round robin]
 
## Custom Partitioner
 * We can have custom partitioner to implement our logic 
 * Lets say we want to send messages from Washington DC stores to partition 1 .Key format "partition-location"
 * StorePartitioner trying to get partition from key .ex: 0-DC -> 0 partition DC->location 
 * [StorePartitioner](src/main/java/com/prakash/kafka/clients/producer/StorePartitioner.java)
 
## Important/Useful Producer Configs
 * 