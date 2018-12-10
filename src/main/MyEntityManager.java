package main;

import javax.persistence.*;

/**
 * Lab 5: JPA CRUD - World Database
 *
 * Class that holds one instance of the EntityManagerFactory and EntityManager.
 * For accessing ONE database in a single-threaded application.
 *
 * @author Lars Jelleryd
 */
public class MyEntityManager {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    /**
     * Private to prevent instantiation. Only static methods in class.
     */
    private MyEntityManager() {
    }

    /**
     * Creates the EntityManager. Should be called first. Will close any
     * previous connections (if called earlier without closing).
     *
     * @param persistenceUnitName Persistence unit name.
     * @return the EntityManager
     */
    public static EntityManager create(String persistenceUnitName) {
        if (emf != null) {
            close();
        }
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
        return em;
    }

    /**
     * Get the EntityManager.<br>
     * Note: MyEntityManager.create() should have been called first, if not,
     * this method will throw a RuntimeException.
     *
     * @return the EntityManager instance
     * @throws RuntimeException if EntityManager not initialized
     */
    public static EntityManager get() throws RuntimeException {
        if (em == null) {
            throw new RuntimeException("EntityManager not initialized. Call MyEntityManager.create() first.");
        }
        return em;
    }

    /**
     * Closes the EntityManager and the EntityManagerFactory.
     */
    public static void close() {
        if (em != null) {
            try {
                em.close();
            } catch (Exception e) {
                System.err.println("Exception on EntityManager close: " + e.getMessage());
            }
        }
        if (emf != null) {
            try {
                emf.close();
            } catch (Exception e) {
                System.err.println("Exception on EntityManagerFactory close: " + e.getMessage());
            }
        }
    }

    /**
     * Helper method to rollback a transaction.
     *
     * @param tx Entity transaction to roll back
     */
    public static void rollback(EntityTransaction tx) {
        if (tx.isActive()) {
            try {
                tx.rollback();
            } catch (RuntimeException e) {
                System.err.println("ROLLBACK FAILURE: " + e.getMessage());
            }
        }
    }

}
