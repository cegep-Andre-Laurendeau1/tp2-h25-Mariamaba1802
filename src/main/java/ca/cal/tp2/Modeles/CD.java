package ca.cal.tp2.Modeles;

import java.time.LocalDate;


public class CD extends Document {

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
