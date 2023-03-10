package com.grzywacz.traveloffice.admin;

import java.util.Arrays;
import java.util.List;

import com.grzywacz.traveloffice.travel.CreateTravelDto;
import com.grzywacz.traveloffice.hotels.HotelType;
import com.grzywacz.traveloffice.travel.TravelDto;
import com.grzywacz.traveloffice.travel.TravelService;
import com.grzywacz.traveloffice.airport.AirportDto;
import com.grzywacz.traveloffice.airport.AirportService;
import com.grzywacz.traveloffice.city.CityDto;
import com.grzywacz.traveloffice.city.CityService;
import com.grzywacz.traveloffice.continent.ContinentDto;
import com.grzywacz.traveloffice.continent.ContinentService;
import com.grzywacz.traveloffice.country.CountryDto;
import com.grzywacz.traveloffice.country.CountryService;
import com.grzywacz.traveloffice.hotels.HotelDto;
import com.grzywacz.traveloffice.hotels.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TravelService travelService;
    private final ContinentService continentService;
    private final CountryService countryService;
    private final CityService cityService;
    private final HotelService hotelService;
    private final AirportService airportService;

    public AdminController(TravelService travelService, ContinentService continentService, CountryService countryService, CityService cityService, HotelService hotelService,
                           AirportService airportService) {
        this.travelService = travelService;
        this.continentService = continentService;
        this.countryService = countryService;
        this.cityService = cityService;
        this.hotelService = hotelService;
        this.airportService = airportService;
    }

    @GetMapping
    public String travels(Model model) {
        List<TravelDto> travels = travelService.getTravels();
        model.addAttribute("travels", travels);
        return "admin/travels";
    }

    @PostMapping("/travel/delete/{id}")
    public String delete(Model model, @PathVariable long id ) {
        travelService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/travel/add")
    public String addTravel(Model model) {
        model.addAttribute("createTravel", new CreateTravelDto());
        model.addAttribute("cities", cityService.getCities());
        model.addAttribute("hotelTypes", Arrays.stream(HotelType.values()).toList());
        return "admin/add-travel";
    }

    @GetMapping("/travel/edit/{id}")
    public String travelEdit(Model model, @PathVariable long id ) {
        TravelDto travel = travelService.getTravelById(id);
        model.addAttribute("travel", travel);
        return "admin/edit-travel";
    }

    @PostMapping(value = "/travel/add")
    public String postBody(@ModelAttribute CreateTravelDto createTravelDto, Model model) {
        travelService.createTravel(createTravelDto);
        return "redirect:/admin";
    }

    @GetMapping("/continents")
    public String continents(Model model) {
        List<ContinentDto> continents = continentService.getContinents();
        model.addAttribute("continents", continents);
        model.addAttribute("newContinent", new ContinentDto());
        return "admin/continents";
    }

    @PostMapping("/continents/add")
    public String addContinent(Model model,@ModelAttribute ContinentDto continentDto) {
        continentService.addContinent(continentDto);
        return "redirect:/admin/continents";
    }

    @PostMapping("/continents/delete/{id}")
    public String deleteContinent(Model model, @PathVariable long id ) {
        continentService.deleteById(id);
        return "redirect:/admin/continents";
    }

    @GetMapping("/countries")
    public String countries(Model model) {
        List<ContinentDto> continents = continentService.getContinents();
        List<CountryDto> countries = countryService.getCountries();
        model.addAttribute("continents", continents);
        model.addAttribute("countries", countries);
        model.addAttribute("newCountry", new CountryDto());
        return "admin/countries";
    }

    @PostMapping("/countries/delete/{id}")
    public String deleteCountry(Model model, @PathVariable long id ) {
        countryService.deleteById(id);
        return "redirect:/admin/countries";
    }

    @PostMapping("/country/add")
    public String addCountry(Model model,@ModelAttribute CountryDto countryDto) {
        countryService.addCountry(countryDto);
        return "redirect:/admin/countries";
    }

    @GetMapping("/cities")
    public String cities(Model model, @RequestParam(required = false) String cityValidation) {
        List<CountryDto> countries = countryService.getCountries();
        List<CityDto> cities = cityService.getCities();
        if (cityValidation!=null && cityValidation.equals("false")) {
            model.addAttribute("cityCanBeDelete", "false");
        }
        model.addAttribute("cities", cities);
        model.addAttribute("countries", countries);
        model.addAttribute("newCity", new CityDto());
        return "admin/cities";
    }

    @PostMapping("/city/add")
    public String addCity(Model model,@ModelAttribute CityDto cityDto) {
        cityService.addCity(cityDto);
        return "redirect:/admin/cities";
    }

    @PostMapping("/city/delete/{id}")
    public String deleteCity(Model model, @PathVariable long id ) {
        boolean validationResult = cityService.checkCityCanBeDelete(id);
        if (!validationResult) {
            return "redirect:/admin/cities?cityValidation=false";
        } else {
            cityService.deleteById(id);
            return "redirect:/admin/cities";
        }
    }

    @GetMapping("/hotels")
    public String hotels(Model model, @RequestParam(required = false) String hotelValidation) {
        List<HotelDto> hotels = hotelService.getHotels();
        List<CityDto> cities = cityService.getCities();
        if (hotelValidation!=null && hotelValidation.equals("false")) {
            model.addAttribute("hotelCanBeDelete", "false");
        }
        model.addAttribute("hotels", hotels);
        model.addAttribute("cities", cities);
        model.addAttribute("newHotel", new HotelDto());
        return "admin/hotels";
    }

    @PostMapping("/hotel/add")
    public String addHotel(Model model,@ModelAttribute HotelDto hotelDto) {
        hotelService.addHotel(hotelDto);
        return "redirect:/admin/hotels";
    }

    @PostMapping("/hotel/delete/{id}")
    public String deleteHotel(@PathVariable long id ) {
        boolean validationResult = hotelService.checkHotelCanBeDelete(id);
        if (!validationResult) {
            return "redirect:/admin/hotels?hotelValidation=false";
        } else {
            hotelService.deleteById(id);
            return "redirect:/admin/hotels";
        }
    }

    @GetMapping("/airports")
    public String airports(Model model) {
        List<AirportDto> airports = airportService.getAirports();
        List<CityDto> cities = cityService.getCities();
        model.addAttribute("airports", airports);
        model.addAttribute("cities", cities);
        model.addAttribute("newAirport", new AirportDto());
        return "admin/airports";
    }

    @PostMapping("/airport/add")
    public String addAirport(Model model,@ModelAttribute AirportDto airportDto) {
        airportService.addAirport(airportDto);
        return "redirect:/admin/airports";
    }

    @PostMapping("/airport/delete/{id}")
    public String deleteAirport(Model model, @PathVariable long id ) {
        airportService.deleteById(id);
        return "redirect:/admin/airports";
    }
}
