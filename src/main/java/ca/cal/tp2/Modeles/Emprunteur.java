package ca.cal.tp2.Modeles;



import java.util.ArrayList;
import java.util.List;


public class Emprunteur extends Utilisateur {

  private List<Emprunt> emprunts = new ArrayList<>();

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
