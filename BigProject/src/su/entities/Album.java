package su.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import su.entities.Song;
import su.entities.Musician;

/**
 * Created by Christy on 24 July 2014
 */
@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //@Column(name = "Album Title")
    private String albumTitle;
    //@Column(name = "Release Date")
    private Date releaseDate;
    //@Column(name = "Label")
    private String label;
    @OneToMany(mappedBy = "album", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Collection<Song> Songs;


    @ManyToMany
    @JoinTable(
            name="album_musician",
            joinColumns={@JoinColumn(name="album_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="musician_id", referencedColumnName="id")})
    private List<Musician> Musicians;

    //@ManyToMany
    //private Musician musician;

    public Album(String albumTitle, Date releaseDate, String label) {
        this.albumTitle = albumTitle;
        this.releaseDate = releaseDate;
        this.label = label;
        Songs = new HashSet<>();
        Musicians = new ArrayList<Musician>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addSong(Song song) {
        Songs.add(song);
        if (song.getAlbum() != this) {
            song.setAlbum(this);
        }
    }

    public void addMusician(Musician m) {
        Musicians.add(m);

    }

    @Override
    public String toString(){
        return "ID: " + id + "/n" +
                "Album Title: " + albumTitle + "/n" +
                "Release Date: " + releaseDate + "/n" +
                "Label: " + label;
    }
}
