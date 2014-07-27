/*
 * Created by Dr. Alvaro Monge <alvaro.monge@csulb.edu>
 * This code is most likely for educational purposes only,
 * please check with me otherwise.
 */

package jpa.app;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.*;
import jpa.entities.Player;
import jpa.entities.Team;

/**
 *
 * @author Alvaro Monge <alvaro.monge@csulb.edu>
 */
public class JPADemo {

    private static final Logger theLogger
            = Logger.getLogger(JPADemo.class.getName());

    // Create the EntityManager
    // sportsPU is a Persistence Unit as defined in persistence.xml that is
    // part of this application (it is the META-INF folder)
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportsPU");
    private static final EntityManager em = emf.createEntityManager();

    private static final Scanner input = new Scanner(System.in);
    
    /**
     * Constructor to setup the initial set of transient objects. 
     */
    JPADemo() {
        for (Player player : PLAYERS_LAKERS) {
            TEAMS[0].addPlayer(player);  // addPlayer is responsible for setting a player's team
        }

        for (Player player : PLAYERS_CLIPPERS) {
            TEAMS[1].addPlayer(player);
        }
    }

    /**
     * A menu-driven program giving user options of executing sample functions on the persistent data
     * @param args no arguments are used
     */
   public static void main(String[] args) {
            JPADemo demo = new JPADemo();
            demo.loadDatabase();

            String userInput;
            do {
                displayMenu();
                userInput = input.nextLine();
            processInput(demo, userInput);
        } while (! userInput.equalsIgnoreCase("quit"));
        return;
    }

    /**
     * Given some user input of the function to be executed, sets it up and executes it.
     * @param demo The demo object that has the functions of this demo
     * @param userInput The user's choice of which function to execute
     */
    public static void processInput(JPADemo demo, String userInput) {
        String teamName;
        
        switch (userInput.toLowerCase()) {
            case "reload":
                System.out.println("Removing DB of all records");
                demo.deleteDB();
                System.out.println("Loading DB with sample initial records");
                demo.loadDatabase();
                break;
            case "roster":
                System.out.print("Team name: ");
                teamName = input.nextLine();
                Collection<Player> roster = demo.getRoster(teamName);
                if (roster != null && !roster.isEmpty()) {
                    System.out.println("The roster: ");
                    for (Player player : roster) {
                        System.out.println(player);
                    }
                } else {
                    System.out.println("No players found");
                }
                break;
            case "remove":
                System.out.println("Demo of a remove operation");
                System.out.println("Enter the first name of the player to be removed.");
                String name = input.nextLine();
                demo.remove(name);
                break;
            case "find":
                System.out.println("Demo of a find operation");
                demo.find();
                break;
            case "delete":
                System.out.println("Demo of a delete statement");
                System.out.print("Team name: ");
                teamName = input.nextLine();
                demo.delete(teamName);
                break;
            default:
                System.out.println("Invalid choice, try again");
        }

    }

    /**
     * Display the menu of choices for the program
     */
    public static void displayMenu() {
        System.out.println();
        for (int i = 0; i < MENU_CHOICES.length; i++) {
            System.out.println(MENU_CHOICES[i] + ": " + CHOICE_SUMMARIES[i]);
        }
        System.out.print("\tYour choice> ");
    }

    /**
     * Method to demonstrate deleting all database objects. This will also
     * clean up the EntityManager by detaching all objects known to be in it.
     */
    private void deleteDB() {
        em.getTransaction().begin();
        
        Query deletePlayers = em.createNamedQuery(Player.DELETE_ALL);
        deletePlayers.executeUpdate();

        Query deleteTeams = em.createNamedQuery(Team.DELETE_ALL);
        deleteTeams.executeUpdate();
        
        em.getTransaction().commit();
        em.clear();
        
    }
            
    /**
     * Load the database tables with some initial records provided in the code.
     */
    private void loadDatabase() {
        // An EntityManager object is used to perform persistence tasks such as
        // starting transactions, persisting objects, creating queries, etc.
        em.getTransaction().begin();
        
        // prior to the statement below, each of the Team objects in the teams array
        // is a transient entity, i.e. just a regular non-persistent Java object.

        // All instances at this point are transient... they're "objects" not "entities"
        for (Team team : TEAMS) {
            em.persist(team);
        }


        // NOTE: Persisting a Player object without assigning a Team fails at run-time. 
//        Player pete = new Player("Nick", "Young", 29, "Swaggy P");
//        em.persist(pete);
        
        // Now they are all persisted... even players due to the CascadeType (see relationship defined in Team.java)
        em.getTransaction().commit();
    }

    /**
     * Retrieves the players who are members of a team.
     * @param teamName The name of the team whose players is to be retrieved
     * @return a List of Players who are members of the named team, or an empty list.
     */
    private Collection<Player> getRoster(String teamName) {
        // Query provide strong type checking
        Query retrieveTeamQuery = em.createNamedQuery(Team.GET_BY_NAME, Team.class);
        retrieveTeamQuery.setParameter("name", teamName);
        List<Team> teams = retrieveTeamQuery.getResultList();


        Query retrieveRosterQuery = em.createNamedQuery(Player.GET_BY_TEAM, Player.class);
        retrieveRosterQuery.setParameter("team", teams.get(0).getId());
        List<Player> players = null;
        if(!(teams == null) && ! teams.isEmpty()) players = retrieveRosterQuery.getResultList();

        return teams == null || teams.isEmpty() || players == null || players.isEmpty() ? null : players;
    }

    /**
     * Removes a Player whose id (PK value) is known.
     */
    private void remove(String firstName) {
        em.getTransaction().begin();

        // GET ID
        Query query2 = em.createQuery("SELECT id FROM Player WHERE first_name = ?0");
        query2.setParameter(0, firstName);
        int id = (Integer) query2.getSingleResult();

        // DELETE DB ENTRY
        Query query = em.createQuery(
                "DELETE FROM Player WHERE first_name = ?0");
        query.setParameter(0, firstName);
        query.executeUpdate();

        // DELETE OBJECT
        Player player = em.find(Player.class, id);

        if (player != null) {
            System.out.println("Removing player from database: " + player.toString());
            em.remove(player);
        }

        em.getTransaction().commit(); // Before a commit, the remove was not guaranteed
    }
    
    /**
     * Demonstrates how to use JPQL to delete from the database. Deletes the Team object
     * if it exists; otherwise, if it doesn't exist then it does nothing.
     * @param teamName the name of the Team to be deleted
     */
    private void delete(String teamName) {
        
        System.out.println("Demo WARNING: This will delete players on the team you're about to delete!");

        Query deleteStatement = em.createQuery("DELETE FROM Team t WHERE t.teamName = :name");
        deleteStatement.setParameter("name", teamName);

        em.getTransaction().begin();
        int count = deleteStatement.executeUpdate();
        theLogger.log(Level.FINE, "Number of teams deleted: {0}", count);
        em.getTransaction().commit();

        /**
         * TODO: Swiss SU Assignment -- Try deleting Teams with and without Players! Check the database before and after you 
         * test this method. Explain what happens in each case.
         *
         * When deleting teams with players, the database throws an exception, complaining about foreign constraints.
         * When deleting teams without players, it works like a charm! The program successfully removes the team
         * from the database.
         */
    }

    /**
     * Method to attempt to find Players with id (the PK) values 1 thru 10.
     * The retrieval stops as soon as a Player with particular id is not found.
     */
    private void find() {
        // Create the EntityManager
        boolean playerFound = true;

        // em.find() requires the PK value by which an entity can be found
        // In the demo below, since we know PK values are auto generated
        // starting at 1, we attempt to find the first 10 players.
        System.out.println("The first players inserted in the database...");
        for (int primaryKey = 1; primaryKey <= 10 && playerFound; primaryKey++) {
            Player player = em.find(Player.class, primaryKey);
            if (player != null) {
                System.out.println(player);
            } else {
                playerFound = false;
            }
        }
    }
    
    /**
     * lakersPlayers is an array of Player objects that will all be assigned to
     * the Los Angeles Lakers Team object.
     */
    private static final Player[] PLAYERS_LAKERS = new Player[]{
        new Player("Kobe", "Bryant", 24, "The Black Mamba"),
        new Player("Steve", "Nash", 10, "Canadian wonder"),
        new Player("Lin", "Jeremy", 7, "Linmania")
    };

    /**
     * clippersPlayers is an array of Player objects that will all be assigned to
     * the Los Angeles Clippers Team object.
     */
    private static final Player[] PLAYERS_CLIPPERS = new Player[]{
        new Player("Blake", "Griffin", 32, "The new Highlight Film"),
        new Player("Jamal", "Crawford", 11, "It's raining three pointers"),
        new Player("Chris", "Paul", 3, "The nonstop assist generator")

    };

    /**
     * teams is an array of Team objects, with players to be assigned in a program
     */
    private static final Team[] TEAMS = new Team[]{
        new Team("Los Angeles Lakers", "West"),
        new Team("Los Angeles Clippers", "West"),
        new Team("Miami Heat", "East"),
        new Team("Brooklyn Nets", "East"),
        new Team("Oklahoma City Thunder", "West")
    };


    /**
     * Menu choices
     */
    private static final String MENU_CHOICES[] = {"reload", "find", "roster", "remove", "delete", "quit"};
    
    /**
     * Descriptions of the menu choices
     */
    private static final String CHOICE_SUMMARIES[] = {
        "Reload initial data into the database",
        "Demo of a find",
        "View the name of all players from a team in the DB",
        "Demo of using remove() to remove a player from the DB",
        "Demo of executing a JPQL statement to delete a team from the DB",
        "Quit this program"
    };
}
