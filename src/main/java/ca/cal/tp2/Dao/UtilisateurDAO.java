package ca.cal.tp2.Dao;

import ca.cal.tp2.Exceptions.ErreurPersistenceException;
import ca.cal.tp2.Modeles.Emprunteur;
import ca.cal.tp2.Modeles.Utilisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UtilisateurDAO implements repository_parent<Utilisateur> {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate2.ex1");

    @Override
    public void ajouter(Utilisateur utilisateur) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(utilisateur);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ErreurPersistenceException("Erreur lors de l'ajout de l'utilisateur : " + utilisateur, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void supprimer(Utilisateur utilisateur) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        utilisateur = entityManager.merge(utilisateur); // Nécessaire avant suppression
        entityManager.remove(utilisateur);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    // 🔍 Trouver un emprunteur par nom et prénom
    public Emprunteur trouverEmprunteurParNomPrenom(String nom, String prenom) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Emprunteur> query = entityManager.createQuery(
                "SELECT e FROM Emprunteur e LEFT JOIN FETCH e.emprunts WHERE e.nom = :nom AND e.prenom = :prenom",
                Emprunteur.class);
        query.setParameter("nom", nom);
        query.setParameter("prenom", prenom);
        List<Emprunteur> results = query.getResultList();
        entityManager.close();
        return results.isEmpty() ? null : results.get(0);
    }


}
