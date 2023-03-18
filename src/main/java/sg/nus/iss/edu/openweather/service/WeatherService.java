package sg.nus.iss.edu.openweather.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sg.nus.iss.edu.openweather.model.Weather;

@Service
public class WeatherService {
    
    @Value("${open.weather.url}")
    private String openWeatherUrl;

    @Value("${open.weather.key}")
    private String openWeatherApiKey;

    public Optional<Weather> getWeather(String city, String unitMeasurement, String language)
    throws IOException{
        System.out.println("openWeatherUrl: " + openWeatherUrl);
        System.out.println("openWeatherApiKey: " + openWeatherApiKey);
    
        String weatherUrl = UriComponentsBuilder
                            .fromUriString(openWeatherUrl)
                            .queryParam("q", city.replaceAll(" ", "+"))
                            .queryParam("units", unitMeasurement)
                            .queryParam("appId", openWeatherApiKey)
                            .queryParam("lang", language)
                            .toUriString();

        RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.getForEntity(weatherUrl, 
                String.class);
        Weather w = Weather.create(r.getBody());
        if(w != null)
            return Optional.of(w);
        return Optional.empty();

    }

    
}
