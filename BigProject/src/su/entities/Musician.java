package su.entities;

import javax.persistence.*;
import su.entities.Album;

/**
 * Created by Christy on 24 July 2014
 */
@Entity
public class Musician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "Name")
    private String name;
   // @ManyToMany
   // private Album album;

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

    @Override
    public String toString(){
        return "ID: " + id + "/n" +
                "Name: " + name;
    }
}
