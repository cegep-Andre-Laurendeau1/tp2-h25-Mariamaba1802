package ca.cal.tp2.Modeles;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Document")
public abstract class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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
