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

/**
 * Entity class Player
 *
 * @author John O'Conner
 * @author Alvaro Monge
 */
@Entity
@NamedQueries({
    @NamedQuery(name=Player.DELETE_ALL, query="DELETE FROM Player p"),
    @NamedQuery(name=Player.GET_BY_TEAM, query="SELECT p FROM Player p WHERE p.team.id = :team")
})
public class Player implements Serializable {
    
    /**
     * JPQL query string to delete all players.
     */
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

  /**
   * Creates a new instance of Player
   */
  public Player() {
  }

  /**
   * Creates a new instance of Player with some specified values
   *
   * @param firstName the Player's first name
   * @param lastName the Player's last name
   * @param jerseyNumber the Player's jersey number
   * @param quote a quote about or by the Player
   */
  public Player(String firstName, String lastName, int jerseyNumber, String quote) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.jerseyNumber = jerseyNumber;
    this.quote = quote;
  }

  /**
   * Gets the id of this Player
   *
   * @return the id
   */
  public Integer getId() {
    return this.id;
  }

  /**
   * Sets the id of this Player to the specified value.
   *
   * @param id the new id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Sets the Player's full name
   *
   * @param firstName the Player's first name
   * @param lastName the Player's last name
   */
  public void setName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   *
   * @return the player's last name.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Set the last name of the player
   *
   * @param name the last name of the player
   */
  public void setLastName(String name) {
    lastName = name;
  }

  /**
   * Gets the Player's first name
   *
   * @return the first name of the player
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the Player's first name
   *
   * @param name the value for the player's first name
   */
  public void setFirstName(String name) {
    firstName = name;
  }

  /**
   * Gets the jersey number of the player
   *
   * @return the jersey number
   */
  public int getJerseyNumber() {
    return jerseyNumber;
  }

  /**
   * Sets the jersey number of a player
   *
   * @param jerseyNumber the value to be used for the jersey number
   */
  public void setJerseyNumber(int jerseyNumber) {
    this.jerseyNumber = jerseyNumber;
  }

  /**
   * Access the last spoken words of this Player; this is a transient property,
   * that is, it's a property that does not persist so it is annotated as
   * Transient.
   *
   * @return the words spoken last by this Player
   */
  public String getLastSpokenWords() {
    return quote;
  }

  /**
   * Set the last spoken words of this player
   *
   * @param lastWords the value to be used for the words of a player
   */
  public void setLastSpokenWords(String lastWords) {
    quote = lastWords;
  }

  /**
   * Access the team that this Player is a member of
   *
   * @return the team that this Player is a member of.
   */
  public Team getTeam() {
    return team;
  }

  /**
   * Sets the team that a player is a member of
   *
   * @param team the Team on which the player plays
   */
  public void setTeam(Team team) {
    this.team = team;
  }

  /**
   * Returns a hash code value for the object. This implementation computes a
   * hash code value based on the id fields in this object.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  /**
   * Determines whether another object is equal to this Player. The result is
   * <code>true</code> if and only if the argument is not null and is a Player
   * object that has the same id field values as this object.
   *
   * @param object the reference object with which to compare
   * @return <code>true</code> if this object is the same as the argument;
   * <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Player)) {
      return false;
    }
    Player other = (Player) object;
    return this.id != null && this.id.equals(other.id);
  }

  /**
   * Returns a string representation of the object. This implementation
   * constructs that representation based on the id fields.
   *
   * @return a string representation of the object.
   */
  @Override
  public String toString() {
    String player = String.format("[Name: %s %s, Jersey Number: %d, Team: %s]",
            firstName, lastName, jerseyNumber, team.getTeamName());
    return player;
  }
}