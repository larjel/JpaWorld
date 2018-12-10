package world.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import main.MyEntityManager;
import world.City;
import world.Country;

public class CityDAOImpl implements CityDAO {

    /**
     * Singleton instance
     */
    public static final CityDAOImpl INSTANCE = new CityDAOImpl();

    /**
     * Private constructor. Singleton access only.
     */
    private CityDAOImpl() {
    }

    @Override
    public List<City> findCityByName(String name) {
        return MyEntityManager.get()
                .createNamedQuery("City.findByName", City.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public void deleteCityByName(String name) {
        final EntityManager em = MyEntityManager.get();
        final EntityTransaction tx = em.getTransaction();

        // Note: Exception if more than one match!
        // This is by design just to test getSingleResult(), but means that
        // cities with the same name cannot be deleted in this application ;-)
        City city = em.createNamedQuery("City.findByName", City.class)
                .setParameter("name", name)
                .getSingleResult();

        try {
            tx.begin();
            em.remove(city);
            tx.commit();

            // Do a "find and refresh" to update the eclipse link L2 cache and
            // the capital city in Country, otherwise a deleted city capital
            // may not show immediately when getting Country Info
            Country country = em.find(Country.class, city.getCountry().getCode());
            em.refresh(country);
        } catch (RuntimeException e) {
            MyEntityManager.rollback(tx);
            throw e;
        }

    }

}
