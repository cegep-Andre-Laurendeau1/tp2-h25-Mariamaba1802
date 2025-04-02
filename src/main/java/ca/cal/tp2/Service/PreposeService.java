package ca.cal.tp2.Service;

import ca.cal.tp2.DTOs.DocumentDTO;
import ca.cal.tp2.DTOs.EmprunteurDTO;
import ca.cal.tp2.DTOs.PreposeDTO;
import ca.cal.tp2.Dao.AmendeDAO;
import ca.cal.tp2.Dao.DocumentDAO;
import ca.cal.tp2.Dao.EmpruntDAO;
import ca.cal.tp2.Dao.UtilisateurDAO;
import ca.cal.tp2.Exceptions.*;
import ca.cal.tp2.Modeles.*;
import java.sql.SQLException;

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

    public void ajouterDocument(DocumentDTO documentDTO) throws ErreurPersistenceException, DocumentExisteDejaException {
        try {
            Document document = MapperService.versDocumentEntite(documentDTO);
            documentDAO.ajouter(document);

        } catch (DocumentExisteDejaException e) {
            // Gérer spécifiquement l'exception d'unicité
            throw new ErreurPersistenceException("Document  '" + documentDTO.getTitre() + " Existe Deja ",e);
        }
    }



    public void inscrireEmprunteur(EmprunteurDTO emprunteurDTO) {
        Emprunteur emprunteur = MapperService.versEmprunteurEntite(emprunteurDTO);
        utilisateurDAO.ajouter(emprunteur);
    }


    public void inscrirePrepose(PreposeDTO preposeDTO) {
        Prepose prepose = MapperService.versPreposeEntite(preposeDTO);
        utilisateurDAO.ajouter(prepose);
    }


    public void emprunterDocument(EmprunteurDTO emprunteurDTO, List<DocumentDTO> documentsDTO) throws
            DocumentExisteDejaException,
            EmprunteurExistePas,AmendeImpaye {
        Emprunteur emprunteur = utilisateurDAO.trouverEmprunteurParNomPrenom(
                emprunteurDTO.getNom(), emprunteurDTO.getPrenom());

        if (emprunteur == null) {
            throw new EmprunteurExistePas("❌ Emprunteur introuvable !");
        }

        if (aDesAmendesImpayees(emprunteur)) {
            throw new AmendeImpaye("❌ L'emprunteur a des amendes impayées. Impossible d'emprunter.");
        }

        List<Document> documentsDisponibles = new ArrayList<>();

        for (DocumentDTO documentDTO : documentsDTO) {
            Document document = documentDAO.rechercherDocumentParTitre(documentDTO.getTitre());
            if (document == null ) {
                throw new DocumentExistePas("❌ Le document '" + documentDTO.getTitre() + "' existe pas.");
            }
            if ( !empruntDAO.estDisponible(document) ) {
                throw new DocumentIndisponible("❌ Le document '" + documentDTO.getTitre() + "' n'est pas disponible.");
            }
            documentsDisponibles.add(document);
        }
        // Ajoute seulement si tout est valide
        Emprunt nouvelEmprunt = new Emprunt(emprunteur);
        empruntDAO.ajouter(nouvelEmprunt);
        for (Document document : documentsDisponibles) {
            empruntDAO.ajouterLigneEmprunt(document, nouvelEmprunt.getId());;

        }
    }


    private boolean aDesAmendesImpayees(Emprunteur emprunteur) {
        List<Amende> amendes = amendeDAO.trouverAmendesParEmprunteur(emprunteur.getId());
        return amendes.stream().anyMatch(amende -> !amende.EstPayee());
    }

    public void retournerDocument(EmprunteurDTO emprunteurDTO, DocumentDTO documentDTO)throws   DocumentExisteDejaException,
            EmprunteurExistePas, EmpruntExistePas {
        Emprunteur emprunteur = utilisateurDAO.trouverEmprunteurParNomPrenom(emprunteurDTO.getNom(), emprunteurDTO.getPrenom());
        Document document = documentDAO.rechercherDocumentParTitre(documentDTO.getTitre());
        if (emprunteur == null ) {
            throw new EmprunteurExistePas("❌ ERREUR : Emprunteur  non trouvé !");

        }
        if (document == null) {
            throw new DocumentExisteDejaException("❌ ERREUR : Document non trouvé !");

        }

        List<Emprunt> emprunts = empruntDAO.getEmpruntsParEmprunteur(emprunteur.getId());

        for (Emprunt emprunt : emprunts) {
            List<LigneEmprunt> lignesEmprunt = empruntDAO.getLignesEmpruntByEmprunt(emprunt.getId());

            for (LigneEmprunt ligne : lignesEmprunt) {
                if (ligne.getDocument().getId().equals(document.getId())) {
                    ligne.setDateRetourEffectif(LocalDate.now());
                    empruntDAO.updateLigneEmprunt(ligne);

                    verifierRetardEtAjouterAmende(emprunteur, ligne);

                    return;
                }
            }
        }

       throw new EmpruntExistePas("❌ Aucun emprunt trouvé pour ce document !");
    }

    // ✅ Vérifier si un emprunteur a un retard et générer une amende
    public void verifierRetardEtAjouterAmende(Emprunteur emprunteur, LigneEmprunt ligne) {
        LocalDate dateRetour = ligne.getDateRetour();
        LocalDate dateActuelle = LocalDate.now();

        if (dateActuelle.isAfter(dateRetour)) {
            long joursRetard = ChronoUnit.DAYS.between(dateRetour, dateActuelle);
            BigDecimal montantAmende = BigDecimal.valueOf(joursRetard).multiply(MONTANT_AMENDE_PAR_JOUR);
            Amende amende = new Amende(montantAmende, dateActuelle, emprunteur);
            amendeDAO.ajouter(amende);
           // System.out.println("⚠️ Amende de " + montantAmende + "$ ajoutée pour retard.");
        }
    }


    public String payerAmendes(EmprunteurDTO emprunteurDTO)throws EmprunteurExistePas {
        Emprunteur emprunteur = utilisateurDAO.trouverEmprunteurParNomPrenom(emprunteurDTO.getNom(), emprunteurDTO.getPrenom());

        if (emprunteur == null) {
           throw  new  EmprunteurExistePas ("❌ Emprunteur introuvable !");
        }

        List<Amende> amendes = amendeDAO.trouverAmendesParEmprunteur(emprunteur.getId());

        if (amendes.isEmpty()) {
            return "✅ Aucune amende à payer pour " + emprunteur.getNom() + " " + emprunteur.getPrenom();

        }

        amendeDAO.payerAmende(emprunteur.getId());

       return "💰 Toutes les amendes de " + emprunteur.getNom() + " " + emprunteur.getPrenom() + " ont été payées !";
    }

    public String genererRapport() {
        return empruntDAO.genererRapportMensuel();
    }


}
