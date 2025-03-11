package ca.cal.tp2.Modeles;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Amende")
public class Amende {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2) // DECIMAL(10,2)
    private BigDecimal montant;

    @Column(nullable = false)
    private LocalDate dateGeneration;

    @Column(nullable = false)
    private boolean estPayee = false;

    @ManyToOne
    @JoinColumn(name = "emprunteur_id", nullable = true) // Clé étrangère vers Emprunteur
    private Emprunteur emprunteur;

    // 🔹 Constructeurs
    public Amende() {}

    public Amende(BigDecimal  montant, LocalDate dateGeneration, Emprunteur emprunteur) {
        this.montant = montant;
        this.dateGeneration = dateGeneration;
        this.emprunteur = emprunteur;
        this.estPayee = false;
    }

    // 🔹 Getters et Setters
    public Long getId() {
        return id;
    }

    public BigDecimal  getMontant() {
        return montant;
    }

    public LocalDate getDateGeneration() {
        return dateGeneration;
    }

    public boolean EstPayee() {
        return estPayee;
    }

    public Emprunteur getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Emprunteur emprunteur) {
        this.emprunteur = emprunteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amende amende = (Amende) o;
        return id != null && id.equals(amende.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
