package ca.cal.tp2.Dao;

import ca.cal.tp2.Exceptions.ErreurPersistenceException;
import ca.cal.tp2.Modeles.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class DocumentDAO implements DAO_hibernate<Document> {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate2.ex1");

    @Override
    public void ajouter(Document document)  {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        try {
            Query query = entityManager.createQuery(
                    "SELECT d FROM Document d WHERE d.titre = :titre", Document.class);
            query.setParameter("titre", document.getTitre());

            if (!query.getResultList().isEmpty()) {
               System.out.println("Le document '" + document.getTitre() + "' existe déjà !");
                // Je pourrais aussi lancer cette exception, mais je la laisse en commentaire// pour l'instant et affiche plutôt un message avec System.out dans le main,
                // car sinon l'affichage de l'exception ne sera pas agréable visuellement.
                // throw new DocumentExisteDejaException("Le document '" + document.getTitre() + "' existe déjà !");
                return;
            }

            entityManager.persist(document);
            entityManager.getTransaction().commit();
            System.out.println("Le document '" + document.getTitre() + "' a été ajouté a la biblioteque!");
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ErreurPersistenceException("Erreur lors de l'ajout du document", e);
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
