package ca.cal.tp2.Modeles;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "CD")
@PrimaryKeyJoinColumn(name = "id")
public class CD extends Document {
    @Column(nullable = false)
    private String artiste;

    private int duree;

    public CD() {}

    public CD(Long id, String titre, LocalDate dateParution, int nbExemplaire,
              String artiste, int duree) {
        super(id, titre, dateParution, nbExemplaire, 14);
        this.artiste = artiste;
        this.duree = duree;
    }

    public String getArtiste() {
        return artiste;
    }

    public int getDuree() {
        return duree;
    }
}
