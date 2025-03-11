package ca.cal.tp2.DTOs;

import java.time.LocalDate;

public class CdDTO  extends DocumentDTO {
    private String artiste;
    private int duree;
    private static final int DUREE_EMPRUNT = 14;

    public CdDTO(String titre, LocalDate dateParution, int nbExemplaire,
                 int dureeEmpruntAutorisee, String artiste, int duree) {
        super(titre, dateParution, nbExemplaire, dureeEmpruntAutorisee);
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
