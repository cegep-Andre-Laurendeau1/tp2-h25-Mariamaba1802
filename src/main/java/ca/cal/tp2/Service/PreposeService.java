package ca.cal.tp2.Service;

import ca.cal.tp2.DTOs.DocumentDTO;
import ca.cal.tp2.DTOs.EmprunteurDTO;
import ca.cal.tp2.DTOs.PreposeDTO;
import ca.cal.tp2.Dao.AmendeDAO;
import ca.cal.tp2.Dao.DocumentDAO;
import ca.cal.tp2.Dao.EmpruntDAO;
import ca.cal.tp2.Dao.UtilisateurDAO;
import ca.cal.tp2.Modeles.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class PreposeService {
    private final EmpruntDAO empruntDAO;
    private final UtilisateurDAO utilisateurDAO;
    private final DocumentDAO documentDAO;
    private final AmendeDAO amendeDAO;
    private static final BigDecimal MONTANT_AMENDE_PAR_JOUR = BigDecimal.valueOf(0.25);

    public PreposeService(EmpruntDAO empruntDAO, UtilisateurDAO utilisateurDAO, DocumentDAO documentDAO, AmendeDAO amendeDAO) {
        this.empruntDAO = empruntDAO;
        this.utilisateurDAO = utilisateurDAO;
        this.documentDAO = documentDAO;
        this.amendeDAO = amendeDAO;
    }

    public void ajouterDocument(DocumentDTO documentDTO) {
        Document document = MapperService.versDocumentEntite(documentDTO);
        documentDAO.ajouter(document);

    }


    public void inscrireEmprunteur(EmprunteurDTO emprunteurDTO) {
        Emprunteur emprunteur = MapperService.versEmprunteurEntite(emprunteurDTO);
        utilisateurDAO.ajouter(emprunteur);
    }


    public void inscrirePrepose(PreposeDTO preposeDTO) {
        Prepose prepose = MapperService.versPreposeEntite(preposeDTO);
        utilisateurDAO.ajouter(prepose);
    }


    public void gererEmprunt(EmprunteurDTO emprunteurDTO, List<DocumentDTO> documentsDTO) {

        Emprunteur emprunteur = utilisateurDAO.trouverEmprunteurParNomPrenom(
                emprunteurDTO.getNom(), emprunteurDTO.getPrenom());

        if (emprunteur == null) {
            System.out.println("❌ Emprunteur introuvable !");
            return;
        }

        if (aDesAmendesImpayees(emprunteur)) {
            System.out.println("❌ L'emprunteur a des amendes impayées. Impossible d'emprunter.");
            return;
        }

        List<Document> documentsDisponibles = new ArrayList<>();

        for (DocumentDTO documentDTO : documentsDTO) {
            Document document = documentDAO.rechercherDocumentParTitre(documentDTO.getTitre());
            if (document == null || document.getNbExemplaire() <= 0) {
                System.out.println("❌ Le document '" + documentDTO.getTitre() + "' n'est pas disponible.");
                return;
            }
            documentsDisponibles.add(document);
        }

        // Ajoute seulement si tout est valide
        Emprunt nouvelEmprunt = new Emprunt(emprunteur);
        empruntDAO.ajouter(nouvelEmprunt);

        for (Document document : documentsDisponibles) {
            empruntDAO.ajouterLigneEmprunt(document, nouvelEmprunt.getId());
            document.setNbExemplaire(document.getNbExemplaire() - 1);
            documentDAO.mettreAJour(document);
        }

        System.out.println("✅ Emprunt enregistré avec succès.");
    }


    private boolean aDesAmendesImpayees(Emprunteur emprunteur) {
        List<Amende> amendes = amendeDAO.trouverAmendesParEmprunteur(emprunteur.getId());
        return amendes.stream().anyMatch(amende -> !amende.EstPayee());
    }


}
