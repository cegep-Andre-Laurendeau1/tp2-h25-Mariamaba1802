package ca.cal.tp2.Modeles;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "LigneEmprunt")
public class LigneEmprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emprunt_id", nullable = false)
    private Emprunt emprunt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "dateRetour", nullable = false)
    private LocalDate dateRetour;

    @Column(name = "dateRetourEffectif")
    private LocalDate dateRetourEffectif;

    public LigneEmprunt() {}

    public LigneEmprunt(Document document, LocalDate dateRetour) {
        this.document = document;
        this.dateRetour = dateRetour;
        this.dateRetourEffectif = null;
    }

    // 🔹 Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Emprunt getEmprunt() { return emprunt; }
    public void setEmprunt(Emprunt emprunt) { this.emprunt = emprunt; }

    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }

    public LocalDate getDateRetour() { return dateRetour; }
    public void setDateRetour(LocalDate dateRetour) { this.dateRetour = dateRetour; }

    public LocalDate getDateRetourEffectif() { return dateRetourEffectif; }
    public void setDateRetourEffectif(LocalDate dateRetourEffectif) { this.dateRetourEffectif = dateRetourEffectif; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneEmprunt that = (LigneEmprunt) o;
        if (this.id != null && that.id != null) {
            return Objects.equals(this.id, that.id);
        }
        return Objects.equals(this.document, that.document) &&
                Objects.equals(this.dateRetour, that.dateRetour);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }

        return Objects.hash(document, dateRetour);
    }

}
