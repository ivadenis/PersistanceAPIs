package su.heig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

/**
 * Created by Ian on 7/22/2014.
 */
public class Main {

    private static final Logger theLogger
            = Logger.getLogger(Main.class.getName());

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportsPU");
    private static final EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        Main demo = new Main();
        demo.loadDatabase();

        String userInput;
       /* do {
            displayMenu();
            userInput = input.nextLine();
            processInput(demo, userInput);
        } while (! userInput.equalsIgnoreCase("quit"));              */
        return;
    }





    private void loadDatabase() {
        // An EntityManager object is used to perform persistence tasks such as
        // starting transactions, persisting objects, creating queries, etc.
        em.getTransaction().begin();

        // prior to the statement below, each of the Team objects in the teams array
        // is a transient entity, i.e. just a regular non-persistent Java object.

        // All instances at this point are transient... they're "objects" not "entities"
        //for (Team team : TEAMS) {
        //    em.persist(team);
        //}


        // NOTE: Persisting a Player object without assigning a Team fails at run-time.
//        Player pete = new Player("Nick", "Young", 29, "Swaggy P");
//        em.persist(pete);

        // Now they are all persisted... even players due to the CascadeType (see relationship defined in Team.java)
        em.getTransaction().commit();
    }

}
