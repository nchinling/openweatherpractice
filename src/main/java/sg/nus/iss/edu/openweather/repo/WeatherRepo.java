package sg.nus.iss.edu.openweather.repo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.nus.iss.edu.openweather.model.Weather;

@Repository
public class WeatherRepo {
    
      //autowired in a bean.
      @Autowired @Qualifier("weatherbean")

      private RedisTemplate<String, String> template;


    public void save(Weather weather){
        this.template.opsForValue().set(weather.getDataId(), weather.toJSON().toString());
    }

    public List<Weather> findAll(int startIndex) throws IOException{
        Set<String> allKeys = template.keys("*");
        List<Weather> weatherarray = new LinkedList<>();
        for (String key : allKeys) {
            String result = template.opsForValue().get(key);
            System.out.println(">>>>>>>>" + result);

            weatherarray.add(Weather.createUserObjectFromRedis(result));
        }

        return weatherarray;

    }

    public Optional<Weather> findById(String dataId) throws IOException{
        String json = template.opsForValue().get(dataId);
        if(null == json|| json.trim().length() <= 0){
            return Optional.empty();
        }

        //creates a Weather object. 
        return Optional.of(Weather.createUserObjectFromRedis(json));
    }


}
