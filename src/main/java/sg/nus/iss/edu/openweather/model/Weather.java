package sg.nus.iss.edu.openweather.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Weather implements Serializable {
    private static final long serialVersionUID = 1L;

    private String city;
    private String temperature;
    private String visibility;
    private Long sunriseTime;
    private Long sunsetTime;

    private List<WeatherCondition> weathercondition = new LinkedList<>();

    
    public Weather() {}

    

    public Weather(String city, String temperature) {
        this.city = city;
        this.temperature = temperature;
    }


    public Weather(String city, String temperature, List<WeatherCondition> weathercondition) {
        this.city = city;
        this.temperature = temperature;
        this.weathercondition = weathercondition;
    }

    


    public Weather(String city, String temperature, String visibility, Long sunriseTime, Long sunsetTime,
            List<WeatherCondition> weathercondition) {
        this.city = city;
        this.temperature = temperature;
        this.visibility = visibility;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.weathercondition = weathercondition;
    }

    
    public String getVisibility() {return visibility;}
    public void setVisibility(String visibility) {this.visibility = visibility;}

    public Long getSunriseTime() {return sunriseTime;}
    public void setSunriseTime(Long sunriseTime) {this.sunriseTime = sunriseTime;}

    public Long getSunsetTime() {return sunsetTime;}
    public void setSunsetTime(Long sunsetTime) {this.sunsetTime = sunsetTime;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public String getTemperature() {return temperature;}
    public void setTemperature(String temperature) {this.temperature = temperature;}

    public List<WeatherCondition> getWeathercondition() {return weathercondition;}
    public void setWeathercondition(List<WeatherCondition> weathercondition) {
        this.weathercondition = weathercondition;}



    //convert json to Java object
    public static Weather create(String json) throws IOException {
        Weather w = new Weather();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            w.setCity(o.getString("name"));
            JsonObject main = o.getJsonObject("main");
            w.setTemperature(main.getJsonNumber("temp").toString());
            w.setVisibility(o.getJsonNumber("visibility").toString());
            JsonObject sys = o.getJsonObject("sys");
            w.setSunriseTime(sys.getJsonNumber("sunrise").longValue());
            w.setSunsetTime(sys.getJsonNumber("sunset").longValue());
        }

        return w;
    }

    // long sunriseTimeUnix = 1647625182; // Unix timestamp in seconds
    // LocalDateTime sunriseTimeUTC = LocalDateTime.ofEpochSecond(sunriseTimeUnix, 0, ZoneOffset.UTC);
    // ZoneId localZone = ZoneId.systemDefault();
    // LocalDateTime sunriseTimeLocal = sunriseTimeUTC.atZone(ZoneOffset.UTC).withZoneSameInstant(localZone).toLocalDateTime();
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // String sunriseTimeLocalString = sunriseTimeLocal.format(formatter);
    // System.out.println("Local sunrise time: " + sunriseTimeLocalString);

    

    
    
}
