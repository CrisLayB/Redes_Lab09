package firstapp;

import java.util.Random;

public class Sensors {
    private static Random random = new Random();
    
    public static double thermometer(){
        double value = random.nextGaussian() * 10 + 50;
        return Math.max(0, Math.min(value, 100));
    }

    public static int hygrometer(){
        double value = random.nextGaussian() * 15 + 50;
        return (int) Math.max(0, Math.min(value, 100));
    }

    public static String windDirection(){
        String[] directions = {"N", "NW", "W", "SW", "S", "SE", "E", "NE"};
        int randomWindIndex = random.nextInt(directions.length);
        return directions[randomWindIndex];
    }
}
