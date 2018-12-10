package world.dao;

import java.util.List;
import world.City;

public interface CityDAO {

    List<City> findCityByName(String name);

    void deleteCityByName(String name);
}
