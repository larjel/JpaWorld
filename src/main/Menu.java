package main;

import java.util.List;
import userio.MenuInterface;
import userio.SystemInput;
import userio.SystemInputAbortedException;
import world.dao.CityDAOImpl;
import world.dao.CountryDAOImpl;
import world.City;
import world.Country;

/**
 * Lab 5: JPA CRUD - World Database
 *
 * @author Lars Jelleryd
 */
public enum Menu implements MenuInterface {
    OPT_INVALID(-1, "Invalid"), // First enum is required to be 'invalid'
    OPT_SHOW_COUNTRY_INFO(1, "Show country information"),
    OPT_UPDATE_COUNTRY_POPULATION(2, "Update country population"),
    OPT_ADD_COUNTRY(3, "Add new country (will merge if country already exists)"),
    OPT_SHOW_CITY_INFO(4, "Show city information"),
    OPT_DELETE_CITY(5, "Delete city"),
    OPT_EXIT(0, "Exit");

    private final int numeric;
    private final String text;

    //--------------------------------------------------------------
    private Menu(int numeric, String text) {
        this.numeric = numeric;
        this.text = text;
    }

    //--------------------------------------------------------------
    @Override
    public String getText() {
        return this.text;
    }

    //--------------------------------------------------------------
    @Override
    public int getNumeric() {
        return this.numeric;
    }

    //--------------------------------------------------------------
    public static boolean run() {
        try {
            // Display the menu
            MenuInterface.printMenu("MAIN MENU", Menu.values());
            // Wait for user input
            Menu option = MenuInterface.numericToEnum(SystemInput.getInt(), Menu.values());
            switch (option) {
                case OPT_EXIT:
                    return false;
                case OPT_SHOW_COUNTRY_INFO:
                    showCountryInfo();
                    break;
                case OPT_UPDATE_COUNTRY_POPULATION:
                    updateCountryPopulation();
                    break;
                case OPT_ADD_COUNTRY:
                    addCountry();
                    break;
                case OPT_SHOW_CITY_INFO:
                    showCityInfo();
                    break;
                case OPT_DELETE_CITY:
                    deleteCity();
                    break;
                case OPT_INVALID:
                default:
                    System.out.println(">>> Invalid menu choice! Try again.");
                    break;
            }

        } catch (SystemInputAbortedException e) {
            System.out.println(">>> Aborted due to empty input!");
        } catch (Exception e) {
            System.out.println(">>> Operation failed: " + e.getMessage());
        }

        System.out.println("Press Enter to continue...");
        SystemInput.getString();

        return true;
    }

    //--------------------------------------------------------------
    private static void addCountry() throws SystemInputAbortedException {
        System.out.println("--- ENTER NEW COUNTRY INFORMATION ---");
        System.out.print("Name of country (or enter to abort): ");
        String name = SystemInput.getStringAbortOnEmpty();

        System.out.print("Country code, max. 3 characters (or enter to abort): ");
        String code = SystemInput.getStringAbortOnEmpty();

        System.out.print("Population (or enter to abort): ");
        int population = SystemInput.getIntAbortOnEmpty();

        System.out.println("Continent ('Asia','Europe','North America','Africa','Oceania','Antarctica','South America')");
        System.out.print("or enter to abort: ");
        String continent = SystemInput.getStringAbortOnEmpty();

        System.out.print("Region (or enter to abort): ");
        String region = SystemInput.getStringAbortOnEmpty();

        Country country = new Country(code, name, continent, region, population);

        System.out.println("--- ENTER NEW CAPITAL CITY INFORMATION ---");
        System.out.print("Name of capital (or enter to abort): ");
        name = SystemInput.getStringAbortOnEmpty();

        System.out.print("District (or enter to abort): ");
        String district = SystemInput.getStringAbortOnEmpty();

        System.out.print("Population (or enter to abort): ");
        population = SystemInput.getIntAbortOnEmpty();

        City city = new City(name, district, population);
        city.setCountry(country);

        country.setCapital(city);

        // Add country to database
        CountryDAOImpl.INSTANCE.addCountry(country);

        System.out.println(">>> Country added/updated successfully!");
    }

    //--------------------------------------------------------------
    private static void updateCountryPopulation() throws SystemInputAbortedException {
        System.out.print("Enter country code of country to update (or enter to abort): ");
        String code = SystemInput.getStringAbortOnEmpty();

        System.out.print("Enter new population: ");
        int population = SystemInput.getInt();

        // Update country population in database
        CountryDAOImpl.INSTANCE.updateCountryPopulation(code, population);

        System.out.println(">>> Country population updated successfully!");
    }

    //--------------------------------------------------------------
    private static void showCityInfo() throws SystemInputAbortedException {
        System.out.print("Name of city (or enter to abort): ");
        String name = SystemInput.getStringAbortOnEmpty();

        // Find city in database
        List<City> cities = CityDAOImpl.INSTANCE.findCityByName(name);

        System.out.printf("Found %d matches for %s%n", cities.size(), name);
        for (City c : cities) {
            System.out.println(c.getName() + ", " + c.getDistrict()
                    + " with a population of " + c.getPopulation()
                    + " is located in " + c.getCountry().getName());
        }
    }

    //--------------------------------------------------------------
    private static void deleteCity() throws SystemInputAbortedException {
        System.out.print("Name of city to delete (or enter to abort): ");
        String name = SystemInput.getStringAbortOnEmpty();

        // Delete city from database
        CityDAOImpl.INSTANCE.deleteCityByName(name);

        System.out.println(">>> City successfully deleted!");
    }

    //--------------------------------------------------------------
    private static void showCountryInfo() throws SystemInputAbortedException {
        System.out.print("Name of country (or enter to abort): ");
        String name = SystemInput.getStringAbortOnEmpty();

        // Get country information from database
        List<Country> countries = CountryDAOImpl.INSTANCE.findCountryByName(name);

        if (countries.isEmpty()) {
            System.out.println("No country found with the name " + name);
        } else {
            System.out.print("Country found. Enter 'c' to also show cities in country, otherwise just press enter: ");
            boolean showCitiesInCountry = SystemInput.getString().equalsIgnoreCase("c");
            // PRINT COUNTRY INFORMATION
            for (Country c : countries) {
                System.out.println("-----------------------------------------");
                System.out.println("INFORMATION ABOUT " + c.getName().toUpperCase());
                System.out.println("Country code: " + c.getCode());
                if (c.getCapital() != null) {
                    System.out.println("Capital city: " + c.getCapital().getName());
                } else {
                    System.out.println("Capital city: Unknown (not in database)");
                }
                System.out.println("Population: " + c.getPopulation());
                System.out.println("Region: " + c.getRegion());
                if (showCitiesInCountry) {
                    System.out.println("-----------------------------------------");
                    System.out.println("Cities in " + c.getName() + " are:");
                    c.getCities().forEach(c1 -> System.out.println("  - " + c1.getName()
                            + ", " + c1.getDistrict() + " pop. " + c1.getPopulation()));
                }
                System.out.println("-----------------------------------------");
            }
        }

    }

}
