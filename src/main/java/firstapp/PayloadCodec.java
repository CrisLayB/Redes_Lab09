package firstapp;

import org.json.JSONObject;

public class PayloadCodec {
    public static byte[] encode(JSONObject json) {
        int encodedTemperature = encodeTemperature(json.getDouble("temperatura"));
        int encodedHumidity = encodeHumidity(json.getInt("humedad"));
        int encodedWindDirection = encodeWindDirection(json.getString("direccion_viento"));

        int combinedData = (encodedTemperature << 10) | (encodedHumidity << 3) | encodedWindDirection;
        
        byte[] byteArray = new byte[3];
        byteArray[0] = (byte) ((combinedData >> 16) & 0xFF);
        byteArray[1] = (byte) ((combinedData >> 8) & 0xFF);
        byteArray[2] = (byte) (combinedData & 0xFF);

        return byteArray;
    }

    public static JSONObject decode(byte[] byteArray) {
        
        int combinedData = ((byteArray[0] & 0xFF) << 16) | ((byteArray[1] & 0xFF) << 8) | (byteArray[2] & 0xFF);
        
        int decodedTemperature = decodeTemperature(combinedData >> 10);
        int decodedHumidity = decodeHumidity((combinedData >> 3) & 0x7F); 
        String decodedWindDirection = decodeWindDirection(combinedData & 0x7); 
        
        return new JSONObject()
                .put("temperatura", decodedTemperature)
                .put("humedad", decodedHumidity)
                .put("direccion_viento", decodedWindDirection);
    }
    
    private static int encodeTemperature(double temperature) {
        return (int) (temperature * 10);
    }

    private static int decodeTemperature(int encodedTemperature) {
        return encodedTemperature / 10;
    }

    private static int encodeHumidity(int humidity) {
        return humidity;
    }

    private static int decodeHumidity(int encodedHumidity) {
        return encodedHumidity;
    }

    private static int encodeWindDirection(String windDirection) {
        switch (windDirection) {
            case "N": return 0;
            case "NW": return 1;
            case "W": return 2;
            case "SW": return 3;
            case "S": return 4;
            case "SE": return 5;
            case "E": return 6;
            case "NE": return 7;
            default: return 0; 
        }
    }

    private static String decodeWindDirection(int encodedWindDirection) {
        switch (encodedWindDirection) {
            case 0: return "N";
            case 1: return "NW";
            case 2: return "W";
            case 3: return "SW";
            case 4: return "S";
            case 5: return "SE";
            case 6: return "E";
            case 7: return "NE";
            default: return "N/A"; 
        }
    }
}
