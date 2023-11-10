package firstapp;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class Consumer {
    static String bootstrapServers = "<bootstrap_servers_endpoint>"; // usually of the form cell-1.streaming.<region>.oci.oraclecloud.com:9092 ;
    static String tenancyName = "<OCI_tenancy_name>";
    static String username = "<your_OCI_username>";
    static String streamPoolId = "<stream_pool_OCID>";
    static String authToken = "<your_OCI_user_auth_token>"; // from step 8 of Prerequisites section
    static String streamOrKafkaTopicName = "<topic_stream_name>"; // from step 2 of Prerequisites section
    static String consumerGroupName = "<consumer_group_name>"; 

    private static Properties getKafkaProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", consumerGroupName);
        props.put("enable.auto.commit", "false");
        props.put("session.timeout.ms", "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        props.put("auto.offset.reset", "earliest");
        final String value = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                + tenancyName + "/"
                + username + "/"
                + streamPoolId + "\" "
                + "password=\""
                + authToken + "\";";
        props.put("sasl.jaas.config", value);
        return props;
    }

    // ? Para correr el programa segun la documentación:
    // mvn clean install exec:java -Dexec.mainClass=kafka.sdk.oss.example.Consumer
    public static void main(String[] args) {
        final KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(getKafkaProperties());;
        consumer.subscribe(Collections.singletonList(streamOrKafkaTopicName));
        ConsumerRecords<Integer, String> records = consumer.poll(10000);

        System.out.println("size of records polled is "+ records.count());
        for (ConsumerRecord<Integer, String> record : records) {
            System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
        }

        consumer.commitSync();
        consumer.close();
    }
}