package su.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Christy on 24 July 2014
 */
@Entity
@NamedQueries(value = {
        @NamedQuery(name = Musician.GET_BY_NAME, query = "SELECT a FROM Musician a WHERE a.name = :name"),
})
public class Musician {

    public static final String GET_BY_NAME = "Musician.get_by_name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(mappedBy="Musicians")
    private List<Album> Albums;

    public Musician(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return Albums;
    }

    public void setAlbums(List<Album> albums) {
        Albums = albums;
    }

    public void addAlbum(Album a){
        Albums.add(a);
    }

    @Override
    public String toString(){
        return "Name: " + name;
    }
}
