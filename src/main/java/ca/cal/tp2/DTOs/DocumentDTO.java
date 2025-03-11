package ca.cal.tp2.DTOs;

import java.time.LocalDate;
import java.util.Objects;

public abstract class DocumentDTO {
    protected String titre;
    protected LocalDate dateParution;
    protected int nbExemplaire;
   
    protected int dureeEmpruntAutorisee;


    public DocumentDTO(String titre, LocalDate dateParution,
                       int nbExemplaire,  int dureeEmpruntAutorisee) {
        this.titre = titre;
        this.dateParution = dateParution;
        this.nbExemplaire = nbExemplaire;

        this.dureeEmpruntAutorisee = dureeEmpruntAutorisee;
    }

    // Getters et Setters
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateParution() {
        return dateParution;
    }

    public int getNbExemplaire() {
        return nbExemplaire;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentDTO that = (DocumentDTO) o;
        return Objects.equals(titre, that.titre) &&
                Objects.equals(dateParution, that.dateParution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titre, dateParution);
    }

    @Override
    public String toString() {
        return "nom:"+ getTitre() + ", date:"+ getDateParution();
    }
}
