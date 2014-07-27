package su.heig;

import su.entities.Album;
import su.entities.Musician;
import su.entities.Song;

import javax.persistence.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by Ian on 7/22/2014.
 * Modified by Ian on 7/25/2014
 * Modified by Christy on 7/26/2014
 * Modified by Christy on 7/27/2014
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
        return;
    }

    public static void processInput(Main demo, String userInput) throws ParseException {
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
                System.out.print("Album date: ");
                Date d = new Date(new SimpleDateFormat("M d y").parse(input.nextLine()).getTime());
                System.out.print("Album label: ");
                String l = input.nextLine();
                em.getTransaction().begin();
                em.persist(new Album(title,d,l));
                em.getTransaction().commit();
                break;
            case "listalbums":
            case "la":
                demo.listAlbums();
                break;
            case "listmusicians":
            case "lm":
                demo.listMusicians();
                break;
            case "addmusicianalbum":
            case "ama":
                System.out.print("Musician Name: ");
                boolean bool = false;
                String name = input.nextLine();
                // Making two separate objects, because otherwise it gets confused
                Musician existingMus = demo.findMusician(name);
                Musician newMus = new Musician("");
                if(existingMus == null) {
                    // ADD THE MUSICIAN
                    bool = true; // set flag
                    newMus = new Musician(name);
                    em.getTransaction().begin();
                    em.persist(newMus);
                    em.getTransaction().commit();
                    System.out.println("This musician is not yet stored. He/She has just been added!");
                }
                System.out.print("Album title: ");
                String albumName = input.nextLine();
                Album existingAlb = demo.findAlbum(albumName);
                if(existingAlb == null){
                    // ADD THE ALBUM AND THEN THE MUSICIAN
                    System.out.println("This album is not yet stored. Please enter the following info to store it.");
                    System.out.print("Album date: ");
                    Date date = new Date(new SimpleDateFormat("M d y").parse(input.nextLine()).getTime());
                    System.out.print("Album label: ");
                    String label = input.nextLine();
                    Album newAlb = new Album(albumName,date,label);
                    em.getTransaction().begin();
                    em.persist(newAlb);
                    em.getTransaction().commit();
                    if (bool)
                        newAlb.addMusician(newMus);
                    else newAlb.addMusician(existingMus);
                }
                else{
                    // ADD THE MUSICIAN TO THE EXISTING ALBUM
                    if (bool) {
                        existingAlb.addMusician(newMus);
                    }
                    else existingAlb.addMusician(existingMus);
                }
                System.out.println("Musician added to album!");
                break;
            case "listallsongs":
            case "las":
                demo.listSongs();
                break;
            case "listsongsbyalbum":
            case "lsa":
                System.out.println("Album: ");
                String albTitle = input.nextLine();
                Album alb = demo.findAlbum(albTitle);
                demo.listSongs(alb);
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

    private Musician findMusician(String name) {
        Query retrieveTeamQuery = em.createNamedQuery(Musician.GET_BY_NAME, Musician.class);
        retrieveTeamQuery.setParameter("name", name);
        List<Musician> muses = retrieveTeamQuery.getResultList();

        return muses == null || muses.isEmpty() ? null : muses.get(0);
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
        boolean albumFound = true;

        System.out.println("----------ALBUMS----------");
        for (int primaryKey = 1; primaryKey <= 10 && albumFound; primaryKey++) {
            Album album = em.find(Album.class, primaryKey);
            if (album != null) {
                System.out.println(album);
                if (album.getMusicians().size() > 0){
                    System.out.println("   Musicians:");
                    for (Musician musician : album.getMusicians())
                        System.out.println("      " + musician.getName());
                }
            } else {
                albumFound = false;
            }
        }
        System.out.println("--------------------------");
    }

    public void listMusicians(){
        // Create the EntityManager
        boolean musicianFound = true;
        boolean albumFound = true;

        System.out.println("--------MUSICIANS---------");
        for (int primaryKey = 1; primaryKey <= 10 && musicianFound; primaryKey++) {
            Musician mus = em.find(Musician.class, primaryKey);
            if (mus != null) {
                System.out.println(mus.getName());
                Album album = em.find(Album.class, primaryKey);
                if (album != null) {
                    System.out.println("   Album(s): ");
                    System.out.println("   " + album.getAlbumTitle());
                } else {
                    albumFound = false;
                }
            } else {
                musicianFound = false;
            }
        }
        System.out.println("--------------------------");
    }

    public void listSongs(){
        // Create the EntityManager
        boolean songFound = true;

        System.out.println("----------SONGS-----------");
        for (int primaryKey = 1; primaryKey <= 10 && songFound; primaryKey++) {
            Song song = em.find(Song.class, primaryKey);
            if (song != null) {
                System.out.println(song);
            } else {
                songFound = false;
            }
        }
        System.out.println("--------------------------");
    }

    public void listSongs(Album alb){
        if (alb.getSongs().size() > 0)
            for (Song song : alb.getSongs())
                System.out.println(song.toString());
        else System.out.println("  No songs added.");
    }

    public static void displayMenu() {
        System.out.println();
        System.out.println("-----------MENU-----------");
        for (int i = 0; i < MENU_CHOICES.length; i++) {
            System.out.println(MENU_CHOICES[i] + ": " + CHOICE_SUMMARIES[i]);
        }
        System.out.println("--------------------------");
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

    private static final String MENU_CHOICES[] = {"LIST", "   listalbums (la)", "   listmusicians (lm)",
            "   listallsongs (las)", "   listsongsbyalbum (lsa)", "ADD",
            "   addsong (as)", "   addmusicianalbum (ama)", "QUIT"};

    /**
     * Descriptions of the menu choices
     */
    private static final String CHOICE_SUMMARIES[] = {
            "",
            "List albums",
            "List musicians",
            "List all songs",
            "List songs by album",
            "",
            "Add song to album",
            "Add a new or existing musician to a new or existing album",
            "Quit this program"
    };
}