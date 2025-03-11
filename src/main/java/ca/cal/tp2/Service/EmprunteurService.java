package ca.cal.tp2.Service;

import ca.cal.tp2.DTOs.DocumentDTO;
import ca.cal.tp2.DTOs.EmprunteurDTO;
import ca.cal.tp2.Dao.AmendeDAO;
import ca.cal.tp2.Dao.DocumentDAO;
import ca.cal.tp2.Dao.EmpruntDAO;
import ca.cal.tp2.Dao.UtilisateurDAO;
import ca.cal.tp2.Modeles.*;

import java.util.List;

public class EmprunteurService {
    private final EmpruntDAO empruntDAO;
    private final PreposeService preposeService;
    private final UtilisateurDAO utilisateurDAO;
    private final AmendeDAO amendeDAO;

    public EmprunteurService(EmpruntDAO empruntDAO, UtilisateurDAO utilisateurDAO, DocumentDAO documentDAO, AmendeDAO amendeDAO, PreposeService preposeService) {
        this.empruntDAO = empruntDAO;
        this.preposeService = preposeService;
        this.utilisateurDAO = utilisateurDAO;
        this.amendeDAO = amendeDAO;
    }

    // ✅ L'emprunteur demande à emprunter un document
    public void emprunterDocument(EmprunteurDTO emprunteurDTO, List<DocumentDTO> documentsDTO) {
        preposeService.gererEmprunt(emprunteurDTO, documentsDTO);
    }

    // ✅ L'emprunteur retourne un document
    public void retournerDocument(EmprunteurDTO emprunteurDTO, DocumentDTO documentDTO) {
        preposeService.gestionRetourDocument(emprunteurDTO, documentDTO);
    }
    // ✅ Recherche un document dans la bibliothèque
    public DocumentDTO rechercherDocument(String critere) {
        List<Document> documents = empruntDAO.rechercherDocumentsParCritere(critere);
        if (documents.isEmpty()) {
            System.out.println("❌ Aucun document trouvé pour le critère : " + critere);
            return null;
        }
        return MapperService.versDocumentDTO(documents.get(0)); // Retourne le premier document trouvé
    }

    // ✅ Consulter l'état du compte d'un emprunteur
    public String consulterCompte(EmprunteurDTO emprunteurDTO) {
        Emprunteur emprunteur = utilisateurDAO.trouverEmprunteurParNomPrenom(emprunteurDTO.getNom(), emprunteurDTO.getPrenom());
        if (emprunteur == null) {
            return "❌ Aucun emprunteur trouvé.";
        }
        return empruntDAO.consulterCompte(emprunteur);
    }

    // ✅ L'emprunteur paie ses amendes via le préposé
    public void payerAmende(EmprunteurDTO emprunteurDTO) {
        preposeService.gererAmendes(emprunteurDTO);
    }


}
