package ca.cal.tp2.Dao;

import ca.cal.tp2.Exceptions.ErreurPersistenceException;
import ca.cal.tp2.Modeles.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class EmpruntDAO implements repository_parent<Emprunt> {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate2.ex1");

    @Override
    public void ajouter(Emprunt emprunt) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            // 📌 Persister l'emprunt
            entityManager.persist(emprunt);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ErreurPersistenceException("❌ Erreur lors de l'ajout de l'emprunt : ", e);
        } finally {
            entityManager.close();
        }
    }



    @Override
    public void supprimer(Emprunt emprunt) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        emprunt = entityManager.merge(emprunt);
        entityManager.remove(emprunt);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Emprunt> getEmpruntsParEmprunteur(Long emprunteurId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Emprunt> query = entityManager.createQuery(
                "SELECT e FROM Emprunt e WHERE e.emprunteur.id = :emprunteurId", Emprunt.class);
        query.setParameter("emprunteurId", emprunteurId);
        List<Emprunt> result = query.getResultList();
        entityManager.close();
        return result;
    }

    public List<LigneEmprunt> getLignesEmpruntByEmprunt(Long empruntId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<LigneEmprunt> query = entityManager.createQuery(
                "SELECT l FROM LigneEmprunt l WHERE l.emprunt.id = :empruntId", LigneEmprunt.class);
        query.setParameter("empruntId", empruntId);
        List<LigneEmprunt> result = query.getResultList();
        entityManager.close();
        return result;
    }

    public void updateLigneEmprunt(LigneEmprunt ligneEmprunt) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(ligneEmprunt);
        entityManager.getTransaction().commit();
        entityManager.close();
    }


    public List<Document> rechercherDocumentsParCritere(String critere) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Document> query = entityManager.createQuery(
                "SELECT d FROM Document d WHERE d.titre LIKE :critere", Document.class);
        query.setParameter("critere", "%" + critere + "%");
        List<Document> result = query.getResultList();
        entityManager.close();
        return result;
    }

    public String genererRapportMensuel() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        StringBuilder rapport = new StringBuilder();

        TypedQuery<Object[]> queryEmprunts = entityManager.createQuery(
                "SELECT MONTH(e.dateEmprunt), COUNT(e) FROM Emprunt e GROUP BY MONTH(e.dateEmprunt)", Object[].class);

        rapport.append("\n📊 Rapport des emprunts pour ce mois :\n");
        for (Object[] row : queryEmprunts.getResultList()) {
            rapport.append("Mois actuel: ").append(row[0]).append(" | Emprunts: ").append(row[1]).append("\n");
        }

        TypedQuery<Object[]> queryAmendes = entityManager.createQuery(
                "SELECT MONTH(a.dateGeneration), COUNT(a), SUM(a.montant) FROM Amende a GROUP BY MONTH(a.dateGeneration)",
                Object[].class);

        rapport.append("📊 Rapport des amendes par mois :\n");
        if (queryAmendes.getResultList().isEmpty()) {
            rapport.append("Pas d'amende ce mois-ci !\n");
        } else {
            for (Object[] row : queryAmendes.getResultList()) {
                rapport.append("Mois: ").append(row[0]).append(" | Amendes: ").append(row[1])
                        .append(" | Montant: ").append(row[2]).append("$\n");
            }
        }

        entityManager.close();
        return rapport.toString();
    }


    public String consulterCompte(Emprunteur emprunteur) {
        StringBuilder compte = new StringBuilder("📌 Compte de " + emprunteur.getNom() + " " + emprunteur.getPrenom() + "\n");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Récupération des emprunts de l'emprunteur
            TypedQuery<Emprunt> queryEmprunts = entityManager.createQuery(
                    "SELECT e FROM Emprunt e WHERE e.emprunteur.id = :emprunteurId", Emprunt.class);
            queryEmprunts.setParameter("emprunteurId", emprunteur.getId());
            List<Emprunt> emprunts = queryEmprunts.getResultList();

            if (emprunts.isEmpty()) {
                compte.append("✅ Aucun emprunt en cours.\n");
            } else {
                compte.append("📖 Emprunts en cours :\n");
                for (Emprunt emprunt : emprunts) {
                    compte.append(" - Date d'emprunt : ").append(emprunt.getDateEmprunt()).append("\n");

                    // Récupération des lignes d'emprunt associées
                    TypedQuery<LigneEmprunt> queryLignes = entityManager.createQuery(
                            "SELECT l FROM LigneEmprunt l WHERE l.emprunt.id = :empruntId", LigneEmprunt.class);
                    queryLignes.setParameter("empruntId", emprunt.getId());
                    List<LigneEmprunt> lignesEmprunt = queryLignes.getResultList();

                    for (LigneEmprunt ligne : lignesEmprunt) {
                        compte.append("   - ").append(ligne.getDocument().getTitre())
                                .append(" (Date d'échéance : ").append(ligne.getDateRetour()).append(")");

                        if (ligne.getDateRetourEffectif() != null) {
                            compte.append(" ✅ (Retour le : ").append(ligne.getDateRetourEffectif()).append(")");
                        }
                        compte.append("\n");
                    }
                }
            }

            // Récupération des amendes impayées
            TypedQuery<Amende> queryAmendes = entityManager.createQuery(
                    "SELECT a FROM Amende a WHERE a.emprunteur.id = :emprunteurId AND a.estPayee = false", Amende.class);
            queryAmendes.setParameter("emprunteurId", emprunteur.getId());
            List<Amende> amendes = queryAmendes.getResultList();

            if (amendes.isEmpty()) {
                compte.append("✅ Aucune amende impayée.\n");
            } else {
                compte.append("⚠️ Amendes impayées :\n");
                for (Amende amende : amendes) {
                    compte.append(" - ").append(amende.getMontant()).append("$ (Générée le ")
                            .append(amende.getDateGeneration()).append(")\n");
                }
            }
        } finally {
            entityManager.close();
        }

        return compte.toString();
    }


    public void ajouterLigneEmprunt( Document document,Long empruntId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            Emprunt emprunt = entityManager.find(Emprunt.class, empruntId);
            if (emprunt == null) {
                throw new IllegalArgumentException("Emprunt non trouvé pour l'ID : " + empruntId);
            }

            LocalDate dateRetour = LocalDate.now().plusDays(document.getDureeEmpruntAutorisee());

            LigneEmprunt ligneEmprunt = new LigneEmprunt(document, dateRetour);
            ligneEmprunt.setEmprunt(emprunt);

            entityManager.persist(ligneEmprunt);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ErreurPersistenceException("Erreur lors de l'ajout de la ligne d'emprunt", e);
        } finally {
            entityManager.close();
        }
    }



    // Méthode 1 : Compter le nombre d'emprunts pour un document
    private int compterEmprunts(Document document) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(l) FROM LigneEmprunt l WHERE l.document.id = :documentId AND l.dateRetourEffectif IS NULL", Long.class);
            query.setParameter("documentId", document.getId());
            return query.getSingleResult().intValue();
        } finally {
            entityManager.close();
        }
    }

    private int compterExemplairesDisponibles(Document document) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Utiliser Integer.class pour les types INTEGER en base de données
            TypedQuery<Integer> query = entityManager.createQuery(
                    "SELECT d.nbExemplaire FROM Document d WHERE d.id = :documentId", Integer.class);
            query.setParameter("documentId", document.getId());
            Integer result = query.getSingleResult();
            return result != null ? result : 0;
        } finally {
            entityManager.close();
        }
    }


    // Méthode 3 : Vérifier la disponibilité du document
    public boolean estDisponible(Document document) {
        int empruntsEnCours = compterEmprunts(document);
        int nbExemplaires = compterExemplairesDisponibles(document);
        return empruntsEnCours < nbExemplaires;
    }

    public static void modifierDateEmpruntPourRetard(
            String titreDocument,
            String nomEmprunteur,
            String prenomEmprunteur,
            int joursRetard) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            // 1. Trouver l'emprunteur
            String queryEmprunteur = "SELECT e FROM Emprunteur e WHERE e.nom = :nom AND e.prenom = :prenom";
            Emprunteur emprunteur = em.createQuery(queryEmprunteur, Emprunteur.class)
                    .setParameter("nom", nomEmprunteur)
                    .setParameter("prenom", prenomEmprunteur)
                    .getSingleResult();

            // 2. Trouver le document
            String queryDocument = "SELECT d FROM Document d WHERE d.titre = :titre";
            Document document = em.createQuery(queryDocument, Document.class)
                    .setParameter("titre", titreDocument)
                    .getSingleResult();

            // 3. Trouver l'emprunt associé
            String queryEmprunt = "SELECT e FROM Emprunt e WHERE e.emprunteur.id = :emprunteurId";
            List<Emprunt> emprunts = em.createQuery(queryEmprunt, Emprunt.class)
                    .setParameter("emprunteurId", emprunteur.getId())
                    .getResultList();

            for (Emprunt emprunt : emprunts) {
                // 4. Trouver la ligne d'emprunt associée au document
                String queryLigne = "SELECT l FROM LigneEmprunt l WHERE l.emprunt.id = :empruntId AND l.document.id = :documentId";
                List<LigneEmprunt> lignes = em.createQuery(queryLigne, LigneEmprunt.class)
                        .setParameter("empruntId", emprunt.getId())
                        .setParameter("documentId", document.getId())
                        .getResultList();

                for (LigneEmprunt ligne : lignes) {
                    // 5. Modifier les dates pour simuler un retard
                    LocalDate dateRetroactive = LocalDate.now().minusDays(joursRetard);
                    // emprunt.setDateEmprunt(dateRetroactive);
                    // ligne.setDateEmprunt(dateRetroactive);

                    // Mettre à jour la date de retour prévue (antérieure à aujourd'hui)
                    ligne.setDateRetour(dateRetroactive.plusDays(21)); // Durée standard de 21 jours

                    em.merge(emprunt);
                    em.merge(ligne);
                    System.out.println("📅 Dates modifiées pour simuler un retard de " + joursRetard + " jours");
                    System.out.println("   - Date d'emprunt: " + dateRetroactive);
                    System.out.println("   - Date de retour prévue: " + ligne.getDateRetour());
                    System.out.println("   - Date actuelle: " + LocalDate.now());
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erreur lors de la modification des dates : " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}


