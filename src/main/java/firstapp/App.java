package firstapp;

import org.json.JSONArray;

/**
 * Universidad del Valle de Guatemala
 * Facultad de Ingeniería
 * Departamento de Ciencias de la computación
 *
 * Redes - Sección 10
 * 
 * Cristian Fernando Laynez Bachez - 201281
 * Mario David de Leon Muralles - 19019
 */

/**
 * Referencias:
 * https://docs.oracle.com/es-ww/iaas/Content/Streaming/Tasks/streaming-kafka-java-client-quickstart.htm
 */

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static List<JSONObject> generateData(int intervals){
        List<JSONObject> jsonList = new ArrayList<>();

        for(int i = 0; i < intervals; i++){
            JSONObject json = new JSONObject();
            json.put("temperatura", Sensors.thermometer());
            json.put("humedad", Sensors.hygrometer());
            json.put("direccion_viento", Sensors.windDirection());
            jsonList.add(json);
        }
        
        return jsonList;
    }

    private static String saveListoToFileJson(List<JSONObject> jsonList, String fileName){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            JSONArray jsonArray = new JSONArray(jsonList);
            writer.write(jsonArray.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "The data is saved in the list";
    }
    
    public static void main( String[] args ) {
        // Create a loop for Generate Data random
        List<JSONObject> jsonList = generateData(20);
        String result = saveListoToFileJson(jsonList, "./sensorsOutput.json");
        System.out.println(result);
    }
}
