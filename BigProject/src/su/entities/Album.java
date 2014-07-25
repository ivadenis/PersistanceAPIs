package su.entities;

import javax.persistence.*;
import java.sql.Date;
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
    @Column(name = "Album Title")
    private String albumTitle;
    @Column(name = "Release Date")
    private Date releaseDate;
    @Column(name = "Label")
    private String label;
    @OneToMany
    private Song song;
    @ManyToMany
    private Musician musician;

    public Album(String albumTitle, Date releaseDate, String label) {
        this.albumTitle = albumTitle;
        this.releaseDate = releaseDate;
        this.label = label;
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

    @Override
    public String toString(){
        return "ID: " + id + "/n" +
                "Album Title: " + albumTitle + "/n" +
                "Release Date: " + releaseDate + "/n" +
                "Label: " + label;
    }
}
