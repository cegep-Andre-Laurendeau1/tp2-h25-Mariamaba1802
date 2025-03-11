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

}
