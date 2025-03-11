package ca.cal.tp2.DTOs;

import java.time.LocalDate;

public class DvdDTO extends DocumentDTO {
    private String realisateur;
    private int duree;

    // Constructeurs

    public DvdDTO(String titre, LocalDate dateParution, int nbExemplaire,
                  int dureeEmpruntAutorisee, String realisateur, int duree) {
        super(titre, dateParution, nbExemplaire, dureeEmpruntAutorisee);
        this.realisateur = realisateur;
        this.duree = duree;
    }

    // Getters et Setters
    public String getRealisateur() {
        return realisateur;
    }

    public int getDuree() {
        return duree;
    }

}
