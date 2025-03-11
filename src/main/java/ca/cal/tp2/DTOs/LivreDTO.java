package ca.cal.tp2.DTOs;

import java.time.LocalDate;


public class LivreDTO extends DocumentDTO {
    private String auteur;
    private String editeur;
    private int nombreDePages;


    public LivreDTO(String titre, LocalDate dateParution, int nbExemplaire,
                     int dureeEmpruntAutorisee,
                    String auteur, String editeur, int nombreDePages) {
        super(titre, dateParution, nbExemplaire,  dureeEmpruntAutorisee);
        this.auteur = auteur;
        this.editeur = editeur;
        this.nombreDePages = nombreDePages;
    }

    // Getters et Setters
    public String getAuteur() {
        return auteur;
    }

    public String getEditeur() {
        return editeur;
    }

    public int getNombreDePages() {
        return nombreDePages;
    }


}
