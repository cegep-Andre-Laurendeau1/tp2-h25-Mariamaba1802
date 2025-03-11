package ca.cal.tp2.Modeles;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Livre")
@PrimaryKeyJoinColumn(name = "id")
public class Livre extends Document {
    @Column(nullable = false)
    private String auteur;

    @Column(nullable = false)
    private String editeur;

    private int nombreDePages;

    public Livre() {}

    public Livre(Long id, String titre, LocalDate dateParution, int nbExemplaire,
                 String auteur, String editeur, int nombreDePages) {
        super(id, titre, dateParution, nbExemplaire, 21);
        this.auteur = auteur;
        this.editeur = editeur;
        this.nombreDePages = nombreDePages;
    }

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
