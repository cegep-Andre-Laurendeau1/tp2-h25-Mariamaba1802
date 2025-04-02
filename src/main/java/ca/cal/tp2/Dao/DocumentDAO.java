package ca.cal.tp2.Dao;

import ca.cal.tp2.Exceptions.DocumentExisteDejaException;
import ca.cal.tp2.Exceptions.ErreurPersistenceException;
import ca.cal.tp2.Modeles.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class DocumentDAO implements repository_parent<Document> {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate2.ex1");

    @Override
    public void ajouter(Document document) throws DocumentExisteDejaException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            // Tente de persister le document directement
            entityManager.persist(document);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();

            throw new DocumentExisteDejaException("Le document '" + document.getTitre() + "' existe déjà !");
        } finally {
            entityManager.close();
        }
    }



    public void mettreAJour(Document document) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(document);
        entityManager.getTransaction().commit();
        entityManager.close();
    }



    @Override
    public void supprimer(Document document) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        document = entityManager.merge(document);
        entityManager.remove(document);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    // ✅ Rechercher un document par son titre
    public Document rechercherDocumentParTitre(String titre) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery(
                "SELECT d FROM Document d WHERE d.titre = :titre",
                Document.class);
        query.setParameter("titre", titre);
        Document document = (Document) query.getSingleResult();
        entityManager.close();
        return document;
    }

}
