package ca.cal.tp2.DTOs;

import java.time.LocalDate;
import java.util.List;


public class EmpruntDTO {
    private LocalDate dateEmprunt;
    private EmprunteurDTO emprunteur;
    private List<LigneEmpruntDTO> lignesEmprunt;



    public EmpruntDTO(LocalDate dateEmprunt, EmprunteurDTO emprunteur, List<LigneEmpruntDTO> lignesEmprunt) {
        this.dateEmprunt = dateEmprunt;
        this.emprunteur = emprunteur;
        this.lignesEmprunt = lignesEmprunt;
    }

    public EmprunteurDTO getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(EmprunteurDTO emprunteur) {
        this.emprunteur = emprunteur;
    }

 }