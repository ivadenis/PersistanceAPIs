package su.entities;

import javax.persistence.*;
import su.entities.Album;

/**
 * Created by Christy on 24 July 2014
 */
@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "Title")
    private String songTitle;
    @Column(name = "Genre")
    private String genre;
    @Column(name = "Duration")
    private int songLength;
    @Column(name = "Album ID")
    private int albumId;
    @ManyToOne
    private Album album;

    public Song(String songTitle, String genre, int songLength, int albumId) {
        this.songTitle = songTitle;
        this.genre = genre;
        this.songLength = songLength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getSongLength() {
        return songLength;
    }

    public void setSongLength(int songLength) {
        this.songLength = songLength;
    }

    @Override
    public String toString(){
        return "Song Title: " + songTitle + "/n" +
                "Genre: " + genre + "/n" +
                "Song Length " + songLength + "/n" +
                "Ref. Album ID: " + albumId;
    }
}
