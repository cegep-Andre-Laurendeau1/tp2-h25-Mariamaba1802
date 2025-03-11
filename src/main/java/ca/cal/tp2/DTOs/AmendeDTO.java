package ca.cal.tp2.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AmendeDTO {
    private BigDecimal montant;
    private LocalDate dateGeneration;
    private boolean estPayee;
    private EmprunteurDTO emprunteur;



    public AmendeDTO(BigDecimal  montant, LocalDate dateGeneration, boolean estPayee, EmprunteurDTO emprunteur) {
        this.montant = montant;
        this.dateGeneration = dateGeneration;
        this.estPayee = estPayee;
        this.emprunteur = emprunteur;
    }

    // Getters et Setters
    public BigDecimal  getMontant() {
        return montant;
    }


    public LocalDate getDateGeneration() {
        return dateGeneration;
    }

    public EmprunteurDTO getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(EmprunteurDTO emprunteur) {
        this.emprunteur = emprunteur;
    }
}
