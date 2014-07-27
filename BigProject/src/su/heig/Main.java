package su.heig;

import su.entities.Album;
import su.entities.Musician;
import su.entities.Song;

import javax.persistence.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by Ian on 7/22/2014.
 * Modified by Ian on 7/25/2014
 */
public class Main {

    private static final Logger theLogger
            = Logger.getLogger(Main.class.getName());

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SportsPU");
    private static final EntityManager em = emf.createEntityManager();

    public static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws ParseException {
        Main demo = new Main();
        demo.loadDatabase();

        String userInput;
        do {
            displayMenu();
            userInput = input.nextLine();
            processInput(demo, userInput);
        } while (! userInput.equalsIgnoreCase("quit"));

        emf.close();

        return;
    }

    public static void processInput(Main demo, String userInput) throws ParseException {
        String teamName;

        switch (userInput.toLowerCase()) {
            case "addsong":
            case "as":
                System.out.print("Album: ");
                String a = input.nextLine();
                Album album = demo.findAlbum(a);
                if(album == null) {
                    System.out.println("Album does not exist.");
                    break;
                }
                System.out.print("Song name: ");
                String sn = input.nextLine();
                System.out.print("Song duration: ");
                int dur = Integer.parseInt(input.nextLine());
                System.out.print("Song genre: ");
                String sg = input.nextLine();

                demo.addSongToAlbum(album,new Song(sn, sg, dur));

                break;
            case "addalbum":
            case "aa":
                System.out.print("Album title: ");
                String title = input.nextLine();
                System.out.print("Album date(M D Y): ");
                Date d = new Date(new SimpleDateFormat("M d y").parse(input.nextLine()).getTime());
                System.out.print("Album label: ");
                String l = input.nextLine();

                em.getTransaction().begin();
                em.persist(new Album(title,d,l));
                em.getTransaction().commit();
                break;
            case "albuminfo":
            case "ai":
                System.out.print("Album title: ");
                String atitle = input.nextLine();
                demo.albumInfo(atitle);
                break;
            case "list":
            case "l":
                demo.listAlbums();
                break;
            default:
                if(! userInput.equalsIgnoreCase("quit")) System.out.println("Invalid choice, try again");
        }

    }

    private Album findAlbum(String title) {
        Query retrieveTeamQuery = em.createNamedQuery(Album.GET_BY_TITLE, Album.class);
        retrieveTeamQuery.setParameter("title", title);
        List<Album> albums = retrieveTeamQuery.getResultList();

        return albums == null || albums.isEmpty() ? null : albums.get(0);
    }

    private void albumInfo(String title) {
        Album a = findAlbum(title);

        if(!a.getMusicians().isEmpty()) {
            System.out.println("Musicians:");
            for (Musician m : a.getMusicians()) {
                System.out.println(m.getName());
            }
        }
        if(!a.getSongs().isEmpty()) {
            System.out.println("Songs:");

            for(Song s : a.getSongs()) {
                System.out.println(s);
            }
        }

    }

    private void addSongToAlbum(Album a, Song s) {
        em.getTransaction().begin();

        em.persist(s);

        a.addSong(s);

        em.merge(a);

        em.getTransaction().commit();
    }

    private void listAlbums() {
        // Create the EntityManager
        boolean playerFound = true;

        // em.find() requires the PK value by which an entity can be found
        // In the demo below, since we know PK values are auto generated
        // starting at 1, we attempt to find the first 10 players.
        System.out.println("The first players inserted in the database...");
        for (int primaryKey = 1; primaryKey <= 10 && playerFound; primaryKey++) {
            Album player = em.find(Album.class, primaryKey);
            if (player != null) {
                System.out.println(player);
            } else {
                playerFound = false;
            }
        }
    }

    public static void displayMenu() {
        System.out.println();
        for (int i = 0; i < MENU_CHOICES.length; i++) {
            System.out.println(MENU_CHOICES[i] + ": " + CHOICE_SUMMARIES[i]);
        }
        System.out.print("\tYour choice> ");
    }

    private void loadDatabase() {
        // An EntityManager object is used to perform persistence tasks such as
        // starting transactions, persisting objects, creating queries, etc.
        em.getTransaction().begin();

        // prior to the statement below, each of the Team objects in the teams array
        // is a transient entity, i.e. just a regular non-persistent Java object.

        // All instances at this point are transient... they're "objects" not "entities"
        for (Musician m : MUSICIANS) {
            em.persist(m);
        }

        int i = 0;
        for (Album a : ALBUMS) {

            for (Song s : SONGS[i]) {
                a.addSong(s);
            }

            for (Musician m : MUSICIANS) {
                a.addMusician(m);
            }

            em.persist(a);
            i++;
        }


        // NOTE: Persisting a Player object without assigning a Team fails at run-time.

        //ALBUMS[0] = new Album("Panic",new Date(112,2,20),"Some guy");

        Song s = new Song("Maniac","Electroswing",254);
        ALBUMS[0].addSong(s);
        em.persist(s);
        em.merge(ALBUMS[0]);

        // Now they are all persisted... even players due to the CascadeType (see relationship defined in Team.java)
        em.getTransaction().commit();
    }

    Musician MUSICIANS[] = {
            new Musician("Pete"),
            new Musician("Joe")

    };

    Album ALBUMS[] = {
        new Album("Panic",new Date(112,2,20),"Some guy"),
        new Album("Caravan Palace",new Date(110,2,20),"Some guy")
    };

    Song SONGS[][] = {
            {
                    new Song("Clash","Electroswing",254),
                    new Song("Dramophone","Electroswing",3*60+25),
                    new Song("Panic","Electroswing",4*60+6)
            },
            {
                    new Song("Dragons","Electroswing",4*60+6),
                    new Song("Brotherswing","Electroswing",3*60+42),
                    new Song("Suzy","Electroswing",4*60+8)
            }
    };


    private static final String MENU_CHOICES[] = {"list (l)", "addalbum (aa)", "addsong (as)", "albuminfo (ai)", "quit"};

    /**
     * Descriptions of the menu choices
     */
    private static final String CHOICE_SUMMARIES[] = {
            "List albums",
            "Add album",
            "Add song to album",
            "Get album info",
            "Quit this program"
    };

}
