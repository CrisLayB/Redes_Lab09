package firstapp;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

public class ConsumerKafka {
    static String bootstrapServers = "lab9.alumchat.xyz:9092";
    static String tenancyName = "sensor-data-producer";
    private static final String TOPIC = "12345";

    private static Properties getKafkaProperties(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "foo2");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        return props;
    }

    // ? Para correr el programa segun la documentación:
    // mvn clean install exec:java -Dexec.mainClass=firstapp.ConsumerKafka -Djava.awt.headless=true
    public static void main(String[] args) {
        Consumer<String, byte[]> consumer = new KafkaConsumer<>(getKafkaProperties());

        consumer.subscribe(Arrays.asList(TOPIC));

        List<Double> listTemp = new ArrayList<>();
        List<Integer> listHume = new ArrayList<>();
        List<String> listWind = new ArrayList<>();

        // Crear el gráfico
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Telemetría en Vivo").xAxisTitle("Muestras").yAxisTitle("Valores").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

        try {
            while (true) {                
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
                
                records.forEach(record -> {
                    byte[] encodedData = record.value();
                    JSONObject json = PayloadCodec.decode(encodedData);

                    listTemp.add(json.getDouble("temperatura"));
                    listHume.add(json.getInt("humedad"));
                    listWind.add(json.getString("direccion_viento"));

                    updateChart(chart, listTemp, listHume, listWind);
                });                                
            }
        } finally {
            consumer.close();
        }
    }

    private static void updateChart(XYChart chart, List<Double> allTemp, List<Integer> allHume, List<String> allWind) {
        chart.removeSeries("Temperatura");
        chart.removeSeries("Humedad");

        chart.addSeries("Temperatura", null, allTemp);
        chart.addSeries("Humedad", null, allHume);

        try {
            BitmapEncoder.saveBitmap(chart, "./telemetry_chart", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}