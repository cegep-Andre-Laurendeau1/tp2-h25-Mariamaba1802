package ca.cal.tp2.Modeles;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Emprunt {


    private Long id;


    private LocalDate dateEmprunt;


    private Emprunteur emprunteur;

 private List<LigneEmprunt> lignesEmprunt = new ArrayList<>();

    // Constructeurs
    public Emprunt() {}

    public Emprunt(Emprunteur emprunteur) {
        this.dateEmprunt = LocalDate.now();
        this.emprunteur = emprunteur;
    }



    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public Emprunteur getEmprunteur() { return emprunteur; }
    public void setEmprunteur(Emprunteur emprunteur) { this.emprunteur = emprunteur; }

    public List<LigneEmprunt> getLignesEmprunt() { return lignesEmprunt; }

    // Equals & HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprunt emprunt = (Emprunt) o;
        return id != null && id.equals(emprunt.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
