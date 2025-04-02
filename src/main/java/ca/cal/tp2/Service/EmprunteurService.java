package ca.cal.tp2.Service;

import ca.cal.tp2.DTOs.DocumentDTO;
import ca.cal.tp2.DTOs.EmprunteurDTO;
import ca.cal.tp2.Dao.AmendeDAO;
import ca.cal.tp2.Dao.DocumentDAO;
import ca.cal.tp2.Dao.EmpruntDAO;
import ca.cal.tp2.Dao.UtilisateurDAO;
import ca.cal.tp2.Exceptions.DocumentExisteDejaException;
import ca.cal.tp2.Exceptions.EmpruntExistePas;
import ca.cal.tp2.Exceptions.EmprunteurExistePas;
import ca.cal.tp2.Exceptions.RechercheDocumentExistePas;
import ca.cal.tp2.Modeles.*;

import java.sql.SQLException;
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

    // ✅ Recherche un document dans la bibliothèque
    public DocumentDTO rechercherDocument(String critere)throws RechercheDocumentExistePas {
        List<Document> documents = empruntDAO.rechercherDocumentsParCritere(critere);
        if (documents.isEmpty()) {
            throw new RechercheDocumentExistePas ("❌ Aucun document trouvé pour le critère : " + critere);
        }
        return MapperService.versDocumentDTO(documents.get(0)); // Retourne le premier document trouvé
    }

    // ✅ Consulter l'état du compte d'un emprunteur
    public String consulterCompte(EmprunteurDTO emprunteurDTO)throws EmprunteurExistePas {
        Emprunteur emprunteur = utilisateurDAO.trouverEmprunteurParNomPrenom(emprunteurDTO.getNom(), emprunteurDTO.getPrenom());
        if (emprunteur == null) {
        throw new   EmprunteurExistePas ( "❌ Aucun emprunteur trouvé.");
        }
        return empruntDAO.consulterCompte(emprunteur);
    }



}
