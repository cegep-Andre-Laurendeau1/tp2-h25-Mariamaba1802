package ca.cal.tp2.Modeles;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Emprunteur")
@PrimaryKeyJoinColumn(name = "id")
public class Emprunteur extends Utilisateur {

    @OneToMany(mappedBy = "emprunteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprunt> emprunts = new ArrayList<>();

    @OneToMany(mappedBy = "emprunteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Amende> amendes = new ArrayList<>();

    // Constructeurs
    public Emprunteur() {}

    public Emprunteur(Long id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    // Getters et Setters
    public List<Emprunt> getEmprunts() { return emprunts; }
    public List<Amende> getAmendes() { return amendes; }
}
