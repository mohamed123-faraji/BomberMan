package fr.malibu.assistants.jws.domain.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lastbomb")
    private Timestamp lastbomb;

    @Column(name = "lastmovement")
    private Timestamp lastmovement;

    @Column(name = "lives")
    private Integer lives;

    @Column(name = "name")
    private String name;

    @Column(name = "posx")
    private Integer posx;

    @Column(name = "posy")
    private Integer posy;

    @Column(name = "position")
    @JsonIgnoreProperties
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private GameEntity game;

}
