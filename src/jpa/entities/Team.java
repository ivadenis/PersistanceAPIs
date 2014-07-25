/*
 * Team.java
 *
 * Copyright 2007 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of 
 * this software is authorized pursuant to the terms of the license 
 * found at http://developers.sun.com/berkeley_license.html.
 *
 * Original downloaded from: http://java.sun.com/developer/technicalArticles/J2SE/Desktop/persistenceapi/
 * Updated by Alvaro Monge to work with JPA 2.0 EclipseLink
 *
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
//import org.eclipse.persistence.annotations.CascadeOnDelete;

@NamedQueries({
    @NamedQuery(name = Team.GET_BY_NAME, query = "SELECT t FROM Team t WHERE t.teamName = :name"),
    @NamedQuery(name = Team.DELETE_BY_NAME, query = "DELETE FROM Team t WHERE t.teamName = :name"),
    @NamedQuery(name = Team.DELETE_ALL, query = "DELETE FROM Team t")
    // NOTE: While tempting... this does not work b/c JPQL SELECT clause must specify only single-valued expressions:
    // @NamedQuery(name = Team.GET_BY_NAME, query = "SELECT t.roster FROM Team t WHERE t.teamName = :name"),
})
@Entity
public class Team implements Serializable {

    public static final String GET_BY_NAME = "Team.get_by_name";
    public static final String DELETE_BY_NAME = "Team.delete_by_name";
    public static final String DELETE_ALL = "Team.delete";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @javax.persistence.Column(name = "team_name", nullable = false, unique = true, length = 100)
    private String teamName;
    private String league;

    /* For a bidirectional relationship, the annotation below defines 
     * the inverse side of a ManyToOne relationship: a
     * team has many players and relates it to the owning side,
     * in this case the team field of a Player. Thus, given a team T, then
     * the following must be true for every player P in T.players: T == P.getTeam()
     */
    @OneToMany(mappedBy = "team", cascade = {CascadeType.ALL}, orphanRemoval = true)
    //@CascadeOnDelete   // NOTE: This is specific to JPA Provider EclipseLink, thus NOT portable.
    private Collection<Player> roster;

    /* 
     * Alternatively, for a unidirectional relationship, we would remove the reference 
     * to Team from Player and you must explicitly define the FK column, otherwise by default JPA will
     * generate a "join table" that unfortunately acts like a ManyToMany relationship in the database!!
     */
//    @OneToMany
//    @JoinColumn(name="team_fk")
    /**
     * Creates a new instance of Team
     */
    public Team() {
        roster = new HashSet<>();
    }

    public Team(String name, String league) {
        this.teamName = name;
        this.league = league;
        roster = new HashSet<>();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Collection<Player> getRoster() {
        return roster;
    }

    /**
     * Set the collection of players for this team. Also ensure that each
     * player's team is set to this team.
     * From:http://en.wikibooks.org/wiki/Java_Persistence/OneToMany "As the
     * relationship is bi-directional so as the application updates one side of
     * the relationship, the other side should also get updated, and be in
     * synch"
     *
     * @param players is the collection of players making up the team.
     */
    public void setRoster(Collection<Player> players) {
        for (Player p : players) {
            addPlayer(p);
        }
    }

    public void addPlayer(Player player) {
        roster.add(player);
        if (player.getTeam() != this) {
            player.setTeam(this);
        }
    }

    public boolean removePlayer(Player player) {
        boolean success = roster.remove(player);

        if (success && player.getTeam() == this) {
            player.setTeam(null);
        }

        return success;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Team[name=" + teamName + ", roster=" + roster + "]";
    }
}
