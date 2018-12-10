package main;

/**
 * Lab 5: JPA CRUD - World Database
 *
 * @author Lars Jelleryd
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MyEntityManager.create("JpaWorldPU");
            while (Menu.run()) {
            }
        } finally {
            MyEntityManager.close();
        }
    }

}
