package sg.nus.iss.edu.openweather.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.nus.iss.edu.openweather.model.Weather;
import sg.nus.iss.edu.openweather.service.WeatherService;

@Controller
@RequestMapping(path="/weather")
public class WeatherController {
    @Autowired
    private WeatherService wSvc;

    @GetMapping
    public String getWeather(@RequestParam(required=true) String city,
        @RequestParam(defaultValue = "metric",required=false) String units, 
        @RequestParam(defaultValue = "en",required=false) String language, Model model) 
        throws IOException{
        Optional<Weather> w = wSvc.getWeather(city, units, language);
        wSvc.save(w.get());
        model.addAttribute("weather", w.get());
        return "result";
    }

    @GetMapping(path="/list")
    public String getAll(Model model, @RequestParam(defaultValue = "0") Integer startIndex) throws IOException{
        List<Weather> c = wSvc.findAll(startIndex);
        model.addAttribute("datalist", c);
        return "alldata";
    }

    @GetMapping(path="{dataId}")
    public String getDataId(Model model, @PathVariable String dataId) throws IOException{
        
        Optional<Weather> w = wSvc.findById(dataId);
        model.addAttribute("weather", w.get());
        return "idpage";
    }
}
