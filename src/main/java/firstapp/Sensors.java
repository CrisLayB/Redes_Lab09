package firstapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

public class Sensors {
    private static Random random = new Random();
    
    public static List<JSONObject> generateJsonListData(int intervals){
        List<JSONObject> jsonList = new ArrayList<>();

        for(int i = 0; i < intervals; i++){
            JSONObject json = generateJsonData();
            jsonList.add(json);
        }
        
        return jsonList;
    }

    public static JSONObject generateJsonData(){
        JSONObject json = new JSONObject();
        json.put("temperatura", thermometer());
        json.put("humedad", hygrometer());
        json.put("direccion_viento", windDirection());
        return json;
    }

    public static int randomIntNumber(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }

    private static double thermometer(){
        double value = random.nextGaussian() * 10 + 50;
        return Math.max(0, Math.min(value, 100));
    }

    private static int hygrometer(){
        double value = random.nextGaussian() * 15 + 50;
        return (int) Math.max(0, Math.min(value, 100));
    }

    private static String windDirection(){
        String[] directions = {"N", "NW", "W", "SW", "S", "SE", "E", "NE"};
        int randomWindIndex = random.nextInt(directions.length);
        return directions[randomWindIndex];
    }
}
