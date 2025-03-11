package ca.cal.tp2;

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

        // ✅ Inscription d’un préposé et d’emprunteurs
        System.out.println("👤 Préposé inscrit.");
        preposeService.inscrirePrepose(new PreposeDTO("Eli", "Gourdin"));


        System.out.println("📌 Emprunteur inscrit : John Doe");
        EmprunteurDTO johnDoe = new EmprunteurDTO("John", "Doe", new ArrayList<>(), new ArrayList<>());
        preposeService.inscrireEmprunteur(johnDoe);

        System.out.println("📌 Emprunteur inscrit : Mariama");
        EmprunteurDTO mariama = new EmprunteurDTO("Mariama", "ba", new ArrayList<>(), new ArrayList<>());
        preposeService.inscrireEmprunteur(mariama);



        // ✅ Ajout de documents (Livres, CD, DVD)
        LivreDTO livre = new LivreDTO("1984", LocalDate.of(1949, 6, 8), 2, 21, "George Orwell", "Secker & Warburg", 328);
        LivreDTO livre2 = new LivreDTO("passager", LocalDate.of(1749, 6, 8), 1, 21, " Orwel", "Secker & Warburg", 328);
        CdDTO cd = new CdDTO("Dark Side of the Moon", LocalDate.of(1973, 3, 1), 2, 14, "Pink Floyd", 43);
        DvdDTO dvd1 = new DvdDTO("Interstellar", LocalDate.of(2014, 11, 5), 1, 7, "Christopher Nolan", 169);
        DvdDTO copieDVD1 = new DvdDTO("Interstellar", LocalDate.of(2014, 11, 5), 1, 7, "Christopher Nolan", 169);

        preposeService.ajouterDocument(livre);
        preposeService.ajouterDocument(livre2);
        preposeService.ajouterDocument(cd);
        preposeService.ajouterDocument(dvd1);

        // Ajout ducopie exacte du dvd1, devrais donner une erreur
        System.out.println("\n  Ajout du meme dvd, devrais donner une erreur");
        preposeService.ajouterDocument(copieDVD1);

        // ✅ Recherche de documents
        System.out.println("\n Recherche de documents");
        DocumentDTO docRecherche = emprunteurService.rechercherDocument("1984");
        if (docRecherche != null) {
            System.out.println("🔎\n Document trouvé : " + docRecherche.getTitre() + " Date de parition :" +docRecherche.getDateParution() );
        } else {
            System.out.println("\n ❌ Aucun document trouvé.");
        }

        // ✅ Emprunt de documents (John emprunte 1984 et Dark Side of the Moon et passager)
        List<DocumentDTO> documentsAEmprunter = new ArrayList<>();
        documentsAEmprunter.add(livre);
        documentsAEmprunter.add(livre2);
        documentsAEmprunter.add(cd);

        System.out.println("✅ John veut effectuer un emprunt !");
        emprunteurService.emprunterDocument(johnDoe, documentsAEmprunter);


        // ✅ Affichage des emprunts de John Doe
        System.out.println("\n📊 État des emprunts de John Doe :");
        System.out.println(emprunteurService.consulterCompte(johnDoe));

        // ✅ Vérification des exemplaires disponibles Pendant l'emprunt du livre 1984"
        System.out.println("\n Vérification des exemplaires disponibles Pendant l'emprunt du livre 1984,devrais etre 1 car d'origine il y a 2 exemplaires");
        DocumentDTO docVerifiee = emprunteurService.rechercherDocument("1984");
        System.out.println("📖 1984 → Exemplaires disponibles : " + docVerifiee.getNbExemplaire());


        // ✅ Retour d’un document (John retourne "1984")
        System.out.println("\n📌 John Doe retourne '1984'...");
        emprunteurService.retournerDocument(johnDoe, livre);


        // ✅ Vérification après retour
        System.out.println("\n📊 Mise à jour du compte de John Doe après retour :");
        System.out.println(emprunteurService.consulterCompte(johnDoe));

        // ✅ Vérification des exemplaires disponibles après retour
        System.out.println("\n Vérification des exemplaires disponibles après le retour du livre 1984");
        DocumentDTO docVerifie = emprunteurService.rechercherDocument("1984");
        System.out.println("📖 1984 → Exemplaires disponibles : " + docVerifie.getNbExemplaire());

        // ✅ Vérification qu'on ne peut pas emprunter un document dont il ne reste plus d'inventaire

        System.out.println("\n ✅ Mariama veut emprunter l'unique exemplaire disponible du  livre2 qui est deja emprunté par john. Erreur !");
        List<DocumentDTO> docsAEmprunterDeMariama = new ArrayList<>();
        docsAEmprunterDeMariama.add(livre2);
        emprunteurService.emprunterDocument(mariama, docsAEmprunterDeMariama);


        // Rapport du mois Actuel
        preposeService.genererRapport();

        // ✅ Fermeture propre
        entityManagerFactory.close();
    }
}
