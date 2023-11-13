package firstapp;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

import java.util.Properties;

public class ProducerKafka {
    static String bootstrapServers = "lab9.alumchat.xyz:9092";
    static String tenancyName = "sensor-data-producer";
    private static final String TOPIC = "12345";
    
    private static Properties getKafkaProperties(){
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("client.id", tenancyName);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        return props;
    }

    // ? Para correr el programa segun la documentación:
    // mvn clean install exec:java -Dexec.mainClass=firstapp.ProducerKafka
    public static void main(String args[]) {        
        Producer<String, byte[]> producer = new KafkaProducer<>(getKafkaProperties());

        try {
            while (true) {
                JSONObject data = Sensors.generateJsonData();
                byte[] encodedData = PayloadCodec.encode(data);
                String key = "sensor1";
                producer.send(new ProducerRecord<>(TOPIC, key, encodedData));
                Thread.sleep(Sensors.randomIntNumber(15000, 30000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }    
}