package com.grzywacz.traveloffice.country;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.grzywacz.traveloffice.continent.Continent;
import com.grzywacz.traveloffice.continent.ContinentRepository;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    private final ContinentRepository continentRepository;

    public CountryService(CountryRepository countryRepository, ContinentRepository continentRepository) {
        this.countryRepository = countryRepository;
        this.continentRepository = continentRepository;
    }

    public List<CountryDto> getCountries() {
        List<Country> countries = countryRepository.findAll();
        List<CountryDto> collect = countries.stream()
                                            .map(it -> new CountryDto(it.getId(), it.getName(), it.getContinent().getId(), it.getContinent().getName()))
                                            .collect(Collectors.toList());
        return collect;
    }

    public void addCountry(CountryDto countryDto) {
        Country country = new Country();
        country.setName(countryDto.getName());
        Optional<Continent> byId = continentRepository.findById(countryDto.getContinentId());
        byId.ifPresent(it -> {
            country.setContinent(byId.get());
            countryRepository.save(country);
        });
    }

    public void deleteById(long id){
        countryRepository.deleteById(id);
    }
}
