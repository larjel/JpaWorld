package world;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "City.findAll", query = "SELECT c FROM City c")
    , @NamedQuery(name = "City.findById", query = "SELECT c FROM City c WHERE c.id = :id")
    , @NamedQuery(name = "City.findByName", query = "SELECT c FROM City c WHERE c.name = :name")
    , @NamedQuery(name = "City.findByDistrict", query = "SELECT c FROM City c WHERE c.district = :district")
    , @NamedQuery(name = "City.findByPopulation", query = "SELECT c FROM City c WHERE c.population = :population")})
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String district;
    private int population;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "countrycode")
    private Country country;

    public City() {
    }

    public City(String name, String district, int population) {
        this.name = name;
        this.district = district;
        this.population = population;
    }

    @Override
    public String toString() {
        return "City{" + "id=" + id + ", name=" + name + ", district=" + district
                + ", population=" + population + ", country=" + country.getName() + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
