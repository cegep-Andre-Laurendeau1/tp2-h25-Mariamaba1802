package ca.cal.tp2.Dao;

import ca.cal.tp2.Exceptions.ErreurPersistenceException;
import ca.cal.tp2.Modeles.Amende;
import jakarta.persistence.*;

import java.util.List;

public class AmendeDAO implements repository_parent<Amende> {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate2.ex1");

    @Override
    public void ajouter(Amende amende) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            entityManager.persist(amende);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ErreurPersistenceException("❌ Erreur lors de l'ajout de l'amende : " , e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void supprimer(Amende amende) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        amende = entityManager.merge(amende); // Nécessaire avant suppression
        entityManager.remove(amende);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    // ✅ Mettre à jour une amende comme "payée"
    public void payerAmende(Long emprunteurId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery(
                "UPDATE Amende a SET a.estPayee = true WHERE a.emprunteur.id = :emprunteurId");
        query.setParameter("emprunteurId", emprunteurId);
      //  int rowsUpdated = query.executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();

//        if (rowsUpdated > 0) {
//            System.out.println("✅ Amende payée avec succès !");
//        } else {
//           throw  "❌ Aucune amende trouvée pour cet emprunteur !");
//        }
    }

    // ✅ Trouver toutes les amendes d'un emprunteur spécifique
    public List<Amende> trouverAmendesParEmprunteur(Long emprunteurId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Amende> amendes;
        try {
            TypedQuery<Amende> query = entityManager.createQuery(
                    "SELECT a FROM Amende a WHERE a.emprunteur.id = :emprunteurId",
                    Amende.class
            );
            query.setParameter("emprunteurId", emprunteurId);
            amendes = query.getResultList();
        } finally {
            entityManager.close();
        }
        return amendes;
    }



}

