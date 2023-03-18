package sg.nus.iss.edu.openweather.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

public class Weather implements Serializable {
    private static final long serialVersionUID = 1L;

    private String city;
    private String temperature;
    private String visibility;
    private Long sunriseTime;
    private Long sunsetTime;


    private List<WeatherCondition> weathercondition = new LinkedList<>();

      //need to implement id for redis db.
      private String dataId;

    
    public Weather() {}

    public Weather(String dataId) {
        this.dataId = dataId;
    }

    public Weather(String city, String temperature) {
        this.city = city;
        this.temperature = temperature;
    }

    public Weather(String city, String temperature, List<WeatherCondition> weathercondition) {
        this.city = city;
        this.temperature = temperature;
        this.weathercondition = weathercondition;
    }

    public Weather(String dataId, String city, String temperature, List<WeatherCondition> weathercondition) {
        this.dataId = dataId;
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

    public String getDataId() {return dataId;}

    public void setDataId(String dataId) {this.dataId = dataId;}

    private synchronized static String generateId(int size){
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().substring(0, 8);
        return uuidString;
    }

    //convert json to Java object
    public static Weather createUserObject(String json) throws IOException {
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

            w.weathercondition = o.getJsonArray("weather").stream()
            .map(v-> (JsonObject)v)
            .map(v-> WeatherCondition.createFromJson(v))
            .toList();
            
        }

        w.setDataId(generateId(8));
        return w;
    }

    public static Weather createUserObjectFromRedis(String json) throws IOException{
        Weather w = new Weather();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            w.setCity(o.getString("city"));
            w.setDataId(o.getString("dataId"));
            w.setTemperature(o.getString("temperature"));
            w.setVisibility(o.getString("visibility"));
        }

        return w;
    }

    public JsonObject toJSON(){

        //to convert array to JSON
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (WeatherCondition wc : weathercondition) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                    .add("mainWeather", wc.getMainWeather())
                    .add("description", wc.getDescription());
            arrayBuilder.add(objBuilder);
        }
        
        return Json.createObjectBuilder()
                .add("dataId", this.getDataId())
                .add("city", this.getCity())
                .add("temperature", this.getTemperature())
                .add("visibility", this.getVisibility())
                .add("sunriseTime", this.getSunriseTime())
                .add("sunsetTime", this.getSunsetTime())
                .add("weathercondition", arrayBuilder.build())
                .build();
    }

    @Override
    public String toString() {
        return "city=" + this.getCity() + ", temperature=" + this.getTemperature() + ", visibility=" + this.getVisibility()
                + ", sunriseTime=" + this.getSunriseTime() + ", sunsetTime=" + this.getSunsetTime() + ", weathercondition="
                + this.getWeathercondition() + ", dataId=" + this.getDataId() + "";
    }
  
}
