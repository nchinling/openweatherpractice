package sg.nus.iss.edu.openweather.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sg.nus.iss.edu.openweather.model.Weather;
import sg.nus.iss.edu.openweather.repo.WeatherRepo;

@Service
public class WeatherService {
    @Autowired
    private WeatherRepo weatherrepo;

    @Value("${open.weather.url}")
    private String openWeatherUrl;

    @Value("${open.weather.key}")
    private String openWeatherApiKey;

    public void save(final Weather weather){
        String orderId = UUID.randomUUID().toString().substring(0,8);
        weather.setDataId(orderId);
        weatherrepo.save(weather);
    }

    public List<Weather> findAll(int startIndex) throws IOException{
        return weatherrepo.findAll(startIndex);
    }

    public Optional<Weather> findById(final String dataId) throws IOException{
        return weatherrepo.findById(dataId);
    }  


    //function to get info from an external server using API.
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
        //r.getBody is a string response from api.
        Weather w = Weather.createUserObject(r.getBody());
        if(w != null)
            return Optional.of(w);
        return Optional.empty();

    }

    
}
