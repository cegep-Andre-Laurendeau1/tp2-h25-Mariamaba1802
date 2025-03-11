package ca.cal.tp2.DTOs;

public class UtilisateurDTO {
    protected String nom;
    protected String prenom;


    public UtilisateurDTO(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

}
