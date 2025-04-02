package ca.cal.tp2;

import ca.cal.tp2.Exceptions.DocumentExisteDejaException;
import ca.cal.tp2.Exceptions.EmprunteurExistePas;
import ca.cal.tp2.Exceptions.ErreurPersistenceException;
import ca.cal.tp2.Exceptions.RechercheDocumentExistePas;
import ca.cal.tp2.Modeles.Document;
import ca.cal.tp2.Modeles.Emprunteur;
import ca.cal.tp2.Service.MapperService;
import ca.cal.tp2.utilis.TcpServer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import ca.cal.tp2.DTOs.*;
import ca.cal.tp2.Dao.AmendeDAO;
import ca.cal.tp2.Dao.DocumentDAO;
import ca.cal.tp2.Dao.EmpruntDAO;
import ca.cal.tp2.Dao.UtilisateurDAO;
import ca.cal.tp2.Service.EmprunteurService;
import ca.cal.tp2.Service.PreposeService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) throws SQLException {

        TcpServer.createTcpServer();
        // ✅ Initialisation
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate2.ex1");
        EmpruntDAO empruntDAO = new EmpruntDAO();
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        DocumentDAO documentDAO = new DocumentDAO();
        AmendeDAO amendeDAO = new AmendeDAO();
        PreposeService preposeService = new PreposeService(empruntDAO, utilisateurDAO, documentDAO, amendeDAO);
        EmprunteurService emprunteurService = new EmprunteurService(empruntDAO, utilisateurDAO, documentDAO, amendeDAO, preposeService);

        try {
            // ✅ Inscription d’un préposé et d’emprunteurs
            System.out.println("👤 Préposé inscrit : Eli");
            preposeService.inscrirePrepose(new PreposeDTO("Eli", "Gourdin"));


            System.out.println("📌 Emprunteur inscrit : John Doe");
            EmprunteurDTO johnDoe = new EmprunteurDTO("John", "Doe", new ArrayList<>(), new ArrayList<>());
            preposeService.inscrireEmprunteur(johnDoe);

            System.out.println("📌 Emprunteur inscrit : Mariama");
            EmprunteurDTO mariama = new EmprunteurDTO("Mariama", "ba", new ArrayList<>(), new ArrayList<>());
            preposeService.inscrireEmprunteur(mariama);

            EmprunteurDTO retardataire = new EmprunteurDTO("Alice", "Retard",new ArrayList<>(), new ArrayList<>());
            preposeService.inscrireEmprunteur(retardataire);


            // ✅ Ajout de documents (Livres, CD, DVD)
            LivreDTO livre = new LivreDTO("1984", LocalDate.of(1949, 6, 8), 2, 21, "George Orwell", "Secker & Warburg", 328);
            LivreDTO livre2 = new LivreDTO("passager", LocalDate.of(1749, 6, 8), 1, 21, " Orwel", "Secker & Warburg", 328);
            CdDTO cd = new CdDTO("Dark Side of the Moon", LocalDate.of(1973, 3, 1), 2, 14, "Pink Floyd", 43);
            DvdDTO dvd1 = new DvdDTO("Interstellar", LocalDate.of(2014, 11, 5), 1, 7, "Christopher Nolan", 169);
            DvdDTO copieDVD1 = new DvdDTO("Interstellar", LocalDate.of(2014, 11, 5), 1, 7, "Christopher Nolan", 169);

            LivreDTO livreARetourner = new LivreDTO("Livre Retard", LocalDate.of(2023, 1, 1), 1, 21, "Auteur Test", "Maison d'édition", 200);
            preposeService.ajouterDocument(livreARetourner);


            try {
                preposeService.ajouterDocument(livre);
                preposeService.ajouterDocument(livre2);
                preposeService.ajouterDocument(cd);
                System.out.println("Les documents a été ajouté à la bibliothèque !");

                System.out.println("Le document ne devrais pas pouvoir etre sauvegardé!");
                preposeService.ajouterDocument(dvd1);

            } catch (DocumentExisteDejaException e) {
                System.err.println("Erreur : " + e.getMessage());
            }


            // Gestion de doublon
            try {
                preposeService.ajouterDocument(copieDVD1);
            } catch(ErreurPersistenceException e){
                System.out.println(e.getMessage());
            }

            // Recherche de document
            try {
                DocumentDTO docRecherche = emprunteurService.rechercherDocument("1984");
                System.out.println("🔎 Document trouvé : " + docRecherche.getTitre());
            } catch (RechercheDocumentExistePas e) {
                System.out.println(e.getMessage());
            }

            // Emprunt par John
            List<DocumentDTO> documentsAEmprunter = Arrays.asList(livre, livre2, cd);
            try {
                preposeService.emprunterDocument(johnDoe, documentsAEmprunter);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

           try { System.out.println(emprunteurService.consulterCompte(johnDoe));
        } catch (EmprunteurExistePas e) {
            System.out.println(e.getMessage());
        }

            // Retour d'un document
            try {
                preposeService.retournerDocument(johnDoe, livre);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            List<DocumentDTO> docsAEmprunter = Arrays.asList(livreARetourner);

            // Récupère les entités nécessaires (emprunteur et document) depuis les DTO
            Emprunteur emprunteurEntity = utilisateurDAO.trouverEmprunteurParNomPrenom("Alice", "Retard");
            Document documentEntity = documentDAO.rechercherDocumentParTitre("Livre Retard");




            System.out.println("Consultation de compte apres retour");
            try { System.out.println(emprunteurService.consulterCompte(johnDoe));
            } catch (EmprunteurExistePas e) {
                System.out.println(e.getMessage());
            }
            // Mariama emprunte un document indisponible
            try {
                preposeService.emprunterDocument(mariama, Arrays.asList(livre2));

            } catch(Exception e){
                System.out.println(e.getMessage());
            }

            // Rapport mensuel
            System.out.println(preposeService.genererRapport());

        } catch (RuntimeException e) {
            System.err.println("Erreur générale : " + e.getMessage());
        }

            System.out.println("\n📋 TEST DE SIMULATION DE RETARD ET AMENDES");
            System.out.println("------------------------------------------");

            EmprunteurDTO testeur = new EmprunteurDTO("Test", "Retard", new ArrayList<>(), new ArrayList<>());
            preposeService.inscrireEmprunteur(testeur);
            System.out.println("📌 Emprunteur inscrit pour le test : " + testeur.getPrenom() + " " + testeur.getNom());

            // 2. Créer un nouveau document pour ce test
            LivreDTO livreTest = new LivreDTO(
                    "Livre pour test de retard",
                    LocalDate.of(2022, 1, 1),
                    1,  // Nombre d'exemplaires
                    21, // Durée maximale d'emprunt
                    "Auteur Test",
                    "Éditeur Test",
                    100  // Nombre de pages
            );

            preposeService.ajouterDocument(livreTest);
            System.out.println("📚 Document ajouté pour le test : " + livreTest.getTitre());

            // 3. Emprunter le document

        try {
            preposeService.emprunterDocument(testeur, Arrays.asList(livreTest));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
            System.out.println("✅ Document emprunté avec succès");

            // 4. Modifier manuellement la date d'emprunt dans la base de données pour simuler un retard
            // On peut le faire en utilisant une requête JPQL ou SQL directement
            System.out.println("🕒 Simulation d'un retard de 30 jours...");
           empruntDAO.modifierDateEmpruntPourRetard(livreTest.getTitre(), testeur.getNom(), testeur.getPrenom(), 30);

            // 5. Retourner le document (cela devrait générer une amende)
            System.out.println("📎 Retour du document en retard...");
        try {
            preposeService.retournerDocument(testeur, livreTest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(preposeService.genererRapport());
        }

}

