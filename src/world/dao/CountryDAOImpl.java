package world.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import main.MyEntityManager;
import world.Country;

public class CountryDAOImpl implements CountryDAO {

    /**
     * Singleton instance
     */
    public static final CountryDAOImpl INSTANCE = new CountryDAOImpl();

    /**
     * Private constructor. Singleton access only.
     */
    private CountryDAOImpl() {
    }

    @Override
    public void addCountry(Country newCountry) {
        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();

        // Check if the country already exists. Find on primary key (country code).
        Country c = em.find(Country.class, newCountry.getCode());
        try {
            tx.begin();
            if (c == null) {
                // Does not already exist. Persist.
                em.persist(newCountry);
            } else {
                // Already exists. Merge.
                em.merge(newCountry);
            }
            tx.commit();

            // Do a "find and refresh" to update the eclipse link L2 cache and
            // the lazy loaded city list in Country, otherwise the new city (capital)
            // will not show immediately when getting Country Info
            c = em.find(Country.class, newCountry.getCode());
            em.refresh(c);
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    private Country findCountry(EntityManager em, String countryCode) {
        Country country = em.find(Country.class, countryCode);
        if (country == null) {
            throw new RuntimeException("Country not found. Bad country code?");
        }
        return country;
    }

    @Override
    public void updateCountryPopulation(String countryCode, long newPopulation) {
        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();

        Country country = findCountry(em, countryCode);
        try {
            tx.begin();
            country.setPopulation(newPopulation);
            tx.commit();
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }
    }

    @Override
    public List<Country> findCountryByName(String name) {
        return MyEntityManager.get()
                .createNamedQuery("Country.findByName", Country.class)
                .setParameter("name", name)
                .getResultList();
    }

}
