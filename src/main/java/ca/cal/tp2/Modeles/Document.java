package ca.cal.tp2.Modeles;


import java.time.LocalDate;


public abstract class Document {

    private Long id;

    private String titre;

    private LocalDate dateParution;
    private int nbExemplaire;
    private int dureeEmpruntAutorisee;

    public Document() {}

    public Document(Long id, String titre, LocalDate dateParution, int nbExemplaire, int dureeEmpruntAutorisee) {
        this.id = id;
        this.titre = titre;
        this.dateParution = dateParution;
        this.nbExemplaire = nbExemplaire;
        this.dureeEmpruntAutorisee = dureeEmpruntAutorisee;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public LocalDate getDateParution() {
        return dateParution;
    }

    public int getNbExemplaire() {
        return nbExemplaire;
    }

    public int getDureeEmpruntAutorisee() {
        return dureeEmpruntAutorisee;
    }
    public void setNbExemplaire(int nbExemplaire) {
        this.nbExemplaire = nbExemplaire;
    }
}
