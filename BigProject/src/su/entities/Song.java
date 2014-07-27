package su.entities;

import javax.persistence.*;

/**
 * Created by Christy on 24 July 2014
 */
@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String songTitle;
    private String genre;
    private int songLength;
    @ManyToOne
    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Song(String songTitle, String genre, int songLength) {
        this.songTitle = songTitle;
        this.genre = genre;
        this.songLength = songLength;
        this.album = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "Song Title: " + songTitle + "\n" +
                "   Genre: " + genre + "\n" +
                "   Song Length " + songLength;
    }
}
