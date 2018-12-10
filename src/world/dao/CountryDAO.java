package world.dao;

import java.util.List;
import world.Country;

public interface CountryDAO {

    void addCountry(Country newCountry);

    void updateCountryPopulation(String countryCode, long newPopulation);

    List<Country> findCountryByName(String name);
}
