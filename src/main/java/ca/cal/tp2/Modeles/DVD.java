package ca.cal.tp2.Modeles;


import java.time.LocalDate;


public class DVD extends Document {

    private String realisateur;

    private int duree;

    public DVD() {}

    public DVD(Long id, String titre, LocalDate dateParution, int nbExemplaire,
               String realisateur, int duree) {
        super(id, titre, dateParution, nbExemplaire, 7);
        this.realisateur = realisateur;
        this.duree = duree;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public int getDuree() {
        return duree;
    }
}
