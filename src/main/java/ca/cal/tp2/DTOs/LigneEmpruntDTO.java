package ca.cal.tp2.DTOs;

import java.time.LocalDate;

public class LigneEmpruntDTO {
    private DocumentDTO document;
    private LocalDate dateRetour;
    private LocalDate dateRetourEffectif;

    public LigneEmpruntDTO(DocumentDTO document, LocalDate dateRetour, LocalDate dateRetourEffectif) {
        this.document = document;
        this.dateRetour = dateRetour;
        this.dateRetourEffectif = dateRetourEffectif;
    }

    // Getters et Setters
    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }
}
