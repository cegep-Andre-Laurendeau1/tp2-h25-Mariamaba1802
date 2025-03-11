package ca.cal.tp2.Dao;

import ca.cal.tp1.Exceptions.ErreurPersistenceException;
import ca.cal.tp1.Modeles.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class EmpruntDAO implements DAO_hibernate<Emprunt> {

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
            throw new ErreurPersistenceException("❌ Erreur lors de l'ajout de l'emprunt : " , e);
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

    public void genererRapportMensuel() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Object[]> queryEmprunts = entityManager.createQuery(
                "SELECT MONTH(e.dateEmprunt), COUNT(e) FROM Emprunt e GROUP BY MONTH(e.dateEmprunt)", Object[].class);

        System.out.println("\n  📊 Rapport des emprunts pour ce mois :");
        for (Object[] row : queryEmprunts.getResultList()) {
            System.out.println("Mois actuel: " + row[0] + " | Emprunts: " + row[1]);
        }

        TypedQuery<Object[]> queryAmendes = entityManager.createQuery(
                "SELECT MONTH(a.dateGeneration), COUNT(a), SUM(a.montant) FROM Amende a GROUP BY MONTH(a.dateGeneration)",
                Object[].class);

        System.out.println("📊 Rapport des amendes par mois :");
        if (queryAmendes.getResultList().isEmpty()) {
            System.out.println("Pas d'amande ce mois-ci !");
        }else {
            for (Object[] row : queryAmendes.getResultList()) {
                System.out.println("Mois: " + row[0] + " | Amendes: " + row[1] + " | Montant: " + row[2] + "$");
            }
        }

        entityManager.close();
    }

    public String consulterCompte(Emprunteur emprunteur) {
        StringBuilder compte = new StringBuilder("📌 Compte de " + emprunteur.getNom() + " " + emprunteur.getPrenom() + "\n");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<LigneEmprunt> queryTest = entityManager.createQuery(
                "SELECT l FROM LigneEmprunt l", LigneEmprunt.class);


        TypedQuery<Emprunt> queryEmprunts = entityManager.createQuery(
                "SELECT e FROM Emprunt e WHERE e.emprunteur.id = :emprunteurId", Emprunt.class);
        queryEmprunts.setParameter("emprunteurId", emprunteur.getId());
        List<Emprunt> emprunts = queryEmprunts.getResultList();

        if (emprunts.isEmpty()) {
            compte.append("✅ Aucun emprunt en cours.\n");
        } else {
            compte.append("📖 Emprunts en cours :\n");


            for (Emprunt emprunt : emprunts) {
                compte.append(" (Date d'emprunt  : ").append(emprunt.getDateEmprunt()).append(")\n");
                TypedQuery<LigneEmprunt> queryLignes = entityManager.createQuery(
                        "SELECT l FROM LigneEmprunt l WHERE l.emprunt.id = :empruntId", LigneEmprunt.class);
                queryLignes.setParameter("empruntId", emprunt.getId());
                List<LigneEmprunt> lignesEmprunt = queryLignes.getResultList();

                for (LigneEmprunt ligne : lignesEmprunt) {
                    compte.append("- ").append(ligne.getDocument().getTitre())
                            .append(" (Date d'échéance  : ").append(ligne.getDateRetour()).append(")\n");

                    if (ligne.getDateRetourEffectif() != null) {
                        compte.append(" ✅ (Date de Retour Effectif : ").append(ligne.getDateRetourEffectif()).append(")\n");
                    }
                }
            }
        }


        TypedQuery<Amende> queryAmendes = entityManager.createQuery(
                "SELECT a FROM Amende a WHERE a.emprunteur.id = :emprunteurId AND a.estPayee = false", Amende.class);
        queryAmendes.setParameter("emprunteurId", emprunteur.getId());
        List<Amende> amendes = queryAmendes.getResultList();

        if (amendes.isEmpty()) {
            compte.append("✅ Aucune amende impayée.\n");
        } else {
            compte.append("⚠️ Amendes impayées :\n");
            for (Amende amende : amendes) {
                compte.append("- ").append(amende.getMontant()).append("$ (Générée le ")
                        .append(amende.getDateGeneration()).append(")\n");
            }
        }

        entityManager.close();
        return compte.toString();
    }

    public void ajouterLigneEmprunt( Document document,Long empruntId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Emprunt emprunt = entityManager.find(Emprunt.class, empruntId);

        LocalDate dateRetour = LocalDate.now().plusDays(document.getDureeEmpruntAutorisee());

        LigneEmprunt ligneEmprunt = new LigneEmprunt(document, dateRetour);
        ligneEmprunt.setEmprunt(emprunt);

        entityManager.persist(ligneEmprunt);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    }


