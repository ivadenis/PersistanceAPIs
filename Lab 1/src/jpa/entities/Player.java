/*
 * Player.java
 *
 *
 * Copyright 2007 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of 
 * this software is authorized pursuant to the terms of the license 
 * found at http://developers.sun.com/berkeley_license.html.
 *
 *
 * Original downloaded from: http://java.sun.com/developer/technicalArticles/J2SE/Desktop/persistenceapi/
 * Updated by Alvaro Monge to work with JPA 2.0 EclipseLink
 *
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({
    @NamedQuery(name=Player.DELETE_ALL, query="DELETE FROM Player p"),
    @NamedQuery(name=Player.GET_BY_TEAM, query="SELECT p FROM Player p WHERE p.team.id = :team")
})
public class Player implements Serializable {

    public static final String DELETE_ALL = "Player.delete_all";
    public static final String GET_BY_TEAM = "Player.get_by_team";

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Integer id;
      @Column(name = "first_name")
      private String firstName;
      @Column(name = "last_name", length = 50, nullable = false)
      private String lastName;
      private int jerseyNumber;
      @Transient
      private String quote;

      @ManyToOne            // this element does not work in EclipseLink, though it's in the JPA spec: (optional = false)
      @JoinColumn(nullable = false)  // needed since EL has a bug with optional element
      private Team team;

  public Player() {
  }

  public Player(String firstName, String lastName, int jerseyNumber, String quote) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.jerseyNumber = jerseyNumber;
    this.quote = quote;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String name) {
    lastName = name;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String name) {
    firstName = name;
  }

  public int getJerseyNumber() {
    return jerseyNumber;
  }

  public void setJerseyNumber(int jerseyNumber) {
    this.jerseyNumber = jerseyNumber;
  }

  public String getLastSpokenWords() {
    return quote;
  }

  public void setLastSpokenWords(String lastWords) {
    quote = lastWords;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Player)) {
      return false;
    }
    Player other = (Player) object;
    return this.id != null && this.id.equals(other.id);
  }

  @Override
  public String toString() {
    String player = String.format("[Name: %s %s, Jersey Number: %d, Team: %s]",
            firstName, lastName, jerseyNumber, team.getTeamName());
    return player;
  }
}