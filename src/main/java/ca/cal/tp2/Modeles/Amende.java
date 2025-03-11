package ca.cal.tp2.Modeles;



import java.math.BigDecimal;
import java.time.LocalDate;



public class Amende {


    private Long id;


    private BigDecimal montant;


    private LocalDate dateGeneration;


    private boolean estPayee = false;

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
